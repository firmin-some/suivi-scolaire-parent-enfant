package bf.ujkz.suiviscolaireparent.ui.annonces

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import bf.ujkz.suiviscolaireparent.viewmodel.AnnonceViewModel

@Composable
fun AnnoncesScreen(
    modifier: Modifier = Modifier,
    viewModel: AnnonceViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        uiState.errorMessage != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(uiState.errorMessage ?: "", color = MaterialTheme.colorScheme.error)
        }
        else -> {
            LazyColumn(
                modifier = modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Section Notifications
                if (uiState.notifications.isNotEmpty()) {
                    item {
                        Text("🔔 Notifications", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Spacer(Modifier.height(8.dp))
                    }
                    items(uiState.notifications) { notif ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (!notif.lu) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            onClick = { if (!notif.lu) viewModel.marquerLu(notif.id) }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = notif.titre,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (!notif.lu) {
                                        Surface(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = MaterialTheme.shapes.small
                                        ) {
                                            Text(
                                                "Nouveau",
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                                Spacer(Modifier.height(4.dp))
                                Text(notif.message, style = MaterialTheme.typography.bodySmall)
                                Text(notif.date, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                        }
                    }
                    item { Spacer(Modifier.height(8.dp)) }
                }

                // Section Annonces
                item {
                    Text("📢 Annonces de l'école", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(Modifier.height(8.dp))
                }

                if (uiState.annonces.isEmpty()) {
                    item { Text("Aucune annonce disponible.", style = MaterialTheme.typography.bodyMedium) }
                } else {
                    items(uiState.annonces) { annonce ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        annonce.titre,
                                        style = MaterialTheme.typography.titleSmall,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Surface(
                                        color = when (annonce.type) {
                                            "reunion" -> MaterialTheme.colorScheme.primaryContainer
                                            "examen" -> MaterialTheme.colorScheme.errorContainer
                                            "paiement" -> Color(0xFFFFF3E0)
                                            else -> MaterialTheme.colorScheme.surfaceVariant
                                        },
                                        shape = MaterialTheme.shapes.small
                                    ) {
                                        Text(
                                            annonce.type,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                                Text(annonce.date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                HorizontalDivider()
                                Text(annonce.contenu, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}