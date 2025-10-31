package com.barrichello.inventarioapp.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.barrichello.inventarioapp.ui.theme.Theme
val primaryOrange = Color(0xFFF39C12)
val darkBlueGray = Color(0xFF42474E)
val lightGrayBg = Color(0xFFF3F4F6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onNavigateToScanner: () -> Unit,
    onNavigateToStockList: () -> Unit,
    onNavigateToExport: () -> Unit
) {
    val totalItemCount by viewModel.totalItemCount.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventário de Aço") }
            )
        }
    ) { paddingValues ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            // Card de Resumo Visual
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ITENS ATUAIS NO INVENTÁRIO",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "$totalItemCount",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Bobinas",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = onNavigateToScanner,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryOrange,
                    contentColor = Color.White,
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Iniciar Contagem"
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = "INICIAR CONTAGEM", fontSize = 16.sp)
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onNavigateToStockList,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "Visualizar Estoque"
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = "VISUALIZAR ESTOQUE", fontSize = 16.sp)
                }
            }

            Spacer(Modifier.height(16.dp))

            OutlinedButton(
                onClick = onNavigateToExport,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = lightGrayBg,
                    contentColor = darkBlueGray
                ),
                border = BorderStroke(1.dp, darkBlueGray)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.FileUpload,
                        contentDescription = "Finalizar e Exportar"
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "FINALIZAR E EXPORTAR",
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    Theme {
        DashboardScreen(
            onNavigateToScanner = {},
            onNavigateToStockList = {},
            onNavigateToExport = {}
        )
    }
}