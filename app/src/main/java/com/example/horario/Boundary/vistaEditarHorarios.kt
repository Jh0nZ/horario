package com.example.horario.Boundary

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.backstack_tests.Control.VistaBackStack
import com.example.horario.GifImage
import com.example.horario.R

@Composable
fun vistaEditarHorario(
    context: Context,
    vistaBackStack: VistaBackStack = VistaBackStack()
) {
    var showDialog by remember { mutableStateOf(false) }
    LazyColumn (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
       items(vistaBackStack.horario.value.grupos) {
           val colorTexto = if (CalcularLuminosidad(it.color) < 0.5) Color.White else Color.Black
           Button(
               colors = ButtonDefaults.buttonColors(
                   containerColor = it.color,
                   contentColor = colorTexto
               ),
               contentPadding = PaddingValues(0.dp),
               shape = RoundedCornerShape(10),
               onClick = {
                   vistaBackStack.grupoOriginal = it
                   vistaBackStack.grupoTemporalCopiar = it.copy()
                   vistaBackStack.editar_crear.value = "editar_grupo"
                   vistaBackStack.bottomMenuTitle = "Editar materia"
                   vistaBackStack.openBottomSheet.value = true
               }
           ) {
              Row (
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.fillMaxWidth().padding(8.dp)
              ) {
                  Column (
                      verticalArrangement = Arrangement.spacedBy(8.dp),
                      modifier = Modifier.weight(10f)
                  ) {
                      Text(text = "Materia: ${it.extraMateria}")
                      Text(text = "Grupo: ${it.nombre}")
                  }
                  Box(modifier = Modifier.weight(1f)) {
                      IconButton(onClick = {
                          vistaBackStack.grupoElimiar = it
                          showDialog = true
                      }) {
                          Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
                      }
                  }
              }
           }

       }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirmar eliminación") },
            text = { Text("¿Estás seguro que quieres eliminar la materia ${vistaBackStack.grupoElimiar?.extraMateria}?") },
            confirmButton = {
                Button(
                    onClick = {
                        Log.d("a", "Eliminar la materia ${vistaBackStack.grupoElimiar?.extraMateria}")
                        vistaBackStack.horario.value.grupos.remove(vistaBackStack.grupoElimiar)
                        showDialog = false
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        Log.d("a", "No elimiar la materia ${vistaBackStack.grupoElimiar?.extraMateria}")
                        showDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            },
            properties = DialogProperties(dismissOnClickOutside = false)
        )
    }
}

@Composable
fun confirmarEliminar(
    grupo: Grupo,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Confirmar eliminación") },
        text = { Text("¿Estás seguro que quieres eliminar la materia ${grupo.extraMateria}?") },
        confirmButton = {
            Button(
                onClick = {
                    Log.d("a", "Eliminar la materia ${grupo.extraMateria}")
                    onDismissRequest()
                }
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissRequest
            ) {
                Text("Cancelar")
            }
        },
        properties = DialogProperties(dismissOnClickOutside = false)
    )

}