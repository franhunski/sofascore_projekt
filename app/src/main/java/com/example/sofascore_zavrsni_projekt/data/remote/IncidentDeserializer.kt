package com.example.sofascore_zavrsni_projekt.data.remote

import com.example.sofascore_zavrsni_projekt.data.miniSofa_models.Incident
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class IncidentDeserializer : JsonDeserializer<Incident> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Incident {
        val jsonObject = json?.asJsonObject!!
        val type = jsonObject.get("type").asString

        return when (type) {
            "card" -> context?.deserialize<Incident.Card>(json, Incident.Card::class.java)!!
            "goal" -> context?.deserialize<Incident.Goal>(json, Incident.Goal::class.java)!!
            "period" -> context?.deserialize<Incident.Period>(json, Incident.Period::class.java)!!
            else -> throw JsonParseException("Unknown type: $type")
        }
    }
}