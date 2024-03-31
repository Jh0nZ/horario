package com.example.horario.Boundary

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.backstack_tests.Control.VistaBackStack
import com.example.horario.GifImage
import com.example.horario.R

@Composable
fun vistaEditarHorario(context: Context, vistaBackStack: VistaBackStack = VistaBackStack()) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "ToDo 😭")
        GifImage(context = context, recurso = R.drawable.cat)
    }
}