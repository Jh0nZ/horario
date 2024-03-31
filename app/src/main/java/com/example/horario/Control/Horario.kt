package com.example.horario.Control

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import com.example.horario.Boundary.Grupo
import com.example.horario.Boundary.generarColorUnicoParaCodigo
import java.time.DayOfWeek

class Horario(
    val saltosTiempo: Int = 90
) {
    val intervalos = mutableStateListOf<Intervalo>()

    fun obtenerMinimo(): Tiempo {
        return intervalos.minByOrNull{ it.inicio }?.inicio?.copy()?: Tiempo(6, 45)
    }

    fun obtenerMaximo(): Tiempo {
        return intervalos.maxByOrNull { it.fin }?.fin?.copy() ?: Tiempo(12,45)
    }

    fun agregarIntervalo(
        nombre: String,
        horaInicio: Tiempo,
        horaFin: Tiempo,
        dia: DayOfWeek,
        color: Color,
        aula: String
    ) {
        val nuevoIntervalo = Intervalo(nombre, horaInicio, horaFin, dia, color, aula)
        intervalos.add(nuevoIntervalo)
    }

    fun agregarGrupo(
        grupo: Grupo
    ) {
        grupo.intervalos.forEach {
            val nuevoIntervalo = Intervalo(
                grupo.extraMateria,
                Tiempo(it.h_inicio),
                Tiempo(it.h_fin),
                text2Day(it.dia),
                generarColorUnicoParaCodigo(grupo.extraMateria+grupo.nombre),
                it.aula,
                it.extraIGrupo
            )
            intervalos.add(nuevoIntervalo)
        }
    }
    fun quitarGrupo(grupo: Grupo) {
        val aremover = mutableListOf<Intervalo>()
        intervalos.forEach {
            if (it.nombre == grupo.extraMateria && it.nro_grupo == grupo.nombre) {
                aremover.add(it)
            }
        }
        aremover.forEach {
            intervalos.remove(it)
        }
    }


    fun text2Day(diaTexto: String): DayOfWeek {
        return when (diaTexto.uppercase()) {
            "LU" -> DayOfWeek.MONDAY
            "MA" -> DayOfWeek.TUESDAY
            "MI" -> DayOfWeek.WEDNESDAY
            "JU" -> DayOfWeek.THURSDAY
            "VI" -> DayOfWeek.FRIDAY
            "SA" -> DayOfWeek.SATURDAY
            "DO" -> DayOfWeek.SUNDAY
            else -> DayOfWeek.SUNDAY // Valor predeterminado si no coincide ninguno
        }
    }

    fun obtenerDia(dia: DayOfWeek): List<Intervalo> {
        return intervalos.filter {it.dia == dia}
            .sortedBy { it.inicio }
    }

    fun getUsedDays(): List<DayOfWeek> {
        val diasUtilizados = intervalos.map { it.dia }
            .distinct() // Días únicos
            .sorted()   // Ordenados

        return if (diasUtilizados.isNotEmpty()) {
            diasUtilizados
        } else {
            listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)
        }
    }

    fun obtenerChoquesDeHorario(dia: DayOfWeek): List<Intervalo> {
        val choques: MutableList<Intervalo> = mutableListOf()
        val intervalosDelDia = intervalos.filter { it.dia == dia }

        for (i in 0 until intervalosDelDia.size - 1) {
            for (j in i + 1 until intervalosDelDia.size) {
                val intervaloA = intervalosDelDia[i]
                val intervaloB = intervalosDelDia[j]

                if (seSuperponen(intervaloA, intervaloB)) {
                    choques.add(intervaloA)
                    choques.add(intervaloB)
                }
            }
        }
        val intervalosSinChoques = intervalosDelDia.filter { !choques.contains(it) }
        val intervaloChoque = IntervaloChoque("Choque", choques)
        return (intervalosSinChoques + intervaloChoque).sortedBy { it.inicio }
    }
    
    fun obtenerDiaFormato(dia: DayOfWeek): List<Intervalo> {
        val respuesta: MutableList<Intervalo> = mutableListOf()
        val invertalosDelDia = obtenerDia(dia)
        var hora_inicial = obtenerMinimo()
        val auxiliar = obtenerMaximo()
        for (inverval in invertalosDelDia) {
            if (hora_inicial != null) {
                if (hora_inicial == inverval.inicio) {
                    respuesta.add(inverval)
                    hora_inicial = inverval.fin
                } else {
                    respuesta.add(Intervalo(null, hora_inicial, inverval.inicio, dia))
                    respuesta.add(inverval)
                    hora_inicial = inverval.fin
                }
            }
        }
        auxiliar.sumarMinuto(90)
        if (hora_inicial != auxiliar) {
            respuesta.add(Intervalo(null, hora_inicial, auxiliar, dia))
        }

        return respuesta
    }

    fun obtenerDiaFormato24h(dia: DayOfWeek): List<Intervalo> {
        val respuesta: MutableList<Intervalo> = mutableListOf()
        val invertalosDelDia = obtenerDia(dia)

        var hora_inicial = Tiempo()
        val auxiliar = Tiempo(24,0)

        for (inverval in invertalosDelDia) {
            if (hora_inicial == inverval.inicio) {
                respuesta.add(inverval)
                hora_inicial = inverval.fin
            } else {
                respuesta.add(Intervalo(null, hora_inicial, inverval.inicio, dia))
                respuesta.add(inverval)
                hora_inicial = inverval.fin
            }
        }
        if (hora_inicial != auxiliar) {
            respuesta.add(Intervalo(null, hora_inicial, auxiliar, dia))
        }

        return respuesta
    }
    fun obtenerHoras(intervalo: Int = saltosTiempo, minimo: Tiempo? = obtenerMinimo(), maximo: Tiempo? = obtenerMaximo()): List<String> {
        val respuesta:MutableList<String> = mutableListOf()
        var horita: Tiempo? = minimo
        while (horita!! <= maximo!!) {
            respuesta.add(horita.toString())
            horita.sumarMinuto(intervalo)
        }
        return respuesta.toList()
    }

    private fun seSuperponen(intervaloA: Intervalo, intervaloB: Intervalo): Boolean {
        return intervaloA.inicio < intervaloB.fin && intervaloB.inicio < intervaloA.fin
    }

    fun ejemplo(): Horario {
        testHorario(this)
        return this
    }


}

