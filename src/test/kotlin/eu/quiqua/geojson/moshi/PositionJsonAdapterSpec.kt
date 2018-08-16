package eu.quiqua.geojson.moshi

import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.has
import com.natpryce.hamkrest.present
import com.natpryce.hamkrest.throws
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import eu.quiqua.geojson.geometry.Position
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.util.Objects

internal class PositionJsonAdapterSpec : Spek({
    val moshi = Moshi.Builder().add(PositionJsonAdapter()).build()
    val adapter = moshi.adapter<Position>(Position::class.java)
    describe("From JSON to Object") {
        context("Read a valid lng lat String") {
            val jsonString = "[10.0,1.0]"
            it("converts to a Position object with longitude and latitude") {
                val position = adapter.fromJson(jsonString)!!
                assert.that(position.longitude, equalTo(10.0))
                assert.that(position.latitude, equalTo(1.0))
                assert.that(position.altitude, Matcher(Objects::isNull))
            }
        }
        context("Read a valid lng lat alt String") {
            val jsonString = "[10.0,1.0,5.0]"
            it("converts to a Position object with longitude, latitude and altitude") {
                val position = adapter.fromJson(jsonString)!!
                assert.that(position.longitude, equalTo(10.0))
                assert.that(position.latitude, equalTo(1.0))
                assert.that(position.altitude, equalTo(5.0))
            }
        }
        context("Read an invalid String") {
            context("Coordinates are invalid") {
                val jsonString = "[1000.0,1.0]"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("Coordinates only contain one element") {
                val jsonString = "[1000.0]"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
            context("A non-array object is passed") {
                val jsonString = "1000.0"
                it("Throws a JsonDataException") {
                    assert.that({ adapter.fromJson(jsonString) }, throws<JsonDataException>())
                }
            }
        }
    }

    describe("From Object to JSON") {
        context("Write a Position object with longitude and latitude") {
            val position = Position(longitude = 1.0, latitude = 2.0)
            it("converts to a JSON string") {
                val jsonString = adapter.toJson(position)
                assert.that(jsonString, equalTo("[1.0,2.0]"))
            }
        }
        context("Write a Position object with longitude, latitude and altitude") {
            val position = Position(longitude = 1.0, latitude = 2.0, altitude = 3.0)
            it("converts to a JSON string") {
                val jsonString = adapter.toJson(position)
                assert.that(jsonString, equalTo("[1.0,2.0,3.0]"))
            }
        }
        context("Position is null") {
            val position: Position? = null
            it("Throws a JsonDataException") {
                assert.that({ adapter.toJson(position) }, throws<JsonDataException>())
            }
            it("Throws a specific error message") {
                val errorMessage = present(containsSubstring("java.lang.NullPointerException: Position was null!"))
                assert.that({ adapter.toJson(position) }, throws(has(Exception::message, errorMessage)))
            }
        }
    }
})
