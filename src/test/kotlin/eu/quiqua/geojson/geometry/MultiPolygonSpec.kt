package eu.quiqua.geojson.geometry

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.isA
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

internal class MultiPolygonSpec : Spek({
    describe("A MultiPolygon object") {
        context("Create with valid 2D coordinates") {
            val coordinates = listOf(
                listOf(
                    listOf(
                        Position(longitude = 1.0, latitude = 2.0),
                        Position(longitude = 1.0, latitude = 3.0),
                        Position(longitude = 2.0, latitude = 3.0),
                        Position(longitude = 2.0, latitude = 2.0),
                        Position(longitude = 1.0, latitude = 2.0)
                    )
                ),
                listOf(
                    listOf(
                        Position(longitude = 5.0, latitude = 2.0),
                        Position(longitude = 5.0, latitude = 3.0),
                        Position(longitude = 6.0, latitude = 3.0),
                        Position(longitude = 6.0, latitude = 2.0),
                        Position(longitude = 5.0, latitude = 2.0)
                    )
                )
            )
            val multiPolygon = MultiPolygon(coordinates = coordinates)
            it("Will validate successfully") {
                assert.that(multiPolygon.validate(), isA<ValidationResult.Ok>())
            }
            it("Is of Type.MultiPolygon") {
                assert.that(multiPolygon.type, isA<Type.MultiPolygon>())
            }
        }
        context("Create with valid 3D coordinates") {
            val coordinates = listOf(
                listOf(
                    listOf(
                        Position(longitude = 1.0, latitude = 2.0),
                        Position(longitude = 1.0, latitude = 3.0),
                        Position(longitude = 2.0, latitude = 3.0),
                        Position(longitude = 2.0, latitude = 2.0),
                        Position(longitude = 1.0, latitude = 2.0)
                    )
                ),
                listOf(
                    listOf(
                        Position(longitude = 5.0, latitude = 2.0),
                        Position(longitude = 5.0, latitude = 3.0),
                        Position(longitude = 6.0, latitude = 3.0),
                        Position(longitude = 6.0, latitude = 2.0),
                        Position(longitude = 5.0, latitude = 2.0)
                    )
                )
            )
            val multiPolygon = MultiPolygon(coordinates = coordinates)
            it("Will validate successfully") {
                assert.that(multiPolygon.validate(), isA<ValidationResult.Ok>())
            }
        }
        context("Create with mixed coordinate dimensions") {
            val coordinates = listOf(
                listOf(
                    listOf(
                        Position(longitude = 1.0, latitude = 2.0, altitude = 1.0),
                        Position(longitude = 1.0, latitude = 3.0),
                        Position(longitude = 2.0, latitude = 3.0),
                        Position(longitude = 2.0, latitude = 2.0),
                        Position(longitude = 1.0, latitude = 2.0)
                    )
                )
            )
            val multiPolygon = MultiPolygon(coordinates = coordinates)
            it("Returns a Validation.IncompatibleCoordinateDimensions") {
                assert.that(multiPolygon.validate(), isA<ValidationResult.IncompatibleCoordinateDimensions>())
            }
        }
        context("Create with invalid coordinate boundaries") {
            val coordinates = listOf(
                listOf(
                    listOf(
                        Position(longitude = 200.0, latitude = 2.0),
                        Position(longitude = 1.0, latitude = 3.0),
                        Position(longitude = 2.0, latitude = 3.0),
                        Position(longitude = 2.0, latitude = 2.0),
                        Position(longitude = 1.0, latitude = 2.0)
                    )
                )
            )
            val multiPolygon = MultiPolygon(coordinates = coordinates)
            it("Returns a Validation.OutOfRangeError") {
                assert.that(multiPolygon.validate(), isA<ValidationResult.OutOfRange>())
            }
        }
        context("Create with empty coordinates") {
            val coordinates = emptyList<List<List<Position>>>()
            val multiPolygon = MultiPolygon(coordinates = coordinates)
            it("Returns a Validation.TooFewElements") {
                assert.that(multiPolygon.validate(), isA<ValidationResult.TooFewElements>())
            }
        }
        context("Coordinates are not closed") {
            val coordinates = listOf(
                listOf(
                    listOf(
                        Position(longitude = 1.0, latitude = 2.0),
                        Position(longitude = 1.0, latitude = 3.0),
                        Position(longitude = 2.0, latitude = 3.0),
                        Position(longitude = 2.0, latitude = 2.0)
                    )
                )
            )
            val multiPolygon = MultiPolygon(coordinates = coordinates)
            it("Returns a Validation.NoLinearRing") {
                assert.that(multiPolygon.validate(), isA<ValidationResult.NoLinearRing>())
            }
        }
        context("Coordinates only represent a closed line") {
            val coordinates = listOf(
                listOf(
                    listOf(
                        Position(longitude = 1.0, latitude = 2.0),
                        Position(longitude = 1.0, latitude = 3.0),
                        Position(longitude = 1.0, latitude = 2.0)
                    )
                )
            )
            val multiPolygon = MultiPolygon(coordinates = coordinates)
            it("Returns a Validation.NoLinearRing") {
                assert.that(multiPolygon.validate(), isA<ValidationResult.NoLinearRing>())
            }
        }
    }
})
