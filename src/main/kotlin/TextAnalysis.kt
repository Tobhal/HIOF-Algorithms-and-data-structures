import java.io.File
import java.util.*

// Node class to store the words into
class Node(
    var value: String,
) {
    var count = 1
    var left: Node? = null
    var right: Node? = null
}

// The Binary Search Tree class
class TextAnalysis {
    // The root node
    private var root: Node? = null

    // Public function to insert a string to the node network
    fun insert(value: String) {
        // Convert all lines to lowercase
        val value = value.lowercase(Locale.getDefault())

        // Split the line on the characters:
        //      ' ', '.', ',', '(', ')'
        val words = value.split(" |\\. *| *\\(|\\)\\.* *|, *".toRegex())

        // For each word that is not empty ("") insert into the network
        for (word in words)
            if (word != "")
                root = insertR(root, word)
    }

    // Private insert function to enable recursion
    private fun insertR(root: Node?, value: String): Node {
        // Test if the root nods is null
        if (root == null)
            return Node(value)                      // Return a new Node with the value
        else if (value < root.value)            // The value string is lower in the alphabet then the root value
            root.left = insertR(root.left, value)   // Insert into the left node
        else if (value > root.value)            // The value string is higher in the alphabet then the root value
            root.right = insertR(root.right, value) // Insert into the right node
        else            // The value is the same as the node
            root.count++    // Increase the count for that word
        return root
    }

    // Private to string function to enable traversing the node network
    private fun addToStringR(root: Node?): String {
        var retStr = ""
        if (root != null) {
            retStr += addToStringR(root.left)           // Get all nodes to the left first
            retStr += "${root.value}: ${root.count}\n"  // Print the current node value
            retStr += addToStringR(root.right)          // Get all nodes to the right last
        }
        return retStr
    }

    // Simple to string override
    override fun toString(): String = addToStringR(root)
}

fun textAnalysisMain() {
    // Store the Binary search tree class
    val bst = TextAnalysis()

    // Read the file and push each line to the BST to insert into the node network
    File("src/main/kotlin/test.txt").useLines { l -> l.forEach { bst.insert(it) } }

    // Print all words and their occurrences
    println(bst)
}