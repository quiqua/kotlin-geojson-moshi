package eu.quiqua.geojson.model.geometry

interface GeoJson {
    fun validate(): ValidationResult
    val type: Type
}
