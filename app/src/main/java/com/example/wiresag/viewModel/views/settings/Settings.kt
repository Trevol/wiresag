package com.example.wiresag.viewModel.views.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
        Column(modifier = Modifier.fillMaxSize()) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.ArrowBack,
                    modifier = Modifier
                        .padding(ButtonDefaults.ContentPadding)
                        .clickable { settingsMode = false }
                )
                Text("Настройки")
            }


            with(settings) {
                Row {
                    Text("Data: ")
                    Text("$dataDirectory")
                }

                Row {
                    Text("Images: ")
                    Text("$imagesDirectory")
                }

                Row {
                    Text("PhotoRequestTmp: ")
                    Text("$photoRequestTmpDirectory")
                }

                Row {
                    Text("SpanImages: ")
                    Text("$spanImagesDirectory")
                }
            }

            Button(onClick = { clearData() }) {
                Text("Стереть все данные")
            }
        }
    }
}