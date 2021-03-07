package com.example.countdowntimer

import android.icu.text.DecimalFormat
import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.countdowntimer.ui.theme.*
import com.example.countdowntimer.ui.theme.Green2BAE66FF


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun CountDownTimerScreen(totalIntervalTimer : Long) {
    //Log.d("TAG", "CountDownTimerScreen")

    val decFormat = DecimalFormat("00")
    var min: Long by remember { mutableStateOf(0) }
    var sec: Long by remember { mutableStateOf(0) }
    var updatedMinValue: String by remember { mutableStateOf("00") }
    var updatedSecValue: String by remember { mutableStateOf("00") }
    var isTimerRunning: Boolean by remember { mutableStateOf(false) }
    var progress: Float by remember { mutableStateOf(1F) }
    val animatedProgress : Float by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    var countDownTimer : CountDownTimer? = object : CountDownTimer(totalIntervalTimer, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            isTimerRunning = true
            min = millisUntilFinished / 60000 % 60
            sec = millisUntilFinished / 1000 % 60

            var mainSec = (totalIntervalTimer / 1000).toFloat()
            var coveredSec = (millisUntilFinished / 1000).toFloat()

            progress = (coveredSec / mainSec).toFloat()

            updatedMinValue = decFormat.format(min)
            updatedSecValue = decFormat.format(sec)

        }

        override fun onFinish() {
            progress = 1F
            isTimerRunning = false
            updatedMinValue = "00"
            updatedSecValue = "00"
        }
    }


    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Purple200)
    ) {

        Box(
            Modifier
                .fillMaxHeight(animatedProgress)
                .fillMaxWidth(animatedProgress)
                .align(Alignment.Center)
                .background(Color.White)

        )
        Box(
            Modifier
                .background(Color.Transparent)
                .fillMaxWidth()
                .fillMaxHeight()

        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Black)) {
                            append(updatedMinValue)
                        }
                        append(" : ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black   )) {
                            append(updatedSecValue)
                        }

                    },
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier.align(Alignment.CenterHorizontally)

                )

                Spacer(Modifier.size(40.dp))

                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Button(
                        onClick = { if (!isTimerRunning) {
                            countDownTimer!!.start()
                            progress = 1f
                        } },
                    ) {
                        Text("START")
                    }

                }


            }
        }
    }



}
