package com.example.horario.Boundary

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Class
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Room
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.backstack_tests.Control.VistaBackStack
import com.example.horario.Control.Tiempo
import com.example.horario.Control.day2Text
import com.example.horario.Control.spanish2day
import com.example.horario.Control.text2Day
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
        //tonalElevation = 0.dp, //color color de scrim oscurecido arriba 222831
        contentColor = Color(231, 231, 231, 255),
        containerColor = Color(34, 40, 49, 255),
        sheetState = vistaBack.bottomSheetState2,
        scrimColor = Color.Transparent,
        //windowInsets = BottomSheetDefaults.windowInsets, //espacio arriba
        onDismissRequest = {
            vistaBack.openBottomSheet.value = false
        },
        dragHandle = {
            Text(text = "---------------") // arriba del modalBottom
        }
    ) {
        Column  (
            modifier = Modifier.fillMaxSize()
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box (
                    modifier = Modifier.weight(2f)
                ) {
                    IconButton(onClick = {
                        vistaBack.openBottomSheet.value = false
                    }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null)
                    }
                }

                Box (
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.weight(9f)
                ) {
                    Text(
                        text = vistaBack.bottomMenuTitle,
                        textAlign = TextAlign.Center,
                        fontSize = 22.sp,
                        color = Color(231, 231, 231, 255)
                    )
                }

                Box (
                    modifier = Modifier.weight(2f)
                ) {
                    if (vistaBack.editar_crear.value != "editar_grupo") {
                        IconButton(
                            onClick = {
                                vistaBack.editar_crear.value = if (vistaBack.editar_crear.value == "crear") "editar" else "crear"
                                vistaBack.bottomMenuTitle = if (vistaBack.editar_crear.value == "crear") "Seleccionar grupos" else "Grupos seleccionados"
                            }
                        ) {
                            Row {
                                Icon(imageVector = Icons.Outlined.Class, contentDescription = null)
                                Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = null)
                            }
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
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        items(vistaBack.carrera.semestres) {semestre ->
                            Column (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = Color(49, 54, 63, 255),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = Color(196, 196, 196, 255),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                            ) {
                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent,
                                        contentColor = Color(196, 196, 196, 255),
                                    ),
                                    contentPadding = PaddingValues(0.dp),
                                    shape = RectangleShape,
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
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 8.dp)
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
                                            .padding(
                                                start = 30.dp,
                                                end = 8.dp,
                                                top = 8.dp,
                                                bottom = 8.dp
                                            )
                                    ) {
                                        semestre.materias.sortedBy { it.nombre }.forEach { materia ->
                                            Column (
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(
                                                        color = Color(34, 40, 49, 255),
                                                        shape = RoundedCornerShape(10.dp)
                                                    )
                                                    .border(
                                                        width = 1.dp,
                                                        color = Color(196, 196, 196, 255),
                                                        shape = RoundedCornerShape(10.dp)
                                                    )
                                            ) {
                                                Button(
                                                    modifier = Modifier
                                                        .fillMaxWidth(),
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = Color.Transparent,
                                                        contentColor = Color(196, 196, 196, 255)
                                                    ),
                                                    contentPadding = PaddingValues(0.dp),
                                                    shape = RectangleShape,
                                                    onClick = {
                                                        materia.seleccionado.value = !(materia.seleccionado.value)
                                                    }
                                                ) {
                                                    val icono = if (materia.seleccionado.value) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown
                                                    Row (
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .padding(horizontal = 8.dp)
                                                    ) {
                                                        Box (
                                                            modifier = Modifier.weight(10f)
                                                        ) {
                                                            Text(
                                                                text = materia.nombre,
                                                                maxLines = 3,
                                                                overflow = TextOverflow.Ellipsis
                                                            )
                                                        }
                                                        Box (
                                                            modifier = Modifier.weight(1f)
                                                        ) {
                                                            Icon(imageVector = icono, contentDescription = null )
                                                        }
                                                    }
                                                }
                                                if (materia.seleccionado.value) {
                                                    Column (
                                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(
                                                                start = 30.dp,
                                                                end = 8.dp,
                                                                top = 8.dp,
                                                                bottom = 8.dp
                                                            )
                                                    ) {
                                                        materia.grupos.sortedBy { it.nombre }.forEach { grupo ->
                                                            Button(
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .heightIn(min = 50.dp),
                                                                colors = ButtonDefaults.buttonColors(
                                                                    containerColor = Color(49, 54, 63, 255),
                                                                    contentColor = Color(196, 196, 196, 255)
                                                                ),
                                                                shape = RoundedCornerShape(10),
                                                                contentPadding = PaddingValues(0.dp),
                                                                border = BorderStroke(1.dp, Color(196, 196, 196, 255)),
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
                                                                    verticalAlignment = Alignment.CenterVertically,
                                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                                    modifier = Modifier
                                                                        .fillMaxSize()
                                                                        .padding(horizontal = 8.dp)
                                                                ) {
                                                                    Box (
                                                                        modifier = Modifier.weight(10f)
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
                    LazyColumn (
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        items(gruposSeleccionados) { grupo ->
                            val icono = if (grupo.seleccionado.value) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(34, 40, 49, 255),
                                    contentColor = Color(196, 196, 196, 255)
                                ),
                                border = BorderStroke(1.dp, Color(196, 196, 196, 255)),
                                contentPadding = PaddingValues(0.dp),
                                shape = RoundedCornerShape(10.dp),
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
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp)
                                ) {
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier
                                            .weight(9f)
                                            .fillMaxHeight()
                                    ) {
                                        Text(
                                            text = grupo.extraMateria,
                                            maxLines = 2,
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
                        }
                    }
                }
                "editar_grupo" -> {
                    val localFocusManager = LocalFocusManager.current
                    val grupito = vistaBack.grupoTemporalCopiar
                    var nombre_materia by remember {
                        mutableStateOf(grupito.extraMateria)
                    }
                    var nombre_grupo by remember {
                        mutableStateOf(grupito.nombre)
                    }
                    LazyColumn (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(onTap = {
                                    localFocusManager.clearFocus()
                                })
                            }
                    ) {
                        item {
                            campoDeTexto(
                                label = "Materia",
                                texto = nombre_materia,
                                limite = 100,
                                singleLine = false,
                                leadIcon = {
                                    Icon(imageVector = Icons.Outlined.Class, contentDescription = null)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                cambiarValor = { newText ->
                                    nombre_materia = newText
                                    grupito.extraMateria = newText
                                }
                            )
                        }
                        item {
                            campoDeTexto(
                                label = "Grupo",
                                texto = nombre_grupo,
                                limite = 10,
                                leadIcon = {
                                    Icon(imageVector = Icons.Outlined.Group, contentDescription = null)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                cambiarValor = { newText ->
                                    nombre_grupo = newText
                                    grupito.nombre = newText
                                }
                            )
                        }
                        items(grupito.intervalos.sortedBy { text2Day(it.dia) }) {
                            var docente by remember {
                                mutableStateOf(it.docente)
                            }
                            var dia by remember {
                                mutableStateOf(day2Spanish(text2Day(it.dia)))
                            }
                            var aula by remember {
                                mutableStateOf(it.aula)
                            }
                            val tiempoInicio = Tiempo(it.h_inicio)
                            val tiempoFin = Tiempo(it.h_fin)
                            var textoInicio by remember { mutableStateOf(tiempoInicio.toString()) }
                            var textoFin by remember { mutableStateOf(tiempoFin.toString()) }
                            var intervaloExpandido by remember {
                                mutableStateOf(false)
                            }
                            Column (
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .background(
                                        color = Color(49, 54, 63, 255),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = Color(196, 196, 196, 255),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                            ) {
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent,
                                        contentColor = Color(231, 231, 231, 255)
                                    ),
                                    shape = RectangleShape,
                                    onClick = {
                                        intervaloExpandido = !intervaloExpandido
                                    }
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "$dia, $textoInicio hasta $textoFin",
                                            color = Color(196, 196, 196, 255),
                                        )
                                        Icon(
                                            imageVector = if (intervaloExpandido) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                                            contentDescription = null
                                        )
                                    }
                                }
                                if (intervaloExpandido) {
                                    campoDeTexto(
                                        label = "Docente",
                                        texto = docente,
                                        limite = 50,
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = false,
                                        leadIcon = {
                                            Icon(imageVector = Icons.Outlined.Person, contentDescription = null)
                                        },
                                        cambiarValor = { newText ->
                                            docente = newText
                                            it.docente = newText
                                        }
                                    )
                                    campoDeTexto(
                                        label = "Aula",
                                        texto = aula,
                                        limite = 20,
                                        modifier = Modifier.fillMaxWidth(),
                                        leadIcon = {
                                            Icon(imageVector = Icons.Outlined.Room, contentDescription = null)
                                        },
                                        cambiarValor = { newText ->
                                            aula = newText
                                            it.aula = newText
                                        }
                                    )
                                    Row (
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        val timepickerdialogInicio = TimePickerDialog(
                                            context,
                                            { _, h, m ->
                                                it.h_inicio = String.format("%d%02d", h, m)
                                                textoInicio = String.format("%02d:%02d", h, m)
                                            },
                                            tiempoInicio.hora,
                                            tiempoInicio.minuto,
                                            true
                                        )
                                        campoDeTexto(
                                            label = "Inicio",
                                            texto = textoInicio,
                                            limite = 20,
                                            modifier = Modifier.weight(1f),
                                            readOnly = true,
                                            leadIcon = {
                                                IconButton(onClick = {
                                                    timepickerdialogInicio.show()
                                                }) {
                                                    Icon(imageVector = Icons.Outlined.AccessTime, contentDescription = null)
                                                }

                                            },
                                            cambiarValor = {}
                                        )

                                        val timepickerdialogFin = TimePickerDialog(
                                            context,
                                            { _, h, m ->
                                                it.h_fin = String.format("%d%02d", h, m)
                                                textoFin = String.format("%02d:%02d", h, m)
                                            },
                                            tiempoFin.hora,
                                            tiempoFin.minuto,
                                            true
                                        )
                                        campoDeTexto(
                                            label = "Fin",
                                            texto = textoFin,
                                            limite = 20,
                                            modifier = Modifier.weight(1f),
                                            readOnly = true,
                                            leadIcon = {
                                                IconButton(onClick = {
                                                    timepickerdialogFin.show()
                                                }) {
                                                    Icon(imageVector = Icons.Outlined.AccessTime, contentDescription = null)
                                                }
                                            },
                                            cambiarValor = {}
                                        )
                                    }
                                    vistaSeleccionarDia(spanish2day(dia)) { day ->
                                        dia = day2Spanish(day)
                                        it.dia = day2Text(day)
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