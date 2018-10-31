package eu.quiqua.geojson.model.geometry

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.isA
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

internal class GeometryCollectionSpec : Spek({
    describe("A GeometryCollection object") {
        context("Create with an empty geometry array") {
            val geometryCollection = GeometryCollection(emptyList())
            it("Will validate successfully") {
                assert.that(geometryCollection.validate(), isA<ValidationResult.Ok>())
            }
            it("Is of Type.GeometryCollection") {
                assert.that(geometryCollection.type, isA<Type.GeometryCollection>())
            }
        }
        context("Create with a point geometry array") {
            val geometries = listOf<Geometry>(
                Point(coordinates = Position(longitude = 1.0, latitude = 1.0))
            )
            val geometryCollection = GeometryCollection(geometries)
            it("Will validate successfully") {
                assert.that(geometryCollection.validate(), isA<ValidationResult.Ok>())
            }
        }
        context("Create with a multipoint geometry array") {
            val geometries = listOf<Geometry>(
                MultiPoint(
                    coordinates = listOf(
                        Position(longitude = 1.0, latitude = 2.0),
                        Position(longitude = 2.0, latitude = 3.0)
                    )
                )
            )
            val geometryCollection = GeometryCollection(geometries)
            it("Will validate successfully") {
                assert.that(geometryCollection.validate(), isA<ValidationResult.Ok>())
            }
        }
        context("Create with a linestring geometry array") {
            val geometries = listOf<Geometry>(
                LineString(
                    coordinates = listOf(
                        Position(longitude = 1.0, latitude = 2.0),
                        Position(longitude = 2.0, latitude = 3.0)
                    )
                )
            )
            val geometryCollection = GeometryCollection(geometries)
            it("Will validate successfully") {
                assert.that(geometryCollection.validate(), isA<ValidationResult.Ok>())
            }
        }
        context("Create with a multilinestring geometry array") {
            val geometries = listOf<Geometry>(
                MultiLineString(
                    coordinates = listOf(
                        listOf(Position(longitude = 1.0, latitude = 2.0), Position(longitude = 2.0, latitude = 3.0)),
                        listOf(Position(longitude = 4.0, latitude = 2.0), Position(longitude = 5.0, latitude = 3.0))
                    )
                )
            )
            val geometryCollection = GeometryCollection(geometries)
            it("Will validate successfully") {
                assert.that(geometryCollection.validate(), isA<ValidationResult.Ok>())
            }
        }
        context("Create with a polygon geometry array") {
            val geometries = listOf<Geometry>(
                Polygon(
                    coordinates = listOf(
                        listOf(
                            Position(longitude = 1.0, latitude = 2.0),
                            Position(longitude = 1.0, latitude = 3.0),
                            Position(longitude = 2.0, latitude = 3.0),
                            Position(longitude = 2.0, latitude = 2.0),
                            Position(longitude = 1.0, latitude = 2.0)
                        )
                    )
                )
            )
            val geometryCollection = GeometryCollection(geometries)
            it("Will validate successfully") {
                assert.that(geometryCollection.validate(), isA<ValidationResult.Ok>())
            }
        }
        context("Create with a multipolygon geometry array") {
            val geometries = listOf<Geometry>(
                MultiPolygon(
                    coordinates = listOf(
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
                )
            )
            val geometryCollection = GeometryCollection(geometries)
            it("Will validate successfully") {
                assert.that(geometryCollection.validate(), isA<ValidationResult.Ok>())
            }
        }
        context("Create with a mixed geometry array") {
            val geometries = listOf(
                Point(coordinates = Position(longitude = 1.0, latitude = 1.0)),
                LineString(
                    coordinates = listOf(
                        Position(longitude = 1.0, latitude = 2.0),
                        Position(longitude = 2.0, latitude = 3.0)
                    )
                ),
                Polygon(
                    coordinates = listOf(
                        listOf(
                            Position(longitude = 1.0, latitude = 2.0),
                            Position(longitude = 1.0, latitude = 3.0),
                            Position(longitude = 2.0, latitude = 3.0),
                            Position(longitude = 2.0, latitude = 2.0),
                            Position(longitude = 1.0, latitude = 2.0)
                        )
                    )
                )
            )
            val geometryCollection = GeometryCollection(geometries)
            it("Will validate successfully") {
                assert.that(geometryCollection.validate(), isA<ValidationResult.Ok>())
            }
        }
    }
})
