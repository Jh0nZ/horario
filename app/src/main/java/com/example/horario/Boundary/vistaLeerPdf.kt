package com.example.horario.Boundary

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.backstack_tests.Control.VistaBackStack
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream

val regexMateria = Regex("(^[A-Z])\\s(\\d+)\\s([A-Z]+(?:\\.?\\s[A-Z]+)*)\\s([A-Z0-9]*)\\s([A-Z]+)\\s(\\d+)\\s?-\\s?(\\d+)\\s+\\(([A-Za-z0-9]+)\\)\\s([A-Z])\\s(\\.?\\s?[A-Z]+(?:\\s[A-Z]+)*)")
@Composable
fun vistaLeerPdf(context: Context, vistaBack: VistaBackStack, snackbarHostState: SnackbarHostState) {
    val result = remember {
        mutableStateOf<Uri?>(null)
    }
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) {
        result.value = it
        it?.let { uri ->
            val exito = leerTextito(uri, context, vistaBack)
            if (exito) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "¡Archivo PDF leído exitosamente!",
                        actionLabel = "Ok",
                        duration = SnackbarDuration.Short
                    )
                }
            } else {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message ="Error al leer el archivo pdf",
                        actionLabel = "Ok",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Button(onClick = {
                launcher.launch(arrayOf("application/pdf"))
            }) {
                Text(text = "Seleccionar archivo")
            }
        }
    }
}

fun leerTextito(
    documento: Uri,
    context: Context,
    vistaBack: VistaBackStack,
): Boolean {
    Log.d("hola", documento.path.toString())

    val contentResolver = context.contentResolver
    val pdfUri: Uri = documento
    val inputStream: InputStream? = contentResolver.openInputStream(pdfUri)

    return try {
        if (inputStream != null) {
            val tempFile = File(context.cacheDir, "temp_file.pdf")
            tempFile.deleteOnExit()
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }

            val pdfReader = PdfReader(tempFile.absolutePath)
            val numberOfPages = pdfReader.numberOfPages
            var res = ""
            for (pageNumber in 1..numberOfPages) {
                val pageText = PdfTextExtractor.getTextFromPage(pdfReader, pageNumber)
                res += "$pageText\n"
            }
            pdfReader.close()
            inputStream.close()

            vistaBack.carrera.semestres.clear()
            res.lines().drop(3).filter { it.isNotBlank() }.forEach {
                vistaBack.carrera.insertaMateriaEnSemestre(ExtraerMateria(it))
            }

            vistaBack.guardarCarrera(context)
            Log.d("pruebas", vistaBack.carrera.semestres.toString())
            true
        } else {
            Log.e("pdf", "InputStream es nulo")
            false
        }
    } catch (e: Exception) {
        e.message?.let { Log.e("error", it) }
        false
    }
}

fun main6() {
    val texto1 = "B 2008022 ALGEBRA II 5A LU 1715-1845 (690E) C SALINAS PERICON WALTER OSCAR GONZALO"
    val texto2 = "A 2006063 FISICA GENERAL B2 LU 945 -1115 (620) P RUIZ UCUMARI IVAN"
    val materiaTemp = ExtraerMateria(texto1)
    println(materiaTemp)
}

fun main() {
    val nombreArchivo = "C:\\Users\\jhon\\Desktop\\horario_example.txt"

    val informatica = Carrera("Ing informatica")
    File(nombreArchivo).useLines { lines ->
        lines.drop(3).forEach {
            val extraida = ExtraerMateria(it)
            informatica.insertaMateriaEnSemestre(extraida)
        }
    }
    informatica.semestres.forEach {
        println(it)
    }
}

class Carrera(
    val nombre: String
) {
    val semestres = mutableListOf<Semestre>()
    fun insertaMateriaEnSemestre(extraerMateria: ExtraerMateria) {
        semestres.forEach {
            if (it.nivel == extraerMateria.nivel) {
                it.insertarMateria(extraerMateria)
                return
            }
        }
        val nuevo = Semestre(extraerMateria.nivel)
        nuevo.insertarMateria(extraerMateria)
        semestres.add(nuevo)
    }
    fun getSemestre(nivel: String): Semestre? {
        semestres.forEach {
            if (it.nivel == nivel) {
                return it
            }
        }
        return null
    }
}

