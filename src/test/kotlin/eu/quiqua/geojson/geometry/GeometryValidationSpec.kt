package eu.quiqua.geojson.geometry

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.isA
import io.mockk.spyk
import io.mockk.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

internal class GeometryValidationSpec : Spek({
    describe("Validate Point coordinates") {
        context("With valid input") {
            it("Returns ValidationResult.Ok for 2D coordinates") {
                val coordinates = Position(longitude = 1.0, latitude = 1.0)
                assert.that(GeometryValidation.isPoint(coordinates), isA<ValidationResult.Ok>())
            }
            it("Returns ValidationResult.Ok for 3D coordinates") {
                val coordinates = Position(longitude = 1.0, latitude = 1.0, altitude = 2.0)
                assert.that(GeometryValidation.isPoint(coordinates), isA<ValidationResult.Ok>())
            }
        }
        context("With invalid input") {
            it("Returns ValidationResult.OutOfRangeError when longitude is out of range") {
                val coordinates = Position(longitude = 181.0, latitude = 1.0)
                assert.that(GeometryValidation.isPoint(coordinates), isA<ValidationResult.OutOfRangeError>())
            }
            it("Returns ValidationResult.OutOfRangeError when latitude is out of range") {
                val coordinates = Position(longitude = 1.0, latitude = 91.0)
                assert.that(GeometryValidation.isPoint(coordinates), isA<ValidationResult.OutOfRangeError>())
            }
        }
    }
    describe("Validate LineString coordinates") {
        context("With at least two positions") {
            it("Returns ValidationResult.Ok for 2D coordinates") {
                val coordinates = listOf(
                    Position(longitude = 1.0, latitude = 1.0),
                    Position(longitude = 2.0, latitude = 2.0)
                )
                assert.that(GeometryValidation.isLineString(coordinates), isA<ValidationResult.Ok>())
            }
            it("Returns ValidationResult.Ok for 3D coordinates") {
                val coordinates = listOf(
                    Position(longitude = 1.0, latitude = 1.0, altitude = 1.0),
                    Position(longitude = 2.0, latitude = 2.0, altitude = 2.0)
                )
                assert.that(GeometryValidation.isLineString(coordinates), isA<ValidationResult.Ok>())
            }
            it("Calls .isPoint for each coordinate in the linestring") {
                val coordinates = listOf(
                    Position(longitude = 1.0, latitude = 1.0),
                    Position(longitude = 2.0, latitude = 2.0),
                    Position(longitude = 3.0, latitude = 3.0)
                )
                val mock = spyk<GeometryValidation>()
                mock.isLineString(coordinates)
                verify(exactly = 3) { mock.isPoint(any()) }
            }
        }
        context("With empty coordinates input") {
            it("Returns ValidationResult.TooFewElements") {
                val coordinates = emptyList<Position>()
                assert.that(GeometryValidation.isLineString(coordinates), isA<ValidationResult.TooFewElements>())
            }
        }
        context("With one coordinate input") {
            it("Returns ValidationResult.TooFewElements") {
                val coordinates = listOf(Position(longitude = 2.0, latitude = 2.0))
                assert.that(GeometryValidation.isLineString(coordinates), isA<ValidationResult.TooFewElements>())
            }
        }
        context("With different coordinate dimensions") {
            it("Returns ValidationResult.IncompatibleCoordinateDimensions") {
                val coordinates = listOf(
                    Position(longitude = 2.0, latitude = 2.0),
                    Position(longitude = 1.0, latitude = 1.0, altitude = 3.0)
                )
                assert.that(
                    GeometryValidation.isLineString(coordinates),
                    isA<ValidationResult.IncompatibleCoordinateDimensions>()
                )
            }
        }
    }
    describe("Validate Polygon coordinates") {

    }
})
