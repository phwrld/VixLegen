package com.example.vixlegenverso10.AreaAdvogado

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vixlegenverso10.network.ProcessoJuridico
import com.example.vixlegenverso10.network.RetrofitClient
import com.example.vixlegenverso10.ui.Routes.BordoEscuro
import com.example.vixlegenverso10.ui.Routes.BordoPrincipal
import com.example.vixlegenverso10.ui.Routes.FonteSerifadaVix
import com.example.vixlegenverso10.ui.Routes.FundoBranco
import java.text.Normalizer

fun String.removerAcentos(): String {
    val unaccented = Normalizer.normalize(this, Normalizer.Form.NFD)
    return Regex("\\p{InCombiningDiacriticalMarks}+").replace(unaccented, "")
}

@Composable
fun AreaAdvogadoScreen(onSairClick: () -> Unit) {
    var busca by remember { mutableStateOf("") }
    var listaProcessos by remember { mutableStateOf<List<ProcessoJuridico>>(emptyList()) }
    var carregando by remember { mutableStateOf(true) }

    // Busca os dados cadastrados no MySQL via API
    LaunchedEffect(Unit) {
        try {
            listaProcessos = RetrofitClient.instance.getProcessos()
        } catch (e: Exception) {
            // Se não houver banco ativo ou processos vinculados, lista vazia
            listaProcessos = emptyList()
        } finally {
            carregando = false
        }
    }

    // Filtragem em tempo real da busca
    val processosFiltrados = remember(busca, listaProcessos) {
        if (busca.isBlank()) {
            listaProcessos
        } else {
            val buscaLimpa = busca.trim().removerAcentos().lowercase()
            val buscaApenasNumeros = busca.filter { it.isDigit() }

            listaProcessos.filter { processo ->
                val clienteLimpo = (processo.clienteNome ?: "").removerAcentos().lowercase()
                val numeroApenasNumeros = processo.numeroProcesso.filter { it.isDigit() }

                clienteLimpo.contains(buscaLimpa) ||
                        processo.numeroProcesso.lowercase().contains(buscaLimpa) ||
                        (buscaApenasNumeros.isNotEmpty() && numeroApenasNumeros.contains(buscaApenasNumeros))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoBranco)
    ) {
        // Cabeçalho da área restrita
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BordoPrincipal)
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Área do Advogado",
                        fontSize = 22.sp,
                        fontFamily = FonteSerifadaVix,
                        color = Color.White
                    )
                    TextButton(onClick = onSairClick) {
                        Text("Sair", color = Color.White.copy(alpha = 0.8f))
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "📱 Consulta rápida de processos",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = busca,
                onValueChange = { busca = it },
                label = { Text("Buscar por nº do processo ou cliente") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Meus Processos (${processosFiltrados.size})",
                fontSize = 18.sp,
                fontFamily = FonteSerifadaVix,
                fontWeight = FontWeight.Bold,
                color = BordoEscuro
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (carregando) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    CircularProgressIndicator(color = BordoPrincipal)
                }
            } else if (processosFiltrados.isEmpty()) {
                // Tela sem processos cadastrados
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 60.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = if (busca.isNotBlank())
                            "Nenhum processo encontrado para \"$busca\"."
                        else
                            "Nenhum processo vinculado a esta conta.",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(processosFiltrados) { processo ->
                        CardProcessoItem(processo)
                    }
                }
            }
        }
    }
}

@Composable
fun CardProcessoItem(processo: ProcessoJuridico) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = processo.numeroProcesso,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = BordoPrincipal
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Cliente: ${processo.clienteNome ?: "Não informado"}",
                fontSize = 14.sp,
                color = Color.Black
            )

            if (!processo.vara.isNullOrEmpty() || !processo.comarca.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${processo.vara ?: ""} - ${processo.comarca ?: ""}",
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Status: ${processo.status ?: "Em andamento"}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}