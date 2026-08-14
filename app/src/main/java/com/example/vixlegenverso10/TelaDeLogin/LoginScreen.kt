package com.example.vixlegenverso10.TelaDeLogin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vixlegenverso10.Greeting
import com.example.vixlegenverso10.ui.theme.FonteSerifadaVix
import com.example.vixlegenverso10.ui.theme.FundoBranco
import com.example.vixlegenverso10.ui.theme.VixLegenVersão10Theme

@Composable
fun LoginScreen (onLoginClick: ()  -> Unit) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoBranco)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text (
            text = "V.L",
            fontSize = 54.sp,
            fontFamily = FonteSerifadaVix,
            fontWeight = FontWeight.Bold

        )

    }
}