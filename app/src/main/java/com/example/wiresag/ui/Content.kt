package com.example.wiresag.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.wiresag.ui.theme.WireSagTheme

@Composable
fun Main(content: @Composable () -> Unit) {
    WireSagTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            content()
        }
    }
}

@Composable
fun NoPermissions() = Text(text = "No permissions!")

