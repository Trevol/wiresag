package com.example.wiresag.utils

import androidx.compose.runtime.*

@Composable
fun <T> rememberMutableStateOf(
    value: T,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy()
): MutableState<T> {
    return remember { mutableStateOf(value = value, policy = policy) }
}