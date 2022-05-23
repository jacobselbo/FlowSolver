package utils

import Path

/**
 * A Tree encapsulating the Nodes
 *
 * @author Jacob Selbo
 * @since 4/24/22
 *
 * @property starting The starting node of the generic T
 * @property root The root Node containing all others Nodes
 * @property queue If root, a queue of nodes to process
 * @property processed An ArrayList of already tried of the generic (This is used to try and cut down on CPU cost)
 */
data class Tree<T>(private val starting: T, val populateChildren: (node: Node<T>) -> Unit) {
    private val root: Node<T> = Node(starting, this, null)
    val queue: Queue<Node<T>> = Queue()
    val processed: ArrayList<T> = arrayListOf()

    /**
     * Populate the tree with nodes
     *
     * @param check The function used to use to see if the tree is solved
     * @return the solved node
     */
    fun populateTreeCheck(check: (node: Node<T>) -> Boolean): Node<T>? {
        if (check(root))
            return root

        populateChildren(root)

        while (!queue.isEmpty()) {
            val node = queue.dequeue()!!

            if (check(node))
                return node

            populateChildren(node)
        }

        return null
    }

    /**
     * Populate the tree with nodes
     *
     * @param addToSolutions The function used to use to see if a node is a solution
     * @return a list of nodes that are solved
     */
    fun populateTreeAdd(addToSolutions: (node: Node<T>) -> Boolean): ArrayList<Node<T>> {
        val solutions = arrayListOf<Node<T>>()

        populateChildren(root)

        while (!queue.isEmpty()) {
            val node = queue.dequeue()!!

            if (addToSolutions(node)) {
                solutions.add(node)
            } else
                populateChildren(node)
        }

        return solutions
    }
}