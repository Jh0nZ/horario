package com.example.horario.Boundary

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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
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
import com.example.backstack_tests.Control.VistaBackStack
import com.example.horario.Control.Tiempo
import com.example.horario.Control.obtenerHorasString
import com.example.horario.Control.testHorario
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
fun vistaDeDiaPager(
    modifier: Modifier = Modifier,
    vistaBack: VistaBackStack = VistaBackStack()
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
                        text = numeroADia(a),
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
                    ancho = 400.dp,
                    modifier = modifier.fillMaxWidth(),
                    horario = vistaBack.horario,
                    dia = dayOfWeek
                )
            }
        }
    }
}

fun numeroADia(nroDia: Int): String {
    return when (nroDia) {
        0 -> "Lun"
        1 -> "Mar"
        2 -> "Mie"
        3 -> "Jue"
        4 -> "Vie"
        5 -> "Sab"
        else -> "Dom"
    }
}

fun main() {
    val today = LocalDate.now()
    val initialPage = ((DayOfWeek.FRIDAY.value-1) % 7)
    println(initialPage)
}