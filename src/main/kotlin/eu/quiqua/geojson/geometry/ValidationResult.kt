package eu.quiqua.geojson.geometry

sealed class ValidationResult(open val reason: String?) {
    class Ok(reason: String? = null) : ValidationResult(reason)
    data class OutOfRange(override val reason: String) : ValidationResult(reason)
    data class TooFewElements(override val reason: String) : ValidationResult(reason)
    data class NoLinearRing(override val reason: String) : ValidationResult(reason)
    data class IncompatibleCoordinateDimensions(override val reason: String) : ValidationResult(reason)
}
