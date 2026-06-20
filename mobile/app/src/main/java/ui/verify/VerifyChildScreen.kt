package bf.ujkz.suiviscolaireparent.ui.verify

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import bf.ujkz.suiviscolaireparent.ui.theme.SchoolBlue
import bf.ujkz.suiviscolaireparent.viewmodel.VerifyChildViewModel

@Composable
fun VerifyChildScreen(
    onVerified: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VerifyChildViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.verified) {
        if (uiState.verified) onVerified()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(SchoolBlue, Color(0xFF62929E))))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(80.dp).clip(CircleShape).background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.FamilyRestroom, contentDescription = null, tint = SchoolBlue, modifier = Modifier.size(44.dp))
        }
        Spacer(Modifier.height(16.dp))
        Text("Identification de l'enfant", style = MaterialTheme.typography.headlineSmall, color = Color.White)
        Text(
            "Veuillez renseigner le nom et la classe de votre enfant pour accéder à ses données.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.85f)
        )
        Spacer(Modifier.height(28.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                OutlinedTextField(
                    value = uiState.nom,
                    onValueChange = viewModel::onNomChange,
                    label = { Text("Nom de l'enfant") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = uiState.classe,
                    onValueChange = viewModel::onClasseChange,
                    label = { Text("Classe (ex: CM1)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                uiState.errorMessage?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = viewModel::verify,
                    enabled = !uiState.isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = SchoolBlue),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                    } else {
                        Text("Valider")
                    }
                }
            }
        }
    }
}