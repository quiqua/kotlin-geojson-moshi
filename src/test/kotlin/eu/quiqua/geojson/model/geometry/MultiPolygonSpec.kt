package eu.quiqua.geojson.model.geometry

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.isA
import eu.quiqua.geojson.model.Type
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

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
            it("Returns a Validation.Error.IncompatibleCoordinateDimensions") {
                assert.that(multiPolygon.validate(), isA<ValidationResult.Error.IncompatibleCoordinateDimensions>())
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
            it("Returns a Validation.Error.OutOfRange") {
                assert.that(multiPolygon.validate(), isA<ValidationResult.Error.OutOfRange>())
            }
        }
        context("Create with empty coordinates") {
            val coordinates = emptyList<List<List<Position>>>()
            val multiPolygon = MultiPolygon(coordinates = coordinates)
            it("Returns a Validation.Error.TooFewElements") {
                assert.that(multiPolygon.validate(), isA<ValidationResult.Error.TooFewElements>())
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
            it("Returns a Validation.Error.NoLinearRing") {
                assert.that(multiPolygon.validate(), isA<ValidationResult.Error.NoLinearRing>())
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
            it("Returns a Validation.Error.NoLinearRing") {
                assert.that(multiPolygon.validate(), isA<ValidationResult.Error.NoLinearRing>())
            }
        }
    }
})
