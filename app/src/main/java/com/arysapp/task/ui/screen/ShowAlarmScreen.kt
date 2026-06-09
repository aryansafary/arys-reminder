package com.arysapp.task.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arysapp.task.R
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun ShowAlarmScreen(
    taskId: Long,
    title: String,
    description: String,
    onStopClick: () -> Unit
) {
//    val currentTime = remember {
//        val calendar = Calendar.getInstance()
//        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
//        formatter.format(calendar.time)
//    }


    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    var currentTime by remember {
        mutableStateOf(timeFormatter.format(Calendar.getInstance().time))
    }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = timeFormatter.format(Calendar.getInstance().time)
            delay(1000.milliseconds)
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = currentTime,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary ,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = title, style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = description, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(80.dp))

            Button(
                onClick = onStopClick,
                modifier = Modifier.fillMaxWidth(0.85f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                contentPadding = PaddingValues(16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.stop),
                    contentDescription = "stop"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    stringResource(R.string.stop),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}
@Preview
@Composable
fun ShowAlarmScreenPreview(){
 ShowAlarmScreen(
     taskId = 1,
     title = "",
     description = "" ,
 ){}
}