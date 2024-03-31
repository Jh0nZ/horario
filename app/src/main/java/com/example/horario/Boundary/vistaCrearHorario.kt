package com.example.horario.Boundary

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.backstack_tests.Control.VistaBackStack
import java.time.DayOfWeek

@Composable
fun vistaCrearHorario(vistaBack: VistaBackStack) {
    LazyColumn {
        item {
            LazyRow {
                item {
                    horas(horario = vistaBack.contruirHorario)
                }
                items(vistaBack.contruirHorario.getUsedDays()) {
                    pruebasDias(horario = vistaBack.contruirHorario, dia = it)
                }
            }
        }
    }
}