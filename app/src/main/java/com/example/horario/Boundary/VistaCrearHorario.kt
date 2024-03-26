package com.example.horario.Boundary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.horario.Tiempo
import com.example.horario.miHorario
import com.example.horario.obtenerHorasString

@Preview
@Composable
fun CrearHorario(
    modifier: Modifier = Modifier,
    ancho: Dp = 100.dp
) {
    Box (
        modifier = modifier
            .fillMaxSize()

    ) {
        miHorario(ancho = ancho, horas = obtenerHorasString(90, Tiempo(6,45), Tiempo(14,15)))
        Box (
            contentAlignment = Alignment.BottomEnd,
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .size(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add, contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0, 166, 255, 255), shape = RectangleShape)
                )
            }
        }
    }
}