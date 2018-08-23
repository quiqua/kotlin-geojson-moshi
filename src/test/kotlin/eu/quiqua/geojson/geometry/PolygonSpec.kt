package eu.quiqua.geojson.geometry

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.isA
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

internal class PolygonSpec : Spek({
    describe("A Polygon object") {
        context("Create with valid 2D coordinates") {
            val coordinates = listOf(
                listOf(
                    Position(longitude = 1.0, latitude = 2.0),
                    Position(longitude = 1.0, latitude = 3.0),
                    Position(longitude = 2.0, latitude = 3.0),
                    Position(longitude = 2.0, latitude = 2.0),
                    Position(longitude = 1.0, latitude = 2.0)
                )
            )
            val polygon = Polygon(coordinates = coordinates)
            it("Will validate successfully") {
                assert.that(polygon.validate(), isA<ValidationResult.Ok>())
            }
            it("Is of Type.Polygon") {
                assert.that(polygon.type, isA<Type.Polygon>())
            }
        }
        context("Create with valid 3D coordinates") {
            val coordinates = listOf(
                listOf(
                    Position(longitude = 1.0, latitude = 2.0),
                    Position(longitude = 1.0, latitude = 3.0),
                    Position(longitude = 2.0, latitude = 3.0),
                    Position(longitude = 2.0, latitude = 2.0),
                    Position(longitude = 1.0, latitude = 2.0)
                )
            )
            val polygon = Polygon(coordinates = coordinates)
            it("Will validate successfully") {
                assert.that(polygon.validate(), isA<ValidationResult.Ok>())
            }
        }
        context("Create with different coordinate dimensions") {
            val coordinates = listOf(
                listOf(
                    Position(longitude = 1.0, latitude = 2.0),
                    Position(longitude = 1.0, latitude = 3.0, altitude = 1.2),
                    Position(longitude = 2.0, latitude = 3.0),
                    Position(longitude = 2.0, latitude = 2.0),
                    Position(longitude = 1.0, latitude = 2.0)
                )
            )
            val polygon = Polygon(coordinates = coordinates)
            it("Returns a Validation.IncompatibleCoordinateDimensions") {
                assert.that(polygon.validate(), isA<ValidationResult.IncompatibleCoordinateDimensions>())
            }
        }
        context("Create with invalid coordinate boundaries ") {
            val coordinates = listOf(
                listOf(
                    Position(longitude = 1.0, latitude = 2.0),
                    Position(longitude = 100.0, latitude = 93.0),
                    Position(longitude = 2.0, latitude = 3.0),
                    Position(longitude = 2.0, latitude = 2.0),
                    Position(longitude = 1.0, latitude = 2.0)
                )
            )
            val polygon = Polygon(coordinates = coordinates)
            it("Returns a Validation.OutOfRangeError") {
                assert.that(polygon.validate(), isA<ValidationResult.OutOfRange>())
            }
        }
        context("Coordinates are not closed") {
            val coordinates = listOf(
                listOf(
                    Position(longitude = 1.0, latitude = 2.0),
                    Position(longitude = 1.0, latitude = 3.0),
                    Position(longitude = 2.0, latitude = 3.0),
                    Position(longitude = 2.0, latitude = 2.0)
                )
            )
            val polygon = Polygon(coordinates = coordinates)
            it("Returns a Validation.NoLinearRing") {
                assert.that(polygon.validate(), isA<ValidationResult.NoLinearRing>())
            }
        }
        context("Coordinates only represent a closed line") {
            val coordinates = listOf(
                listOf(
                    Position(longitude = 1.0, latitude = 2.0),
                    Position(longitude = 1.0, latitude = 3.0),
                    Position(longitude = 1.0, latitude = 2.0)
                )
            )
            val polygon = Polygon(coordinates = coordinates)
            it("Returns a Validation.NoLinearRing") {
                assert.that(polygon.validate(), isA<ValidationResult.NoLinearRing>())
            }
        }
    }
})
