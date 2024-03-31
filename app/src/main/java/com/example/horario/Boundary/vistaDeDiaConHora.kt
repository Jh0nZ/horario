package com.example.horario.Boundary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.backstack_tests.Control.VistaBackStack
import com.example.horario.Control.Horario
import com.example.horario.Control.Tiempo
import com.example.horario.Control.obtenerHorasString
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

@Preview(showBackground = true)
@Composable
fun vistaDeDiaConHora(
    vistaBackStack: VistaBackStack = VistaBackStack()
) {

    val today = LocalDate.now()
    var currentTime by remember { mutableStateOf(Calendar.getInstance()) }

    LaunchedEffect(key1 = true) {
        while (true) {
            delay(1000)
            currentTime = Calendar.getInstance()
        }
    }

    val formattedTime = remember(currentTime.time) {
        SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(currentTime.time)
    }

    val minutos = remember(currentTime.time) {
        currentTime.get(Calendar.HOUR_OF_DAY)*60 + currentTime.get(Calendar.MINUTE)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formattedTime,
                color = Color.Black,
            )
            Text(text = minutos.toString())
        }
        LazyColumn {
            item {
                Box {
                    Row {
                        Column(
                            modifier = Modifier
                                .height((24 * 60).dp)
                        ) {
                            for (texto in obtenerHorasString(
                                60,
                                Tiempo(),
                                Tiempo(23, 59)
                            ) ){
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .width(100.dp)
                                ) {
                                    Text(
                                        text = texto,
                                        maxLines = 1
                                    )
                                }
                            }
                        }

                        Column(
                            modifier = Modifier
                                .height((24 * 60).dp)
                        ) {
                            //today.dayOfWeek
                            val lista = vistaBackStack.horario.obtenerDiaFormato24h(today.dayOfWeek)
                            if (!lista.isEmpty()) {
                                for (invertalo in lista) {
                                    val colorTexto = if (CalcularLuminosidad(invertalo.color) < 0.5) Color.White else Color.Black
                                    Box(
                                        modifier = Modifier
                                            .height(invertalo.duracion.dp)
                                            .fillMaxWidth()
                                            .background(invertalo.color),
                                    ) {
                                        if (invertalo.nombre != null) {
                                            Column(
                                                verticalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = invertalo.nombre,
                                                    maxLines = 1,
                                                    color = colorTexto
                                                )
                                                Text(
                                                    text = invertalo.aula,
                                                    maxLines = 1,
                                                    color = colorTexto
                                                )
                                            }
                                        }


                                    }
                                }
                            }
                        }

                    }

                    Column {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height((minutos).dp)

                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color.Magenta)
                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(((24 * 60) - (minutos) - 1).dp)

                        )

                    }
                }
            }
        }

    }
}
