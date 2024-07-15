package com.example.blerefrigerator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.blerefrigerator.ui.theme.BLERefrigeratorTheme

var door: MutableState<Boolean> = mutableStateOf(false)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        door.value=false
        val getBLE = GetBLE()
        super.onCreate(savedInstanceState)
            getBLE.startScan()
            setContent {
                BLERefrigeratorTheme {
                    // A surface container using the 'background' color from the theme
                    Screen()
                }
            }
        }
}

@Composable
fun Screen(){

        remember { door }
        if (door.value) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Red
            ) {
                GreetingText(
                    message = "開いている"
                )
            }
        } else {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Cyan
            ) {
                GreetingText(
                    message = "閉じている"
                )
            }
        }
}

@Composable
fun GreetingText(message: String) {
    Box(contentAlignment = Alignment.Center) {
        Text(
            text = message,
            fontSize = 70.sp,
            lineHeight = 116.sp,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun BirthdayCardPreview() {
    BLERefrigeratorTheme {
        GreetingText(message = "Happy Birthday Sam!")
    }
}