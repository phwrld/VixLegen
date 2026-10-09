package com.example.vixlegenverso10.AreaAdvogado

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.vixlegenverso10.R
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.style.TextOverflow
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
    var confirmarSaida by remember { mutableStateOf(false) }
    val pretoLegivel = Color(0xFF1C1C1C)

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

    if (confirmarSaida) {
        AlertDialog(
            onDismissRequest = { confirmarSaida = false },
            title = { Text("Encerrar sessão?", color = BordoEscuro, fontWeight = FontWeight.Bold) },
            text = { Text("Deseja sair da sua área jurídica? Você poderá entrar novamente quando quiser.", color = Color(0xFF1C1C1C)) },
            confirmButton = {
                Button(onClick = { confirmarSaida = false; onSairClick() }, colors = ButtonDefaults.buttonColors(containerColor = BordoPrincipal)) {
                    Text("Sim, sair", color = Color.White)
                }
            },
            dismissButton = { TextButton(onClick = { confirmarSaida = false }) { Text("Continuar aqui", color = BordoEscuro) } },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
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
                .padding(horizontal = 22.dp, vertical = 26.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(shape = RoundedCornerShape(16.dp), color = Color.White) {
                        Image(
                            painter = painterResource(id = R.drawable.vixlgicon),
                            contentDescription = "Logo VixLegen",
                            modifier = Modifier.size(62.dp).padding(6.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("VIX LEGEN", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.4.sp)
                        Spacer(Modifier.height(3.dp))
                        Text("Área do Advogado", color = Color.White, fontSize = 22.sp, fontFamily = FonteSerifadaVix, fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = { confirmarSaida = true },
                        border = BorderStroke(1.dp, Color.White),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) { Text("Sair", color = Color.White, fontWeight = FontWeight.Bold) }
                }
                Spacer(Modifier.height(14.dp))
                Text("Seu painel jurídico", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Text("Acompanhe seus processos com clareza e organização.", color = Color.White, fontSize = 13.sp)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BordoPrincipal.copy(alpha = 0.22f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text("Visão geral", fontSize = 13.sp, color = pretoLegivel, fontWeight = FontWeight.Medium)
                    Spacer(Modifier.height(6.dp))
                    Text("${listaProcessos.size} processos cadastrados", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = BordoEscuro)
                    Spacer(Modifier.height(3.dp))
                    Text("Consulte e encontre seus registros abaixo.", fontSize = 13.sp, color = pretoLegivel)
                }
            }
            Spacer(Modifier.height(20.dp))
            OutlinedTextField(
                value = busca,
                onValueChange = { busca = it },
                label = { Text("Número do processo ou cliente", color = pretoLegivel) },
                placeholder = { Text("Buscar processos...", color = pretoLegivel) },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = pretoLegivel, unfocusedTextColor = pretoLegivel, focusedContainerColor = Color.White, unfocusedContainerColor = Color.White, focusedBorderColor = BordoPrincipal, unfocusedBorderColor = BordoPrincipal, focusedLabelColor = pretoLegivel, unfocusedLabelColor = pretoLegivel, focusedPlaceholderColor = pretoLegivel, unfocusedPlaceholderColor = pretoLegivel, cursorColor = BordoPrincipal),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Meus Processos (${processosFiltrados.size})",
                fontSize = 21.sp,
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
                Box(
                    modifier = Modifier.fillMaxSize().padding(top = 20.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, BordoPrincipal.copy(alpha = 0.2f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 30.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.vixlgicon),
                                contentDescription = "VixLegen",
                                modifier = Modifier.size(80.dp)
                            )
                            Spacer(Modifier.height(14.dp))
                            Text(
                                if (busca.isNotBlank()) "Nenhum resultado encontrado" else "Seu espaço de processos",
                                fontFamily = FonteSerifadaVix,
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold,
                                color = BordoEscuro,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                if (busca.isNotBlank()) "Tente pesquisar por outro nome ou número de processo."
                                else "Os processos vinculados à sua conta aparecerão aqui assim que estiverem disponíveis.",
                                color = pretoLegivel,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 21.sp
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
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
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(1.dp, BordoPrincipal.copy(alpha = 0.13f)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = processo.numeroProcesso,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = BordoPrincipal
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Cliente: ${processo.clienteNome ?: "Não informado"}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = BordoEscuro
            )

            if (!processo.vara.isNullOrEmpty() || !processo.comarca.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${processo.vara ?: ""} - ${processo.comarca ?: ""}",
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Status: ${processo.status ?: "Em andamento"}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = BordoEscuro
                )
            }
        }
    }
}