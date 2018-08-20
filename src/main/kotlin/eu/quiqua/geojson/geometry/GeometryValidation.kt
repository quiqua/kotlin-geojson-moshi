package eu.quiqua.geojson.geometry

object GeometryValidation {

    private const val MINIMUM_LINEAR_RING_COORDINATES = 4

    fun isPoint(coordinates: Position): ValidationResult = coordinates.validate()

    fun isLineString(coordinates: List<Position>): ValidationResult {
        val validations = mutableListOf(
            hasAtLeastTwoCoordinates(coordinates),
            hasConsistentDimension(coordinates)
        )

        coordinates.forEach {
            validations.add(isPoint(it))
        }

        return with(validations.filterNot { it is ValidationResult.Ok }) {
            if (isEmpty()) ValidationResult.Ok() else first()
        }
    }

    fun isPolygon(coordinates: List<List<Position>>): ValidationResult {
        coordinates.forEach {
            val validation = isLinearRing(it)
            if (validation !is ValidationResult.Ok) {
                return validation
            }
        }
        return ValidationResult.Ok()
    }

    private fun hasAtLeastTwoCoordinates(coordinates: List<Position>): ValidationResult {
        return when (coordinates.count() < 2) {
            true -> ValidationResult.TooFewElements("A LineString consists of at least two coordinate pairs")
            false -> ValidationResult.Ok()
        }
    }

    private fun hasConsistentDimension(coordinates: List<Position>): ValidationResult {
        val coordinatesWithAltitude = coordinates.filter { it.hasAltitude }
        val coordinatesWithoutAltitude = coordinates.filterNot { it.hasAltitude }
        return when (coordinatesWithAltitude.isNotEmpty() and coordinatesWithoutAltitude.isNotEmpty()) {
            true -> ValidationResult.IncompatibleCoordinateDimensions("Coordinates consist of 2D and 3D geometries")
            false -> ValidationResult.Ok()
        }
    }

    private fun isLinearRing(coordinates: List<Position>): ValidationResult {
        val validation = isLineString(coordinates)
        return when (validation) {
            is ValidationResult.Ok -> with(coordinates) {
                if (count() >= MINIMUM_LINEAR_RING_COORDINATES && first() == last()) {
                    ValidationResult.Ok()
                } else {
                    ValidationResult.NoLinearRing("The coordinates do not meet the LinearRing criteria")
                }
            }
            else -> validation
        }
    }
}
