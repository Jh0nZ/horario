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

class MateriaAdapter : JsonSerializer<Materia>, JsonDeserializer<Materia> {
    override fun serialize(
        src: Materia?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonObject().apply {
            addProperty("nombre", src?.nombre)
            addProperty("codigo", src?.codigo)
            //addProperty("seleccionado", src?.seleccionado?.value ?: false)
            add("grupos", context?.serialize(src?.grupos))
        }
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Materia {
        val jsonObject = json?.asJsonObject
        val nombre = jsonObject?.get("nombre")?.asString ?: ""
        val codigo = jsonObject?.get("codigo")?.asString ?: ""
        //val seleccionado = jsonObject?.get("seleccionado")?.asBoolean ?: false
        val grupos = context?.deserialize<MutableList<Grupo>>(
            jsonObject?.get("grupos"),
            object : TypeToken<MutableList<Grupo>>() {}.type
        ) ?: mutableListOf()

        val materia = Materia(nombre, codigo, mutableStateOf(false))
        materia.grupos.addAll(grupos)
        return materia
    }
}
