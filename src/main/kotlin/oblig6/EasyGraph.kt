package oblig6

import java.io.File
import java.io.IOException
import java.util.*
import kotlin.system.exitProcess

open class EasyGraph(
    val fileName: String,
) {
    var n = 0
    lateinit var neighbour: Array<BooleanArray>
    lateinit var data: Array<String>

    init {
        read()
    }

    fun read() {
        try {
            val inn = Scanner(File(fileName))

            n = inn.nextInt()
            neighbour = Array(n) {BooleanArray(n)}
            data = Array(n) {_ -> ""}
            for (i in 0 until n)
                for (j in 0 until n)
                    neighbour[i][j] = (i == j)

            for (i in 0  until  n) {
                val nodeNum = inn.nextInt()
                data[nodeNum] = inn.next()

                val numNeighbour = inn.nextInt()
                for (j in 0  until  numNeighbour) {
                    val neighbourNum = inn.nextInt()
                    neighbour[nodeNum][neighbourNum] = true
                }
            }
        } catch (e: Exception) {
            System.err.println(e)
            exitProcess(0)
        }
    }

    fun print() {
        for (i in 0 until n) {
            print("${data[i]}: ")
            for (j in 0 until n)
                if (neighbour[i][j] && i!=j)
                    print("${data[j]} ")
            println()
        }
    }

    fun main(args: Array<String>) {
        val fileName: String
        try {
            if (args.size != 1)
                throw IOException("Missing file name")
            fileName = args[0]
        } catch (e: Exception) {
            System.err.println(e)
            exitProcess(0)
        }
        val g = EasyGraph(fileName)
        g.print()
    }
}