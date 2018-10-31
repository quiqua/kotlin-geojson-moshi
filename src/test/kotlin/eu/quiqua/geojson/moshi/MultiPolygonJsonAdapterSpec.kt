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
import eu.quiqua.geojson.model.geometry.MultiPolygon
import eu.quiqua.geojson.model.geometry.Position
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.io.EOFException

internal class MultiPolygonJsonAdapterSpec : Spek({
    val moshi = Moshi.Builder().add(MultiPolygonJsonAdapter()).build()
    val adapter = moshi.adapter<MultiPolygon>(MultiPolygon::class.java)
    describe("From JSON to Object") {
        context("Read a valid MultiPolygon object String") {
            context("With one polygon coordinate subarray") {
                val jsonString =
                    "{\"coordinates\": [[[[10.0,1.0],[10.0,2.0],[11.0,2.0],[11.0,1.0],[10.0,1.0]]]], \"type\": \"multipolygon\"}"
                it("converts to a MultiPolygon object") {
                    val multiPolygon = adapter.fromJson(jsonString)!!
                    assert.that(multiPolygon.coordinates.count(), equalTo(1))
                    assert.that(multiPolygon.type, isA<Type.MultiPolygon>())
                }
            }
            context("With two polygon coordinate subarrays") {
                val jsonString =
                    "{\"coordinates\": [[[[10.0,1.0],[10.0,2.0],[11.0,2.0],[11.0,1.0],[10.0,1.0]]],[[[10.0,1.0],[10.0,2.0],[11.0,2.0],[11.0,1.0],[10.0,1.0]]]], \"type\": \"multipolygon\"}"
                it("converts to a MultiPolygon object") {
                    val multiPolygon = adapter.fromJson(jsonString)!!
                    assert.that(multiPolygon.coordinates.count(), equalTo(2))
                    assert.that(multiPolygon.type, isA<Type.MultiPolygon>())
                }
            }
        }
        context("Read an invalid MultiPolygon object String") {
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
                val jsonString = "{\"coordinates\": [[[[10.0,1.0],[10.0,2.0],[11.0,2.0],[11.0,1.0],[10.0,1.0]]]]}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With missing attribute coordinates") {
                val jsonString = "{\"type\": \"multipolygon\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With wrong attribute type") {
                val jsonString =
                    "{\"coordinates\": [[[[10.0,1.0],[10.0,2.0],[11.0,2.0],[11.0,1.0],[10.0,1.0]]]], \"type\": \"linestring\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With wrong attribute coordinates") {
                val jsonString = "{\"coordinates\": 1.0, \"type\": \"multipolygon\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With out of bounds coordinates") {
                val jsonString =
                    "{\"coordinates\": [[[[200.0,1.0],[10.0,2.0],[11.0,2.0],[11.0,1.0],[10.0,1.0]]]], \"type\": \"multipolygon\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With too few coordinates") {
                val jsonString =
                    "{\"coordinates\": [[[[10.0,1.0],[10.0,2.0],[11.0,2.0],[11.0,1.0]]]], \"type\": \"multipolygon\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With invalid linear ring coordinates") {
                val jsonString =
                    "{\"coordinates\": [[[[10.0,1.0],[10.0,2.0],[11.0,2.0],[11.0,3.0],[11.0,1.0]]]], \"type\": \"multipolygon\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With different coordinate dimensions") {
                val jsonString =
                    "{\"coordinates\": [[[[10.0,1.0,3.0],[10.0,2.0],[11.0,2.0],[11.0,1.0],[10.0,1.0]]]], \"type\": \"multipolygon\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
        }
    }

    describe("From Object to JSON") {
        context("Write a MultiPolygon object") {
            context("With one coordinate subarray") {
                val multiPolygon = MultiPolygon(
                    listOf(
                        listOf(
                            listOf(
                                Position(longitude = 1.0, latitude = 2.0),
                                Position(longitude = 2.0, latitude = 2.0),
                                Position(longitude = 2.0, latitude = 3.0),
                                Position(longitude = 1.0, latitude = 3.0),
                                Position(longitude = 1.0, latitude = 2.0)
                            )
                        )
                    )
                )
                it("converts to a JSON string") {
                    val jsonString = adapter.toJson(multiPolygon)
                    assert.that(
                        jsonString,
                        equalTo("{\"coordinates\":[[[[1.0,2.0],[2.0,2.0],[2.0,3.0],[1.0,3.0],[1.0,2.0]]]],\"type\":\"MultiPolygon\"}")
                    )
                }
            }
            context("With two coordinate subarrays") {
                val multiPolygon = MultiPolygon(
                    listOf(
                        listOf(
                            listOf(
                                Position(longitude = 1.0, latitude = 2.0),
                                Position(longitude = 2.0, latitude = 2.0),
                                Position(longitude = 2.0, latitude = 3.0),
                                Position(longitude = 1.0, latitude = 3.0),
                                Position(longitude = 1.0, latitude = 2.0)
                            )
                        ),
                        listOf(
                            listOf(
                                Position(longitude = 1.0, latitude = 2.0),
                                Position(longitude = 2.0, latitude = 2.0),
                                Position(longitude = 2.0, latitude = 3.0),
                                Position(longitude = 1.0, latitude = 3.0),
                                Position(longitude = 1.0, latitude = 2.0)
                            )
                        )
                    )
                )
                it("converts to a JSON string") {
                    val jsonString = adapter.toJson(multiPolygon)
                    assert.that(
                        jsonString,
                        equalTo("{\"coordinates\":[[[[1.0,2.0],[2.0,2.0],[2.0,3.0],[1.0,3.0],[1.0,2.0]]],[[[1.0,2.0],[2.0,2.0],[2.0,3.0],[1.0,3.0],[1.0,2.0]]]],\"type\":\"MultiPolygon\"}")
                    )
                }
            }
        }
        context("MultiPolygon is null") {
            val multiPolygon: MultiPolygon? = null
            it("Throws a JsonDataException") {
                assert.that({ adapter.toJson(multiPolygon) }, throws<JsonDataException>())
            }
            it("Throws a specific error message") {
                val errorMessage =
                    present(containsSubstring("java.lang.NullPointerException: MultiPolygon was null!"))
                assert.that({ adapter.toJson(multiPolygon) }, throws(has(Exception::message, errorMessage)))
            }
        }
    }
})