fun obtenerHorasString(intervalo: Int, minimo: Tiempo, maximo: Tiempo): List<String> {
    val respuesta: MutableList<String> = mutableListOf()
    var horita: Tiempo = minimo
    while (horita <= maximo) {
        respuesta.add(horita.toString())
        horita.sumarMinuto(intervalo)
    }
    return respuesta.toList()
}

open class Intervalo(
    val nombre: String?,
    val inicio: Tiempo,
    val fin: Tiempo,
    val dia: DayOfWeek,
    val color: Color = Color.White,
    val aula: String = "",
    val nro_grupo: String = ""
) {
    val duracion: Int = diferenciaMinutos(inicio, fin)

    fun diferenciaMinutos(primero: Tiempo, segundo: Tiempo): Int {
        val minutes1 = primero.minutosTotal
        val minutes2 = segundo.minutosTotal
        return if (minutes1 > minutes2) {
            minutes1 - minutes2
        } else {
            minutes2 - minutes1
        }
    }

    override fun toString(): String {
        return "$nombre, $inicio, $fin, $dia, $duracion"
    }
}
class IntervaloChoque(nombre: String?, val choques: List<Intervalo>)
    : Intervalo(nombre = nombre, inicio = choques.minOf { it.inicio }, fin = choques.maxOf { it.fin }, dia = choques.first().dia) {

    override fun toString(): String {
        return super.toString()+ ", Intervalos: $choques"
    }
}

