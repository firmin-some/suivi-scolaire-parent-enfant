package bf.ujkz.suiviscolaireparent.ui.notes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import bf.ujkz.suiviscolaireparent.utils.openPdfUrl
import bf.ujkz.suiviscolaireparent.viewmodel.NoteViewModel

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    viewModel: NoteViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.bulletinUrl) {
        uiState.bulletinUrl?.let {
            openPdfUrl(context, it)
            viewModel.bulletinHandled()
        }
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(1, 2, 3).forEach { t ->
                FilterChip(
                    selected = uiState.trimestre == t,
                    onClick = { viewModel.selectTrimestre(t) },
                    label = { Text("Trim. $t") }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = viewModel::downloadBulletin,
            enabled = !uiState.isLoadingBulletin,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isLoadingBulletin) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Télécharger le bulletin (PDF)")
            }
        }

        uiState.bulletinError?.let {
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(12.dp))

        when {
            uiState.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            uiState.errorMessage != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(uiState.errorMessage ?: "", color = MaterialTheme.colorScheme.error)
            }
            uiState.data != null -> {
                val matieres = uiState.data!!.matieres
                if (matieres.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Aucune note pour ce trimestre.")
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(matieres) { matiere ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = matiere.matiere, style = MaterialTheme.typography.titleMedium)
                                        Text(
                                            text = "Moy: ${matiere.moyenne}/20",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    matiere.notes.forEach { note ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = "${note.type} (coef. ${note.coefficient})", style = MaterialTheme.typography.bodySmall)
                                            Text(text = "${note.valeur}/20", style = MaterialTheme.typography.bodySmall)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}