package eu.quiqua.geojson.geometry

sealed class ValidationResult {
    class Ok : ValidationResult()
    data class OutOfRange(val reason: String) : ValidationResult()
    data class TooFewElements(val reason: String) : ValidationResult()
    data class NoLinearRing(val reason: String) : ValidationResult()
    data class IncompatibleCoordinateDimensions(val reason: String) : ValidationResult()
}
