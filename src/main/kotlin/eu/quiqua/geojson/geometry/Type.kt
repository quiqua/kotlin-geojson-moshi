package eu.quiqua.geojson.geometry

sealed class Type {
    object Point : Type()
    object LineString : Type()
    object Polygon : Type()
    object MultiPoint : Type()
    object MultiLineString : Type()
    object MultiPolygon : Type()
    object GeometryCollection : Type()
    object Unknown : Type()

    companion object {
        fun convertFromString(value: String): Type {
            return when (value.toLowerCase()) {
                "point" -> Type.Point
                "linestring" -> Type.LineString
                "polygon" -> Type.Polygon
                "multipoint" -> Type.MultiPoint
                "multilinestring" -> Type.MultiLineString
                "multipolygon" -> Type.MultiPolygon
                "geometrycollection" -> Type.GeometryCollection
                else -> Type.Unknown
            }
        }

        fun convertToString(value: Type): String {
            return when (value) {
                is Point -> "Point"
                is LineString -> "LineString"
                is Polygon -> "Polygon"
                is MultiPoint -> "MultiPoint"
                is MultiLineString -> "MultiLineString"
                is MultiPolygon -> "MultiPolygon"
                is GeometryCollection -> "GeometryCollection"
                else -> "Unknown"
            }
        }
    }
}
