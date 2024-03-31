package com.example.horario.Boundary

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import com.example.backstack_tests.Control.VistaBackStack
import com.example.horario.Control.Horario
import com.example.horario.Control.Intervalo
import com.example.horario.Control.Tiempo
import com.example.horario.Control.testHorario
import java.security.MessageDigest
import java.time.DayOfWeek

@Preview(showBackground = true)
@Composable
fun vistaHorarioSemana(
    ancho: Dp = 100.dp,
    modifier: Modifier = Modifier,
    vistaBackStack: VistaBackStack = VistaBackStack()
) {
    LazyColumn {
        item {
            LazyRow {
                item {
                    horas(horario = vistaBackStack.horario)
                }
                items(vistaBackStack.horario.getUsedDays()) {
                    pruebasDias(horario = vistaBackStack.horario, dia = it)
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
    horario: Horario = Horario().ejemplo(),
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
        for (it in horario.obtenerHoras(90)) {
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
    horario: Horario = Horario().ejemplo(),
    modifier: Modifier = Modifier,
    ancho: Dp = 100.dp,
    dia: DayOfWeek = DayOfWeek.FRIDAY
) {
    Log.d("pruebaaa", "recomposicion una vez")
    Column {
        Text(
            text = dia.toString(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier
                .background(Color.White)
                .height(40.dp)
                .width(ancho)
        )
        for (inter in horario.obtenerDiaFormato(dia)) {
            if (inter.nombre != null) {
                val colorTexto = if (CalcularLuminosidad(inter.color) < 0.5) Color.White else Color.Black
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
                        overflow = TextOverflow.Ellipsis,
                        color = colorTexto
                    )
                    Text(
                        text = inter.aula,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = colorTexto
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