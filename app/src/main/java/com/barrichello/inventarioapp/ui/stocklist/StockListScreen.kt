package com.barrichello.inventarioapp.ui.stocklist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                    text = item.barcode,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Contado em: ${item.lastUpdate.toFormattedDate()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.width(16.dp))
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Deletar Item",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun Long.toFormattedDate(): String {
    val sdf = remember { SimpleDateFormat("HH:mm:ss dd/MM/yy", Locale.getDefault()) }
    return sdf.format(Date(this))
}