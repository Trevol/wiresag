package com.example.wiresag.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun Icon(imageVector: ImageVector, modifier: Modifier = Modifier) {
    Icon(imageVector = imageVector, contentDescription = "", modifier = modifier)
}

@Composable
fun Icon(@DrawableRes id: Int, modifier: Modifier = Modifier) {
    Icon(painter = painterResource(id), contentDescription = "", modifier = modifier)
}