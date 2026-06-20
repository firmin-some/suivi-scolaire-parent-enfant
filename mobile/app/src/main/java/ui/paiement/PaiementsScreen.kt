package bf.ujkz.suiviscolaireparent.ui.paiements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import bf.ujkz.suiviscolaireparent.utils.openPdfUrl
import bf.ujkz.suiviscolaireparent.viewmodel.PaiementViewModel

@Composable
fun PaiementsScreen(
    modifier: Modifier = Modifier,
    viewModel: PaiementViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showForm by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.submitSuccess) {
        if (uiState.submitSuccess) {
            showForm = false
            viewModel.resetSubmitState()
        }
    }

    when {
        uiState.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        uiState.errorMessage != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(uiState.errorMessage ?: "", color = MaterialTheme.colorScheme.error)
        }
        uiState.data != null -> {
            val data = uiState.data!!
            LazyColumn(
                modifier = modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Récapitulatif", style = MaterialTheme.typography.titleMedium)
                            HorizontalDivider()
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Total dû"); Text("${data.montant_total_du} FCFA")
                            }
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Total payé"); Text("${data.montant_paye} FCFA", color = Color(0xFF2E7D32))
                            }
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Restant")
                                Text(
                                    "${data.montant_restant} FCFA",
                                    color = if (data.montant_restant > 0) MaterialTheme.colorScheme.error else Color(0xFF2E7D32)
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { showForm = !showForm }, modifier = Modifier.fillMaxWidth()) {
                        Text(if (showForm) "Annuler" else "Effectuer un paiement")
                    }

                    if (showForm) {
                        Spacer(Modifier.height(8.dp))
                        PaymentForm(
                            isSubmitting = uiState.isSubmitting,
                            errorMessage = uiState.submitError,
                            onSubmit = { montant, mode -> viewModel.submitPaiement(montant, mode) }
                        )
                    }

                    Spacer(Modifier.height(8.dp))
                    Text("Historique des versements", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(4.dp))
                }

                if (data.versements.isEmpty()) {
                    item { Text("Aucun versement enregistré.") }
                } else {
                    items(data.versements) { versement ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(versement.date, style = MaterialTheme.typography.bodySmall)
                                    Text("${versement.montant} FCFA", style = MaterialTheme.typography.bodyLarge, color = Color(0xFF2E7D32))
                                }
                                versement.mode_paiement?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
                                versement.recu_url?.let { url ->
                                    TextButton(onClick = { openPdfUrl(context, url) }) {
                                        Text("Voir / imprimer le reçu")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaymentForm(
    isSubmitting: Boolean,
    errorMessage: String?,
    onSubmit: (Int, String) -> Unit
) {
    var montant by remember { mutableStateOf("") }
    var modePaiement by remember { mutableStateOf("Mobile money") }
    val modes = listOf("Mobile money", "Espèces", "Virement bancaire")
    var expanded by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = montant,
                onValueChange = { montant = it.filter { c -> c.isDigit() } },
                label = { Text("Montant (FCFA)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                OutlinedTextField(
                    value = modePaiement,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Mode de paiement") },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    modes.forEach { mode ->
                        DropdownMenuItem(text = { Text(mode) }, onClick = { modePaiement = mode; expanded = false })
                    }
                }
            }

            errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }

            Button(
                onClick = { montant.toIntOrNull()?.let { onSubmit(it, modePaiement) } },
                enabled = !isSubmitting && montant.toIntOrNull() != null && montant.toIntOrNull()!! > 0,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Valider le paiement")
                }
            }
        }
    }
}