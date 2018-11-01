package eu.quiqua.geojson.moshi

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import com.squareup.moshi.Moshi
import eu.quiqua.geojson.model.Feature
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

internal class FeatureJsonAdapterSpec : Spek({
    val moshi =
        Moshi.Builder()
            .add(FeatureJsonAdapterFactory())
            .build()
    val adapter = moshi.adapter<Feature>(Feature::class.java)
    describe("From Object to JSON") {
        context("With a properties map") {
            val properties = hashMapOf("foo" to 1, "bar" to "baz")
            val feature = Feature(properties = properties)
            it("serializes all properties to JSON") {
                assert.that(adapter.toJson(feature), equalTo("Foo"))
            }
        }
    }
})
