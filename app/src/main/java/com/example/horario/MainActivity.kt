package com.example.horario

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.backstack_tests.Control.VistaBackStack
import com.example.horario.Boundary.VistaAjustes
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

        setContent {
            WindowCompat.getInsetsController(window, LocalView.current).isAppearanceLightStatusBars = true
            aaa(vistaBack, this)
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
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var initialized by remember { mutableStateOf(false) }
    var cambio by remember { mutableStateOf("")}
    val openBottonSheet = remember {
        mutableStateOf(false)
    }
    val openSelectMateria = remember {
        mutableStateOf(false)
    }

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val bottomSheetState2 = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var currentOption by remember {
        mutableStateOf("")
    }

    val snackbarHostState = remember { SnackbarHostState() }

    val listaSemetres = listOf("Nivel a", "nivel b", "nivel c", "nivel d", "nivel e", "nivel f", "nivel g",
        "Nivel h", "nivel i", "nivel j", "nivel k", "nivel l", "nivel m", "nivel n",
        )
    val listaMaterias = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14")

    LaunchedEffect(drawerState.currentValue) {
        if (initialized) {
            when (drawerState.currentValue) {
                DrawerValue.Open -> {
                    vistaBack.navigateTo("nav_drawer")
                    Log.d("back", "el stack esta asi: $vistaBack")
                    Log.d("Drawer", "Se ha abierto el drawer")
                }

                DrawerValue.Closed -> {
                    Log.d("back", "el stack esta asi: $vistaBack")
                    Log.d("Drawer", "Se ha cerrado el drawer")
                    vistaBack.popStack()
                    if (cambio != "") {
                        vistaBack.navigateTo(cambio)
                        cambio = ""
                    }
                }
            }
        }
        initialized = true
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Dia") },
                    selected = false,
                    onClick = {
                        cambio = "Dia"
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Horario semanal") },
                    selected = false,
                    onClick = {
                        cambio = "Horario semanal"
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Leer pdf") },
                    selected = false,
                    onClick = {
                        cambio = "Leer pdf"
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "crear horario") },
                    selected = false,
                    onClick = {
                        cambio = "crear horario"
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Editar horario") },
                    selected = false,
                    onClick = {
                        cambio = "Editar horario"
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Ajustes") },
                    selected = false,
                    onClick = {
                        cambio = "Ajustes"
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
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
                            text = vistaBack.currentLocation.value,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {
                        if (vistaBack.currentLocation.value == "crear horario") {
                            Button(onClick = {
                                vistaBack.guardarHorario(context)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Guardado exitosamente")
                                }
                            }) {
                                Text(text = "Guardar")
                            }
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )

            },
            floatingActionButton = {
                if (vistaBack.currentLocation.value == "crear horario") {
                    FloatingActionButton(
                        onClick = {
                            Log.d("back", "el stack esta asi: $vistaBack")
                            openBottonSheet.value = true
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
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                when (vistaBack.currentLocation.value) {
                    "/" -> {
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
                        vistaLeerPdf(context, vistaBack)
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
                            Text(text = "esto no se deberia de ver xd")
                            GifImage(context = context)
                        }
                    }
                }
            }
        }
    }

    if (openBottonSheet.value) {
        ModalBottomSheet(
            //tonalElevation = 0.dp, //color color de scrim oscurecido arriba
            contentColor = Color(19,19,19),
            containerColor = Color(255, 235, 178, 255),
            sheetState = bottomSheetState2,
            scrimColor = Color.Transparent,
            //windowInsets = BottomSheetDefaults.windowInsets, //espacio arriba
            onDismissRequest = {openBottonSheet.value = false},
            dragHandle = {
                Text(text = "---------------") // esto es la cabezera
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
                    IconButton(onClick = {
                        openBottonSheet.value = false
                    }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null)
                    }
                }
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(231, 95, 255, 255),
                        contentColor = Color(29, 29, 29, 255),
                        disabledContainerColor = Color(243, 173, 255, 255),
                        disabledContentColor = Color(133, 133, 133, 255)
                    ),
                    modifier = Modifier.padding(8.dp),
                    onClick = {
                        currentOption = "semestre"
                        openSelectMateria.value = true
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
                        currentOption = "materia"
                        openSelectMateria.value = true
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
                                        modifier = Modifier.width(300.dp).padding(8.dp)
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
                                            openBottonSheet.value = false
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
    }

    if (openSelectMateria.value) {
        ModalBottomSheet(
            //tonalElevation = 0.dp, //color color de scrim oscurecido arriba
            contentColor = Color(29,29,29),
            containerColor = Color(139, 147, 255, 255),
            sheetState = bottomSheetState,
            //scrimColor = Color(111, 236, 43, 59),
            windowInsets = BottomSheetDefaults.windowInsets,//espacio arriba
            onDismissRequest = {openSelectMateria.value = false},
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
                            text = "Seleccionar $currentOption",
                            textAlign = TextAlign.Center,
                            fontSize = 22.sp,
                        )
                    }
                    IconButton(onClick = {openSelectMateria.value = false}) {
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

                    if (currentOption == "semestre") {

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
                                    vistaBack.currentMateria.value = "Seleccionar carrera"
                                    openSelectMateria.value = false
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
                                        openSelectMateria.value = false
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

    BackHandler(
        enabled = true,
        onBack = {
            when (vistaBack.currentLocation.value) {
                "nav_drawer" -> scope.launch {
                    drawerState.apply {
                        close()
                    }
                }
                "/" -> scope.launch {
                    // no deberia de cerrar la app, deberia mandarla al background
                    // i suppose onStop?
                    exitProcess(0)
                }
                else -> {
                    Log.d("back", "Stack pre backPress: ${vistaBack}")
                    vistaBack.popStack()
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