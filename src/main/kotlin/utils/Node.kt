package utils

/**
 * A Node used in a Tree that contains a Board, and processed children Nodes.
 *
 * @author Jacob Selbo
 * @since 4/24/22
 *
 * @property value The value of type T
 * @property parent The parent Utilities.Node of this Utilities.Node
 * @property nodes An ArrayList of the children Nodes
 * @property tree The Utilities.Tree encapsulating the Nodes
 */
data class Node<T>(val value: T, val tree: Tree<T>, val parent: Node<T>?, ) {
    val nodes: ArrayList<Node<T>> = arrayListOf()
}