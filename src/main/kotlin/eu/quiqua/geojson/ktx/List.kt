package eu.quiqua.geojson.ktx

import eu.quiqua.geojson.geometry.ValidationResult

internal fun List<ValidationResult>.getFirstErrorOrOk(): ValidationResult {
    return with(filterNot { it is ValidationResult.Ok }) {
        if (isEmpty()) ValidationResult.Ok() else first()
    }
}
