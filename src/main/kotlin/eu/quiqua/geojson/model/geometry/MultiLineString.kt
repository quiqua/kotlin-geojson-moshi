package eu.quiqua.geojson.model.geometry

data class MultiLineString(val coordinates: List<List<Position>>) : Geometry {
    override val type: Type
        get() = Type.MultiLineString

    override fun validate(): ValidationResult = GeometryValidation.isMultiLineString(coordinates)
}
