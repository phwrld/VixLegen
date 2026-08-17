package com.example.vixlegenverso10.TelaDeInicio

import androidx.compose.foundation.background
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vixlegenverso10.ui.Routes.BordoPrincipal
import com.example.vixlegenverso10.ui.Routes.FonteSerifadaVix
import com.example.vixlegenverso10.ui.Routes.FundoBranco
import com.example.vixlegenverso10.ui.Routes.VixLegenVersão10Theme

@Composable
fun SplashScreen(onTimeOut: () -> Unit) {
    // Efeito que Roda quando entra na tela de exibição
    LaunchedEffect(Unit) {
        delay(3000)
        onTimeOut()
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(FundoBranco),
        contentAlignment = Alignment.Center

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)


        ) {
            Text(
                text = "Vix Legen",
                fontSize = 42.sp,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FonteSerifadaVix,
                fontWeight = FontWeight.Bold,
                color = BordoPrincipal

            )
            Text(
                text = "O Direito de forma simples",
                fontSize = 16.sp,
                fontFamily = FonteSerifadaVix,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            CircularProgressIndicator(
                color = BordoPrincipal,
                strokeWidth = 3.dp
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    VixLegenVersão10Theme {
        SplashScreen(onTimeOut = {})
    }
}