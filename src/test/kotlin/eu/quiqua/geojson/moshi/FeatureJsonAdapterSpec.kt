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
import eu.quiqua.geojson.model.Feature
import eu.quiqua.geojson.model.Type
import eu.quiqua.geojson.model.geometry.Point
import eu.quiqua.geojson.model.geometry.Position
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.EOFException

internal class FeatureJsonAdapterSpec : Spek({
    val moshi = Moshi.Builder()
        .add(FeatureJsonAdapterFactory())
        .build()
    val adapter = moshi.adapter<Feature>(Feature::class.java)
    describe("From JSON to Object") {
        context("Read a valid Feature object String") {
            context("With additional properties") {
                val jsonString =
                    "{\"type\": \"Feature\",\"geometry\": {\"type\": \"Point\",\"coordinates\": [102.0, 0.5]\n},\"properties\": {\"prop0\": \"value0\"}}"
                it("converts to a Feature object") {
                    val feature = adapter.fromJson(jsonString)!!
                    assert.that(feature.type, isA<Type.Feature>())
                }
            }
            context("Without additional properties") {
                val jsonString =
                    "{\"type\": \"Feature\",\"geometry\": {\"type\": \"Point\",\"coordinates\": [102.0, 0.5]\n}}"
                it("converts to a Feature object") {
                    val feature = adapter.fromJson(jsonString)!!
                    assert.that(feature.type, isA<Type.Feature>())
                }
            }
            context("with an unlocated geometry") {
                val jsonString =
                    "{\"type\": \"Feature\",\"geometry\": null}"
                it("converts to a Feature object") {
                    val feature = adapter.fromJson(jsonString)!!
                    assert.that(feature.type, isA<Type.Feature>())
                }
            }
        }
        context("Read an invalid Feature object string") {
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
                val jsonString = "{\"geometry\": {\"coordinates\": [1.0, 1.0], \"type\": \"point\"}}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("With wrong attribute type") {
                val jsonString =
                    "{\"geometry\": {\"coordinates\": [1.0, 1.0], \"type\": \"point\"}, \"type\": \"linestring\"}"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
        }
    }
    describe("From Object to JSON") {
        context("Without geometry and properties values") {
            val feature = Feature(properties = null, geometry = null)
            it("serializes all properties to JSON") {
                assert.that(
                    adapter.toJson(feature), equalTo("{\"geometry\":null,\"properties\":null,\"type\":\"Feature\"}")
                )
            }
        }
        context("With a properties map") {
            val properties = hashMapOf("foo" to 1, "bar" to "baz")
            val feature = Feature(properties = properties)
            it("serializes all properties to JSON") {
                assert.that(
                    adapter.toJson(feature),
                    equalTo("{\"geometry\":null,\"properties\":{\"bar\":\"baz\",\"foo\":1},\"type\":\"Feature\"}")
                )
            }
        }
        context("With a geometry object") {
            val point = Point(Position(longitude = 1.0, latitude = 1.0))
            val feature = Feature(geometry = point)
            it("serializes all attributes to JSON") {
                assert.that(
                    adapter.toJson(feature),
                    equalTo("{\"geometry\":{\"coordinates\":[1.0,1.0],\"type\":\"Point\"},\"properties\":null,\"type\":\"Feature\"}")
                )
            }
        }
        context("Feature is null") {
            val feature: Feature? = null
            it("Throws a JsonDataException") {
                assert.that({ adapter.toJson(feature) }, throws<JsonDataException>())
            }
            it("Throws a specific error message") {
                val errorMessage =
                    present(containsSubstring("java.lang.NullPointerException: Feature was null!"))
                assert.that({ adapter.toJson(feature) }, throws(has(Exception::message, errorMessage)))
            }
        }
    }
})
