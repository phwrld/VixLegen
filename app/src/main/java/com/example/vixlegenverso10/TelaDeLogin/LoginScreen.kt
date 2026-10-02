package com.example.vixlegenverso10.TelaDeLogin

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vixlegenverso10.R

// IMPORTS DAS CLASSES DA REDE
import com.example.vixlegenverso10.network.LoginRequest
import com.example.vixlegenverso10.network.RetrofitClient

// IMPORTS DAS CORES E FONTES DO PROJETO
import com.example.vixlegenverso10.ui.Routes.BordoEscuro
import com.example.vixlegenverso10.ui.Routes.BordoPrincipal
import com.example.vixlegenverso10.ui.Routes.FonteSerifadaVix
import com.example.vixlegenverso10.ui.Routes.FundoBranco

import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoginSucesso: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var cpfSenha by remember { mutableStateOf("") }
    var carregando by remember { mutableStateOf(false) }
    var mensagemErro by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoBranco)
    ) {
        // Faixas decorativas de fundo
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val topPath = Path().apply {
                moveTo(width * 0.45f, 0f)
                lineTo(width, 0f)
                lineTo(width, height * 0.32f)
                close()
            }
            drawPath(path = topPath, color = BordoPrincipal)

            val topSubPath = Path().apply {
                moveTo(width * 0.35f, 0f)
                lineTo(width * 0.45f, 0f)
                lineTo(width, height * 0.32f)
                lineTo(width, height * 0.37f)
                close()
            }
            drawPath(path = topSubPath, color = BordoEscuro)

            val bottomPath = Path().apply {
                moveTo(0f, height * 0.68f)
                lineTo(0f, height)
                lineTo(width * 0.55f, height)
                close()
            }
            drawPath(path = bottomPath, color = BordoPrincipal)

            val bottomSubPath = Path().apply {
                moveTo(0f, height * 0.63f)
                lineTo(0f, height * 0.68f)
                lineTo(width * 0.55f, height)
                lineTo(width * 0.65f, height)
                close()
            }
            drawPath(path = bottomSubPath, color = BordoEscuro)
        }

        // Formulário de Login
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.vixlgicon),
                contentDescription = "Logo VixLegen",
                modifier = Modifier
                    .size(140.dp)
                    .padding(bottom = 20.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it; mensagemErro = null },
                label = { Text("E-mail do Advogado") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !carregando
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = cpfSenha,
                onValueChange = { cpfSenha = it; mensagemErro = null },
                label = { Text("CPF") },
                shape = RoundedCornerShape(12.dp), // Removido o PasswordVisualTransformation
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !carregando
            )

            mensagemErro?.let { erro ->
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = erro,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {
                    if (email.isBlank() || cpfSenha.isBlank()) {
                        mensagemErro = "Preencha todos os campos cadastrados no sistema."
                        return@Button
                    }

                    carregando = true
                    mensagemErro = null

                    scope.launch {
                        try {
                            // Limpa pontuações do CPF para evitar incompatibilidade com a consulta
                            val cpfLimpo = cpfSenha.replace(".", "").replace("-", "").trim()
                            val request = LoginRequest(
                                email = email.trim(),
                                cpf = cpfLimpo
                            )

                            val response = RetrofitClient.instance.autenticarUsuario(request)

                            if (response.idUsuario > 0 || response.token != null) {
                                onLoginSucesso()
                            } else {
                                mensagemErro = "E-mail ou CPF incorretos."
                            }
                        } catch (e: Exception) {
                            mensagemErro = "Acesso negado: E-mail/CPF incorretos ou erro de conexão com o servidor."
                        } finally {
                            carregando = false
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BordoPrincipal),
                shape = RoundedCornerShape(12.dp),
                enabled = !carregando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (carregando) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "ENTRAR",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = FonteSerifadaVix
                    )
                }
            }
        }
    }
}