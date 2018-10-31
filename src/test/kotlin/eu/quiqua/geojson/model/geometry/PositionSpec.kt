package eu.quiqua.geojson.model.geometry

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.cast
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.has
import com.natpryce.hamkrest.isA
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

internal class PositionSpec : Spek({
    describe("A Position object") {
        context("Create with valid boundaries") {
            val position = Position(longitude = 1.0, latitude = 2.0)
            it("Will validate successfully") {
                assert.that(position.validate(), isA<ValidationResult.Ok>())
            }
            context("With altitude") {
                it("Returns true when calling .hasAltitude") {
                    assert.that(position.copy(altitude = 1.0).hasAltitude, equalTo(true))
                }
            }
            context("Without altitude") {
                it("Returns false when calling .hasAltitude") {
                    assert.that(position.hasAltitude, equalTo(false))
                }
            }
        }
        context("Create with invalid longitude") {
            val longitude = 188.0
            val position = Position(longitude = longitude, latitude = 2.0)
            it("Returns a Validation.Error.OutOfRange") {
                assert.that(position.validate(), isA<ValidationResult.Error.OutOfRange>())
            }
            it("Specifies the error message") {
                val errorMessage = equalTo("Longitude '$longitude' is out of range -180 to 180")
                assert.that(position.validate(), cast(has(ValidationResult.Error.OutOfRange::reason, errorMessage)))
            }
        }
        context("Create with invalid latitude") {
            val latitude = 91.0
            val position = Position(longitude = 1.0, latitude = latitude)
            it("Returns a Validation.Error.OutOfRange") {
                assert.that(position.validate(), isA<ValidationResult.Error.OutOfRange>())
            }
            it("Specifies the error message") {
                val errorMessage = equalTo("Latitude '$latitude' is out of range -90 to 90")
                assert.that(position.validate(), cast(has(ValidationResult.Error.OutOfRange::reason, errorMessage)))
            }
        }
    }
})
