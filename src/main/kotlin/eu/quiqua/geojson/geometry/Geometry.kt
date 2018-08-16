package eu.quiqua.geojson.geometry

interface Geometry {
    fun validate(): ValidationResult
    val type: Type
}
