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
import eu.quiqua.geojson.model.geometry.MultiPoint
import eu.quiqua.geojson.model.geometry.Position
import eu.quiqua.geojson.model.geometry.Type
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.io.EOFException

internal class MultiPointJsonAdapterSpec : Spek({
    val moshi = Moshi.Builder().add(MultiPointJsonAdapter()).build()
    val adapter = moshi.adapter<MultiPoint>(MultiPoint::class.java)
    describe("From JSON to Object") {
        context("Read a valid MultiPoint object String") {
            context("With one coordinate pair") {
                val jsonString = "{\"coordinates\": [[10.0,1.0]], \"type\": \"multipoint\"}"
                it("converts to a MultiPoint object") {
                    val lineString = adapter.fromJson(jsonString)!!
                    assert.that(lineString.coordinates.count(), equalTo(1))
                    assert.that(lineString.coordinates.first().longitude, equalTo(10.0))
                    assert.that(lineString.coordinates.first().latitude, equalTo(1.0))
                    assert.that(lineString.type, isA<Type.MultiPoint>())
                }
            }
            context("With two coordinate pairs") {
                val jsonString = "{\"coordinates\": [[10.0,1.0], [20.0, 2.0]], \"type\": \"multipoint\"}"
                it("converts to a MultiPoint object") {
                    val lineString = adapter.fromJson(jsonString)!!
                    assert.that(lineString.coordinates.count(), equalTo(2))
                    assert.that(lineString.type, isA<Type.MultiPoint>())
                }
            }
        }
        context("Read an invalid MultiPoint object String") {
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
                val jsonString = "{\"coordinates\": [[1.0,1.0]]}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With missing attribute coordinates") {
                val jsonString = "{\"type\": \"multipoint\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With wrong attribute type") {
                val jsonString = "{\"coordinates\": [[1.0,1.0]], \"type\": \"linestring\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With wrong attribute coordinates") {
                val jsonString = "{\"coordinates\": 1.0, \"type\": \"multipoint\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With out of bounds coordinates") {
                val jsonString = "{\"coordinates\": [[1111.0, 1.0]], \"type\": \"multipoint\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With different coordinate dimensions") {
                val jsonString = "{\"coordinates\": [[111.0, 1.0], [1.0, 1.0, 1.0]], \"type\": \"multipoint\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
        }
    }

    describe("From Object to JSON") {
        context("Write a MultiPoint object") {
            context("With one coordinate pair") {
                val position = Position(longitude = 1.0, latitude = 2.0)
                val multiPoint = MultiPoint(listOf(position))
                it("converts to a JSON string") {
                    val jsonString = adapter.toJson(multiPoint)
                    assert.that(jsonString, equalTo("{\"coordinates\":[[1.0,2.0]],\"type\":\"MultiPoint\"}"))
                }
            }
            context("With two coordinate pairs") {
                val position = Position(longitude = 1.0, latitude = 2.0)
                val multiPoint = MultiPoint(listOf(position, position))
                it("converts to a JSON string") {
                    val jsonString = adapter.toJson(multiPoint)
                    assert.that(jsonString, equalTo("{\"coordinates\":[[1.0,2.0],[1.0,2.0]],\"type\":\"MultiPoint\"}"))
                }
            }
        }
        context("MultiPoint is null") {
            val multiPoint: MultiPoint? = null
            it("Throws a JsonDataException") {
                assert.that({ adapter.toJson(multiPoint) }, throws<JsonDataException>())
            }
            it("Throws a specific error message") {
                val errorMessage = present(containsSubstring("java.lang.NullPointerException: MultiPoint was null!"))
                assert.that({ adapter.toJson(multiPoint) }, throws(has(Exception::message, errorMessage)))
            }
        }
    }
})
