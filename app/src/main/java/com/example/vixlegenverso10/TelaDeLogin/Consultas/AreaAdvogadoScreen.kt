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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vixlegenverso10.ui.Routes.BordoEscuro
import com.example.vixlegenverso10.ui.Routes.BordoPrincipal
import com.example.vixlegenverso10.ui.Routes.FonteSerifadaVix
import com.example.vixlegenverso10.ui.Routes.FundoBranco
import com.example.vixlegenverso10.ui.Routes.VixLegenVersão10Theme
import java.text.Normalizer

data class Processo(
    val numero: String,
    val cliente: String,
    val status: String,
    val ultimaAtualizacao: String
)


fun String.removerAcentos(): String {
    val unaccented = Normalizer.normalize(this, Normalizer.Form.NFD)
    return Regex("\\p{InCombiningDiacriticalMarks}+").replace(unaccented, "")
}

@Composable
fun AreaAdvogadoScreen(onSairClick: () -> Unit) {
    var busca by remember { mutableStateOf("") }

    // Dados fictícios do app
    val listaProcessos = remember {
        listOf(
            Processo("0001234-88.2026.8.08.0001", "João Silva", "Andamento Normal", "Hoje às 10:30"),
            Processo("0009876-11.2025.8.08.0001", "Maria Oliveira", "Petição Juntada", "Ontem"),
            Processo("0004567-33.2026.8.08.0001", "Tech Soluções LTDA", "Aguardando Despacho", "12/08/2026")
        )
    }

    // Lógica de Filtragem da Busca
    val processosFiltrados = remember(busca, listaProcessos) {
        if (busca.isBlank()) {
            listaProcessos
        } else {
            val buscaLimpa = busca.trim().removerAcentos().lowercase()
            val buscaApenasNumeros = busca.filter { it.isDigit() }

            listaProcessos.filter { processo ->
                val clienteLimpo = processo.cliente.removerAcentos().lowercase()
                val numeroApenasNumeros = processo.numero.filter { it.isDigit() }


                clienteLimpo.contains(buscaLimpa) ||
                        processo.numero.lowercase().contains(buscaLimpa) ||
                        (buscaApenasNumeros.isNotEmpty() && numeroApenasNumeros.contains(buscaApenasNumeros))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoBranco)
    ) {
        // Cabeçalho da Área do Advogado
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


            if (processosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = "Nenhum processo encontrado para \"$busca\"",
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
fun CardProcessoItem(processo: Processo) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = processo.numero,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = BordoPrincipal
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Cliente: ${processo.cliente}",
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Status: ${processo.status}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = processo.ultimaAtualizacao,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AreaAdvogadoScreenPreview() {
    VixLegenVersão10Theme {
        AreaAdvogadoScreen(onSairClick = {})
    }
}