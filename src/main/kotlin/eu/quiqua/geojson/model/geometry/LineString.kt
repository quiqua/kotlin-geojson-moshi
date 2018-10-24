package eu.quiqua.geojson.model.geometry

data class LineString(val coordinates: List<Position>) : Geometry {
    override fun validate(): ValidationResult = GeometryValidation.isLineString(coordinates)

    override val type: Type
        get() = Type.LineString
}
