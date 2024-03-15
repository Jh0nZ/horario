package com.example.horario

import android.annotation.SuppressLint
import android.widget.Space
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun ThreeTabSlider(
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val initialPage = ((today.dayOfWeek.value-1) % 7)
    val pagerState = rememberPagerState(
        pageCount = {
            7
        },
        initialPage = initialPage
    )
    val corutinaaa = rememberCoroutineScope()
    Column {
        Row (
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            for (a in 0..6) {
                IconButton(onClick = {
                    corutinaaa.launch {
                        pagerState.scrollToPage(a)
                    }

                }) {
                    Text(
                        text = (a).toString(),
                        modifier = modifier
                            .background(
                                if (pagerState.currentPage == a) {
                                    Color(39, 181, 247, 255)
                                } else {
                                    Color(151, 217, 248, 255)
                                },

                                shape = RoundedCornerShape(50)
                            )
                            .size(40.dp)
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = modifier.fillMaxSize()
        ) { page ->
            val dayOfWeek = when (page) {
                0 -> DayOfWeek.MONDAY
                1 -> DayOfWeek.TUESDAY
                2 -> DayOfWeek.WEDNESDAY
                3 -> DayOfWeek.THURSDAY
                4 -> DayOfWeek.FRIDAY
                5 -> DayOfWeek.SATURDAY
                else -> DayOfWeek.SUNDAY
            }
            Row (
                modifier = modifier.fillMaxSize()
            ) {
                horas()
                pruebasDias(
                    nombre = dayOfWeek.toString(),
                    ancho = 400.dp,
                    modifier = modifier.fillMaxWidth(),
                    intervalosss = testHorario().obtenerDiaFormato(dayOfWeek))

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Clock() {

    val today = LocalDate.now()
    var currentTime by remember { mutableStateOf(Calendar.getInstance()) }

    LaunchedEffect(key1 = true) {
        while (true) {
            delay(1000) // Espera 1 minuto
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
                            val lista = testHorario().obtenerDiaFormato24h(today.dayOfWeek)
                            if (!lista.isEmpty()) {
                                for (invertalo in lista) {
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
                                                    maxLines = 1
                                                )
                                                Text(
                                                    text = invertalo.aula,
                                                    maxLines = 1
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

fun main() {
    val today = LocalDate.now()
    val initialPage = ((DayOfWeek.FRIDAY.value-1) % 7)
    println(initialPage)
}