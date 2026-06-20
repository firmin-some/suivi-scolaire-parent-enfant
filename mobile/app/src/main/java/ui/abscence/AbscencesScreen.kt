package bf.ujkz.suiviscolaireparent.ui.absences

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import bf.ujkz.suiviscolaireparent.viewmodel.AbsenceViewModel

@Composable
fun AbsencesScreen(
    modifier: Modifier = Modifier,
    viewModel: AbsenceViewModel = viewModel()
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
            if (uiState.absences.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Aucune absence enregistrée.")
                }
            } else {
                LazyColumn(
                    modifier = modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text(
                            text = "${uiState.absences.size} absence(s) au total",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(4.dp))
                    }
                    items(uiState.absences) { absence ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(absence.date, style = MaterialTheme.typography.bodyMedium)
                                    absence.motif?.let {
                                        Text(it, style = MaterialTheme.typography.bodySmall)
                                    } ?: Text("Motif non renseigné", style = MaterialTheme.typography.bodySmall)
                                }
                                Surface(
                                    color = if (absence.justifiee) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error,
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    Text(
                                        text = if (absence.justifiee) "Justifiée" else "Non justifiée",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}