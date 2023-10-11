package com.example.wiresag.utils

import androidx.compose.runtime.*

@Composable
fun <T> rememberMutableStateOf(
    value: T,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy()
) = remember { mutableStateOf(value = value, policy = policy) }

@Composable
fun <T> rememberDerivedStateOf(calculation: () -> T) = remember { derivedStateOf(calculation) }