package bf.ujkz.suiviscolaireparent.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import bf.ujkz.suiviscolaireparent.ui.theme.SchoolBlue
import bf.ujkz.suiviscolaireparent.ui.theme.SchoolGold
import bf.ujkz.suiviscolaireparent.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit = {},
    viewModel: DashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            uiState.errorMessage != null -> Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(uiState.errorMessage ?: "", color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(12.dp))
                Button(onClick = viewModel::loadDashboard) { Text("Réessayer") }
            }
            uiState.eleve != null -> {
                val eleve = uiState.eleve!!
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                ) {
                    // En-tête avec dégradé
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Brush.verticalGradient(listOf(SchoolBlue, Color(0xFF62929E))))
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier.size(72.dp).clip(CircleShape).background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = SchoolBlue, modifier = Modifier.size(44.dp))
                            }
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = "Bienvenue, ${uiState.parentLabel}",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Text(
                                text = "Espace de suivi de ${eleve.prenom} ${eleve.nom}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                            Text(
                                text = "Classe : ${eleve.classe}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Description de l'application
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = SchoolGold.copy(alpha = 0.15f))
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = SchoolGold, modifier = Modifier.size(28.dp))
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = "Cette application vous permet de suivre en temps réel la scolarité de votre enfant : notes, absences, paiements et annonces de l'école.",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // Résumé rapide
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(modifier = Modifier.weight(1f), label = "Moyenne", value = "${eleve.moyenne_generale}/20", icon = Icons.Default.Star)
                        StatCard(modifier = Modifier.weight(1f), label = "Rang", value = "${eleve.rang_classe}e / ${eleve.effectif_classe}", icon = Icons.Default.EmojiEvents)
                    }

                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = "Accès rapide",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(12.dp))

                    // Boutons d'accès rapide
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickAccessButton(icon = Icons.Default.List, label = "Consulter les notes", description = "Notes et moyennes par matière") { onNavigate("notes") }
                        QuickAccessButton(icon = Icons.Default.ShoppingCart, label = "Suivi des paiements", description = "Historique et reçus de paiement") { onNavigate("paiements") }
                        QuickAccessButton(icon = Icons.Default.DateRange, label = "Absences", description = "Liste des absences de l'élève") { onNavigate("absences") }
                        QuickAccessButton(icon = Icons.Default.Notifications, label = "Annonces de l'école", description = "Réunions, examens, actualités") { onNavigate("annonces") }
                        QuickAccessButton(icon = Icons.Default.Lock, label = "Modifier mon mot de passe", description = "Changer votre mot de passe de connexion") { viewModel.showPasswordDialog() }
                    }

                    Spacer(Modifier.height(24.dp))
                }
            }
        }

        // Dialogue changement de mot de passe
        if (uiState.showPasswordDialog) {
            PasswordChangeDialog(
                ancienMdp = uiState.ancienMdp,
                nouveauMdp = uiState.nouveauMdp,
                confirmMdp = uiState.confirmMdp,
                isLoading = uiState.isUpdatingPassword,
                success = uiState.passwordSuccess,
                errorMessage = uiState.passwordError,
                onAncienChange = viewModel::onAncienMdpChange,
                onNouveauChange = viewModel::onNouveauMdpChange,
                onConfirmChange = viewModel::onConfirmMdpChange,
                onSubmit = viewModel::submitPasswordUpdate,
                onDismiss = viewModel::hidePasswordDialog
            )
        }
    }
}

@Composable
private fun StatCard(modifier: Modifier = Modifier, label: String, value: String, icon: ImageVector) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = SchoolGold, modifier = Modifier.size(28.dp))
            Spacer(Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Text(text = label, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun QuickAccessButton(
    icon: ImageVector,
    label: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape).background(SchoolBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                Text(text = description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
private fun PasswordChangeDialog(
    ancienMdp: String,
    nouveauMdp: String,
    confirmMdp: String,
    isLoading: Boolean,
    success: Boolean,
    errorMessage: String?,
    onAncienChange: (String) -> Unit,
    onNouveauChange: (String) -> Unit,
    onConfirmChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Modifier le mot de passe") },
        text = {
            if (success) {
                Text("Mot de passe modifié avec succès !", color = Color(0xFF2E7D32))
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = ancienMdp,
                        onValueChange = onAncienChange,
                        label = { Text("Ancien mot de passe") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = nouveauMdp,
                        onValueChange = onNouveauChange,
                        label = { Text("Nouveau mot de passe") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = confirmMdp,
                        onValueChange = onConfirmChange,
                        label = { Text("Confirmer le nouveau mot de passe") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    errorMessage?.let {
                        Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        },
        confirmButton = {
            if (success) {
                Button(onClick = onDismiss) { Text("Fermer") }
            } else {
                Button(onClick = onSubmit, enabled = !isLoading) {
                    if (isLoading) CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White)
                    else Text("Valider")
                }
            }
        },
        dismissButton = {
            if (!success) {
                TextButton(onClick = onDismiss) { Text("Annuler") }
            }
        }
    )
}