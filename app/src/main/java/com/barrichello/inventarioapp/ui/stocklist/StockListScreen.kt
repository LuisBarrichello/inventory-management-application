package com.barrichello.inventarioapp.ui.stocklist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.barrichello.inventarioapp.domain.model.InventarioItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockListScreen(
    onViewModel: StockListViewModel = hiltViewModel(),
    onNavegateBack: () -> Unit
) {
    val itemList by onViewModel.itemList.collectAsState()
    val uiState by onViewModel.uiState.collectAsState()

    var selectedItemForDetail by remember { mutableStateOf<InventarioItem?>(null) }

    if (uiState.showClearConfirmDialog) {
        AlertDialog(
            onDismissRequest = { onViewModel.onClearAllDismissed() },
            title = { Text("Limpar Inventário") },
            text = { Text("Tem certeza que deseja apagar todos os itens contados? Esta ação não pode ser desfeita.") },
            confirmButton = {
                TextButton(
                    onClick = { onViewModel.onClearAllConfirmed() }
                ) {
                    Text("Limpar TUDO")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onViewModel.onClearAllDismissed() }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (selectedItemForDetail != null) {
        CoilDetailDialog(
            item = selectedItemForDetail!!,
            onDismiss = { selectedItemForDetail = null }
        )
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Estoque Atual (${itemList.size})") },
                navigationIcon = {
                    IconButton(onClick = onNavegateBack) {
                        Icon(Icons.Default.ArrowBack, "Voltar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onViewModel.onClearAllClicked() },
                containerColor = MaterialTheme.colorScheme.errorContainer
            ) {
                Icon(Icons.Default.Delete, "Limpar Lista")
            }
        }
    ) {
        paddingValues ->

        if (itemList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhum item contado ainda.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(itemList, key = { it.barcode }) { item ->
                    StockListItem(
                        item = item,
                        onClick = { selectedItemForDetail = item },
                        onDelete = { onViewModel.onDeleteItem(item.barcode) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StockListItem(
    item: InventarioItem,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Bobina: ${item.coilId}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Tag: ${item.barcode}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${item.weight} kg  •  ${item.lastUpdate.toFormattedDate()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.width(16.dp))
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Deletar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun CoilDetailDialog(
    item: InventarioItem,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Detalhes da Bobina",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar")
                    }
                }

                Spacer(Modifier.height(16.dp))

                DetailRow("ID Bobina (G)", item.coilId, highlight = true)
                DetailRow("Peso", "${item.weight} kg")
                DetailRow("Espessura", item.thickness)
                DetailRow("Qualidade", item.quality)
                DetailRow("Cor", item.color)
                DetailRow("Localização", item.location)

                if (item.observation.isNotBlank()) {
                    DetailRow("Observação", item.observation)
                }

                DetailRow("Etiqueta (Barcode)", item.barcode)
                DetailRow("Data Leitura", item.lastUpdate.toFormattedDate())
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, highlight: Boolean = false) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = value.ifEmpty { "-" },
            style = if (highlight) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyLarge,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal,
            color = if (highlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun Long.toFormattedDate(): String {
    val sdf = remember { SimpleDateFormat("HH:mm:ss dd/MM/yy", Locale.getDefault()) }
    return sdf.format(Date(this))
}