fun main22() {
    val int1 = Intervalo(inicio = Tiempo(), fin = Tiempo(5,0), dia = DayOfWeek.TUESDAY, nombre = null)
    println(int1)
}


fun main() {
    val horario = Horario().ejemplo()

    println(horario)
    println(horario)
}

fun testHorario(
    horario: Horario = Horario()
): Horario {
    horario.agregarIntervalo(
        "Taller de sistemas operativos",
        Tiempo(8, 15),
        Tiempo(9, 45),
        DayOfWeek.MONDAY,
        Color(243, 166, 12, 255),
        "623"
    )
    horario.agregarIntervalo(
        "Estructura y semantica de lenguajes de programacion",
        Tiempo(11, 15),
        Tiempo(12, 45),
        DayOfWeek.MONDAY,
        Color(46, 236, 215, 255),
        "617B"
    )

    horario.agregarIntervalo(
        "Estructura y semantica de lenguajes de programacion",
        Tiempo(8, 15),
        Tiempo(9, 45),
        DayOfWeek.TUESDAY,
        Color(46, 236, 215, 255),
        "690C"
    )
    horario.agregarIntervalo(
        "Ingenieria de software",
        Tiempo(11, 15),
        Tiempo(12, 45),
        DayOfWeek.TUESDAY,
        Color(243, 69, 69, 255),
        "690B"
    )
    horario.agregarIntervalo(
        "Graficacion por computadora",
        Tiempo(12, 45),
        Tiempo(14, 15),
        DayOfWeek.TUESDAY,
        Color(200, 206, 38, 255),
        "Laboratorio"
    )

    horario.agregarIntervalo(
        "Graficacion por computadora",
        Tiempo(6, 45),
        Tiempo(8, 15),
        DayOfWeek.WEDNESDAY,
        Color(200, 206, 38, 255),
        "Laboratorio"
    )
    horario.agregarIntervalo(
        "Ingenieria de software",
        Tiempo(9, 45),
        Tiempo(11, 15),
        DayOfWeek.WEDNESDAY,
        Color(243, 69, 69, 255),
        "Laboratorio"
    )
    horario.agregarIntervalo(
        "Taller de base de datos",
        Tiempo(12, 45),
        Tiempo(14, 15),
        DayOfWeek.WEDNESDAY,
        Color(196, 55, 211, 255),
        "Laboratorio"
    )

    horario.agregarIntervalo(
        "Graficacion por computadora",
        Tiempo(6, 45),
        Tiempo(8, 15),
        DayOfWeek.THURSDAY,
        Color(200, 206, 38, 255),
        "Laboratorio"
    )
    horario.agregarIntervalo(
        "Taller de sistemas operativos",
        Tiempo(8, 15),
        Tiempo(9, 45),
        DayOfWeek.THURSDAY,
        Color(243, 166, 12, 255),
        "Laboratorio"
    )
    horario.agregarIntervalo(
        "Estructura y semantica de lenguajes de programacion",
        Tiempo(11, 15),
        Tiempo(12, 45),
        DayOfWeek.THURSDAY,
        Color(46, 236, 215, 255),
        "692H"
    )
    horario.agregarIntervalo(
        "Taller de base de datos",
        Tiempo(12, 45),
        Tiempo(14, 15),
        DayOfWeek.THURSDAY,
        Color(196, 55, 211, 255),
        "690B"
    )

    horario.agregarIntervalo(
        "Taller de sistemas operativos",
        Tiempo(8, 15),
        Tiempo(9, 45),
        DayOfWeek.FRIDAY,
        Color(243, 166, 12, 255),
        "624"
    )
    horario.agregarIntervalo(
        "Ingenieria de software",
        Tiempo(9, 45),
        Tiempo(11, 15),
        DayOfWeek.FRIDAY,
        Color(243, 69, 69, 255),
        "691D"
    )
    return horario
}