package eu.quiqua.geojson.model

import eu.quiqua.geojson.model.geometry.Type
import eu.quiqua.geojson.model.geometry.ValidationResult

interface GeoJson {
    fun validate(): ValidationResult
    val type: Type
}
