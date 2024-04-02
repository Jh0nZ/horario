package com.example.horario.Boundary

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
fun vistaBottomMenuSecundario(vistaBack: VistaBackStack) {
    ModalBottomSheet(
        //tonalElevation = 0.dp, //color color de scrim oscurecido arriba
        contentColor = Color(29,29,29),
        containerColor = Color(139, 147, 255, 255),
        sheetState = vistaBack.materiaSheetState,
        //scrimColor = Color(111, 236, 43, 59),
        windowInsets = BottomSheetDefaults.windowInsets,//espacio arriba
        onDismissRequest = {vistaBack.openSelectMateria.value = false},
        dragHandle = {
            Text(text = "---------------") // esto es la cabezera
        }
    ) {
        Column {
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
                        text = "Seleccionar ${vistaBack.currentOption.value}",
                        textAlign = TextAlign.Center,
                        fontSize = 22.sp,
                    )
                }
                IconButton(onClick = {vistaBack.openSelectMateria.value = false}) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                }

            }
            LazyColumn (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    //.background(Color(86, 255, 187, 255))
                    .requiredHeight(400.dp)

            ) {

                if (vistaBack.currentOption.value == "semestre") {
                    items(vistaBack.carrera.semestres) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(87, 85, 254, 255), // Color de fondo del botón
                                contentColor = Color.White // Color del texto del botón
                            ),
                            modifier = Modifier
                                .widthIn(150.dp)
                                .padding(8.dp),
                            onClick = {
                                vistaBack.currentSemestre.value = it.nivel
                                vistaBack.currentMateria.value = "Seleccionar materia"
                                vistaBack.openSelectMateria.value = false
                            }
                        ) {
                            Text(
                                text = it.nivel,
                                color = Color.White
                            )
                        }
                    }
                } else {
                    val semestre = vistaBack.carrera.getSemestre(vistaBack.currentSemestre.value)
                    if (semestre != null) {
                        items(semestre.materias) {
                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(87, 85, 254, 255), // Color de fondo del botón
                                    contentColor = Color.White // Color del texto del botón
                                ),
                                modifier = Modifier
                                    .widthIn(150.dp)
                                    .padding(8.dp),
                                onClick = {
                                    vistaBack.currentMateria.value = it.nombre
                                    vistaBack.openSelectMateria.value = false
                                }
                            ) {
                                Text(
                                    text = it.nombre,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}