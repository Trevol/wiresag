package com.example.wiresag.viewModel.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import com.example.wiresag.ui.stopClickPropagation
import com.example.wiresag.viewModel.WireSagViewModel
import com.example.wiresag.viewModel.views.annotation.WireSagAnnotation
import com.example.wiresag.viewModel.views.mapView.MapView
import com.example.wiresag.viewModel.views.settings.Settings

@Composable
fun WireSagViewModel.View() {
    saveDependencyEval()
    Box{
        DialogsBox {
            Settings()
            WireSagAnnotation()
        }
        MapView()
    }
}

@Composable
private fun DialogsBox(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .zIndex(1f)
            .background(Color.White)
            .stopClickPropagation()
        ,
        content = content
    )
}

private inline fun WireSagViewModel.saveDependencyEval() {
    objectContext.saveRequest
}