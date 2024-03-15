package com.example.horario

import kotlin.math.abs

val m24horas: Int = 24*60

class Tiempo(var hora: Int = 0, var minuto: Int = 0): Comparable<Tiempo> {
    private var overflow: Byte = 0;
    var minutosTotal: Int = hora * 60 + minuto
    fun sumarMinuto(minutos: Int): Tiempo {
        minutosTotal += minutos
        if (minutosTotal == m24horas) {
            overflow = 1
            minutosTotal = 0
        } else if (minutosTotal > m24horas) {
            overflow = 1
            minutosTotal = 0 + minutosTotal%m24horas
        } else if (minutosTotal < 0){
            overflow = -1
            minutosTotal = m24horas-(abs(minutosTotal)%m24horas)
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
}