package com.example.backstack_tests.Control

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.horario.Boundary.Carrera
import com.example.horario.Boundary.Materia
import com.example.horario.Boundary.Semestre
import com.example.horario.Control.Horario
import com.example.horario.Control.testHorario
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
class VistaBackStack : ViewModel() {
    val stackList = mutableListOf<String>();
    val backStackEnabled = mutableStateOf(false)
    val currentLocation = mutableStateOf("/")
    var carrera = Carrera("Ing Informatica")
    var currentSemestre = mutableStateOf("Seleccionar semestre")
    var currentSemestreObject: Semestre? = null
    var currentMateriaObject: Materia? = null
    var currentMateria = mutableStateOf("Seleccionar materia")
    var contruirHorario = mutableStateOf(Horario())
    val horario = mutableStateOf(Horario().ejemplo())
    val materiaSheetState = SheetState(skipPartiallyExpanded = true)
    val bottomSheetState2 = SheetState(skipPartiallyExpanded = true)
    val openBottomSheet = mutableStateOf(false)
    val editar_crear = mutableStateOf("crear")
    val currentOption = mutableStateOf("")
    val openSelectMateria = mutableStateOf(false)

    fun guardarHorario(context: Context) {
        horario.value = contruirHorario.value
        contruirHorario.value = Horario()
        val prefs = context.getSharedPreferences("carrera", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = Gson()
        val horarioJson = gson.toJson(horario.value)
        editor.putString("horario", horarioJson)
        editor.apply()
    }

    fun cargarHorario(context: Context) {
        val prefs = context.getSharedPreferences("carrera", Context.MODE_PRIVATE)
        val gson = Gson()
        val savedHorario = prefs.getString("horario", null)
        val horrr = gson.fromJson(savedHorario, Horario::class.java)?: Horario()
        horario.value = horrr
    }

    fun cargarCarrera(context: Context) {
        val carreraGuardada = cargarCarreraDesdePrefs(context)
        carrera = carreraGuardada ?: Carrera("Ing Informatica")
    }

    fun guardarCarrera(context: Context) {
        guardarCarreraEnPrefs(carrera, context)
    }
    fun guardarCarreraEnPrefs(carrera: Carrera, context: Context) {
        val prefs = context.getSharedPreferences("carrera", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = Gson()

        val carreraJson = gson.toJson(carrera)
        editor.putString("carrera", carreraJson)
        editor.apply()
    }
    fun cargarCarreraDesdePrefs(context: Context): Carrera? {
        val prefs = context.getSharedPreferences("carrera", Context.MODE_PRIVATE)
        val gson = Gson() // Necesitarás la dependencia de Gson en tu build.gradle

        val carreraJson = prefs.getString("carrera", null)
        return gson.fromJson(carreraJson, Carrera::class.java)
    }



    fun navigateTo(elemento: String) {
        stackList.add(elemento)
        backStackEnabled.value = true
        currentLocation.value = elemento
    }
    fun navigateUniqueTo(elemento: String) {
        stackList.removeAll { it == elemento }
        navigateTo(elemento)
    }
    fun popStack() {
        if (stackList.isNotEmpty()) {
            stackList.removeAt(stackList.size-1)
        }

        if (stackList.isNotEmpty()) {
            currentLocation.value = stackList.last()
        } else {
            currentLocation.value = "/"
        }

        backStackEnabled.value = stackList.isNotEmpty()
    }
    fun emptyStack(): Boolean {
        return !backStackEnabled.value
    }
    override fun toString(): String {
        return stackList.toString()
    }
}
