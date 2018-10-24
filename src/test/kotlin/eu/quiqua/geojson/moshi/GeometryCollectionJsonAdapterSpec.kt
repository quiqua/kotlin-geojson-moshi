package eu.quiqua.geojson.moshi

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.isA
import com.squareup.moshi.Moshi
import eu.quiqua.geojson.model.geometry.GeometryCollection
import eu.quiqua.geojson.model.geometry.Type
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

internal class GeometryCollectionJsonAdapterSpec : Spek({
    val moshi = Moshi.Builder().add(PointJsonAdapter()).add(GeometryCollectionJsonAdapter()).build()
    val adapter = moshi.adapter<GeometryCollection>(GeometryCollection::class.java)
    describe("From JSON to Object") {
        context("Read a valid MultiPolygon object String") {
            context("With one polygon coordinate subarray") {
                val jsonString =
                    "{\"geometries\": [{\"coordinates\": [1.0, 1.0], \"type\": \"point\"}], \"type\": \"geometrycollection\"}"
                it("converts to a MultiPolygon object") {
                    val geometryCollection = adapter.fromJson(jsonString)!!
                    assert.that(geometryCollection.type, isA<Type.GeometryCollection>())
                }
            }
        }
    }
})
