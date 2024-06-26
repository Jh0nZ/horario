package com.example.horario.Boundary

import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.backstack_tests.Control.VistaBackStack
import com.example.horario.Control.Horario
import java.security.MessageDigest
import java.time.DayOfWeek

@Preview(showBackground = true)
@Composable
fun vistaHorarioSemana(
    ancho: Dp = 100.dp,
    modifier: Modifier = Modifier,
    vistaBackStack: VistaBackStack = VistaBackStack()
) {
    LazyColumn (
        modifier = modifier
            .fillMaxSize()
    ) {
        item {
            Row {
                horas(horario = vistaBackStack.horario)
                LazyRow {
                    items(vistaBackStack.horario.value.getUsedDays()) {
                        pruebasDias(horario = vistaBackStack.horario, dia = it)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun horas(
    nombre: String = "Hora",
    saltos: Int = 90,
    horario: MutableState<Horario> = mutableStateOf(Horario().ejemplo()),
    modifier: Modifier = Modifier,
    ancho: Dp = 60.dp
) {
    Column {
        Text(
            text = nombre,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.White,
            modifier = modifier
                .height(40.dp)
                .width(ancho)
                .wrapContentHeight(align = Alignment.CenterVertically),
        )
        for (it in horario.value.obtenerHoras(90)) {
            Text(
                text = it,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(Color.Transparent)
                    .height(saltos.dp)
                    .width(ancho)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun pruebasDias(
    horario: MutableState<Horario> = mutableStateOf(Horario().ejemplo()),
    modifier: Modifier = Modifier,
    ancho: Dp = 125.dp,
    dia: DayOfWeek = DayOfWeek.FRIDAY
) {
    Column (
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .height(40.dp)
                .background(Color.Transparent)
                .width(ancho),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day2Spanish(dia),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                color = Color.White,
            )
        }

        val delDia = horario.value.obtenerDiaFormatoChoque(dia)
        for (inter in delDia) {
            if (inter.nombre != null) {
                val colorTexto = if (CalcularLuminosidad(inter.color) < 0.5) Color.White else Color.Black
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .background(
                            if (inter.esChoque) Color(221, 221, 221, 255) else inter.color,
                            shape = RoundedCornerShape(10)
                        )
                        .height(inter.duracion.dp)
                        .width(ancho)
                        .padding(4.dp)
                ) {
                    Text(
                        text = inter.nombre!!,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        color = if (inter.esChoque) Color(255, 32, 78, 255) else colorTexto
                    )
                    Row (
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = inter.aula,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = if (inter.esChoque) Color(255, 32, 78, 255) else colorTexto
                        )
                        Text(
                            text = "G: ${inter.nro_grupo}",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = if (inter.esChoque) Color(255, 32, 78, 255) else colorTexto
                        )
                    }
                }
            } else {
                Spacer(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .height(inter.duracion.dp)
                        .width(ancho)
                )
            }
        }
    }
}


fun generarColorUnicoParaCodigo(codigo: String): Color {
    val messageDigest = MessageDigest.getInstance("MD5")
    val hashBytes = messageDigest.digest(codigo.toByteArray())

    // Tomamos los primeros 3 bytes del hash para R, G, B
    val r = hashBytes[0].toInt() and 0xFF
    val g = hashBytes[1].toInt() and 0xFF
    val b = hashBytes[2].toInt() and 0xFF

    return Color(r, g, b)
}

fun CalcularLuminosidad(color: Color): Float {
    val r = color.red
    val g = color.green
    val b = color.blue
    return (0.2126f * r + 0.7152f * g + 0.0722f * b)
}

fun day2Spanish(dayOfWeek: DayOfWeek): String {
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> "Lunes"
        DayOfWeek.TUESDAY -> "Martes"
        DayOfWeek.WEDNESDAY -> "Miercoles"
        DayOfWeek.THURSDAY -> "Jueves"
        DayOfWeek.FRIDAY -> "Viernes"
        DayOfWeek.SATURDAY -> "Sabado"
        DayOfWeek.SUNDAY -> "Domingo"
    }
}