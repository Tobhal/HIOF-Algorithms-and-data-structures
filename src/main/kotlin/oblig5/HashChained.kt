package oblig5

import java.io.IOException
import java.util.*
import kotlin.math.abs
import kotlin.system.exitProcess

class HashNode(
    var data: String,
    var next: HashNode? = null,
)

class HashChained(
    private var hashLength: Int
) {
    private var hashTable = Array<HashNode?>(hashLength) {_ -> null}
    var numCollisions = 0
        get() = field

    fun loadFactor(): Float = hashTable.size / hashLength.toFloat()

    fun numData(): Int = hashTable.size

    fun hash(s: String): Int = abs(s.hashCode()) % hashLength

    fun insert(s: String) {
        val h = hash(s)
        if (hashTable[h] != null)
            numCollisions++
        hashTable[h] = HashNode(s, hashTable[h])
    }

    /*
    Oppgave 3: Delete data
     */
    fun delete(s: String) {
        // Loop true the hash table
        for (i in hashTable.indices) {
            // Set the next HashNode to be the first element in the hashTable
            var current = hashTable[i]
            // This hashNode equals s
            if (current != null && current.data == s) {
                // Sett the first element to the next element
                hashTable[i] = current.next
            }

            // While current is not null
            while (current != null) {
                // If the current next data equals s
                if (current.next != null && current.next!!.data == s) {
                    // Sett the current next to current next's next
                    current.next = current.next!!.next
                    // Done with removing
                    return
                }
                // Sett current to the next HashNode
                current = current.next
            }
        }
    }

    fun search(s: String): Boolean {
        var hN = hashTable[hash(s)]

        while (hN != null) {
            if (hN.data.compareTo(s) == 0)
                return true
            hN = hN.next
        }
        return false
    }

    // Simple function to print the data stored
    // Also used to se how the data is laid out
    fun print() {
        for (hN in hashTable) {
            var next = hN
            print("[")
            while (next != null) {
                print("${next.data} -> ")
                next = next.next
            }
            println("null]")
        }
    }
}

fun hashChainedMain(args: Array<String>) {
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

    val hL = HashChained(hashLength)

    // Add elements to the hash chain
    var readInput = true
    while (readInput) {
        print("Inn: ")
        val str = readln()
        if (str == "")
            readInput = false
        else
            hL.insert(str)
    }

    hL.print()

    // Remove elements from the hash chain
    readInput = true
    while (readInput) {
        print("Delete: ")
        val str = readln()
        if (str == "")
            readInput = false
        else
            hL.delete(str)
    }

    println("Hash length : $hashLength")
    println("Elements    : ${hL.numData()}")
    println("Load factor : %5.3f".format(hL.loadFactor()))
    println("Probes      : ${hL.numCollisions}")

    hL.print()

    var s = "Volkswagen Karmann Ghia"
    if (hL.search(s))
        println("\"$s\" exists in the hash table")
    s = "Il Tempo Gigante"
    if (!hL.search(s))
        println("\"$s\" does not exists in the hash table")
}