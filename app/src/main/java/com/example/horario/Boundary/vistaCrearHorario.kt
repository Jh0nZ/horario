package com.example.horario.Boundary

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.backstack_tests.Control.VistaBackStack

@Composable
fun vistaCrearHorario(vistaBack: VistaBackStack) {
    LazyColumn {
        item {
            Row {
                horas(horario = vistaBack.contruirHorario)
                LazyRow {
                    items(vistaBack.contruirHorario.value.getUsedDays()) {
                        pruebasDias(horario = vistaBack.contruirHorario, dia = it)
                    }
                }
            }
        }
    }
}