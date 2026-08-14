package com.example.vixlegenverso10.TelaDeInicio

import android.window.SplashScreen
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeOut: () -> Unit) {
    // Efeito que Roda quando entra na tela de exibição
    LaunchedEffect(Unit) {
        delay(3000)
        onTimeOut()
    }

    Box (
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)


        ) {
            Text(
                text = "Vix Legen",
                style = MaterialTheme.typography.bodyMedium

            )
            Text(
                text = "O Direito de forma simples",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            CircularProgressIndicator()
        }
    }
}