import utils.Position
import java.lang.StringBuilder

/**
 * A Flow Board that contains a matrix of the Cells
 *
 * @author Jacob Selbo
 * @since 4/23/22
 *
 * @property width The width of the blank Board
 * @property height The height of the blank Board
 * @property cells All the cells in the Flow Board
 * @property paths All the paths in the Flow Board
 * @constructor Creates a blank Flow Board
 */
class Board(val width: Int, val height: Int) {
    val cells: Array<Array<Cell>> = Array(height) {
            y -> Array(width) {
            x -> Cell(this, Position(x, y)) } }

    var paths: Array<Path> = arrayOf()
        get() {
            if (field.isNotEmpty())
                return field

            val cellsSorted: HashMap<Int, ArrayList<Cell>> = hashMapOf() // An ArrayList of the same ID's cells
            val paths: ArrayList<Path> = arrayListOf()

            for (row in cells) {
                for (cell in row) {
                    if (cell.type != CellType.NONE) {
                        if (cellsSorted[cell.id] == null)
                            cellsSorted[cell.id] = arrayListOf()

                        cellsSorted[cell.id]!!.add(cell)
                    }
                }
            }

            for ((_, cells) in cellsSorted) {
                val pathPositions: ArrayList<Position> = arrayListOf()
                var startCell: Cell? = null
                var endCell: Cell? = null

                for (cell in cells)
                    if (cell.type == CellType.PATH)
                        pathPositions.add(cell.position)
                    else if (startCell == null) // type == CellType.RECEIVER
                        startCell = cell
                    else // type == CellType.RECEIVER && startCell is set
                        endCell = cell

                paths.add(Path(startCell!!, endCell!!, pathPositions))
            }

            field = paths.toTypedArray()
            return field
        }

    /**
     * Gets the path for a certain ID, or null
     *
     * @param id The ID of the path to find
     * @return The path with the same ID, or null if there isn't one
     */
    fun getPathFromID(id: Int): Path? {
        for (path in paths)
            if (path.id == id) return path

        return null
    }

    /**
     * @return A perfect clone of this Board
     */
    fun clone(): Board {
        return Board(width, height).also {
            for (row in cells)
                for (cell in row)
                    it.cells[cell.position.y][cell.position.x] = cell.clone(it)
        }
    }

    /**
     * @return A Boolean confirming if the Board is 100% filled, and all Paths are connected
     */
    fun isSolved(): Boolean {
        // Check if all the paths are completed
        for (path in paths)
            if (!path.isCompleted()) return false

        // Check that 100% of the cells are filled
        for (row in cells)
            for (cell in row)
                if (cell.type == CellType.NONE) return false

        return true
    }

    /**
     * Adds a new Cell to the board, and to the respective Path
     *
     * @param position The position of the new Cell
     * @param id The ID of the new Cell
     */
    private fun addCellPath(position: Position, id: Int) {
        val newCell = Cell(this, position, CellType.PATH, id)

        cells[position.y][position.x] = newCell
        getPathFromID(id)!!.pathPositions.add(newCell.position)
    }

    /**
     * Put a path into a cloned version of this board
     *
     * @param path The path to put into the new board
     * @return A new board, with the path if it can be fit
     */
    fun fitPathInNewBoard(path: Path): Board? {
        val clone = clone()

        for (position in path.pathPositions) {
            if (cells[position.y][position.x].type == CellType.NONE)
                clone.addCellPath(position, path.id)
            else
                return null
        }

        return clone
    }

    /**
     * Creates a human-readable board
     *
     * @return Creates a Flow Board from a String
     */
    override fun toString(): String {
        val output = StringBuilder()

        for (row in cells) {
            output.append("[ ")

            for (cell in row)
                output.append("$cell ")

            output.append("]\n")
        }

        return output.toString()
    }

    companion object {
        /**
         * Creates a Flow Board from a console input
         *
         * @param boardString The board as a String
         * @return A Flow Board from the provided String
         */
        fun fromConsole(boardString: Array<Array<String>>): Board {
            return Board(boardString[0].size, boardString.size).apply {
                // Adds the Cells from the string
                for ((rowIndex, row) in boardString.withIndex())
                    for ((cellIndex, cellString) in row.withIndex())
                        cells[rowIndex][cellIndex] = Cell.fromString(
                            this,
                            Position(cellIndex, rowIndex),
                            cellString)
            }
        }
    }
}