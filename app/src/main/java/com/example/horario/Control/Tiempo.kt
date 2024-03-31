package com.example.horario.Control

import kotlin.math.abs

val m24horas: Int = 24*60

class Tiempo(var hora: Int = 0, var minuto: Int = 0): Comparable<Tiempo> {
    private var overflow: Byte = 0;
    var minutosTotal: Int = hora * 60 + minuto
    // Constructor que acepta una cadena de tiempo en formato "HHMM"
    constructor(tiempoString: String) : this() {
        if (tiempoString.length < 3 || tiempoString.length > 4) {
            throw IllegalArgumentException("El formato de tiempo debe ser 'HHMM' o 'HMM'")
        }
        try {
            val horas: Int
            val minutos: Int
            if (tiempoString.length == 3) {
                horas = tiempoString.substring(0, 1).toInt()
                minutos = tiempoString.substring(1).toInt()
            } else {
                horas = tiempoString.substring(0, 2).toInt()
                minutos = tiempoString.substring(2).toInt()
            }

            if (horas !in 0..23 || minutos !in 0..59) {
                throw IllegalArgumentException("Las horas deben estar entre 00 y 23, y los minutos entre 00 y 59")
            }
            this.hora = horas
            this.minuto = minutos
            this.minutosTotal = horas * 60 + minutos
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Formato de tiempo no válido. Debe ser 'HHMM' o 'HMM'")
        }
    }
    fun sumarMinuto(minutos: Int): Tiempo {
        minutosTotal += minutos
        if (minutosTotal == m24horas) {
            overflow = 1
            minutosTotal = 0
        } else if (minutosTotal > m24horas) {
            overflow = 1
            minutosTotal = 0 + minutosTotal% m24horas
        } else if (minutosTotal < 0){
            overflow = -1
            minutosTotal = m24horas -(abs(minutosTotal)% m24horas)
            if (minutosTotal == m24horas) {
                minutosTotal = 0
            }
        } else {
            overflow = 0
        }

        hora = minutosTotal/60
        minuto = minutosTotal%60
        return this
    }
    override fun toString(): String {
        return String.format("%02d:%02d", hora, minuto)
    }
    override operator fun compareTo(other: Tiempo): Int {
        if (overflow != other.overflow) {
            return overflow.compareTo(other.overflow)
        }
        return minutosTotal.compareTo(other.minutosTotal)
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Tiempo) return false
        return minutosTotal == (other.minutosTotal) && overflow == (other.overflow)
    }
    fun copy(): Tiempo {
        val tiempoCopiado = Tiempo(this.hora, this.minuto)
        tiempoCopiado.overflow = this.overflow
        tiempoCopiado.minutosTotal = this.minutosTotal
        return tiempoCopiado
    }
}

fun main() {
    val timeee = Tiempo(6, 45)
    val maximo = Tiempo(23, 59)
    while (timeee <= maximo) {
        println(timeee)
        timeee.sumarMinuto(90)
    }
    var timeqwe = Tiempo("645")
    println(timeqwe)
    timeqwe = Tiempo("000")
    println(timeqwe)
    timeqwe = Tiempo("2015")
    println(timeqwe)
}