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


    /*
    Oppgave 1. Last come, first served
     */
    // Function to move a element at a index position to another position
    private fun move(i: Int, originalInt: Int) {
        // Calculate the next index to use
        val next = (i + 1) % hashLength

        // If the next index is the start index, we have looped, so the array is full
        if (next == originalInt) {
            System.err.println("\nHash table is full, canceling")
            exitProcess(0)
        }

        // If the next element is empty move the current element to the next element to the new position
        // Sett the current element to null
        if (hashTable[next] == null) {
            hashTable[next] = hashTable[i]
            hashTable[i] = null
        } else {    // There is a element on this position so move that.
            move(next, originalInt)
        }
    }

    fun insert(s: String) {
        val h = hash(s)
        while (hashTable[h] != null) {
            // Changes this to move the value if there is something there
            move(h, h)
            numProbes++
        }
        hashTable[h] = s
    }

    /*
    Oppgave 2. Robin Hood
        Based on: https://programming.guide/robin-hood-hashing.html
     */
    // Return the index difference of the hash
    private fun hashDiff(s: String, i: Int): Int = abs(hash(s) - i)

    fun insert2(s: String) {
        var workingString = s
        var h: Int = hash(s)
        var next: Int = h
        while (hashTable[next] != null) {
            // If string at index is larger or the same, so move on
            if (hashTable[next]?.let { hashDiff(it, next) }!! >= hashDiff(s, next)) {
                next = (next + 1) % hashLength
            } else {    // The string at the index is smaller, swap positions
                val tmp = hashTable[next]
                hashTable[next] = workingString
                // Needs to be a null check to make Kotlin happy
                if (tmp != null) {
                    workingString = tmp
                    h = hash(workingString)
                }
                numProbes++
            }

            // If the inserting has looped the hash list is full return error
            if (next == hash(s)) {
                System.err.println("\nHash table is full, canceling")
                exitProcess(0)
            }
        }
        hashTable[next] = workingString
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

    var readInput = true
    while (readInput) {
        print("Inn: ")
        val str = readln()
        if (str == "")
            readInput = false
        else
            hL.insert(str)
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