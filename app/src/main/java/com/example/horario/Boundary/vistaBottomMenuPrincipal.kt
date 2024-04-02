package com.example.horario.Boundary

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.backstack_tests.Control.VistaBackStack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun vistaBottomMenuPrincipal(
    vistaBack: VistaBackStack
) {
    ModalBottomSheet(
        //tonalElevation = 0.dp, //color color de scrim oscurecido arriba
        contentColor = Color(19,19,19),
        containerColor = Color(255, 235, 178, 255),
        sheetState = vistaBack.bottomSheetState2,
        scrimColor = Color.Transparent,
        //windowInsets = BottomSheetDefaults.windowInsets, //espacio arriba
        onDismissRequest = {vistaBack.openBottomSheet.value = false},
        dragHandle = {
            Text(text = "---------------") // esto esta arriba xd
        }
    ) {
        Column  (
            modifier = Modifier.fillMaxSize()
        ) {
            Box (
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Seleccionar grupo",
                        textAlign = TextAlign.Center,
                        fontSize = 22.sp,
                        color = Color(29, 29, 29, 255)
                    )
                }
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize()
                ) {
                    IconButton(onClick = {
                        vistaBack.openBottomSheet.value = false
                    }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null)
                    }
                    IconButton(onClick = {
                        vistaBack.editar_crear.value = if (vistaBack.editar_crear.value == "crear") "editar" else "crear"
                    }) {
                        Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = null)
                    }
                }
            }
            if (vistaBack.editar_crear.value == "crear") {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(231, 95, 255, 255),
                        contentColor = Color(29, 29, 29, 255),
                        disabledContainerColor = Color(243, 173, 255, 255),
                        disabledContentColor = Color(133, 133, 133, 255)
                    ),
                    modifier = Modifier.padding(8.dp),
                    onClick = {
                        vistaBack.currentOption.value = "semestre"
                        vistaBack.openSelectMateria.value = true
                    }
                ) {
                    Text(
                        text = vistaBack.currentSemestre.value,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.widthIn(200.dp)
                    )
                }
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(231, 95, 255, 255),
                        contentColor = Color(29, 29, 29, 255),
                        disabledContainerColor = Color(243, 173, 255, 255),
                        disabledContentColor = Color(133, 133, 133, 255)
                    ),
                    onClick = {
                        vistaBack.currentOption.value = "materia"
                        vistaBack.openSelectMateria.value = true
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = vistaBack.currentMateria.value,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.widthIn(200.dp)
                    )
                }
                LazyColumn (
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    val semestre = vistaBack.carrera.getSemestre(vistaBack.currentSemestre.value)
                    if (semestre != null) {
                        val materia = semestre.getMateria(vistaBack.currentMateria.value)
                        if (materia != null) {
                            item {
                                Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                            }
                            items(materia.grupos) {grupo ->
                                Row (
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = grupo.toString(),
                                        modifier = Modifier
                                            .width(300.dp)
                                            .padding(8.dp)
                                    )
                                    Checkbox(
                                        checked = grupo.seleccionado,
                                        onCheckedChange = {
                                            Log.d("test", "grupo sel: ${grupo.seleccionado}, check: $it")
                                            grupo.seleccionado = it
                                            if (it) {
                                                vistaBack.contruirHorario.value.agregarGrupo(grupo)
                                            } else {
                                                vistaBack.contruirHorario.value.quitarGrupo(grupo)
                                            }
                                            vistaBack.openBottomSheet.value = false
                                        }
                                    )
                                }
                                Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                            }
                        }
                    }
                }
            } else {
                Text(text = "Materias seleccionadas")
                val gruposSeleccionados = mutableListOf<Grupo>()
                vistaBack.carrera.semestres.forEach { semestre ->
                    semestre.materias.forEach { materia ->
                        materia.grupos.forEach { grupo ->
                            if (grupo.seleccionado) {
                                gruposSeleccionados.add(grupo)
                            }
                        }
                    }
                }
                LazyColumn {
                    items(gruposSeleccionados) {grupo ->
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Column {
                                Text(
                                    text = grupo.extraMateria,
                                    modifier = Modifier
                                        .width(300.dp)
                                )
                                Text(
                                    text = grupo.toString(),
                                    modifier = Modifier
                                        .width(300.dp)
                                )
                            }
                            Checkbox(
                                checked = grupo.seleccionado,
                                onCheckedChange = {
                                    Log.d("test", "grupo sel: ${grupo.seleccionado}, check: $it")
                                    grupo.seleccionado = it
                                    if (it) {
                                        vistaBack.contruirHorario.value.agregarGrupo(grupo)
                                    } else {
                                        vistaBack.contruirHorario.value.quitarGrupo(grupo)
                                    }
                                    vistaBack.openBottomSheet.value = false
                                }
                            )
                        }
                        Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }
    }
}