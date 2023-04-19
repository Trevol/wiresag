package com.example.wiresag.viewModel.views

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.wiresag.viewModel.WireSagViewModel
import com.example.wiresag.viewModel.views.annotation.WireSagAnnotation
import com.example.wiresag.viewModel.views.mapView.Map
import com.example.wiresag.viewModel.views.settings.Settings

@Composable
fun WireSagViewModel.View() {
    saveDependencyEval()
    Settings()
    WireSagAnnotation()
    Map()
}

@Composable
private fun Dialogs() {
    Box(modifier = Modifier) {}
}

private inline fun WireSagViewModel.saveDependencyEval() {
    objectContext.saveRequest
}