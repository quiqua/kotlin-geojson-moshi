package eu.quiqua.geojson.model.geometry

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.isA
import eu.quiqua.geojson.model.Type
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

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
            it("Returns a Validation.Error.IncompatibleCoordinateDimensions") {
                assert.that(polygon.validate(), isA<ValidationResult.Error.IncompatibleCoordinateDimensions>())
            }
        }
        context("Create with invalid coordinate boundaries") {
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
            it("Returns a Validation.Error.OutOfRange") {
                assert.that(polygon.validate(), isA<ValidationResult.Error.OutOfRange>())
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
            it("Returns a Validation.Error.NoLinearRing") {
                assert.that(polygon.validate(), isA<ValidationResult.Error.NoLinearRing>())
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
            it("Returns a Validation.Error.NoLinearRing") {
                assert.that(polygon.validate(), isA<ValidationResult.Error.NoLinearRing>())
            }
        }
    }
})
