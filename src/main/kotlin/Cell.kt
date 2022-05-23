import utils.Position

/**
 * A Cell class that contains the position of the cell, whether it's a Receiver, or a Path, and its ID.
 * An ID is used instead of a Flow "Color" because there can be, effectively, an infinite number of ID's, and it decreases complexity:
 * printing the Cell, interacting with the Cell, and, the input of the Cell.
 *
 * @author Jacob Selbo
 * @since 4/23/22
 *
 * @property board The Board of this Cell
 * @property position The Utilities.Position of the Cell
 * @property type What is held inside the cell: A Receiver, Nothing, or a Path
 * @property id In Flow terms, the "Color" of the cell
 * @constructor Creates a basic Cell from a position
 */
data class Cell(
    val board: Board,
    val position: Position,
    var type: CellType = CellType.NONE,
    var id: Int = 0)
{
    /**
     * @param board The new board for this clone
     * @return A perfect clone of this Cell
     */
    fun clone(board: Board): Cell {
        return Cell(board, position.clone(), type, id)
    }

    /**
     * @return A readable form of the cell
     */
    override fun toString(): String {
        return when (type) {
            CellType.NONE -> "   "
            CellType.RECEIVER -> "+$id+"
            CellType.PATH -> "|$id|"
        }
    }

    companion object {
        /**
         * Takes a console inputted string, and interprets it into a CellType and ID for a new Cell.
         *
         * char[0] == ID
         * char[1] == CellType
         *
         * 0 == Empty Cell
         * "+" == CellType.RECEIVER
         * "-" == CellType.PATH
         *
         * @param board The Board for the new Cell
         * @param position The Utilities.Position for the new Cell
         * @param string The console inputted string, to be interpreted into a CellType and ID
         * @return A new Cell from a console inputted string
         */
        fun fromString(board: Board, position: Position, string: String): Cell {
            val chars = string.toCharArray()
            val id = chars[0].digitToInt()

            require(id >= 0) { "ID is less than 0" }

            return if (id == 0)
                Cell(board, position)
            else
                Cell(board, position,
                    if(chars[1] == '+') CellType.RECEIVER
                    else CellType.PATH, id) // else == '-'
        }
    }
}