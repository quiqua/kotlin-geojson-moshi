package eu.quiqua.geojson.geometry

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.isA
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

internal class TypeSpec : Spek({
    describe("Create a Type object from String") {
        context("with a mixed-case String") {
            it("Creates a Type.Point object") {
                assert.that(Type.convertFromString("Point"), isA<Type.Point>())
            }
            it("Creates a Type.LineString object") {
                assert.that(Type.convertFromString("LineString"), isA<Type.LineString>())
            }
            it("Creates a Type.Polygon object") {
                assert.that(Type.convertFromString("Polygon"), isA<Type.Polygon>())
            }
            it("Creates a Type.MultiPoint object") {
                assert.that(Type.convertFromString("MultiPoint"), isA<Type.MultiPoint>())
            }
            it("Creates a Type.MultiLineString object") {
                assert.that(Type.convertFromString("MultiLineString"), isA<Type.MultiLineString>())
            }
            it("Creates a Type.MultiPolygon object") {
                assert.that(Type.convertFromString("MultiPolygon"), isA<Type.MultiPolygon>())
            }
            it("Creates a Type.GeometryCollection object") {
                assert.that(Type.convertFromString("GeometryCollection"), isA<Type.GeometryCollection>())
            }
        }
        context("with a lower case String") {
            it("Creates a Type.Point object") {
                assert.that(Type.convertFromString("point"), isA<Type.Point>())
            }
        }
        context("with a upper case String") {
            it("Creates a Type.Point object") {
                assert.that(Type.convertFromString("POINT"), isA<Type.Point>())
            }
        }
        context("with an unmapped String") {
            it("Creates a Type.Unknown object") {
                assert.that(Type.convertFromString("does-not-exist"), isA<Type.Unknown>())
            }
        }
    }
})
