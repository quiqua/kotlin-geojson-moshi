package eu.quiqua.geojson.model

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isA
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

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
            it("Creates a Type.Feature object") {
                assert.that(Type.convertFromString("Feature"), isA<Type.Feature>())
            }
            it("Creates a Type.FeatureCollection object") {
                assert.that(Type.convertFromString("FeatureCollection"), isA<Type.FeatureCollection>())
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
    describe("Create a String from a Type object") {
        it("Creates a Point string") {
            assert.that(Type.convertToString(Type.Point), equalTo("Point"))
        }
        it("Creates a LineString string") {
            assert.that(Type.convertToString(Type.LineString), equalTo("LineString"))
        }
        it("Creates a Polygon string") {
            assert.that(Type.convertToString(Type.Polygon), equalTo("Polygon"))
        }
        it("Creates a MultiPoint string") {
            assert.that(Type.convertToString(Type.MultiPoint), equalTo("MultiPoint"))
        }
        it("Creates a MultiLineString string") {
            assert.that(Type.convertToString(Type.MultiLineString), equalTo("MultiLineString"))
        }
        it("Creates a MultiPolygon string") {
            assert.that(Type.convertToString(Type.MultiPolygon), equalTo("MultiPolygon"))
        }
        it("Creates a GeometryCollection string") {
            assert.that(Type.convertToString(Type.GeometryCollection), equalTo("GeometryCollection"))
        }
        it("Creates a Feature string") {
            assert.that(Type.convertToString(Type.Feature), equalTo("Feature"))
        }
        it("Creates a FeatureCollection string") {
            assert.that(Type.convertToString(Type.FeatureCollection), equalTo("FeatureCollection"))
        }
    }
})
