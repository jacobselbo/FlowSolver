import utils.Position

/**
 * A potential Flow Path consisting of receiver cells, and positions of possible path cells
 *
 * @author Jacob Selbo
 * @since 4/23/22
 *
 * @property id The ID (Flow "Color") used by this path
 * @property startCell One of the end points for the path, the Receiver
 * @property endCell One of the end points for the path, the Receiver
 * @property pathPositions All the path Positions that connect to the Receivers
 * @property board The board that the Cells are on
 * @constructor A basic Path
 */
data class Path(val startCell: Cell, val endCell: Cell) {
    val id: Int = startCell.id
    val pathPositions: ArrayList<Position> = arrayListOf()
    val board = startCell.board

    /**
     * A secondary constructor with the Path Cells already completed
     *
     * @param startCell One of the end points for the path, the Receiver
     * @param endCell One of the end points for the path, the Receiver
     * @param pathPositions All the path Positions that connect to the Receivers
     */
    constructor(startCell: Cell, endCell: Cell, pathPositions: ArrayList<Position>) : this(startCell, endCell) {
        this.pathPositions.addAll(pathPositions)
    }

    /**
     * Gets the Cell to continue the Path
     *
     * @return The last cell in [pathPositions], or [startCell]
     */
    fun getCurrentPathCellPosition(): Position {
        return if (pathPositions.isEmpty())
            startCell.position
        else
            pathPositions.last()
    }

    /**
     * @return A Boolean of whether the [pathPositions] are correctly connected to both [startCell] and [endCell]
     */
    fun isCompleted(): Boolean {
        return getCurrentPathCellPosition().isBeside(endCell.position)
    }

    /**
     * @return A perfect clone of this Position
     */
    fun clone(): Path {
        val newPathPositions = arrayListOf<Position>()

        for (position in pathPositions) {
            newPathPositions.add(position.clone())
        }

        return Path(startCell, endCell, newPathPositions)
    }

    // Sanity checks
    init {
        require(startCell.board === endCell.board) { "The receivers are on different Boards" }
        require(startCell.id == endCell.id) { "The receivers ID's are different" }
        require(id > 0) { "ID is less than 1" }
    }
}