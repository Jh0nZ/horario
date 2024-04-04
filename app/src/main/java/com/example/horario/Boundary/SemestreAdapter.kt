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

class SemestreAdapter : JsonSerializer<Semestre>, JsonDeserializer<Semestre> {
    override fun serialize(
        src: Semestre?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonObject().apply {
            addProperty("nivel", src?.nivel)
            //addProperty("seleccionado", src?.seleccionado?.value ?: false)
            add("materias", context?.serialize(src?.materias))
        }
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Semestre {
        val jsonObject = json?.asJsonObject
        val nivel = jsonObject?.get("nivel")?.asString ?: ""
        //val seleccionado = jsonObject?.get("seleccionado")?.asBoolean ?: false
        val materias = context?.deserialize<MutableList<Materia>>(
            jsonObject?.get("materias"),
            object : TypeToken<MutableList<Materia>>() {}.type
        ) ?: mutableListOf()

        val semestre = Semestre(nivel, mutableStateOf(false))
        semestre.materias.addAll(materias)
        return semestre
    }
}
