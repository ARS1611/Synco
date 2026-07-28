package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SynkoAccent
import com.example.ui.theme.SynkoBorder
import com.example.ui.theme.SynkoPrimary
import com.example.ui.theme.SynkoSurface
import com.example.ui.theme.SynkoTextPrimary
import com.example.ui.theme.SynkoTextSecondary
import com.example.ui.theme.SynkoWhite

@Composable
fun SynkoGlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 28.dp,
    containerColor: Color = SynkoSurface,
    borderColor: Color = Color(0xFFF1F5F9),
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(cornerRadius),
                ambientColor = Color(0x08000000),
                spotColor = Color(0x0F000000)
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            ),
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            content()
        }
    }
}

@Composable
fun StatusBadge(
    text: String,
    color: Color,
    backgroundColor: Color,
    icon: ImageVector? = null
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}

@Composable
fun QrCodeCanvas(
    code: String,
    modifier: Modifier = Modifier,
    sizeDp: Dp = 200.dp
) {
    Box(
        modifier = modifier
            .size(sizeDp)
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .background(SynkoWhite, RoundedCornerShape(20.dp))
            .border(1.dp, SynkoBorder, RoundedCornerShape(20.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(sizeDp - 32.dp)) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val gridSize = 17
            val cellSize = canvasWidth / gridSize

            val seed = code.hashCode()

            // Draw outer corner finder blocks
            fun drawFinderPattern(startX: Int, startY: Int) {
                val outerRect = Size(cellSize * 5, cellSize * 5)
                val innerRect = Size(cellSize * 3, cellSize * 3)
                val coreRect = Size(cellSize * 1.5f, cellSize * 1.5f)

                drawRect(
                    color = Color(0xFF0F172A),
                    topLeft = Offset(startX * cellSize, startY * cellSize),
                    size = outerRect
                )
                drawRect(
                    color = Color.White,
                    topLeft = Offset((startX + 0.8f) * cellSize, (startY + 0.8f) * cellSize),
                    size = Size(cellSize * 3.4f, cellSize * 3.4f)
                )
                drawRect(
                    color = Color(0xFF2563EB),
                    topLeft = Offset((startX + 1.5f) * cellSize, (startY + 1.5f) * cellSize),
                    size = Size(cellSize * 2f, cellSize * 2f)
                )
            }

            drawFinderPattern(0, 0)
            drawFinderPattern(gridSize - 5, 0)
            drawFinderPattern(0, gridSize - 5)

            // Random matrix data based on connection code
            val random = java.util.Random(seed.toLong())
            for (r in 0 until gridSize) {
                for (c in 0 until gridSize) {
                    // Skip finder zones
                    if ((r < 5 && c < 5) || (r < 5 && c >= gridSize - 5) || (r >= gridSize - 5 && c < 5)) continue

                    if (random.nextBoolean()) {
                        val isAccent = random.nextInt(10) > 7
                        drawRect(
                            color = if (isAccent) Color(0xFF3B82F6) else Color(0xFF1E293B),
                            topLeft = Offset(c * cellSize, r * cellSize),
                            size = Size(cellSize * 0.85f, cellSize * 0.85f)
                        )
                    }
                }
            }
        }
    }
}
