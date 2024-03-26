package com.example.horario

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.horario.Boundary.CrearHorario
import com.example.horario.ui.theme.HorarioTheme


class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            HorarioTheme {
                when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.Compact -> {
                        Principal(100.dp, Modifier, this)
                    }
                    WindowWidthSizeClass.Expanded -> {
                        Principal(150.dp, Modifier, this)
                    }
                }
            }
        }
    }
}

@SuppressLint("RestrictedApi")
@Preview(showBackground = true)
@Preview(showBackground = true, name = "horizontal",
    device = "spec:parent=pixel_5,orientation=landscape"
)
@Composable
fun Principal(
    ancho: Dp = 100.dp,
    modifier: Modifier = Modifier,
    context: Context = MainActivity()
) {
    var currentNav by remember { mutableStateOf("Home") }
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "Home",
        enterTransition = {EnterTransition.None},
        exitTransition = {ExitTransition.None},
        popEnterTransition = {EnterTransition.None},
        popExitTransition = {ExitTransition.None}
    ) {
        composable("Home") { currentNav="Home" }
        composable("Account") { currentNav="Account" }
        composable("Add") { currentNav="Add" }
        composable("Build") { currentNav="Build" }
        composable("Settings") { currentNav="Settings"}
    }
    contenidoo(ancho, navController, modifier, currentNav)
    BackHandler {
        Log.d("testBackHandler", "se presiono Back Stack")
    }
}

@Preview
@Composable
fun bottomSlider(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(80.dp)
    ) {
        val bottomBarIcons = listOf(
            Pair(Icons.Default.Home, "Home"),
            Pair(Icons.Default.AccountBox, "Account"),
            Pair(Icons.Default.Add, "Add"),
            Pair(Icons.Default.Build, "Build"),
            Pair(Icons.Default.Settings, "Settings")
        )

        for ((icono, nombre) in bottomBarIcons) {
            Button(
                shape = RectangleShape,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.Black
                ),
                onClick = {
                    if (navController.currentDestination!!.route != nombre) {
                        navController.navigate(nombre)
                    }
                },
                modifier = modifier.fillMaxHeight()

            ) {
                Icon(imageVector = icono, contentDescription = null)
            }
        }
    }
}

@Composable
fun contenidoo(
    ancho: Dp = 100.dp,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    currentNav: String = "Home"
) {
    Column (
        modifier = modifier.fillMaxSize()
    ) {
        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Red)
        ){
            Text(
                text = currentNav,
                fontSize = 22.sp
            )
        }
        Box(modifier = modifier
            .fillMaxWidth()
            .weight(15f)) {
            when (currentNav) {
                "Home" -> ThreeTabSlider()
                "Account" -> miHorario(ancho = ancho, horas = obtenerHorasString(90, Tiempo(6,45), Tiempo(14,15)))
                "Add" -> CrearHorario(modifier, ancho)
                "Build" -> verDiaDia(horas = obtenerHorasString(60, Tiempo(0,0), Tiempo(23,59)))
                "Settings" -> Clock()
            }
        }
        bottomSlider(navController, modifier.weight(1f))
    }
}