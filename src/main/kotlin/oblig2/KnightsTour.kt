package oblig2

import java.util.*
import java.util.concurrent.ThreadLocalRandom

// Oblig 2

// A simple vec2 class to store x and y values and make the kode easier
class Vec2(
    var x: Int = 0,
    var y: Int = 0,
) {
    // Overloading Vec2 + Vec2
    operator fun plus(nPos: Vec2): Vec2 =
        Vec2(x + nPos.x, y + nPos.y)

    override operator fun equals(other: Any?): Boolean =
        !(other == null || other !is Vec2 || x != other.x || y != other.y)

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}

// Class to store all the Cells of the board
class Cells(
    val SIZE: Int
) {
    // Store all cells
    private var cells = Array(SIZE) { i -> Array(SIZE) { l -> add(i, l)}}

    // Define static methods
    companion object {
        @JvmStatic fun add(i: Int, l: Int): String = if (((i + l)) % 2 == 0) "\u2B1C" else "\u2B1B"
        @JvmStatic fun add(pos: Vec2): String = add(pos.x, pos.y)
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
        return if (cellEmpty(pos)) {
            cells[pos] = num.toString()
            true
        } else {
            false
        }
    }

    fun cellEmpty(pos: Vec2): Boolean =
        pos in cells && cells[pos].toIntOrNull() == null

    // Reset the cell to its original square
    fun reset(pos: Vec2) {
        cells[pos] = Cells.add(pos)
    }

    override fun toString(): String = cells.toString()
}

class KnightsTour(
    private var startPos: Vec2,
    private val SIZE: Int,
    private val animate: Boolean,
) {
    private var totLen = 0
    private val animationSpeed: Long = 750 // Ms
    var board = Board(SIZE)

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

                // "Animate" the board movement
                if (animate) {
                    println(board)
                    Thread.sleep(animationSpeed)
                }

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

    // Get the amount of possible squares to the position
    private fun getDegree(pos: Vec2): Int {
        var count = 0
        for (i in moves.indices) {
            if (board.cellEmpty(pos + moves[i])) {
                count++
            }
        }
        return count
    }

    // Select the next move
    private fun nextMove(pos: Vec2, move: Int): Vec2? {
        // Sett opp variables
        var minDegIdx = -1
        var minDeg = moves.size + 1
        var c: Int
        var nPos = Vec2()

        // Sett some random value
        val r = ThreadLocalRandom.current().nextInt(1000) % moves.size

        // For each possible move
        for (count in moves.indices) {
            // Select the move index
            val i = (r + count) % moves.size
            // Select the new move
            val newPos = pos + moves[i]
            // Get all possible moves from the new position
            c = getDegree(newPos)
            // If the cell is not empty add to possible move
            if (board.cellEmpty(newPos) && c < minDeg) {
                minDegIdx = i
                minDeg = c
            }
        }

        // If there is no possible next move
        if (minDegIdx == -1)
            return null

        // Set the best position to move to
        nPos = pos + moves[minDegIdx]

        // Move the knight
        board.moveKnight(nPos, move+1)

        if (animate) {
            println(board)
            Thread.sleep(animationSpeed)
        }

        return nPos
    }

    // Warnsdorff’s algorithm
    private fun solver2(pos: Vec2): Boolean {
        // Working position
        var ret: Vec2? = pos
        // Move the knight
        board.moveKnight(pos, 0)

        if (animate)
            Thread.sleep(animationSpeed)

        // Move the knight to a new position then for each possible cell in the board.
        // Return if found a move
        for (i in 0 until totLen - 1) {
            ret = ret?.let { nextMove(it, i) }
            if (ret == null)
                return false
        }

        return true
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

    // Function for running Warnsdorff’s algorithm
    fun run2() {
        var foundSolution = false

        // Running the solver, but limiting so to not be a infinet loop
        for (i in 0..totLen*8) {
            if (solver2(startPos)) {
                foundSolution = true
                break
            }
        }

        if (foundSolution)
            println("Solution found!")
        else
            println("No solution found")

        println(board)
    }
}

// Main function
fun knightsTourMain() {
    print("Enter start position (x,y): ")
    // Read input, split get the numbers form "x,y" or "x, y" or "x y"
    val (x, y) = readln().split(",| ".toRegex())

    print("Enter board size: ")
    val s = readln()

    // Select to print all steps or not
    print("Animate?: ")
    val a = readln().lowercase(Locale.getDefault())
    val animate: Boolean = when (a) {
        "true", "t", "yes", "y" -> true
        "false", "f", "no", "n" -> false
        else -> false
    }

    // Select Backtracking or Warnsdorff’s
    print("Solver (1 or 2): ")
    val solver = readln().toInt()

    // Initiate the Knights tour class
    val kt = KnightsTour(Vec2(
        x.toInt(),
        y.toInt()),
        s.toInt(),
        animate
    )

    // Run the correct solver
    when (solver) {
        1 -> kt.run()
        2 -> kt.run2()
        else -> kt.run()
    }
}