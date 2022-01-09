package oblig5

import java.io.IOException
import java.util.Scanner
import kotlin.math.abs
import kotlin.system.exitProcess

class HashLinear(
    private var hashLength: Int
) {
    private var hashTable = Array<String?>(hashLength) {_ -> null}
    var numProbes = 0
        get() = field

    fun loadFactor(): Float = hashTable.size / hashLength.toFloat()

    fun numData(): Int = hashTable.size

    fun hash(s: String): Int = abs(s.hashCode()) % hashLength

    fun insert(s: String) {
        val h = hash(s)
        var next = h
        while (hashTable[next] != null) {
            numProbes++
            next = (next + 1) % hashLength
            if (next == h) {
                System.err.println("\nHash table is full, canceling")
                exitProcess(0)
            }
        }

        hashTable[next] = s
    }

    fun search(s: String): Boolean {
        val h = hash(s)
        var next = h

        while (hashTable[next] != null) {
            if (hashTable[next]?.compareTo(s) == 0)
                return true

            next = (next + 1) % hashLength

            if (next == h)
                return false
        }

        return false
    }

}

fun hashLinerMain(args: Array<String>) {
    val hashLength: Int
    val input = Scanner(System.`in`)

    try {
        if (args.size != 1)
            throw IOException("Error: Hash length needs to be given")
        hashLength = args[0].toInt()
        if (hashLength < 1)
            throw IOException("Error: Hash length needs to be larger then 0")
    } catch (e: Exception) {
        System.err.println(e)
        exitProcess(0)
    }

    val hL = HashLinear(hashLength)

    while (input.hasNext()) {
        hL.insert(input.nextLine())
    }

    println("Hash length : $hashLength")
    println("Elements    : ${hL.numData()}")
    println("Load factor : %5.3f".format(hL.loadFactor()))
    println("Probes      : ${hL.numProbes}")

    var s = "Volkswagen Karmann Ghia"
    if (hL.search(s))
        println("\"$s\" exists in the hash table")
    s = "Il Tempo Gigante"
    if (!hL.search(s))
        println("\"$s\" does not exists in the hash table")
}