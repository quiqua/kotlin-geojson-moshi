package eu.quiqua.geojson.moshi

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import eu.quiqua.geojson.geometry.MultiPolygon
import eu.quiqua.geojson.geometry.Position
import eu.quiqua.geojson.geometry.Type
import eu.quiqua.geojson.geometry.ValidationResult
import java.lang.NullPointerException

class MultiPolygonJsonAdapter {
    companion object {
        private const val COORDINATES_ATTRIBUTE = "coordinates"
        private const val TYPE_ATTRIBUTE = "type"
    }

    private val options: JsonReader.Options = JsonReader.Options.of(COORDINATES_ATTRIBUTE, TYPE_ATTRIBUTE)
    private val positionJsonAdapter = PositionJsonAdapter()

    @FromJson
    fun fromJson(reader: JsonReader): MultiPolygon {
        var type: Type? = null
        val coordinates = mutableListOf<List<List<Position>>>()
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.selectName(options)) {
                0 -> {
                    reader.beginArray()
                    while (reader.hasNext()) {
                        reader.beginArray()
                        val polygon = mutableListOf<List<Position>>()
                        while (reader.hasNext()) {
                            reader.beginArray()
                            val linearRing = mutableListOf<Position>()
                            while (reader.hasNext()) {
                                linearRing.add(positionJsonAdapter.fromJson(reader))
                            }
                            polygon.add(linearRing)
                            reader.endArray()
                        }
                        coordinates.add(polygon)
                        reader.endArray()
                    }
                    reader.endArray()
                }
                1 -> type = Type.convertFromString(reader.nextString())
                -1 -> {
                    reader.skipName()
                    reader.skipValue()
                }
            }
        }
        reader.endObject()

        if (coordinates.isEmpty()) {
            throw JsonDataException("Required coordinates are missing at ${reader.path}")
        }
        if (type == null) {
            throw JsonDataException("Required type is missing at ${reader.path}")
        }
        if (type !== Type.MultiPolygon) {
            throw JsonDataException("Required type is not a MultiPolygon at ${reader.path}")
        }
        val multiPolygon = MultiPolygon(coordinates)
        val validationResult = multiPolygon.validate()
        return when (validationResult) {
            is ValidationResult.Ok -> multiPolygon
            is ValidationResult.OutOfRange -> throw JsonDataException(validationResult.reason)
            is ValidationResult.TooFewElements -> throw JsonDataException(validationResult.reason)
            is ValidationResult.IncompatibleCoordinateDimensions -> throw JsonDataException(validationResult.reason)
            is ValidationResult.NoLinearRing -> throw JsonDataException(validationResult.reason)
        }
    }

    @ToJson
    fun toJson(writer: JsonWriter, value: MultiPolygon?) {
        if (value == null) {
            throw NullPointerException("MultiPolygon was null! Wrap in .nullSafe() to write nullable values.")
        }
        writer.beginObject()
        writer.name(COORDINATES_ATTRIBUTE)
        writer.beginArray()
        value.coordinates.forEach {
            writer.beginArray()
            it.forEach {
                writer.beginArray()
                it.forEach {
                    positionJsonAdapter.toJson(writer, it)
                }
                writer.endArray()
            }
            writer.endArray()
        }
        writer.endArray()

        writer.name(TYPE_ATTRIBUTE)
        writer.value(Type.convertToString(value.type))
        writer.endObject()
    }
}
