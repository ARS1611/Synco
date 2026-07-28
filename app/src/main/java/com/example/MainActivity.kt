package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.components.DeepLinkConnectDialog
import com.example.ui.components.IncomingRequestDialog
import com.example.ui.screens.ConnectScreen
import com.example.ui.screens.ConsentScreen
import com.example.ui.screens.DevicesScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.OpenSourceLicensesScreen
import com.example.ui.screens.PrivacyPolicyScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QrScannerScreen
import com.example.ui.screens.RemoteSessionScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.ShareDeviceScreen
import com.example.ui.screens.TermsOfServiceScreen
import com.example.ui.theme.SynkoAccentLight
import com.example.ui.theme.SynkoPrimary
import com.example.ui.theme.SynkoTextDark
import com.example.ui.theme.SynkoTextMuted
import com.example.ui.theme.SynkoTheme
import com.example.ui.theme.SynkoWhite
import com.example.ui.viewmodel.AuthViewModel
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check for deep link intent e.g. synko://connect/483629154
        handleIntent(intent)

        setContent {
            SynkoTheme {
                SynkoApp(
                    authViewModel = authViewModel,
                    mainViewModel = mainViewModel
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val data = intent?.data
        if (data != null && data.scheme == "synko" && data.host == "connect") {
            val code = data.lastPathSegment ?: ""
            if (code.isNotBlank()) {
                mainViewModel.handleIncomingDeepLink(code)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SynkoApp(
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel
) {
    val navController = rememberNavController()
    val currentUser by authViewModel.currentUser.collectAsState()
    val liveState by mainViewModel.liveSessionState.collectAsState()
    val incomingRequestDevice by mainViewModel.incomingRequestDeviceName.collectAsState()
    val deepLinkCode by mainViewModel.deepLinkCode.collectAsState()

    var showOnboarding by remember { mutableStateOf(false) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    val showBottomBar = currentUser != null && !showOnboarding && !liveState.isActive && currentRoute in listOf("home", "history", "devices", "settings")

    // Global Incoming Request Dialog
    if (incomingRequestDevice != null) {
        IncomingRequestDialog(
            deviceName = incomingRequestDevice!!,
            accountName = currentUser?.email ?: "john.doe@gmail.com",
            connectionTime = "Just now",
            onAllowOnce = { mainViewModel.acceptIncomingRequest() },
            onAlwaysAllow = { mainViewModel.acceptIncomingRequest() },
            onDecline = { mainViewModel.declineIncomingRequest() }
        )
    }

    // Deep Link Connect Invite Dialog
    if (deepLinkCode != null) {
        DeepLinkConnectDialog(
            code = deepLinkCode!!,
            onAccept = {
                val code = deepLinkCode!!
                mainViewModel.dismissDeepLink()
                mainViewModel.connectWithCode(code, "Remote Device")
            },
            onCancel = { mainViewModel.dismissDeepLink() }
        )
    }

    if (showOnboarding) {
        OnboardingScreen(
            onGetStarted = { showOnboarding = false }
        )
    } else if (currentUser == null) {
        LoginScreen(viewModel = authViewModel)
    } else if (liveState.isActive) {
        RemoteSessionScreen(viewModel = mainViewModel)
    } else {
        Scaffold(
            topBar = {
                if (currentRoute in listOf("home", "history", "devices", "settings")) {
                    TopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(SynkoPrimary, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .border(2.dp, Color.White, RoundedCornerShape(2.dp))
                                            .rotate(45f)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "SYNKO",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    color = SynkoTextDark
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { navController.navigate("profile") }) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(Color(0xFFF1F5F9), CircleShape)
                                        .border(1.dp, Color(0xFFE2E8F0), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Profile",
                                        tint = Color(0xFF475569),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = SynkoWhite
                        )
                    )
                }
            },
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar(
                        containerColor = SynkoWhite,
                        tonalElevation = 0.dp,
                        modifier = Modifier
                            .border(width = 1.dp, color = Color(0xFFF1F5F9))
                            .navigationBarsPadding()
                    ) {
                        NavigationBarItem(
                            selected = currentRoute == "home",
                            onClick = { navController.navigate("home") { popUpTo("home") { saveState = true } } },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                            label = { Text("Home", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SynkoPrimary,
                                selectedTextColor = SynkoPrimary,
                                indicatorColor = SynkoAccentLight,
                                unselectedIconColor = SynkoTextMuted,
                                unselectedTextColor = SynkoTextMuted
                            )
                        )

                        NavigationBarItem(
                            selected = currentRoute == "history",
                            onClick = { navController.navigate("history") { popUpTo("home") { saveState = true } } },
                            icon = { Icon(Icons.Default.History, contentDescription = "History") },
                            label = { Text("History", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SynkoPrimary,
                                selectedTextColor = SynkoPrimary,
                                indicatorColor = SynkoAccentLight,
                                unselectedIconColor = SynkoTextMuted,
                                unselectedTextColor = SynkoTextMuted
                            )
                        )

                        NavigationBarItem(
                            selected = currentRoute == "devices",
                            onClick = { navController.navigate("devices") { popUpTo("home") { saveState = true } } },
                            icon = { Icon(Icons.Default.Devices, contentDescription = "Devices") },
                            label = { Text("Devices", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SynkoPrimary,
                                selectedTextColor = SynkoPrimary,
                                indicatorColor = SynkoAccentLight,
                                unselectedIconColor = SynkoTextMuted,
                                unselectedTextColor = SynkoTextMuted
                            )
                        )

                        NavigationBarItem(
                            selected = currentRoute == "settings",
                            onClick = { navController.navigate("settings") { popUpTo("home") { saveState = true } } },
                            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                            label = { Text("Settings", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SynkoPrimary,
                                selectedTextColor = SynkoPrimary,
                                indicatorColor = SynkoAccentLight,
                                unselectedIconColor = SynkoTextMuted,
                                unselectedTextColor = SynkoTextMuted
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(SynkoWhite)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeScreen(
                            viewModel = mainViewModel,
                            user = currentUser,
                            onNavigateToShare = { navController.navigate("share") },
                            onNavigateToConnect = { navController.navigate("connect") },
                            onNavigateToQrScanner = { navController.navigate("qr_scanner") }
                        )
                    }

                    composable("share") {
                        ShareDeviceScreen(viewModel = mainViewModel)
                    }

                    composable("connect") {
                        ConnectScreen(
                            viewModel = mainViewModel,
                            onNavigateToQrScanner = { navController.navigate("qr_scanner") }
                        )
                    }

                    composable("qr_scanner") {
                        QrScannerScreen(
                            viewModel = mainViewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable("history") {
                        HistoryScreen(viewModel = mainViewModel)
                    }

                    composable("devices") {
                        DevicesScreen(viewModel = mainViewModel)
                    }

                    composable("profile") {
                        ProfileScreen(authViewModel = authViewModel)
                    }

                    composable("settings") {
                        SettingsScreen(
                            authViewModel = authViewModel,
                            onNavigateToProfile = { navController.navigate("profile") },
                            onNavigateToDevices = { navController.navigate("devices") },
                            onNavigateToPrivacyPolicy = { navController.navigate("privacy_policy") },
                            onNavigateToTermsOfService = { navController.navigate("terms_of_service") },
                            onNavigateToLicenses = { navController.navigate("licenses") },
                            onNavigateToConsent = { navController.navigate("consent") }
                        )
                    }

                    composable("privacy_policy") {
                        PrivacyPolicyScreen(onBack = { navController.popBackStack() })
                    }

                    composable("terms_of_service") {
                        TermsOfServiceScreen(onBack = { navController.popBackStack() })
                    }

                    composable("licenses") {
                        OpenSourceLicensesScreen(onBack = { navController.popBackStack() })
                    }

                    composable("consent") {
                        ConsentScreen(
                            onContinue = { navController.popBackStack() },
                            onOpenPrivacyPolicy = { navController.navigate("privacy_policy") },
                            onOpenTermsOfService = { navController.navigate("terms_of_service") }
                        )
                    }
                }
            }
        }
    }
}
