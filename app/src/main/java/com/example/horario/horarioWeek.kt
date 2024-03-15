package com.example.horario

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.nio.file.WatchEvent
import java.time.DayOfWeek

@Preview(showBackground = true)
@Composable
fun miHorario(
    ancho: Dp = 100.dp,
    horas: List<String> = testHorario().obtenerHoras(),
    modifier: Modifier = Modifier
) {
    LazyColumn (
        modifier = Modifier.background(Color(221, 97, 97, 255))
    ){
        item {
            LazyRow {
                item {
                    horas()
                }
                items(
                    testHorario().getUsedDays()
                ) {
                    pruebasDias(it.name, intervalosss = testHorario().obtenerDiaFormato(it), ancho = ancho)
                    Log.d("info", testHorario().obtenerDiaFormato(it).toString())
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun horas(
    nombre: String = "HORAS",
    saltos: Int = 90,
    horas: List<String> = testHorario().obtenerHoras(90),
    modifier: Modifier = Modifier,
    ancho: Dp = 60.dp
) {
    Column {
        Text(
            text = nombre,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier
                .background(Color(54, 169, 219, 255))
                .height(40.dp)
                .width(ancho)
                .wrapContentHeight(align = Alignment.CenterVertically),
        )
        for (it in horas) {
            Text(
                text = it,
                modifier = Modifier
                    .background(Color.White)
                    .height(saltos.dp)
                    .width(ancho)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun pruebasDias(
    nombre: String = "LUNES",
    intervalosss: List<Intervalo> = testHorario().obtenerDiaFormato(DayOfWeek.TUESDAY),
    modifier: Modifier = Modifier,
    ancho: Dp = 80.dp
) {
    Column {
        Text(
            text = nombre,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier
                .background(Color.White)
                .height(40.dp)
                .width(ancho)
        )
        for (inter in intervalosss) {
            if (inter.nombre != null) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .background(inter.color, shape = RoundedCornerShape(10))
                        .height(inter.duracion.dp)
                        .width(ancho)
                        .padding(4.dp)
                ) {
                    Text(
                        text = inter.nombre,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = inter.aula,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            } else {
                Spacer(
                    modifier = Modifier
                        .background(inter.color)
                        .height(inter.duracion.dp)
                        .width(ancho)
                )
            }
        }
    }
}