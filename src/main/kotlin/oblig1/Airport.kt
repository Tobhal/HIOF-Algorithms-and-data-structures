package oblig1

import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.*

// Get a random number with a mean, taken form the task
fun getPoissonRandom(mean: Double): Int {
    val r = Random()
    val L = Math.exp(-mean)
    var k = 0
    var p = 1.0

    do {
        p *= r.nextDouble()
        k++
    } while (p > L)

    return k - 1
}

// Store stats for the algorithm
class Stats(
    val timeUnits: Int
) {
    var tot = 0
    var landed = 0
    var departures = 0
    var rejected = 0
    var unitsEmpty = 0.0

    var landing = 0
    var waiting = 0

    var departureWaitLen = 0.0
    var departureWait: Double = 0.0
        set(value) { // Setter
            field = value
            departureWaitLen++
        }

    var takeoffWaitLen = 0.0
    var takeoffWait = 0.0
        set(value) { // Setter
            field = value
            takeoffWaitLen++
        }

    override fun toString(): String {
        return """
            Simulation time           $timeUnits
            Total Planes handled    : $tot
            Planes landed           : $landed
            Planes departure        : $departures
            Planes ready to land    : $landing
            Planes ready to takeoff : $waiting
            % wait time             : ${((unitsEmpty / timeUnits) * 100).roundToInt()}%
            Average wait, landing   : ${departureWait / departureWaitLen}
            Average wait, takeOff   : ${takeoffWait / takeoffWaitLen}
        """.trimIndent()
    }
}

// Simple class to store the plane values
class Plane {
    val ID: Int = runningID++
    var waitTime = 0

    // Define a static variable to keep the next ID for the plane
    companion object {
        @JvmStatic var runningID: Int = 0
    }

    override fun toString(): String {
        return "Plane(ID=$ID,WT=$waitTime)"
    }
}

// The main class for the oblig
class Airport(
    private val timeUnits: Int,
    private val arrivals: Double,
    private val departures: Double,
    private val landingSize: Int,
    private val departureSize: Int,
) {
    // Store the planes in each list. The number is the ID for the plane
    private var landing = LinkedList<Plane>()
    private var departure = LinkedList<Plane>()
    private val stats = Stats(timeUnits)

    fun run() {
        // For every simulation unit (t) in the defined time units
        for (t in 0..timeUnits) {
            println("Time: $t")

            // Handle planes landing
            var newPlanes = getPoissonRandom(arrivals)
            for (l in 1..newPlanes) {
                if (landing.size <= landingSize) {
                    landing.add(Plane())
                    stats.tot++
                    println("Plane ${landing.last.ID} ready to land")
                } else {
                    println("Plane rejected for landing")
                }
            }

            // Handle planes taking off
            newPlanes = getPoissonRandom(departures)
            for (l in 1..newPlanes) {
                if (departure.size <= departureSize) {
                    departure.add(Plane())

                    println("Plane ${departure.last.ID} waiting to take off.")
                } else {
                    println("Plane needs to wait to departure")
                }
            }

            // If the landing queue or departure queue is empty do the correct thing
            if (!landing.isEmpty()) {
                println("Plane ${landing.first.ID} landed. Waited ${landing.first.waitTime}")

                // Update stats
                stats.departureWait += landing.first.waitTime
                stats.landed++

                // Remove the plane
                landing.removeFirst()
            } else if (!departure.isEmpty()) {
                println("Plane ${departure.first.ID} took off.")

                // Update stats
                stats.takeoffWait += departure.first.waitTime
                stats.departures++

                // Remove the plane
                departure.removeFirst()
            } else {
                println("No planes in airport")

                // Update the stats
                stats.unitsEmpty++
            }

            // For every plane still in a queue update the wait time variable and print the info
            print("Planes waiting to land = [")
            for (p in landing) {
                p.waitTime++
                print("${p}, ")
            }
            println("]")

            print("Planes waiting to take off = [")
            for (p in departure) {
                p.waitTime++
                print("${p}, ")
            }
            println("]")

            println()
        }

        // Update the stats
        stats.landing = landing.size
        stats.waiting = departure.size

        // Print the stats
        println(stats)
    }
}

// Main function
fun airportMain() {
    println("Velkommen til Halden flyplass.")

    print("Number of time units: ")
    // Read the amount of time unints to use
    val timeUnits = readln()

    print("Number of arrivals: ")
    // Read the amount of arrivals
    val arrivals = readln()

    print("Number of departures: ")
    // Read the amount of departures
    val departures = readln()

    // Initiate the Airport class and run the simulation
    Airport(
        timeUnits.toInt(),
        arrivals.toDouble(),
        departures.toDouble(),
        20, 30
    ).run()
}