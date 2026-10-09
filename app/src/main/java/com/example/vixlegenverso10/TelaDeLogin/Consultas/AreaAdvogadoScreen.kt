package com.example.vixlegenverso10.AreaAdvogado

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.OutlinedTextFieldDefaults
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

    Column(modifier = Modifier.fillMaxSize().background(FundoBranco)) {
        Column(
            modifier = Modifier.fillMaxWidth().background(BordoPrincipal)
                .padding(horizontal = 24.dp, vertical = 26.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "PAINEL JURÍDICO",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 2.sp,
                        color = Color.White.copy(alpha = 0.82f)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Área do Advogado",
                        fontFamily = FonteSerifadaVix,
                        fontWeight = FontWeight.Bold,
                        fontSize = 27.sp,
                        color = Color.White
                    )
                }
                TextButton(onClick = onSairClick) {
                    Text("Sair", fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(
                "Consulte e acompanhe seus processos em um só lugar.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.88f)
            )
        }

        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 22.dp)) {
            Text(
                "Consultar processos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = BordoEscuro
            )
            Spacer(Modifier.height(5.dp))
            Text(
                "Encontre um processo pelo número ou nome do cliente.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = busca,
                onValueChange = { busca = it },
                label = { Text("Número do processo ou cliente") },
                placeholder = { Text("Digite para buscar") },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BordoPrincipal,
                    focusedLabelColor = BordoPrincipal,
                    cursorColor = BordoPrincipal,
                    unfocusedBorderColor = BordoEscuro.copy(alpha = 0.45f)
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Meus processos",
                    fontFamily = FonteSerifadaVix,
                    fontWeight = FontWeight.Bold,
                    fontSize = 21.sp,
                    color = BordoEscuro
                )
                Text(
                    "${processosFiltrados.size} encontrado(s)",
                    style = MaterialTheme.typography.labelMedium,
                    color = BordoPrincipal
                )
            }
            Spacer(Modifier.height(14.dp))
            if (carregando) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BordoPrincipal)
                }
            } else if (processosFiltrados.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Nenhum processo encontrado",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = BordoEscuro,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            if (busca.isNotBlank()) "Nenhum resultado para \"$busca\"."
                            else "Nenhum processo vinculado a esta conta.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    items(processosFiltrados) { processo -> CardProcessoItem(processo) }
                }
            }
        }
    }
}

@Composable
fun CardProcessoItem(processo: ProcessoJuridico) {
    Card(
        modifier = Modifier.fillMaxWidth().border(
            1.dp, BordoPrincipal.copy(alpha = 0.14f), RoundedCornerShape(16.dp)
        ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text("PROCESSO", style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold, letterSpacing = 1.3.sp, color = Color.Gray)
            Spacer(Modifier.height(5.dp))
            Text(
                processo.numeroProcesso,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = BordoPrincipal
            )
            Spacer(Modifier.height(15.dp))
            Text("CLIENTE", style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold, color = Color.Gray)
            Text(
                processo.clienteNome ?: "Não informado",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            if (!processo.vara.isNullOrEmpty() || !processo.comarca.isNullOrEmpty()) {
                Spacer(Modifier.height(10.dp))
                Text(
                    listOfNotNull(processo.vara?.takeIf { it.isNotBlank() },
                        processo.comarca?.takeIf { it.isNotBlank() }).joinToString(" • "),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }
            Spacer(Modifier.height(15.dp))
            HorizontalDivider(color = BordoPrincipal.copy(alpha = 0.13f))
            Spacer(Modifier.height(12.dp))
            Text(
                "Status: ${processo.status ?: "Em andamento"}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = BordoEscuro
            )
        }
    }
}
