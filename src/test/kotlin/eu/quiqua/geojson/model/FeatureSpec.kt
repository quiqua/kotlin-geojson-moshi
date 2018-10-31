package eu.quiqua.geojson.model

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.isA
import com.natpryce.hamkrest.present
import eu.quiqua.geojson.model.geometry.Point
import eu.quiqua.geojson.model.geometry.Position
import eu.quiqua.geojson.model.geometry.ValidationResult
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

internal class FeatureSpec : Spek({
    describe("Create a Feature Object") {
        context("Without any optional attributes") {
            val feature = Feature(geometry = null, properties = null)
            it("validates successfully") {
                assert.that(feature.validate(), isA<ValidationResult.Ok>())
            }
            it("has no geometry object") {
                assert.that(feature.geometry, absent())
            }
            it("has no properties object") {
                assert.that(feature.properties, absent())
            }
        }
        context("With additional properties") {
            val properties = hashMapOf("foo" to "bar", "spam" to "egg")
            val feature = Feature(properties = properties)
            it("has a properties object") {
                assert.that(feature.properties, present())
            }
            it("allows access to all defined properties") {
                assert.that(feature.properties!!["foo"], present())
                assert.that(feature.properties!!["spam"], present())
            }
        }
        context("With a geometry object") {
            val geometry = Point(Position(latitude = 1.0, longitude = 1.0))
            val feature = Feature(geometry = geometry)
            it("has a geometry object") {
                assert.that(feature.geometry, present())
            }
            it("validates successfully") {
                assert.that(feature.validate(), isA<ValidationResult.Ok>())
            }
        }
    }
})