class Semestre(
    val nivel: String,
    var seleccionado: Boolean = false
)
{
    val materias = mutableListOf<Materia>()
    fun insertarMateria(extraerMateria: ExtraerMateria) {
        materias.forEach {
            if (it.nombre == extraerMateria.nom_mat) {
                it.insertarGrupo(extraerMateria)
                return
            }
        }
        val newMateria = Materia(extraerMateria.nom_mat, extraerMateria.cod_mat)
        newMateria.insertarGrupo(extraerMateria)
        materias.add(newMateria)
    }
    fun getMateria(nombre: String): Materia? {
        materias.forEach {
            if (it.nombre == nombre) {
                return it
            }
        }
        return null
    }
    override fun toString(): String {
        var res = "\n"
        materias.forEach {
            res += "$it\n"
        }
        return "$nivel: $res"
    }
}

class Materia(
    val nombre: String,
    val codigo: String,
    var seleccionado: Boolean = false
)
{
    val grupos = mutableListOf<Grupo>()
    fun insertarGrupo(extraerMateria: ExtraerMateria) {
        grupos.forEach {
            if (it.nombre == extraerMateria.grupo) {
                it.insertarIntervalo(extraerMateria)
                return
            }
        }
        val newGroup = Grupo(extraerMateria.grupo, extraerMateria.nom_mat, extraerMateria.cod_mat)
        newGroup.insertarIntervalo(extraerMateria)
        grupos.add(newGroup)
    }
    override fun toString(): String {
        var res = "\n"
        grupos.forEach {
            res += "$it\n"
        }
        return "$nombre: $res"
    }
}

class Grupo(
    val nombre: String,
    val extraMateria: String,
    val extraCodMat: String,
    var seleccionado: Boolean = false
)
{
    var intervalos = mutableListOf<Intervalo>()
    fun insertarIntervalo(extraerMateria: ExtraerMateria) {
        intervalos.add(extraerMateria.intervalo!!)
    }
    override fun toString(): String {
        val docentesUnicos = intervalos.map { it.docente }.distinct()
        val cadenaDocentes = docentesUnicos.joinToString(separator = ",")
        return "Grupo: $nombre, $cadenaDocentes"
    }
}

class Intervalo (
    val extraIMateria: String,
    val extraIGrupo: String,
    var docente: String,
    var dia: String,
    var aula: String,
    var h_inicio: String,
    var h_fin: String,
    var tipo: String
) {
    override fun toString(): String {
        return "docente: $docente, dia: $dia, aula: $aula, h_inicio: $h_inicio, h_fin: $h_fin, tipo: $tipo"
    }
}

class ExtraerMateria(
    texto: String
) {
    var intervalo: Intervalo? = null
    var nivel: String = ""
    var cod_mat: String = ""
    var nom_mat: String = ""
    var grupo: String = ""
    init {
        val match = regexMateria.find(texto)
        if (match != null) {
            nivel = match.groupValues[1]
            cod_mat = match.groupValues[2]
            nom_mat = match.groupValues[3]
            grupo = match.groupValues[4]
            intervalo = Intervalo(
                extraIMateria = nom_mat,
                extraIGrupo = grupo,
                docente = match.groupValues[10],
                dia = match.groupValues[5],
                aula = match.groupValues[8],
                h_inicio = match.groupValues[6],
                h_fin = match.groupValues[7],
                tipo = match.groupValues[9]
            )
        } else {
            throw Exception("No es una materia valida $texto")
        }
    }
    override fun toString(): String {
        return "nivel: $nivel, cod_mat: $cod_mat, nom_mat: $nom_mat, grupo: $grupo, $intervalo"
    }
}