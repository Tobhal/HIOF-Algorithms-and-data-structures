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

    fun search(s: String): Boolean {
        var hN = hashTable[hash(s)]

        while (hN != null) {
            if (hN.data.compareTo(s) == 0)
                return true
            println("D-> ${hN.data} - h: ${hash(hN.data)}")
            hN = hN.next
        }
        return false
    }

    fun print() {
        var hN = hashTable[0]

        while (hN != null) {
            println("D-> ${hN.data} - h: ${hash(hN.data)}")
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
    println("Probes      : ${hL.numCollisions}")

    hL.print()

    var s = "Volkswagen Karmann Ghia"
    if (hL.search(s))
        println("\"$s\" exists in the hash table")
    s = "Il Tempo Gigante"
    if (!hL.search(s))
        println("\"$s\" does not exists in the hash table")
}