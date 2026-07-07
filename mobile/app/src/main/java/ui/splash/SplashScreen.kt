package bf.ujkz.suiviscolaireparent.ui.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bf.ujkz.suiviscolaireparent.ui.theme.SchoolBlue
import bf.ujkz.suiviscolaireparent.ui.theme.SchoolGold

@Composable
fun SplashScreen(onCommencer: (destination: String) -> Unit) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "alpha"
    )

    LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0D2137), SchoolBlue, Color(0xFF1B5E8A))
                )
            )
            .alpha(alpha)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .padding(top = 60.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo école
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = SchoolBlue,
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Suivi scolaire",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(Modifier.width(40.dp).height(2.dp).background(SchoolGold))
                Text(
                    text = "  Espace parent  ",
                    fontSize = 16.sp,
                    color = SchoolGold,
                    fontWeight = FontWeight.Medium
                )
                Box(Modifier.width(40.dp).height(2.dp).background(SchoolGold))
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Application mobile permettant\naux parents de suivre la vie\nscolaire de leurs enfants.",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(40.dp))

            // Fonctionnalités cliquables
            val features = listOf(
                Triple(Icons.Default.Star, "Notes", "notes"),
                Triple(Icons.Default.ShoppingCart, "Paiements", "paiements"),
                Triple(Icons.Default.Description, "Bulletins", "notes"),
                Triple(Icons.Default.DateRange, "Absences", "absences"),
                Triple(Icons.Default.Notifications, "Notifications", "annonces"),
            )

            features.forEach { (icon, label, destination) ->
                FeatureItem(
                    icon = icon,
                    label = label,
                    onClick = { onCommencer(destination) }
                )
                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.weight(1f))

            // Bouton COMMENCER → dashboard par défaut
            Button(
                onClick = { onCommencer("dashboard") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(
                    text = "COMMENCER",
                    color = SchoolBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 2.sp
                )
                Spacer(Modifier.width(8.dp))
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = SchoolBlue
                )
            }
        }
    }
}

@Composable
private fun FeatureItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = SchoolGold, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.width(16.dp))
        Text(text = label, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.White.copy(alpha = 0.6f))
    }
}