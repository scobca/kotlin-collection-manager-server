package org.itmo.collectionservice.collection.items

enum class Furnish {
    DESIGNER,
    FINE,
    LITTLE,
    NONE;

    companion object {
        fun validate(type: String): Furnish {
            return try {
                Furnish.valueOf(type.uppercase())
            } catch (e: IllegalArgumentException) {
                NONE
            }
        }
    }
}