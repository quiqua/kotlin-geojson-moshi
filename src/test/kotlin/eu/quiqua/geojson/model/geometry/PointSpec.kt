package eu.quiqua.geojson.model.geometry

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.isA
import com.natpryce.hamkrest.present
import eu.quiqua.geojson.model.Type
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

internal class PointSpec : Spek({
    describe("A Point object") {
        context("Create with valid 2D coordinates") {
            val point = Point(coordinates = Position(longitude = 1.0, latitude = 2.0))
            it("Will validate successfully") {
                assert.that(point.validate(), isA<ValidationResult.Ok>())
            }
            it("Is of Type.Point") {
                assert.that(point.type, isA<Type.Point>())
            }
            it("Has a latitude") {
                assert.that(point.latitude, present())
            }
            it("Has a longitude") {
                assert.that(point.longitude, present())
            }
            it("Has no altitude") {
                assert.that(point.altitude, absent())
            }
        }
        context("Create with valid 3D coordinates") {
            val point = Point(coordinates = Position(longitude = 1.0, latitude = 2.0, altitude = 3.0))
            it("Will validate successfully") {
                assert.that(point.validate(), isA<ValidationResult.Ok>())
            }
            it("Has a altitude") {
                assert.that(point.altitude, present())
            }
        }
        context("Create with invalid coordinates") {
            val point = Point(coordinates = Position(longitude = -1000.0, latitude = 302.0))
            it("Returns a Validation.Error.OutOfRange") {
                assert.that(point.validate(), isA<ValidationResult.Error.OutOfRange>())
            }
        }
    }
})
