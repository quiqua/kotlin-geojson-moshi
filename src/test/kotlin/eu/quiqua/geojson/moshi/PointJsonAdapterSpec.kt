package eu.quiqua.geojson.moshi

import com.natpryce.hamkrest.Matcher
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
import eu.quiqua.geojson.model.geometry.Point
import eu.quiqua.geojson.model.geometry.Position
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.EOFException
import java.util.Objects

internal class PointJsonAdapterSpec : Spek({
    val moshi = Moshi.Builder().add(PointJsonAdapter()).build()
    val adapter = moshi.adapter<Point>(Point::class.java)
    describe("From JSON to Object") {
        context("Read a valid Point object String") {
            val jsonString = "{\"coordinates\": [10.0,1.0], \"type\": \"point\"}"
            it("converts to a Point object with longitude and latitude") {
                val point = adapter.fromJson(jsonString)!!
                assert.that(point.longitude, equalTo(10.0))
                assert.that(point.latitude, equalTo(1.0))
                assert.that(point.altitude, Matcher(Objects::isNull))
                assert.that(point.type, isA<Type.Point>())
            }
            context("foobar") {
                val jsonString = "{\"coordinates\": [10.0,1.0], \"type\": \"point\", \"foo\": 1}"
                it("barbar") {
                    val point = adapter.fromJson(jsonString)!!
                    assert.that(point.type, isA<Type.Point>())
                }
            }
        }
        context("Read an invalid Point object String") {
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
        context("Write a Point object") {
            val position = Position(longitude = 1.0, latitude = 2.0)
            val point = Point(position)
            it("converts to a JSON string") {
                val jsonString = adapter.toJson(point)
                assert.that(jsonString, equalTo("{\"coordinates\":[1.0,2.0],\"type\":\"Point\"}"))
            }
        }
        context("Point is null") {
            val point: Point? = null
            it("Throws a JsonDataException") {
                assert.that({ adapter.toJson(point) }, throws<JsonDataException>())
            }
            it("Throws a specific error message") {
                val errorMessage = present(containsSubstring("java.lang.NullPointerException: Point was null!"))
                assert.that({ adapter.toJson(point) }, throws(has(Exception::message, errorMessage)))
            }
        }
    }
})
