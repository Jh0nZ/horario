package com.example.horario.Boundary

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.horario.Control.Tiempo
import com.example.horario.Control.day2Text
import com.example.horario.Control.getDaysOfWeek
import java.time.DayOfWeek

@Composable
fun vistaSeleccionarDia(
    dia: MutableState<String> = mutableStateOf("LU"),
    intervalo: Intervalo) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        getDaysOfWeek().forEach {
            botonDia(it, dia, intervalo)
        }
    }
}

@Composable
fun botonDia(
    dayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    dia: MutableState<String> = mutableStateOf("LU"),
    intervalo: Intervalo,
) {
    Button(
        onClick = {
            dia.value = day2Text(dayOfWeek)
            intervalo.dia = dia.value
        },
        modifier= Modifier.size(50.dp),  //avoid the oval shape
        shape = CircleShape,
        border= BorderStroke(2.dp, Color(243, 133, 255, 255)),
        contentPadding = PaddingValues(0.dp),  //avoid the little icon
        colors = ButtonDefaults.buttonColors(
            containerColor = if (dia.value == day2Text(dayOfWeek)) Color(243, 133, 255, 255) else Color(228, 228, 228, 255),
            contentColor = Color.Black
        )
    ) {
        Text(text = day2SpanishLetter(dayOfWeek))
    }
}

@Composable
fun horaPicker(
    context: Context,
    hora: Int,
    minuto: Int,
    texto: String,
    onSelect: (Any, Int, Int) -> Unit
) {
    val timepickerdialog = TimePickerDialog(
        context,
        onSelect,
        hora,
        minuto,
        true
    )
    Button(onClick = { timepickerdialog.show()}) {
        Text(text = texto)
    }
}

fun day2SpanishLetter(dayOfWeek: DayOfWeek): String {
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> "L"
        DayOfWeek.TUESDAY -> "M"
        DayOfWeek.WEDNESDAY -> "M"
        DayOfWeek.THURSDAY -> "J"
        DayOfWeek.FRIDAY -> "V"
        DayOfWeek.SATURDAY -> "S"
        DayOfWeek.SUNDAY -> "D"
    }
}