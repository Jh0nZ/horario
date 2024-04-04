package com.example.horario.Boundary

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.backstack_tests.Control.VistaBackStack
import com.example.horario.Control.Tiempo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun vistaBottomMenuPrincipal(
    context: Context,
    vistaBack: VistaBackStack,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
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
                    if (vistaBack.editar_crear.value != "editar_grupo") {
                        IconButton(onClick = {
                            vistaBack.editar_crear.value = if (vistaBack.editar_crear.value == "crear") "editar" else "crear"
                        }) {
                            Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = null)
                        }
                    } else {
                        IconButton(onClick = {
                            val grupoNuevo = vistaBack.grupoTemporalCopiar
                            vistaBack.grupoOriginal.intervalos = grupoNuevo.intervalos
                            vistaBack.grupoOriginal.nombre = grupoNuevo.nombre
                            vistaBack.grupoOriginal.extraMateria = grupoNuevo.extraMateria
                            vistaBack.grupoOriginal.extraCodMat = grupoNuevo.extraCodMat
                            vistaBack.guardarCambiosEditar(context)

                            vistaBack.openBottomSheet.value = false

                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message ="Guardado exitosamente",
                                    actionLabel = "Ok",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Save, contentDescription = null)
                        }
                    }
                }
            }
            when (vistaBack.editar_crear.value) {
                "crear" -> {
                    LazyColumn (
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        items(vistaBack.carrera.semestres) {semestre ->
                            Column (
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(231, 111, 81, 255),
                                        contentColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(10),
                                    onClick = {
                                        if (semestre.seleccionado.value) {
                                            semestre.seleccionado.value = false
                                        } else {
                                            semestre.materias.forEach { mat ->
                                                mat.seleccionado.value = false
                                            }
                                            semestre.seleccionado.value = true
                                        }

                                    }
                                ) {
                                    val icono = if (semestre.seleccionado.value) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown
                                    Row (
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Text(text = "Semestre ${semestre.nivel}")
                                        Icon(imageVector = icono, contentDescription = null )
                                    }
                                }
                                if (semestre.seleccionado.value) {
                                    Column (
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 40.dp)
                                    ) {
                                        semestre.materias.sortedBy { it.nombre }.forEach { materia ->
                                            Column {
                                                Button(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(40.dp),
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = Color(244, 162, 97, 255),
                                                        contentColor = Color.Black
                                                    ),
                                                    shape = RoundedCornerShape(10),
                                                    onClick = {
                                                        materia.seleccionado.value = !(materia.seleccionado.value)
                                                    }
                                                ) {
                                                    val icono = if (materia.seleccionado.value) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown
                                                    Row (
                                                        modifier = Modifier.fillMaxSize()
                                                    ) {
                                                        Text(
                                                            text = materia.nombre,
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis
                                                        )
                                                        Icon(imageVector = icono, contentDescription = null )
                                                    }
                                                }
                                                if (materia.seleccionado.value) {
                                                    Column (
                                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                                        modifier = Modifier.fillMaxWidth()
                                                    ) {
                                                        materia.grupos.sortedBy { it.nombre }.forEach { grupo ->
                                                            Button(
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .padding(start = 40.dp)
                                                                    .heightIn(min = 50.dp),
                                                                colors = ButtonDefaults.buttonColors(
                                                                    containerColor = if (grupo.seleccionado.value) {
                                                                        Color(183, 156, 56, 255) // Color cuando está seleccionado
                                                                    } else {
                                                                        Color(233, 196, 106, 255) // Color por defecto
                                                                    },
                                                                    contentColor = Color.Black
                                                                ),
                                                                shape = RoundedCornerShape(10),
                                                                onClick = {
                                                                    if (grupo.seleccionado.value) {
                                                                        grupo.seleccionado.value = false
                                                                        vistaBack.contruirHorario.value.quitarGrupo(grupo)
                                                                    } else {
                                                                        grupo.seleccionado.value = true
                                                                        vistaBack.contruirHorario.value.agregarGrupo(grupo)
                                                                    }
                                                                }
                                                            ) {
                                                                val icono = if (grupo.seleccionado.value) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank
                                                                Row (
                                                                    modifier = Modifier.fillMaxSize()
                                                                ) {
                                                                    Box (
                                                                        modifier = Modifier.weight(9f)
                                                                    ) {
                                                                        Text(
                                                                            text = grupo.toString(),
                                                                            overflow = TextOverflow.Ellipsis,
                                                                            modifier = Modifier.fillMaxSize()
                                                                        )
                                                                    }
                                                                    Box (
                                                                        modifier = Modifier.weight(1f)
                                                                    ) {
                                                                        Icon(imageVector = icono, contentDescription = null )
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                "editar" -> {
                    Text(text = "Materias seleccionadas")
                    val gruposSeleccionados = mutableListOf<Grupo>()
                    vistaBack.carrera.semestres.forEach { semestre ->
                        semestre.materias.forEach { materia ->
                            materia.grupos.forEach { grupo ->
                                if (grupo.seleccionado.value) {
                                    gruposSeleccionados.add(grupo)
                                }
                            }
                        }
                    }
                    LazyColumn {
                        items(gruposSeleccionados) { grupo ->
                            val icono =
                                if (grupo.seleccionado.value) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (grupo.seleccionado.value) {
                                        Color(183, 156, 56, 255) // Color cuando está seleccionado
                                    } else {
                                        Color(233, 196, 106, 255) // Color por defecto
                                    },
                                    contentColor = Color.Black
                                ),
                                shape = RectangleShape,
                                onClick = {
                                    if (grupo.seleccionado.value) {
                                        grupo.seleccionado.value = false
                                        vistaBack.contruirHorario.value.quitarGrupo(grupo)
                                    } else {
                                        grupo.seleccionado.value = true
                                        vistaBack.contruirHorario.value.agregarGrupo(grupo)

                                    }
                                }
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .weight(9f)
                                            .fillMaxHeight()
                                    ) {
                                        Text(
                                            text = grupo.extraMateria,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = grupo.toString(),
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    Box(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(imageVector = icono, contentDescription = null)
                                    }
                                }
                            }
                            Divider(
                                color = Color.Gray,
                                thickness = 1.dp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }
                "editar_grupo" -> {
                    val grupito = vistaBack.grupoTemporalCopiar
                    val nombre_materia = remember {
                        mutableStateOf(grupito.extraMateria)
                    }
                    val nombre_grupo = remember {
                        mutableStateOf(grupito.nombre)
                    }
                    LazyColumn (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        item {
                            Row (
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextField(
                                    value = nombre_materia.value, onValueChange = {
                                        nombre_materia.value = it
                                        grupito.extraMateria = it
                                    },
                                    label = {
                                        Text(text = "Materia")
                                    }
                                )
                            }
                        }
                        item {
                            Row (
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                TextField(
                                    value = nombre_grupo.value, onValueChange = {
                                        nombre_grupo.value = it
                                        grupito.nombre = it
                                    },
                                    label = {
                                        Text(text = "Grupo")
                                    }
                                )
                            }
                        }
                        item {
                            Text(text = "intervalos: ")
                        }
                        items(grupito.intervalos) {
                            val docente = remember {
                                mutableStateOf(it.docente)
                            }
                            val dia = remember {
                                mutableStateOf(it.dia)
                            }
                            var aula by remember {
                                mutableStateOf(it.aula)
                            }

                            Row (
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth().padding(start = 20.dp)
                            ) {

                                TextField(
                                    value = docente.value, onValueChange = {newText ->
                                        docente.value = newText
                                        it.docente = newText
                                    },
                                    label = {
                                        Text(text = "Docente")
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                TextField(
                                    value = aula,
                                    onValueChange = {newText ->
                                        it.aula = newText
                                        aula = newText
                                    },
                                    label = {
                                        Text(text = "Aula")
                                    }
                                )
                            }

                            Row (
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround,
                                modifier = Modifier.fillMaxWidth().padding(start = 20.dp)
                            ) {
                                val tiempo_inicio = Tiempo(it.h_inicio)
                                val tiempoTexto_inicio = remember {
                                    mutableStateOf(tiempo_inicio.toString())
                                }

                                val tiempo_fin = Tiempo(it.h_fin)
                                val tiempoTexto_fin = remember {
                                    mutableStateOf(tiempo_fin.toString())
                                }
                                Text(text = "Hora inicio: ")
                                horaPicker(context, tiempo_inicio.hora, tiempo_inicio.minuto, tiempoTexto_inicio.value) { _, h, m ->
                                    tiempo_inicio.hora = h
                                    tiempo_inicio.minuto = m
                                    it.h_fin = String.format("%d%02d", h, m)
                                    tiempoTexto_inicio.value = tiempo_inicio.toString()
                                }

                                Text(text = "Hora fin: ")
                                horaPicker(context, tiempo_fin.hora, tiempo_fin.minuto, tiempoTexto_fin.value) { _, h, m ->
                                    tiempo_fin.hora = h
                                    tiempo_fin.minuto = m
                                    it.h_inicio = String.format("%d%02d", h, m)
                                    tiempoTexto_fin.value = tiempo_fin.toString()
                                }
                            }

                            Row (
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth().padding(start = 20.dp)
                            ) {
                                vistaSeleccionarDia(dia, it)
                            }
                            Divider()
                        }
                    }
                }
            }

        }
    }
}