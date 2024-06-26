package com.example.horario

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.backstack_tests.Control.VistaBackStack
import com.example.horario.Boundary.VistaAjustes
import com.example.horario.Boundary.testtt
import com.example.horario.Boundary.vistaBottomMenuPrincipal
import com.example.horario.Boundary.vistaBottomMenuSecundario
import com.example.horario.Boundary.vistaCrearHorario
import com.example.horario.Boundary.vistaDeDiaConHora
import com.example.horario.Boundary.vistaDeDiaPager
import com.example.horario.Boundary.vistaEditarHorario
import com.example.horario.Boundary.vistaHorarioSemana
import com.example.horario.Boundary.vistaLeerPdf
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    val vistaBack by viewModels<VistaBackStack>()
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        vistaBack.cargarCarrera(this)
        vistaBack.cargarHorario(this)
        super.onCreate(savedInstanceState)
        PDFBoxResourceLoader.init(this);
        window.statusBarColor = Color(0,0,0,0).toArgb()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        LocalFocusManager
        setContent {
            WindowCompat.getInsetsController(window, LocalView.current).isAppearanceLightStatusBars = true
            aaa(vistaBack, this)
            //testtt()
        }
    }
}

/*
val windowSizeClass = calculateWindowSizeClass(this)
HorarioTheme {
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            Principal(100.dp, Modifier, vistaBack, this)
        }
        WindowWidthSizeClass.Expanded -> {
            Principal(150.dp, Modifier, vistaBack,this)
        }
    }
}
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun aaa(
    vistaBack: VistaBackStack = VistaBackStack(),
    context: Context
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var cambio by remember { mutableStateOf("")}

    val snackbarHostState = remember { SnackbarHostState() }

    DisposableEffect(drawerState.currentValue) {
        onDispose {
            when (drawerState.currentValue) {
                DrawerValue.Open -> {
                    vistaBack.navigateTo("nav_drawer")
                    Log.d("Drawer", "Se ha abierto el drawer")
                }
                DrawerValue.Closed -> {
                    vistaBack.popStack()
                    Log.d("Drawer", "Se ha cerrado el drawer")
                    Log.d("BACKSTACK", "$vistaBack")
                    if (cambio != "") {
                        vistaBack.navigateTo(cambio)
                        cambio != ""
                    }
                }
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.background(Color.Red),
        drawerContent = {
            ModalDrawerSheet (
                drawerContainerColor = Color(49, 54, 63, 255)
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .height(80.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Horario ╰(*°▽°*)╯",
                        fontSize = 30.sp,
                        color = Color.White
                    )
                }
                Divider()
                NavigationDrawerItem(
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color(49, 54, 63, 255), //
                        selectedContainerColor = Color(34, 40, 49, 255),
                        unselectedTextColor = Color.White,
                        selectedTextColor = Color.White
                    ),
                    shape = RectangleShape,
                    label = { Text(text = "Home") },
                    selected = cambio == "Home",
                    onClick = {
                        cambio = "Home"
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
                Divider()
                NavigationDrawerItem(
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color(49, 54, 63, 255), //
                        selectedContainerColor = Color(34, 40, 49, 255),
                        unselectedTextColor = Color.White,
                        selectedTextColor = Color.White
                    ),
                    shape = RectangleShape,
                    label = { Text(text = "Dia") },
                    selected = cambio == "Dia",
                    onClick = {
                        cambio = "Dia"
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
                Divider()
                NavigationDrawerItem(
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color(49, 54, 63, 255), //
                        selectedContainerColor = Color(34, 40, 49, 255),
                        unselectedTextColor = Color.White,
                        selectedTextColor = Color.White
                    ),
                    shape = RectangleShape,
                    label = { Text(text = "Horario semanal") },
                    selected = cambio == "Horario semanal",
                    onClick = {
                        cambio = "Horario semanal"
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
                Divider()
                NavigationDrawerItem(
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color(49, 54, 63, 255), //
                        selectedContainerColor = Color(34, 40, 49, 255),
                        unselectedTextColor = Color.White,
                        selectedTextColor = Color.White
                    ),
                    shape = RectangleShape,
                    label = { Text(text = "Leer pdf") },
                    selected = cambio == "Leer pdf",
                    onClick = {
                        cambio = "Leer pdf"
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
                Divider()
                NavigationDrawerItem(
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color(49, 54, 63, 255), //
                        selectedContainerColor = Color(34, 40, 49, 255),
                        unselectedTextColor = Color.White,
                        selectedTextColor = Color.White
                    ),
                    shape = RectangleShape,
                    label = { Text(text = "crear horario") },
                    selected = cambio == "crear horario",
                    onClick = {
                        cambio = "crear horario"
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
                Divider()
                NavigationDrawerItem(
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color(49, 54, 63, 255), //
                        selectedContainerColor = Color(34, 40, 49, 255),
                        unselectedTextColor = Color.White,
                        selectedTextColor = Color.White
                    ),
                    shape = RectangleShape,
                    label = { Text(text = "Editar horario") },
                    selected = cambio == "Editar horario",
                    onClick = {
                        cambio = "Editar horario"
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
                Divider()
                NavigationDrawerItem(
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color(49, 54, 63, 255), //
                        selectedContainerColor = Color(34, 40, 49, 255),
                        unselectedTextColor = Color.White,
                        selectedTextColor = Color.White
                    ),
                    shape = RectangleShape,
                    label = { Text(text = "Ajustes") },
                    selected = cambio == "Ajustes",
                    onClick = {
                        cambio = "Ajustes"
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
                Divider()
            }
        },
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = topAppBarColors(
                        containerColor = Color(231, 95, 255, 255),
                        titleContentColor = Color(0, 0, 0)
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    title = {
                        Text(
                            text = vistaBack.currentLocationPublic,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {
                        if (vistaBack.currentLocation == "crear horario") {
                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(49, 54, 63, 255),
                                    contentColor = Color(196, 196, 196, 255)
                                ),
                                shape = RoundedCornerShape(20.dp),
                                contentPadding = PaddingValues(0.dp),
                                border = BorderStroke(1.dp, Color(196, 196, 196, 255)),
                                onClick = {
                                vistaBack.guardarHorario(context)
                                vistaBack.carrera.semestres.forEach { semestre ->
                                    semestre.seleccionado.value = false
                                    semestre.materias.forEach { materia ->
                                        materia.seleccionado.value = false
                                        materia.grupos.forEach { grupo ->
                                            grupo.seleccionado.value = false
                                        }
                                    }
                                }

                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message ="Guardado exitosamente",
                                        actionLabel = "Ok",
                                        duration = SnackbarDuration.Short
                                    )

                                }
                            }) {
                                Box (
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                ) {
                                    Text(text = "Guardar")
                                }
                            }
                        }
                    }
                )
            },
            floatingActionButton = {
                if (vistaBack.currentLocation == "crear horario") {
                    FloatingActionButton(
                        onClick = {
                            Log.d("back", "el stack esta asi: $vistaBack")
                            vistaBack.editar_crear.value = "crear"
                            vistaBack.bottomMenuTitle = "Seleccionar grupos"
                            vistaBack.openBottomSheet.value = true
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }

            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .background(Color(34, 40, 49, 255))
                    .fillMaxSize()
            ) {
                when (vistaBack.currentLocationPublic) {
                    "Home" -> {
                        vistaDeDiaConHora(vistaBack = vistaBack)
                    }
                    "Dia" -> {
                        vistaDeDiaPager(vistaBack = vistaBack)
                    }
                    "Horario semanal" -> {
                        vistaHorarioSemana(vistaBackStack = vistaBack)
                    }
                    "Ajustes" -> {
                        VistaAjustes()
                    }
                    "Leer pdf" -> {
                        vistaLeerPdf(context, vistaBack, snackbarHostState)
                    }
                    "Editar horario" -> {
                        vistaEditarHorario(context, vistaBack)
                    }
                    "crear horario" -> {
                        vistaCrearHorario(vistaBack)
                    }
                    else -> {
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "esto no se deberia de ver xd",
                                color = Color.White
                            )
                            GifImage(context = context)
                        }
                    }
                }
            }
        }
    }

    if (vistaBack.openBottomSheet.value) {
        vistaBottomMenuPrincipal(context = context, vistaBack = vistaBack, snackbarHostState, scope)
    }

    BackHandler(
        enabled = true,
        onBack = {
            when (vistaBack.currentLocation) {
                "nav_drawer" -> {
                    scope.launch {
                        drawerState.apply {
                            close()
                        }
                    }
                }
                else -> {
                    Log.d("back", "Stack pre backPress: ${vistaBack}")
                    vistaBack.popStack()
                    if (vistaBack.stackList.isEmpty()) {
                        (context as ComponentActivity).moveTaskToBack(true)
                        //exitProcess(0)
                    }
                    Log.d("back", "Stack post backPress: ${vistaBack}")
                }
            }
        }
    )
}

@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    context: Context,
    recurso: Int = R.drawable.dancing
) {
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(ImageDecoderDecoder.Factory())
        }
        .build()
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(data = recurso).apply(block = {
                size(coil.size.Size.ORIGINAL)
            }).build(), imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = modifier.fillMaxWidth(),
    )
}