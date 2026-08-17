package com.example.vixlegenverso10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.vixlegenverso10.AreaAdvogado.AreaAdvogadoScreen
import com.example.vixlegenverso10.TelaDeInicio.SplashScreen
import com.example.vixlegenverso10.TelaDeLogin.LoginScreen
import com.example.vixlegenverso10.ui.Routes.VixLegenVersão10Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VixLegenVersão10Theme {
                var telaAtual by remember { mutableStateOf("splash") }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (telaAtual) {
                            "splash" -> SplashScreen(
                                onTimeOut = { telaAtual = "login" }
                            )

                            "login" -> LoginScreen(
                                onLoginClick = { telaAtual = "advogado" }
                            )

                            "advogado" -> AreaAdvogadoScreen(
                                onSairClick = { telaAtual = "login" }
                            )
                        }
                    }
                }
            }
        }
    }
}
