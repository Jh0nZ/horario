package com.example.horario.Boundary

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.backstack_tests.Control.VistaBackStack
import com.example.horario.GifImage
import com.example.horario.R

@Composable
fun vistaEditarHorario(
    context: Context, 
    vistaBackStack: VistaBackStack = VistaBackStack()) {
    LazyColumn (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
       items(vistaBackStack.horario.value.grupos) {
           Button(
               colors = ButtonDefaults.buttonColors(
                   containerColor = Color(252, 86, 86, 255)
               ),
               shape = RoundedCornerShape(10),
               onClick = {
                   vistaBackStack.grupoOriginal = it
                   vistaBackStack.grupoTemporalCopiar = it.copy()
                   vistaBackStack.editar_crear.value = "editar_grupo"
                   vistaBackStack.openBottomSheet.value = true
               }
           ) {
               Column (
                   modifier = Modifier.fillMaxWidth()
               ) {
                   Row (
                       modifier = Modifier.fillMaxWidth()
                   ) {
                       Text(text = "Nombre de materia: ")
                       Text(text = it.extraMateria)
                   }
                   Row (
                       modifier = Modifier.fillMaxWidth()
                   ) {
                       Text(text = "Nombre de grupo: ")
                       Text(text = it.nombre)
                   }
               }
           }
       }
    }
}