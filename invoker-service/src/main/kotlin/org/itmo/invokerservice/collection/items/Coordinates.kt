package org.itmo.invokerservice.collection.items

import reactor.core.publisher.Mono

/**
 * A class for representing flat coordinates as a pair of values: x and y.
 * Provides functionality for obtaining and setting these coordinates.
 */
class Coordinates(private var x: Long? = null, private var y: Float? = null) {
    /**
     * Возвращает значение координаты X.
     *
     * @return X coordinate or null.
     */
    fun getX(): Mono<Long>? = x?.let { Mono.just(it) }

    /**
     * Возвращает значение координаты Y.
     *
     * @return Y coordinate or null.
     */
    fun getY(): Mono<Float>? = y?.let { Mono.just(it) }

    /**
     * Sets the value of the X coordinate and returns the current object for chaining methods.
     *
     * @param x is the new value of the X coordinate.
     * @return The current Coordinates object.
     */
    fun setX(x: Long) = apply { this.x = x }

    /**
     * Sets the value of the Y coordinate and returns the current object for chaining methods.
     *
     * @param y is the new value of the Y coordinate.
     * @return The current Coordinates object.
     */
    fun setY(y: Float) = apply { this.y = y }

    /**
     * Returns a string representation of the object in the format "Coordinates(x=x, y=y)".
     *
     * @return String representation of the object.
     */
    override fun toString(): String {
        return "Coordinates(x=$x, y=$y)"
    }
}