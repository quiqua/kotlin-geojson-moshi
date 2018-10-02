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
import eu.quiqua.geojson.geometry.MultiLineString
import eu.quiqua.geojson.geometry.Position
import eu.quiqua.geojson.geometry.Type
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.io.EOFException

internal class MultiLineStringJsonAdapterSpec : Spek({
    val moshi = Moshi.Builder().add(MultiLineStringJsonAdapter()).build()
    val adapter = moshi.adapter<MultiLineString>(MultiLineString::class.java)
    describe("From JSON to Object") {
        context("Read a valid MultiLineString object String") {
            context("With one coordinate subarray") {
                val jsonString = "{\"coordinates\": [[[10.0,1.0],[11.0,2.0]]], \"type\": \"multilinestring\"}"
                it("converts to a MultiLineString object") {
                    val multiLineString = adapter.fromJson(jsonString)!!
                    assert.that(multiLineString.coordinates.count(), equalTo(1))
                    assert.that(multiLineString.type, isA<Type.MultiLineString>())
                }
            }
            context("With two coordinate subarrays") {
                val jsonString =
                    "{\"coordinates\": [[[10.0,1.0],[11.0,2.0]],[[3.0,3.0],[4.0,4.0]]], \"type\": \"multilinestring\"}"
                it("converts to a MultiLineString object") {
                    val multiLineString = adapter.fromJson(jsonString)!!
                    assert.that(multiLineString.coordinates.count(), equalTo(2))
                    assert.that(multiLineString.type, isA<Type.MultiLineString>())
                }
            }
        }
        context("Read an invalid MultiLineString object String") {
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
                val jsonString = "{\"coordinates\": [[[1.0,1.0],[2.0,2.0]]]}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With missing attribute coordinates") {
                val jsonString = "{\"type\": \"multilinestring\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With wrong attribute type") {
                val jsonString = "{\"coordinates\": [[[1.0,1.0],[2.0,2.0]]], \"type\": \"linestring\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With wrong attribute coordinates") {
                val jsonString = "{\"coordinates\": 1.0, \"type\": \"multilinestring\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With out of bounds coordinates") {
                val jsonString = "{\"coordinates\": [[[1111.0,1.0],[2.0,2.0]]], \"type\": \"multilinestring\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With too few coordinates") {
                val jsonString = "{\"coordinates\": [[[1.0,1.0]]], \"type\": \"multilinestring\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With different coordinate dimensions") {
                val jsonString = "{\"coordinates\": [[[1.0,1.0],[2.0,2.0,2.0]]], \"type\": \"multilinestring\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
        }
    }

    describe("From Object to JSON") {
        context("Write a MultiLineString object") {
            context("With one coordinate subarray") {
                val multiLineString = MultiLineString(
                    listOf(
                        listOf(
                            Position(longitude = 1.0, latitude = 2.0),
                            Position(longitude = 2.0, latitude = 3.0)
                        )
                    )
                )
                it("converts to a JSON string") {
                    val jsonString = adapter.toJson(multiLineString)
                    assert.that(
                        jsonString,
                        equalTo("{\"coordinates\":[[[1.0,2.0],[2.0,3.0]]],\"type\":\"MultiLineString\"}")
                    )
                }
            }
            context("With two coordinate subarrays") {
                val multiLineString = MultiLineString(
                    listOf(
                        listOf(
                            Position(longitude = 1.0, latitude = 2.0),
                            Position(longitude = 2.0, latitude = 3.0)
                        ),
                        listOf(
                            Position(longitude = 3.0, latitude = 2.0),
                            Position(longitude = 4.0, latitude = 3.0)
                        )
                    )
                )
                it("converts to a JSON string") {
                    val jsonString = adapter.toJson(multiLineString)
                    assert.that(
                        jsonString,
                        equalTo("{\"coordinates\":[[[1.0,2.0],[2.0,3.0]],[[3.0,2.0],[4.0,3.0]]],\"type\":\"MultiLineString\"}")
                    )
                }
            }
        }
        context("MultiLineString is null") {
            val multiLineString: MultiLineString? = null
            it("Throws a JsonDataException") {
                assert.that({ adapter.toJson(multiLineString) }, throws<JsonDataException>())
            }
            it("Throws a specific error message") {
                val errorMessage =
                    present(containsSubstring("java.lang.NullPointerException: MultiLineString was null!"))
                assert.that({ adapter.toJson(multiLineString) }, throws(has(Exception::message, errorMessage)))
            }
        }
    }
})
