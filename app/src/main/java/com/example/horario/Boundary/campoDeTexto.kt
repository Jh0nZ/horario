package com.example.horario.Boundary

import android.content.res.Configuration
import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Room
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(device = "spec:width=1080px,height=2340px,dpi=440", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun testtt() {
    val localFocusManager = LocalFocusManager.current
    Column (
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    localFocusManager.clearFocus()
                })
            }
    ) {
        var texto by remember { mutableStateOf("666b") }
        campoDeTexto(
            label = "Aula",
            texto = texto,
            limite = 20,
            leadIcon = {
                 Icon(imageVector = Icons.Outlined.Room, contentDescription = null)
            },
            cambiarValor = {
                texto = it
            }
        )
        var docente by remember { mutableStateOf("Docente de materia1") }
        campoDeTexto(
            label = "Docente",
            texto = docente,
            limite = 50,
            leadIcon = {
                Icon(imageVector = Icons.Outlined.Person, contentDescription = null)
            },
            cambiarValor = {
                docente = it
            }
        )
    }
}

@Composable
fun campoDeTexto(
    label: String,
    texto: String,
    limite: Int,
    obligado: Boolean = true,
    borrar: Boolean = false,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    leadIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    cambiarValor : (String)-> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    Box (
        modifier = modifier
            .padding(8.dp)
    ) {
        OutlinedTextField(
            label = {
                Text(text = if (isFocused || texto.isNotEmpty()) label else "$label*")
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(231, 231, 231, 255),
                focusedLeadingIconColor = Color(231, 231, 231, 255),
                focusedBorderColor = Color(231, 95, 255, 255),
                focusedLabelColor = Color(231, 95, 255, 255),
                focusedSupportingTextColor = Color(231, 231, 231, 255),

                cursorColor = Color(231, 95, 255, 255),

                unfocusedTextColor = Color(196, 196, 196, 255),
                unfocusedLeadingIconColor = Color(196, 196, 196, 255),
                unfocusedBorderColor = Color(196, 196, 196, 255),
                unfocusedLabelColor = Color(231, 231, 231, 255),
                unfocusedSupportingTextColor = Color(196, 196, 196, 255),
            ),
            leadingIcon = leadIcon,
            value = texto,
            onValueChange = {
                if (texto.length < limite || it.length < texto.length) {
                    cambiarValor(it)
                }
            },

            supportingText = {
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "*obligatorio")
                    Text(text = "${texto.length}/$limite")
                }
            },
            readOnly = readOnly,
            singleLine = singleLine,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }
        )
    }
}