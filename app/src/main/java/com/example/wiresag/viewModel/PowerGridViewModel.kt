package com.example.wiresag.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.wiresag.model.Pylon

class PowerGridViewModel(
    val pylons: SnapshotStateList<Pylon> = mutableStateListOf()
)