package com.example.reclamos

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reclamos.viewmodel.MainActivity
import kotlinx.coroutines.delay
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}

@Composable
fun SplashScreen(onFinished: () -> Unit) {

    var startAnim by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        startAnim = true
        delay(2000)
        onFinished()
    }

    val scale by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0.6f,
        animationSpec = tween(
            durationMillis = 700,
            easing = OvershootInterpolator(2f).toEasing()
        )
    )

    val glowRadius by animateFloatAsState(
        targetValue = if (startAnim) 60f else 20f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1500
                20f at 0
                60f at 750
                20f at 1500
            },
            repeatMode = RepeatMode.Restart
        )
    )

    val alphaText by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0f,
        animationSpec = tween(900)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .size(120.dp)
                .scale(scale)
                .glowEffect(glowRadius),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "JBG",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Reclamos",
            color = Color(0xFFCCCCCC),
            fontSize = 20.sp,
            modifier = Modifier.alpha(alphaText)
        )
    }
}

// 100% compatible con todas las versiones de Compose
fun Modifier.glowEffect(radius: Float): Modifier = this.drawBehind {
    drawCircle(
        color = Color.White.copy(alpha = 0.35f),
        radius = (size.minDimension / 2) + radius
    )
}

fun OvershootInterpolator.toEasing(): Easing = Easing { x -> getInterpolation(x) }
