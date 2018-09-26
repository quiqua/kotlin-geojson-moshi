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
import eu.quiqua.geojson.geometry.LineString
import eu.quiqua.geojson.geometry.Position
import eu.quiqua.geojson.geometry.Type
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.io.EOFException

internal class LineStringJsonAdapterSpec : Spek({
    val moshi = Moshi.Builder().add(LineStringJsonAdapter()).build()
    val adapter = moshi.adapter<LineString>(LineString::class.java)
    describe("From JSON to Object") {
        context("Read a valid LineString object String") {
            val jsonString = "{\"coordinates\": [[10.0,1.0],[2.0,20.0]], \"type\": \"linestring\"}"
            it("converts to a LineString object with two coordinates") {
                val lineString = adapter.fromJson(jsonString)!!
                assert.that(lineString.coordinates.count(), equalTo(2))
                assert.that(lineString.coordinates.first().longitude, equalTo(10.0))
                assert.that(lineString.coordinates.first().latitude, equalTo(1.0))
                assert.that(lineString.type, isA<Type.LineString>())
            }
        }
        context("Read an invalid LineString object String") {
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
                val jsonString = "{\"coordinates\": [1.0,1.0]}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With missing attribute coordinates") {
                val jsonString = "{\"type\": \"point\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With wrong attribute type") {
                val jsonString = "{\"coordinates\": [1.0,1.0], \"type\": \"linestring\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With wrong attribute coordinates") {
                val jsonString = "{\"coordinates\": 1.0, \"type\": \"point\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With out of bounds coordinates") {
                val jsonString = "{\"coordinates\": [1111.0, 1.0], \"type\": \"point\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
        }
    }

    describe("From Object to JSON") {
        context("Write a LineString object") {
            val position = Position(longitude = 1.0, latitude = 2.0)
            val lineString = LineString(listOf(position, position))
            it("converts to a JSON string") {
                val jsonString = adapter.toJson(lineString)
                assert.that(jsonString, equalTo("{\"coordinates\":[[1.0,2.0],[1.0,2.0]],\"type\":\"LineString\"}"))
            }
        }
        context("LineString is null") {
            val lineString: LineString? = null
            it("Throws a JsonDataException") {
                assert.that({ adapter.toJson(lineString) }, throws<JsonDataException>())
            }
            it("Throws a specific error message") {
                val errorMessage = present(containsSubstring("java.lang.NullPointerException: LineString was null!"))
                assert.that({ adapter.toJson(lineString) }, throws(has(Exception::message, errorMessage)))
            }
        }
    }
})
