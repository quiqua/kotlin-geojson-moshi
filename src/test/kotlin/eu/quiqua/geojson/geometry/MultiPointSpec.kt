package eu.quiqua.geojson.geometry

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.isA
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

internal class MultiPointSpec : Spek({
    describe("A MultiPoint object") {
        context("Create with valid 2D coordinates") {
            val coordinates = listOf(
                Position(longitude = 1.0, latitude = 2.0),
                Position(longitude = 2.0, latitude = 3.0)
            )
            val multiPoint = MultiPoint(coordinates = coordinates)
            it("Will validate successfully") {
                assert.that(multiPoint.validate(), isA<ValidationResult.Ok>())
            }
            it("Is of Type.MultiPoint") {
                assert.that(multiPoint.type, isA<Type.MultiPoint>())
            }
        }
        context("Create with valid 3D coordinates") {
            val coordinates = listOf(
                Position(longitude = 1.0, latitude = 2.0, altitude = 1.0),
                Position(longitude = 2.0, latitude = 3.0, altitude = 1.5)
            )
            val multiPoint = MultiPoint(coordinates = coordinates)
            it("Will validate successfully") {
                assert.that(multiPoint.validate(), isA<ValidationResult.Ok>())
            }
        }
        context("Create with mixed coordinate dimensions") {
            val coordinates = listOf(
                Position(longitude = 1.0, latitude = 2.0),
                Position(longitude = 2.0, latitude = 3.0, altitude = 1.5)
            )
            val multiPoint = MultiPoint(coordinates = coordinates)
            it("Returns a Validation.IncompatibleCoordinateDimensions") {
                assert.that(multiPoint.validate(), isA<ValidationResult.IncompatibleCoordinateDimensions>())
            }
        }
        context("Create with invalid coordinate boundaries") {
            val coordinates = listOf(
                Position(longitude = -1000.0, latitude = 2.0)
            )
            val multiPoint = MultiPoint(coordinates = coordinates)
            it("Returns a Validation.OutOfRangeError") {
                assert.that(multiPoint.validate(), isA<ValidationResult.OutOfRange>())
            }
        }
        context("Create with empty coordinates") {
            val coordinates = emptyList<Position>()
            val multiPoint = MultiPoint(coordinates = coordinates)
            it("Returns a Validation.TooFewElements") {
                assert.that(multiPoint.validate(), isA<ValidationResult.TooFewElements>())
            }
        }
    }
})
