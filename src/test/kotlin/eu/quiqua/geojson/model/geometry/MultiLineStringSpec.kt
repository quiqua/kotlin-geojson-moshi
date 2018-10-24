package eu.quiqua.geojson.model.geometry

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.isA
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

internal class MultiLineStringSpec : Spek({
    describe("A MultiLineString object") {
        context("Create with valid 2D coordinates") {
            val coordinates = listOf(
                listOf(Position(longitude = 1.0, latitude = 2.0), Position(longitude = 2.0, latitude = 3.0)),
                listOf(Position(longitude = 4.0, latitude = 2.0), Position(longitude = 5.0, latitude = 3.0))
            )
            val multiLineString = MultiLineString(coordinates = coordinates)
            it("Will validate successfully") {
                assert.that(multiLineString.validate(), isA<ValidationResult.Ok>())
            }
            it("Is of Type.MultiLineString") {
                assert.that(multiLineString.type, isA<Type.MultiLineString>())
            }
        }
        context("Create with valid 3D coordinates") {
            val coordinates = listOf(
                listOf(
                    Position(longitude = 1.0, latitude = 2.0, altitude = 1.0),
                    Position(longitude = 2.0, latitude = 3.0, altitude = 1.0)
                ),
                listOf(
                    Position(longitude = 4.0, latitude = 2.0, altitude = 1.0),
                    Position(longitude = 5.0, latitude = 3.0, altitude = 1.0)
                )
            )
            val multiLineString = MultiLineString(coordinates = coordinates)
            it("Will validate successfully") {
                assert.that(multiLineString.validate(), isA<ValidationResult.Ok>())
            }
        }
        context("Create with mixed coordinate dimensions") {
            val coordinates = listOf(
                listOf(
                    Position(longitude = 1.0, latitude = 2.0, altitude = 1.0),
                    Position(longitude = 2.0, latitude = 3.0)
                )
            )
            val multiLineString = MultiLineString(coordinates = coordinates)
            it("Returns a Validation.IncompatibleCoordinateDimensions") {
                assert.that(multiLineString.validate(), isA<ValidationResult.IncompatibleCoordinateDimensions>())
            }
        }
        context("Create with invalid coordinate boundaries") {
            val coordinates = listOf(
                listOf(Position(longitude = 1000.0, latitude = 2.0), Position(longitude = 2.0, latitude = 3.0)),
                listOf(Position(longitude = 4.0, latitude = 2.0), Position(longitude = 5.0, latitude = 3.0))
            )
            val multiLineString = MultiLineString(coordinates = coordinates)
            it("Returns a Validation.OutOfRangeError") {
                assert.that(multiLineString.validate(), isA<ValidationResult.OutOfRange>())
            }
        }
        context("Create with empty coordinates") {
            val coordinates = emptyList<List<Position>>()
            val multiLineString = MultiLineString(coordinates = coordinates)
            it("Returns a Validation.TooFewElements") {
                assert.that(multiLineString.validate(), isA<ValidationResult.TooFewElements>())
            }
        }
    }
})
