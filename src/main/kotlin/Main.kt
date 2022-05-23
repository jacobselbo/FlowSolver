import utils.Node
import utils.Tree
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.system.measureTimeMillis

fun populatePathChildren(node: Node<Path>) {
    val path = node.value
    val board = path.board
    val tree = node.tree
    val currentCellPosition = path.getCurrentPathCellPosition()

    // Try all cells in 4 directions
    for (i in 0 until 4) {
        val newPosition = when (i) {
            0 -> currentCellPosition.move(-1, 0) // LEFT
            1 -> currentCellPosition.move(1, 0) // RIGHT
            2 -> currentCellPosition.move(0, 1) // DOWN
            else -> currentCellPosition.move(0, -1) // UP
        }

        if (newPosition.x >= 0 && newPosition.y >= 0 &&
                newPosition.x < board.width && newPosition.y < board.height) {
            if (board.cells[newPosition.y][newPosition.x].type == CellType.NONE &&
                !path.pathPositions.contains(newPosition)) {
                val newPath = path.clone()

                newPath.pathPositions.add(newPosition)

                val newNode = Node(newPath, tree, node)

                node.nodes.add(newNode)
                tree.queue.enqueue(newNode)
            }
        }
    }
}

fun createPathSolutions(board: Board): HashMap<Int, List<Path>> {
    return HashMap<Int, List<Path>>().apply {
        for (path in board.paths) {
            val pathTree = Tree(path, ::populatePathChildren)
            val populatedPathSolutions = pathTree.populateTreeAdd {
                    pathNode ->
                pathNode.value.isCompleted()
            }

            this[path.id] = populatedPathSolutions.map { it.value }
        }
    }
}

fun solve(board: Board): Board? {
    val pathSolutions = createPathSolutions(board)

    val boardTree = Tree(board) { node ->
        val boardNode = node.value
        val tree = node.tree

        for ((id, paths) in pathSolutions) {
            if (!boardNode.getPathFromID(id)!!.isCompleted()) {
                for (path in paths) {
                    val newBoard = boardNode.fitPathInNewBoard(path)

                    if (newBoard != null) {
                        val newNode = Node(newBoard, tree, node)

                        if (!tree.processed.contains(newBoard)) {
                            node.nodes.add(newNode)
                            tree.queue.enqueue(newNode)
                            tree.processed.add(newBoard)
                        }
                    }
                }
            }
        }
    }

    val solution = boardTree.populateTreeCheck { node -> node.value.isSolved() }

    return solution?.value
}

fun console() {
    // Get size of the board
    println("Flow Solver")
    println("What is the size of the board? (5x2)")

    val sizeString = readln()

    require(sizeString.length == 3) { "Board size formatting is incorrect" }

    val sizeSplit = sizeString.split('x')
    val width = sizeSplit[0].toIntOrNull()
    val height =sizeSplit[1].toIntOrNull()

    requireNotNull(width) { "Formatting on the width is incorrect" }
    requireNotNull(height) { "Formatting on the width is incorrect" }

    // Get Board
    println("To input the board use 0 as an empty space, and a number with a + as a receiver.")
    println("For a one line example \"0 0 1+ 0 0\"")

    val rowStrings = ArrayList<String>()

    for (i in 0 until height) {
        rowStrings.add(readln())
        println("") // this is the weirdest thing I've ever seen. If I don't have this println the readLn() fails and the array is completely wrong
    }

    val boardArrayList = Array(height) { Array(width) { "0" } }

    for ((rowNumber, row) in rowStrings.withIndex()) {
        val split = row.split(' ')

        require(split.size == width) { "Wrong amount of cells given for the set width" }

        for ((colNumber, cell) in split.withIndex()) {
            boardArrayList[rowNumber][colNumber] = cell
        }
    }

    val board = Board.fromConsole(boardArrayList)
    var solution: Board?

    val time = measureTimeMillis {
        solution = solve(board)
    }

    println(solution)
    println("Time took: $time milliseconds")
}

fun main() {
    while (true) {
        console()

        println("Would you like to continue? (y/n)")

        if (readln() == "n")
            break
    }
}