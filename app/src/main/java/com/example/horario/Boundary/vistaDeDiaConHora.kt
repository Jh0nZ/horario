package com.example.horario.Boundary

import androidx.compose.animation.core.AnimationScope
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.backstack_tests.Control.VistaBackStack
import com.example.horario.Control.Horario
import com.example.horario.Control.Tiempo
import com.example.horario.Control.obtenerHorasString
import kotlinx.coroutines.coroutineScope
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
fun vistaDeDiaConHora(
    vistaBack: VistaBackStack = VistaBackStack(),
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
                horadia24(vistaBack, dayOfWeek)
            }
        }
    }
}

@Composable
fun horadia24(
    vistaBackStack: VistaBackStack = VistaBackStack(),
    dayOfWeek: DayOfWeek = LocalDate.now().dayOfWeek
) {
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

    var scrolledAutomatically by remember {
        mutableStateOf(false)
    }

    val scrollState = rememberLazyListState()

    /*
    LaunchedEffect(minutos) {
        if (!scrolledAutomatically) {

            delay(250)
            coroutineScope {
                scrollState.animateScrollToItem(
                    index = (24 * 60) - (minutos) - 1,
                    0
                )
            }
            scrolledAutomatically = true
        }
    }
     */

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formattedTime,
                color = Color.White
            )
            Text(
                text = minutos.toString(),
                color = Color.White
            )
        }
        LazyColumn (
            state = scrollState
        ) {
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
                                        .width(60.dp)
                                ) {
                                    Text(
                                        text = texto,
                                        maxLines = 1,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Column(
                            modifier = Modifier
                                .height((24 * 60).dp)
                        ) {
                            //today.dayOfWeek
                            val lista = vistaBackStack.horario.value.obtenerDiaFormato24h(dayOfWeek)
                            if (lista.isNotEmpty()) {
                                for (intervalo in lista) {
                                    val colorTexto = if (CalcularLuminosidad(intervalo.color) < 0.5) Color.White else Color.Black
                                    Box(
                                        modifier = Modifier
                                            .height(intervalo.duracion.dp)
                                            .fillMaxWidth()
                                            .background(
                                                if (intervalo.nombre == null) Color.Transparent else intervalo.color,
                                                shape = RoundedCornerShape(10.dp)
                                            ),
                                    ) {
                                        if (intervalo.nombre != null) {
                                            Column(
                                                verticalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxSize().padding(8.dp)
                                            ) {
                                                Text(
                                                    text = intervalo.nombre!!,
                                                    maxLines = 3,
                                                    overflow = TextOverflow.Ellipsis,
                                                    color = if (intervalo.esChoque) Color.Red else colorTexto
                                                )
                                                Row (
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Text(
                                                        text = intervalo.aula,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis,
                                                        color = if (intervalo.esChoque) Color.Red else colorTexto
                                                    )
                                                    Text(
                                                        text = "G: ${intervalo.nro_grupo}",
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis,
                                                        color = if (intervalo.esChoque) Color.Red else colorTexto
                                                    )
                                                }
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
