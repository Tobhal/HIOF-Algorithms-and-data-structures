// Oblig 2

// A simple vec2 class to store x and y values and make the kode easier
class Vec2(
    var x: Int,
    var y: Int,
) {
    // Overloading Vec2 + Vec2
    operator fun plus(nPos: Vec2): Vec2 =
        Vec2(x + nPos.x, y + nPos.y)
}

// Class to store all the Cells of the board
class Cells(
    val SIZE: Int
) {
    // Store all cells
    private var cells = Array(SIZE) { i -> Array(SIZE) { l -> addCell(i, l)}}

    // Define static methods
    companion object {
        @JvmStatic fun addCell(i: Int, l: Int): String = if (((i + l)) % 2 == 0) "\u2B1C" else "\u2B1B"
        @JvmStatic fun addCell(pos: Vec2): String = addCell(pos.x, pos.y)
    }

    // Iterator to have foreach loop
    operator fun iterator(): Iterator<String> {
        return object: Iterator<String> {
            val state = cells.sortedBy{it.size}
            var i = 0
            var l = 0

            override fun hasNext(): Boolean = i < state.size
            override fun next(): String {
                val retVal = state[i][l]

                if (l == SIZE - 1) {
                    l = 0
                    i++
                } else {
                    l++
                }

                return retVal
            }
        }
    }

    /*
    Operator overloading
     */
    // Overload [x, y]
    operator fun get(x: Int, y: Int): String =
         cells[x][y]

    // Overload [index]
    operator fun get(index: Vec2): String =
        get(index.x, index.y)

    // Overload [index] = value
    operator fun set(pos: Vec2, v: String) {
        cells[pos.x][pos.y] = v
    }

    // Overloads value in object
    operator fun contains(pos: Vec2): Boolean =
        pos.x in (0 until SIZE) && pos.y in (0 until SIZE)

    override fun toString(): String {
        var retStr = ""

        for (c in cells) {
            for (r in c)
                retStr += "$r "
            retStr += "\n"
        }

        return retStr
    }
}

// Store the state of a board at a given size
class Board(
    SIZE: Int,
) {
    // Store all cells
    private var cells = Cells(SIZE)

    // Move the knight
    fun moveKnight(pos: Vec2, num: Int): Boolean {
        return if (pos in cells && (cells[pos]).toIntOrNull() == null) {
            cells[pos] = num.toString()
            true
        } else {
            false
        }
    }

    // Reset the cell to its original square
    fun reset(pos: Vec2) {
        cells[pos] = Cells.addCell(pos)
    }

    override fun toString(): String = cells.toString()
}

class KnightsTour(
    private var startPos: Vec2,
    SIZE: Int,
) {
    private var totLen = 0
    private var board = Board(SIZE)

    // All move modifiers for the knight, how it can move
    private val moves = arrayOf(
        Vec2(1, 2),
        Vec2(1, -2),
        Vec2(2, 1),
        Vec2(2, -1),
        Vec2(-1, 2),
        Vec2(-1, -2),
        Vec2(-2, 1),
        Vec2(-2, -1),
    )

    // Initiate the class
    init {
        totLen = SIZE * SIZE
        // While the input is invalid get the input again
        while (startPos.x >= SIZE || startPos.y >= SIZE) {
            println("Error, cant place Knight at X= ${startPos.x}, Y= ${startPos.y}")
            print("Enter start position: ")

            val (x, y) = readln().split(",| ".toRegex())
            startPos.x = x.toInt()
            startPos.y = y.toInt()
        }
    }

    // Main solver function based on: https://www.geeksforgeeks.org/the-knights-tour-problem-backtracking-1/
    private fun solver(pos: Vec2, move: Int): Boolean {
        // Se if all cells are used
        if (move == totLen) return true

        // For every possible move test if there is a possible solution
        for (k in moves.indices) {
            // Set the next position from the move
            val nextPos = pos + moves[k]

            // Try to move the knight to the next position
            if (board.moveKnight(nextPos, move)) {

                // Run the solver again
                if (solver(nextPos, move + 1)) {
                    return true
                } else {
                    board.reset(nextPos)
                }
            }
        }

        return false
    }

    // Function for running the Knights tour algorithm
    fun run() {
        println("Running using x= ${startPos.x}, y= ${startPos.y} and len= $totLen")

        // Do the first move to the location that is defined when running
        board.moveKnight(startPos, 0)

        // Do the solver with recursive the function
        if (!solver(startPos, 1)) {
            println("No solution found")
        } else {
            println("Solution found!")
            println(board)
        }
    }
}

// Main function
fun knightsTourMain() {
    print("Enter start position (x,y): ")
    // Read input, split get the numbers form "x,y" or "x, y" or "x y"
    val (x, y) = readln().split(",| ".toRegex())

    print("Enter board size: ")
    val s = readln()

    // Initiate the Knights tour class
    KnightsTour(Vec2(
        x.toInt(),
        y.toInt()),
        s.toInt()
    ).run() // Run the algorithm
}