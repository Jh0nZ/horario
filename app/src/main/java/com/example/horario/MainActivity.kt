package com.example.horario

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import com.example.horario.ui.theme.HorarioTheme
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)

            HorarioTheme {
                when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.Compact -> {
                        Principal(100.dp, Modifier)
                    }
                    WindowWidthSizeClass.Expanded -> {
                        Principal(150.dp, Modifier)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, name = "horizontal",
    device = "spec:parent=pixel_5,orientation=landscape"
)
@Composable
fun Principal(
    ancho: Dp = 100.dp,
    modifier: Modifier = Modifier
) {
    var leftBarStatus by rememberSaveable {
        mutableStateOf(false)
    }
    var currentHeader by rememberSaveable {
        mutableStateOf("Overview")
    }

    Box (
        modifier = modifier.fillMaxSize()
    ) {
        Column (
            modifier = modifier.fillMaxSize()
        ) {
            Row (
                modifier = modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.LightGray)
            ) {
                IconButton(onClick = {leftBarStatus = !leftBarStatus}) {
                    Icon(
                        imageVector = Icons.Outlined.List,
                        contentDescription = "Icono de Estrella",
                        modifier = modifier
                            .padding(end = 8.dp)
                            .size(40.dp),
                    )
                }
                Text(
                    text = currentHeader,
                    fontSize = 22.sp
                )
            }
            when (currentHeader) {
                "Overview" -> ThreeTabSlider()
                "Horario" -> miHorario(ancho = ancho, horas = obtenerHorasString(90, Tiempo(6,45), Tiempo(14,15)))
                "Crear horario" -> Text(text = "Crear horario")
                "Horarios" -> verDiaDia(horas = obtenerHorasString(60, Tiempo(0,0), Tiempo(23,59)))
                "Escanear pdf" -> Clock()
            }

        }
        if (leftBarStatus) {
            Surface(
                modifier = modifier.fillMaxSize(),
                color = Color.Transparent

            ) {
                Box (
                    modifier = modifier.fillMaxSize(),
                ){
                    Column(
                        modifier = modifier
                            .background(
                                Color(224, 224, 224, 255),
                                shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
                            )
                            .width(280.dp)
                            .fillMaxHeight()
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "HORARIO",
                            color = Color(5, 184, 255, 255),
                            fontWeight = FontWeight.Bold,
                            fontSize = 40.sp
                        )
                        LazyColumn(
                            modifier = modifier
                                .fillMaxWidth()
                        ) {
                            items(listOf("Overview", "Horario", "Crear horario", "Horarios", "Escanear pdf")) {
                                Button(
                                    onClick = {
                                        leftBarStatus = false
                                        currentHeader = it
                                    },
                                    modifier = modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(85, 191, 248, 255),
                                        contentColor = Color.Black,
                                        disabledContainerColor = Color.DarkGray,
                                        disabledContentColor = Color.DarkGray
                                    )
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.Start,
                                        modifier = modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Home,
                                            contentDescription = null,
                                            modifier = modifier.padding(end = 8.dp)
                                        )

                                        Text(
                                            text = it,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}