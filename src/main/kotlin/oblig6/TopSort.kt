package oblig6

class TopSort(
    fileName: String
) : EasyGraph(fileName) {

    // Depth First search inspired of given class
    fun DFS(at: Int, v: Array<Boolean>) {
        // Print the current data element
        print("${data[at]} -> ")

        // Set the node to visited
        v[at] = true
        // For i < N
        for (i in data.indices)
            // Check if the current node is a neighbour node to the current "at" node and also not itself
            if (neighbour[at][i] && !v[at])
                // There is a neighbour node, so run the DFS again
                DFS(i, v)
    }

    // Main print function
    fun findAndPrint() {
        // Initiate the needed arrays
        var ordering = Array(data.size) {_->0}
        var v = Array(data.size) {_->false}
        // Sett the index
        var i = data.size - 1

        // For at < N
        for (at in data.indices) {
            // If not visited this node
            if (!v[at]) {
                // Depth First Search
                DFS(at, v)
            }
        }
        // Print to indicate the end of things.
        println("null")
    }
}

fun topSortMain() {
    // Path to the file dir
    val filePath = "src/main/kotlin/oblig6/"

    // Do topSort on data form file 1
    val topSort = TopSort(filePath + "graf_topsort_1.txt")
    println("Data:")
    topSort.print()
    println("Top Sort:")
    topSort.findAndPrint()

    println("----------------------------------------------------")

    // do topSort on data from file 2
    val topSort2 = TopSort(filePath + "graf_topsort_2.txt")
    println("Data:")
    topSort2.print()
    println("Top Sort:")
    topSort2.findAndPrint()
}