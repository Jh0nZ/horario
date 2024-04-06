package com.example.horario.Boundary

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun VistaAjustes() {
    var notificacionesActivadas by remember { mutableStateOf(false) }
    var recordatoriosActivados by remember { mutableStateOf(false) }
    var temaOscuroActivado by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Ajustes",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
            color = Color(196, 196, 196, 255)
        )

        // Switch para notificaciones
        SwitchSetting(
            text = "Notificaciones",
            isChecked = notificacionesActivadas,
            onCheckedChange = { notificacionesActivadas = it }
        )

        // Switch para recordatorios
        SwitchSetting(
            text = "Recordatorios",
            isChecked = recordatoriosActivados,
            onCheckedChange = { recordatoriosActivados = it }
        )

        // Switch para tema oscuro
        SwitchSetting(
            text = "Tema Oscuro",
            isChecked = temaOscuroActivado,
            onCheckedChange = { temaOscuroActivado = it }
        )

        // Opciones adicionales
        Divider(color = Color.LightGray, thickness = 1.dp)
        Text(
            text = "Opciones adicionales",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp),
            color = Color(196, 196, 196, 255)
        )

        // Opción 1
        SettingItem(text = "Mostrar fines de semana", icon = Icons.Filled.DateRange)

        // Opción 2
        SettingItem(text = "Recordar eventos", icon = Icons.Filled.Alarm)

        // Opción 3
        SettingItem(text = "Mostrar notificaciones emergentes", icon = Icons.Filled.Notifications)

        // Opción 4
        SettingItem(text = "Ordenar eventos por hora", icon = Icons.Filled.Schedule)
    }
}

@Composable
fun SwitchSetting(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            color = Color(196, 196, 196, 255)
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = Color(231, 95, 255, 255)
            )
        )
    }
}

@Composable
fun SettingItem(text: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            color = Color(196, 196, 196, 255)
        )
    }
}