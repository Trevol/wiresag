package com.example.sensors

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import com.example.sensors.ui.theme.SensorsTheme
import com.example.sensors.utils.round
import kotlin.math.sqrt


class MainActivityForSensors : ComponentActivity() {
    private val sensorType = Sensor.TYPE_LINEAR_ACCELERATION

    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor

    val values = mutableStateOf(FloatArray(3))

    private val sensorListener = object : SensorEventListener {
        override fun onSensorChanged(sensorEvent: SensorEvent?) {
            values.value = sensorEvent?.values?.copyOf() ?: FloatArray(3)
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) = Unit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SensorsTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        for (v in values.value) {
                            Text(text = v.round(2).toString())
                        }
                        Text(text = sqrt(values.value.map { it * it }.sum()).round(2).toString())
                    }
                }
            }
        }

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(sensorType)

    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorListener)
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SensorsTheme {
        Greeting("Android")
    }
}