package com.example.w05

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.w05.ui.theme.W05Theme
// Coroutines의 delay 함수를 사용하기 위해 import 추가
import kotlinx.coroutines.delay
// 시간 포맷을 위한 java.util.concurrent.TimeUnit import 추가
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            W05Theme {
                val count = remember { mutableStateOf(0) }
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    CounterApp(count)
                    StopWatchApp()
                    ColorToggleButtonApp()
                }
            }
        }
    }
}


@Composable
fun CounterApp(count: MutableState<Int>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Count: ${count.value}",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Row {
            Button(onClick = { count.value++ }) { Text("Increase") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { count.value = 0 }) { Text("Reset") }
        }
    }
}

// timeInMillis를 "MM:SS.L" 형식으로 변환하는 함수 추가
fun formatTime(timeInMillis: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis) % 60
    val milliseconds = (timeInMillis / 10) % 100 // 10밀리초 단위로 표시
    return String.format("%02d:%02d.%02d", minutes, seconds, milliseconds)
}

@Composable
fun StopWatchApp() {
    // 1. 시간(밀리초)과 타이머 실행 여부를 기억할 State 변수 추가
    var timeInMillis by remember { mutableStateOf(0L) } // 초기 시간을 0L으로 변경
    var isRunning by remember { mutableStateOf(false) }

    // 1. isRunning 상태가 true일 때만 실행되는 LaunchedEffect 추가
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (true) {
                delay(10L) // 10밀리초마다
                timeInMillis += 10L // 시간을 10밀리초씩 증가
            }
        }
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = formatTime(timeInMillis), // 2. State 변수를 사용해 시간 표시
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        Row {
            // 3. 버튼 클릭 시 isRunning 상태를 변경
            Button(onClick = { isRunning = true }) { Text("Start") }
            Spacer(modifier = Modifier.width(8.dp)) // 버튼 사이에 간격 추가
            Button(onClick = { isRunning = false }) { Text("Stop") }
            Spacer(modifier = Modifier.width(8.dp)) // 버튼 사이에 간격 추가
            Button(onClick = {
                isRunning = false
                timeInMillis = 0L
            }) { Text("Reset") }
        }
    }
}

@Composable
fun ColorToggleButtonApp() {
    var currentColor by remember { mutableStateOf(Color.Red) }

    Box(
        modifier = Modifier.fillMaxWidth().height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(currentColor)
                .clickable {
                    currentColor = if (currentColor == Color.Red) Color.Blue else Color.Red
                }
                .padding(30.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Click Me",
                color = Color.White,
                fontSize = 30.sp
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CounterAppPreview() {
    W05Theme {
        val dummyCount = remember { mutableStateOf(0) }
        CounterApp(dummyCount)
    }
}

@Preview(showBackground = true)
@Composable
fun StopWatchPreview() {
    StopWatchApp()
}

@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Composable
fun ColorToggleButtonAppPreview() {
    ColorToggleButtonApp()
}