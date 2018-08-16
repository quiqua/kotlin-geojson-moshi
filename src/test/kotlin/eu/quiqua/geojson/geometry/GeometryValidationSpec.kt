package eu.quiqua.geojson.geometry

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.isA
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

internal class GeometryValidationSpec : Spek({
    describe("Validate Point coordinates") {
        context("valid input") {
            it("returns ValidationResult.Ok for 2D coordinates") {
                val coordinates = Position(longitude = 1.0, latitude = 1.0)
                assert.that(GeometryValidation.isPoint(coordinates), isA<ValidationResult.Ok>())
            }
            it("returns ValidationResult.Ok for 3D coordinates") {
                val coordinates = Position(longitude = 1.0, latitude = 1.0, altitude = 2.0)
                assert.that(GeometryValidation.isPoint(coordinates), isA<ValidationResult.Ok>())
            }
        }
        context("invalid input") {
            it("returns ValidationResult.OutOfRangeError when longitude is out of range") {
                val coordinates = Position(longitude = 181.0, latitude = 1.0)
                assert.that(GeometryValidation.isPoint(coordinates), isA<ValidationResult.OutOfRangeError>())
            }
            it("returns ValidationResult.OutOfRangeError when latitude is out of range") {
                val coordinates = Position(longitude = 1.0, latitude = 91.0)
                assert.that(GeometryValidation.isPoint(coordinates), isA<ValidationResult.OutOfRangeError>())
            }
        }
    }
})
