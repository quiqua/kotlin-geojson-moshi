package eu.quiqua.geojson.moshi

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.has
import com.natpryce.hamkrest.isA
import com.natpryce.hamkrest.present
import com.natpryce.hamkrest.throws
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import eu.quiqua.geojson.model.Type
import eu.quiqua.geojson.model.geometry.GeometryCollection
import eu.quiqua.geojson.model.geometry.LineString
import eu.quiqua.geojson.model.geometry.Point
import eu.quiqua.geojson.model.geometry.Position
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.EOFException

internal class GeometryCollectionJsonAdapterSpec : Spek({
    val moshi =
        Moshi.Builder()
            .add(GeometryCollectionJsonAdapter())
            .build()
    val adapter = moshi.adapter<GeometryCollection>(GeometryCollection::class.java)
    describe("From JSON to Object") {
        context("Read a valid GeometryCollection object String") {
            context("With one point geometry object") {
                val jsonString =
                    "{\"geometries\": [{\"coordinates\": [1.0, 1.0], \"type\": \"point\"}], \"type\": \"geometrycollection\"}"
                it("converts to a GeometryCollection object") {
                    val geometryCollection = adapter.fromJson(jsonString)!!
                    assert.that(geometryCollection.type, isA<Type.GeometryCollection>())
                }
                it("has one point geometry object") {
                    val geometryCollection = adapter.fromJson(jsonString)!!
                    assert.that(geometryCollection.geometries.size, equalTo(1))
                    assert.that(geometryCollection.geometries.first().type, isA<Type.Point>())
                }
            }
            context("With multiple geometry objects") {
                val jsonString =
                    "{\"geometries\": [{\"coordinates\": [1.0, 1.0], \"type\": \"point\"}, {\"coordinates\": [[1.0, 1.0],[2.0, 2.0]], \"type\": \"linestring\"}], \"type\": \"geometrycollection\"}"
                it("converts to a GeometryCollection object") {
                    val geometryCollection = adapter.fromJson(jsonString)!!
                    assert.that(geometryCollection.type, isA<Type.GeometryCollection>())
                }
                it("has two geometry objects") {
                    val geometryCollection = adapter.fromJson(jsonString)!!
                    assert.that(geometryCollection.geometries.size, equalTo(2))
                }
            }
        }
        context("Read an invalid GeometryCollection object string") {
            context("With empty string") {
                val jsonString = ""
                it("Throws a EOFException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<EOFException>())
                }
            }
            context("With wrong object") {
                val jsonString = "{\"foo\": 1}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With missing attribute type") {
                val jsonString = "{\"geometries\": [{\"coordinates\": [1.0, 1.0], \"type\": \"point\"}]}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With missing attribute geometries") {
                val jsonString = "{\"type\": \"geometrycollection\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With wrong attribute type") {
                val jsonString =
                    "{\"geometries\": [{\"coordinates\": [1.0, 1.0], \"type\": \"point\"}], \"type\": \"linestring\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
        }
    }
    describe("From Object to JSON") {
        context("Write a GeometryCollection object") {
            val position = Position(longitude = 1.0, latitude = 2.0)
            val point = Point(position)
            val lineString = LineString(listOf(position, position))
            val geometryCollection = GeometryCollection(listOf(point, lineString))
            it("converts to a JSON string") {
                val jsonString = adapter.toJson(geometryCollection)
                assert.that(
                    jsonString, equalTo(
                        "{\"geometries\":[{\"coordinates\":[1.0,2.0],\"type\":\"Point\"},{\"coordinates\":[[1.0,2.0],[1.0,2.0]],\"type\":\"LineString\"}],\"type\":\"GeometryCollection\"}"
                    )
                )
            }
        }
        context("GeometryCollection is null") {
            val geometryCollection: GeometryCollection? = null
            it("Throws a JsonDataException") {
                assert.that({ adapter.toJson(geometryCollection) }, throws<JsonDataException>())
            }
            it("Throws a specific error message") {
                val errorMessage =
                    present(containsSubstring("java.lang.NullPointerException: GeometryCollection was null!"))
                assert.that({ adapter.toJson(geometryCollection) }, throws(has(Exception::message, errorMessage)))
            }
        }
    }
})
