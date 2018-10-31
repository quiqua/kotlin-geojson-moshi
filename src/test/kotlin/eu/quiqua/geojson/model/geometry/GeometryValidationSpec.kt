package eu.quiqua.geojson.model.geometry

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.isA
import io.mockk.mockkObject
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
                assert.that(GeometryValidation.isPoint(coordinates), isA<ValidationResult.Error.OutOfRange>())
            }
            it("Returns ValidationResult.OutOfRangeError when latitude is out of range") {
                val coordinates = Position(longitude = 1.0, latitude = 91.0)
                assert.that(GeometryValidation.isPoint(coordinates), isA<ValidationResult.Error.OutOfRange>())
            }
        }
    }
    describe("Validate MultiPoint coordinates") {
        context("With an empty array") {
            it("Returns ValidationResult.TooFewElements") {
                assert.that(GeometryValidation.isMultiPoint(emptyList()), isA<ValidationResult.Error.TooFewElements>())
            }
        }
        context("With at least one coordinate pair") {
            val coordinates = listOf(
                Position(longitude = 1.0, latitude = 1.0),
                Position(longitude = 2.0, latitude = 2.0),
                Position(longitude = 3.0, latitude = 3.0)
            )
            it("Calls .isPoint for each coordinate pair") {
                mockkObject(GeometryValidation) {
                    GeometryValidation.isMultiPoint(coordinates)
                    verify(exactly = 3) { GeometryValidation.isPoint(any()) }
                }
            }
            it("Returns ValidationResult.Ok") {
                assert.that(GeometryValidation.isMultiPoint(coordinates), isA<ValidationResult.Ok>())
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
                mockkObject(GeometryValidation) {
                    GeometryValidation.isLineString(coordinates)
                    verify(exactly = 3) { GeometryValidation.isPoint(any()) }
                }
            }
        }
        context("With empty coordinates input") {
            it("Returns ValidationResult.TooFewElements") {
                val coordinates = emptyList<Position>()
                assert.that(GeometryValidation.isLineString(coordinates), isA<ValidationResult.Error.TooFewElements>())
            }
        }
        context("With one coordinate input") {
            it("Returns ValidationResult.TooFewElements") {
                val coordinates = listOf(Position(longitude = 2.0, latitude = 2.0))
                assert.that(GeometryValidation.isLineString(coordinates), isA<ValidationResult.Error.TooFewElements>())
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
                    isA<ValidationResult.Error.IncompatibleCoordinateDimensions>()
                )
            }
        }
    }
    describe("Validate MultiLineString coordinates") {
        context("With an empty array") {
            it("Returns ValidationResult.TooFewElements") {
                assert.that(
                    GeometryValidation.isMultiLineString(emptyList()),
                    isA<ValidationResult.Error.TooFewElements>()
                )
            }
        }
        context("With at least one linestring coordinate array") {
            val lineString = listOf(
                Position(longitude = 1.0, latitude = 1.0),
                Position(longitude = 2.0, latitude = 2.0),
                Position(longitude = 3.0, latitude = 3.0)
            )
            val coordinates = listOf(lineString, lineString)
            it("Calls .isLineString for each coordinate array") {
                mockkObject(GeometryValidation) {
                    GeometryValidation.isMultiLineString(coordinates)
                    verify(exactly = 2) { GeometryValidation.isLineString(any()) }
                }
            }
            it("Returns ValidationResult.Ok") {
                assert.that(GeometryValidation.isMultiLineString(coordinates), isA<ValidationResult.Ok>())
            }
        }
    }
    describe("Validate Polygon coordinates") {
        context("With a linear ring") {
            it("Returns ValidationResult.Ok for 2D coordinates") {
                val coordinates = listOf(
                    listOf(
                        Position(longitude = 1.0, latitude = 1.0),
                        Position(longitude = 2.0, latitude = 2.0),
                        Position(longitude = 1.0, latitude = 3.0),
                        Position(longitude = 1.0, latitude = 1.0)
                    )
                )
                assert.that(GeometryValidation.isPolygon(coordinates), isA<ValidationResult.Ok>())
            }
            it("Returns ValidationResult.Ok for 3D coordinates") {
                val coordinates = listOf(
                    listOf(
                        Position(longitude = 1.0, latitude = 1.0, altitude = 1.0),
                        Position(longitude = 2.0, latitude = 2.0, altitude = 1.0),
                        Position(longitude = 1.0, latitude = 3.0, altitude = 1.0),
                        Position(longitude = 1.0, latitude = 1.0, altitude = 1.0)
                    )
                )
                assert.that(GeometryValidation.isPolygon(coordinates), isA<ValidationResult.Ok>())
            }
            it("Calls .isLineString for each list of coordinates") {
                val coordinates = listOf(
                    listOf(
                        Position(longitude = 1.0, latitude = 1.0, altitude = 1.0),
                        Position(longitude = 2.0, latitude = 2.0, altitude = 1.0),
                        Position(longitude = 1.0, latitude = 3.0, altitude = 1.0),
                        Position(longitude = 1.0, latitude = 1.0, altitude = 1.0)
                    )
                )
                mockkObject(GeometryValidation) {
                    GeometryValidation.isPolygon(coordinates)
                    verify(exactly = 1) { GeometryValidation.isLineString(any()) }
                }
            }
        }
        context("With empty coordinates input") {
            it("Returns ValidationResult.TooFewElements") {
                val coordinates = emptyList<List<Position>>()
                assert.that(GeometryValidation.isPolygon(coordinates), isA<ValidationResult.Error.TooFewElements>())
            }
        }
        context("With too few coordinates") {
            it("Returns ValidationResult.NoLinearRing") {
                val coordinates = listOf(
                    listOf(
                        Position(longitude = 1.0, latitude = 1.0),
                        Position(longitude = 2.0, latitude = 2.0),
                        Position(longitude = 1.0, latitude = 1.0)
                    )
                )
                assert.that(GeometryValidation.isPolygon(coordinates), isA<ValidationResult.Error.NoLinearRing>())
            }
        }
        context("With different coordinates for start- and endpoint") {
            it("Returns ValidationResult.NoLinearRing") {
                val coordinates = listOf(
                    listOf(
                        Position(longitude = 1.0, latitude = 1.0),
                        Position(longitude = 2.0, latitude = 2.0),
                        Position(longitude = 3.0, latitude = 3.0),
                        Position(longitude = 4.0, latitude = 4.0)
                    )
                )
                assert.that(GeometryValidation.isPolygon(coordinates), isA<ValidationResult.Error.NoLinearRing>())
            }
        }
        context("With different coordinate dimensions") {
            it("Returns ValidationResult.IncompatibleCoordinateDimensions") {
                val coordinates = listOf(
                    listOf(
                        Position(longitude = 1.0, latitude = 1.0, altitude = 1.0),
                        Position(longitude = 2.0, latitude = 2.0),
                        Position(longitude = 3.0, latitude = 3.0),
                        Position(longitude = 4.0, latitude = 4.0)
                    )
                )
                assert.that(
                    GeometryValidation.isPolygon(coordinates),
                    isA<ValidationResult.Error.IncompatibleCoordinateDimensions>()
                )
            }
        }
    }
    describe("Validate MultiPolygon coordinates") {
        context("With an empty array") {
            it("Returns ValidationResult.TooFewElements") {
                assert.that(
                    GeometryValidation.isMultiPolygon(emptyList()),
                    isA<ValidationResult.Error.TooFewElements>()
                )
            }
        }
        context("With at least one polygon coordinate array") {
            val polygon = listOf(
                listOf(
                    Position(longitude = 1.0, latitude = 1.0, altitude = 1.0),
                    Position(longitude = 2.0, latitude = 2.0, altitude = 1.0),
                    Position(longitude = 1.0, latitude = 3.0, altitude = 1.0),
                    Position(longitude = 1.0, latitude = 1.0, altitude = 1.0)
                )
            )
            val coordinates = listOf(polygon, polygon)
            it("Calls .isPolygon for each coordinate array") {
                mockkObject(GeometryValidation) {
                    GeometryValidation.isMultiPolygon(coordinates)
                    verify(exactly = 2) { GeometryValidation.isPolygon(any()) }
                }
            }
            it("Returns ValidationResult.Ok") {
                assert.that(GeometryValidation.isMultiPolygon(coordinates), isA<ValidationResult.Ok>())
            }
        }
    }
})
