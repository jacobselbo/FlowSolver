package utils

/**
 * A basic Position carrying an X and Y
 *
 * @author Jacob Selbo
 * @since 4/21/22
 *
 * @property x How far right from the top left corner of the board
 * @property y How far up from the top left corner of the board
 * @constructor Creates a Utilities.Position
 */
data class Position(val x: Int, val y: Int) {
    /**
     * Uses the current position, and adds the x and y to a new Position
     *
     * @param x current X + new X
     * @param y current Y + new Y
     * @return New position
     */
    fun move(x: Int, y: Int): Position {
        return Position(this.x + x, this.y + y)
    }

    /**
     * @param other Other position to check if next to
     * @return Whether this and the [other] position are 1 position away, ignoring diagonals
     */
    fun isBeside(other: Position): Boolean {
        return (y == other.y && other.x in (x - 1)..(x + 1)) ||
                (x == other.x && other.y in (y - 1)..(y + 1))
    }

    /**
     * @return A perfect clone of this Position
     */
    fun clone(): Position {
        return Position(x, y)
    }
}