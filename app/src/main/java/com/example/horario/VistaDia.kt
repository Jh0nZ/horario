package com.example.horario

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun verDiaDia(
    dia: Int = Calendar.getInstance().get(Calendar.DAY_OF_WEEK),
    horas: List<String>,
    modifier: Modifier = Modifier
) {
    val nombreDia = when (dia) {
        Calendar.MONDAY -> "Lunes"
        Calendar.TUESDAY -> "Martes"
        Calendar.WEDNESDAY -> "Miércoles"
        Calendar.THURSDAY -> "Jueves"
        Calendar.FRIDAY -> "Viernes"
        Calendar.SATURDAY -> "Sábado"
        else -> "Día desconocido"
    }

    Column (
        modifier = modifier.fillMaxSize()
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .background(Color(223, 189, 241, 255))
                .fillMaxWidth()
        ) {
            for (diaa in listOf("LUN","MAR","MIE","JUE","VIE","SAB","DOM")) {
                IconButton(
                    modifier = modifier
                        .padding(4.dp)
                    ,
                    //colors = IconButtonColors(
                    //    containerColor = Color.Magenta,
                    //    contentColor = Color.White,
                    //    disabledContainerColor = Color(219, 170, 245, 255),
                    //    disabledContentColor = Color.White
                    //),
                    enabled = true,
                    onClick = { /*TODO*/ }
                ) {
                    Text(text = diaa)
                }
            }
        }
        Row (
            modifier = modifier
                .background(Color.Green)
                .fillMaxSize()
        ) {
            horas222(null, modifier = modifier, horas = horas)
        }
    }
}

@Composable
fun horas222(
    nombre: String?,
    saltos: Int = 90,
    horas: List<String>,
    modifier: Modifier = Modifier,
    ancho: Dp  = 100.dp
) {
    LazyColumn {
        if (nombre != null) {
            item {
                Text(
                    text = nombre,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier
                        .background(Color.White)
                        .height(40.dp)
                        .width(ancho)
                )
            }
        }

        items(horas) {
            Text(
                text = it,
                modifier = modifier
                    .background(Color.White)
                    .height(saltos.dp)
                    .width(ancho)
            )
        }
    }
}
