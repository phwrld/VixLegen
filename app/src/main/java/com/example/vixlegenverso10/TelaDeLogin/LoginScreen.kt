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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vixlegenverso10.R
import com.example.vixlegenverso10.ui.theme.BordoEscuro
import com.example.vixlegenverso10.ui.theme.BordoPrincipal
import com.example.vixlegenverso10.ui.theme.FonteSerifadaVix
import com.example.vixlegenverso10.ui.theme.FundoBranco
import com.example.vixlegenverso10.ui.theme.VixLegenVersão10Theme

@Composable
fun LoginScreen(onLoginClick: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoBranco)
    ) {

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


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo do VIX
            Image(
                painter = painterResource(id = R.drawable.vixlgicon),
                contentDescription = "Logo VixLegen",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 20.dp)
            )

            // Campo E-mail / Documento
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail, CPF ou CNPJ 💼") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Senha
            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Senha") },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botão Entrar
            Button(
                onClick = onLoginClick,
                colors = ButtonDefaults.buttonColors(containerColor = BordoPrincipal),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
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
