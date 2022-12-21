package com.example.wiresag.utils

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class PermissionsRequest(private val parent: ComponentActivity, vararg val permissions: String) {
    private var requestResult: (granted: Boolean) -> Unit = {}
    private val request =
        parent.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.all { it.value }
            requestResult(granted)
        }

    fun check() = permissions.all {
        ContextCompat.checkSelfPermission(parent, it) == PackageManager.PERMISSION_GRANTED
    }

    fun launch(result: (granted: Boolean) -> Unit) {
        requestResult = result
        request.launch(permissions as Array<String>)
    }
}