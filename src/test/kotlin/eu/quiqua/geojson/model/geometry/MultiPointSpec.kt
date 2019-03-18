package eu.quiqua.geojson.model.geometry

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.isA
import eu.quiqua.geojson.model.Type
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

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
            it("Returns a Validation.Error.IncompatibleCoordinateDimensions") {
                assert.that(multiPoint.validate(), isA<ValidationResult.Error.IncompatibleCoordinateDimensions>())
            }
        }
        context("Create with invalid coordinate boundaries") {
            val coordinates = listOf(
                Position(longitude = -1000.0, latitude = 2.0)
            )
            val multiPoint = MultiPoint(coordinates = coordinates)
            it("Returns a Validation.Error.OutOfRange") {
                assert.that(multiPoint.validate(), isA<ValidationResult.Error.OutOfRange>())
            }
        }
        context("Create with empty coordinates") {
            val coordinates = emptyList<Position>()
            val multiPoint = MultiPoint(coordinates = coordinates)
            it("Returns a Validation.Error.TooFewElements") {
                assert.that(multiPoint.validate(), isA<ValidationResult.Error.TooFewElements>())
            }
        }
    }
})
