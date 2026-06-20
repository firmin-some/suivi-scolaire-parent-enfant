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
            if (uiState.annonces.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Aucune annonce disponible.")
                }
            } else {
                LazyColumn(
                    modifier = modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.annonces) { annonce ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(annonce.titre, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
                                    Surface(
                                        color = when (annonce.type) {
                                            "reunion" -> MaterialTheme.colorScheme.primaryContainer
                                            "examen" -> MaterialTheme.colorScheme.errorContainer
                                            else -> MaterialTheme.colorScheme.surfaceVariant
                                        },
                                        shape = MaterialTheme.shapes.small
                                    ) {
                                        Text(
                                            text = annonce.type,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                                Text(annonce.date, style = MaterialTheme.typography.bodySmall)
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