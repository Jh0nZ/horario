package com.example.horario.Boundary

import androidx.compose.runtime.mutableStateOf
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class GrupoAdapter : JsonSerializer<Grupo>, JsonDeserializer<Grupo> {
    override fun serialize(
        src: Grupo?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonObject().apply {
            addProperty("nombre", src?.nombre)
            addProperty("extraMateria", src?.extraMateria)
            addProperty("extraCodMat", src?.extraCodMat)
            //addProperty("seleccionado", src?.seleccionado?.value ?: false)
            add("intervalos", context?.serialize(src?.intervalos))
        }
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Grupo {
        val jsonObject = json?.asJsonObject
        val nombre = jsonObject?.get("nombre")?.asString ?: ""
        val extraMateria = jsonObject?.get("extraMateria")?.asString ?: ""
        val extraCodMat = jsonObject?.get("extraCodMat")?.asString ?: ""
        //val seleccionado = jsonObject?.get("seleccionado")?.asBoolean ?: false
        val intervalos = context?.deserialize<MutableList<Intervalo>>(
            jsonObject?.get("intervalos"),
            object : TypeToken<MutableList<Intervalo>>() {}.type
        ) ?: mutableListOf()

        val grupo = Grupo(nombre, extraMateria, extraCodMat, mutableStateOf(false))
        grupo.intervalos.addAll(intervalos)
        return grupo
    }
}
