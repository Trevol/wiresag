package com.example.wiresag.viewModel.views.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import com.example.wiresag.ui.components.Icon
import com.example.wiresag.ui.components.TransparentButton
import com.example.wiresag.viewModel.WireSagViewModel

@Composable
fun WireSagViewModel.Settings() {
    if (settingsMode) {
        BackHandler(onBack = { settingsMode = false })
        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .zIndex(1f)
        ) {
            Row {
                Icon(
                    Icons.Outlined.Done,
                    modifier = Modifier
                        .padding(ButtonDefaults.ContentPadding)
                        .clickable { settingsMode = false }
                )

            }
            Text("Settings!!!")
        }
    }
}