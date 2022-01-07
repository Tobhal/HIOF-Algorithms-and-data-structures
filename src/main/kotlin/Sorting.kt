import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.reflect.KFunction3
import kotlin.time.DurationUnit

class Util {
    companion object {
        @JvmStatic fun swap(arr: Array<Int>, i: Int, j:Int) {
            val tmp = arr[i]
            arr[i] = arr[j]
            arr[j] = tmp
        }

        // Checks if an array is sorted
        @JvmStatic fun isSorted(arr: Array<Int>): Boolean {
            for (i in 1 until arr.size)
                if (arr[i] < arr[i - 1])
                    return false
            return true
        }
    }
}

class Sorting {
    companion object {
        @OptIn(ExperimentalTime::class)
        // Return the time spent running a sorting algorithm
        @JvmStatic fun run(sorting: Unit): Duration {
            return measureTime(::Sorting)
        }

        // Function to find the estimate for C. take inn the array and a function with 3 parameters
        //      Using 3 parameters for all sorting algorithms because it is easier for me
        @JvmStatic fun estimate(sorting: KFunction3<Array<Int>, Int, Int, Unit>) {
            // Print the first line
            println("    n   |" +
                    "         T(ms)           |" +
                    "          T/n            |" +
                    "         T/n^2           |" +
                    "         T/n^3           |" +
                    "        T/log n          |" +
                    "        T/n log n        ")

            // Lists to keep all the values of C for each O(n) for each run
            val aTn1 = ArrayList<Double>()
            val aTn2 = ArrayList<Double>()
            val aTn3 = ArrayList<Double>()
            val alogN = ArrayList<Double>()
            val aNLogN = ArrayList<Double>()

            // Run the sorting with increasing amount of values in the array
            for (n in 1000..100000 step 1000) {
                var T = 0.0
                val lim = 5

                // Running the soring multiple times to get an average
                for (l in 0..lim) {
                    // Sett opp a new array with random number and a length of i
                    val m = n * 2
                    val arr = Array(n) {(0..m).random()}
                    arr.shuffle()

                    // Run the sorting on the chosen soring algorithm
                    T += run(sorting(arr, 0, arr.size - 1)).toDouble(DurationUnit.MILLISECONDS)
                }

                // Calc the average time
                T /= lim

                // Calculate the value for C using multiple O(n) functions and add them to the array
                val Tn1 = T/n
                aTn1.add(Tn1)

                // val Tn2 = T/(n.pow(2))
                val Tn2 = T/(n * n)
                aTn2.add(Tn2)

                // val Tn3 = T/(n.pow(3))
                val Tn3 = T/(n * n * n)
                aTn3.add(Tn3)

                val logN = T/log(n.toDouble(), 10.0)
                alogN.add(logN)

                val nLogN = T/n * log(n.toDouble(), 10.0)
                aNLogN.add(nLogN)

                // Print each round
                println("${n.toString().padStart(6)} | " +
                        "${T.toString().padStart(22)} | " +
                        "${Tn1.toString().padStart(23)} | " +
                        "${Tn2.toString().padStart(23)} | " +
                        "${Tn3.toString().padStart(23)} | " +
                        "${logN.toString().padStart(23)} | " +
                        nLogN.toString().padStart(23)
                )
            }

            // Print the total diff for each O(n) calculation
            println()
            println("Total diff: ")
            println("T/n:   ${aTn1.maxOrNull()?.minus(aTn1.minOrNull()!!)}")
            println("T/n^2: ${aTn2.maxOrNull()?.minus(aTn2.minOrNull()!!)}")
            println("T/n^3: ${aTn3.maxOrNull()?.minus(aTn3.minOrNull()!!)}")
            println("logN:  ${alogN.maxOrNull()?.minus(alogN.minOrNull()!!)}")
            println("nLogN: ${aNLogN.maxOrNull()?.minus(aNLogN.minOrNull()!!)}")
        }

        // Insertion sort
        @JvmStatic fun insertion(arr: Array<Int>, t1: Int = 0, t2: Int = 0) {
            // For every index in arr
            for (i in 1 until arr.size) {
                // Get the current value
                val key = arr[i]
                // Sett test value to one index lower
                var j = i - 1

                // Move the key down it the value at that position is higher
                while (j >= 0 && arr[j] > key) {
                    // Move J to the next index
                    arr[j + 1] = arr[j]
                    j -= 1
                }
                // Sett the key to the correct place
                arr[j + 1] = key
            }
        }

        // Partition utility function for quick sort
        @JvmStatic fun partion(arr: Array<Int>, low: Int, high: Int): Int {
            // Sett the pivot point
            val pivot = arr[high]
            var i = (low - 1)

            // For every j = low to j < high
            for (j in low until high) {
                // If value j is lower than pivot  swap the position else iterate the array
                if (arr[j] < pivot) {
                    i++
                    Util.swap(arr, i, j)
                }
            }

            // Do the final swap
            Util.swap(arr, i + 1, high)

            return (i + 1)
        }

        // Quick sort
        @JvmStatic fun quick(arr: Array<Int>, low: Int, high: Int) {
            // Stop to sort when low and high is the same value
            if (low < high) {
                // Set the pivot index
                val pi = partion(arr, low, high)

                // Sort each side of the pivot point
                quick(arr, low, pi - 1)
                quick(arr, pi + 1, high)
            }
        }

        // Merge utility function to merge sort function
        @JvmStatic fun m(arr: Array<Int>, left: Int, mid: Int, right:Int) {
            // Size of array 1 and 2 for iterating
            val tArr1 = mid - left + 1
            val tArr2 = right - mid

            // Left and right array
            val leftArr = Array(tArr1) {i -> arr[left + i]}
            val rightArr = Array(tArr2) {i -> arr[mid + 1 + i]}

            // Index's for the temporary arrays and the merging to the array
            var idxTArr1 = 0
            var idxTArr2 = 0
            var idxMerge = left

            // Merge the values form teh left/right array to arr
            while (idxTArr1 < tArr1 && idxTArr2 < tArr2) {
                if (leftArr[idxTArr1] <= rightArr[idxTArr2]) {
                    arr[idxMerge] = leftArr[idxTArr1]
                    idxTArr1++
                } else {
                    arr[idxMerge] = rightArr[idxTArr2]
                    idxTArr2++
                }
                idxMerge++
            }

            // Merge the rest of the values
            while (idxTArr1 < tArr1) {
                arr[idxMerge] = leftArr[idxTArr1]
                idxTArr1++
                idxMerge++
            }
            while (idxTArr2 < tArr2) {
                arr[idxMerge] = rightArr[idxTArr2]
                idxTArr2++
                idxMerge++
            }
        }

        // Merge sort
        @JvmStatic fun merge(arr: Array<Int>, start: Int, end: Int) {
            // Return if not needed to split
            if (start >= end)
                return

            // Calculate the middle value
            val mid = start + (end - start) / 2
            // Run merge sort 2 more times on each side of the mid-point
            merge(arr, start, mid)
            merge(arr, mid + 1, end)

            // merge the arrays created
            m(arr, start, mid, end)
        }

        // Radix sort
        @JvmStatic fun radix(arr: Array<Int>, t1: Int = 0, t2: Int = 0) {
            // Get the max value form the array, if it is null then return
            val max = arr.maxOrNull() ?: return

            // Main sorting loop
            var exp = 1

            // While there is number left to sort
            while ((max / exp) > 0) {
                // Initiate the arrays to 0 for the sorting to happen in
                val arrOut = Array(arr.size) { 0}
                val count = Array(10) { 0}

                // For every index in arr add 1 to the count of that number
                for (i in arr.indices)
                    count[(arr[i] / exp) % 10]++

                // Sett up for the next face of sorting
                for (i in 1 until 10)
                    count[i] += count[i - 1]

                // Take the index generated in the step above and add that value to the out index
                for (i in arr.size-1 downTo 0) {
                    arrOut[count[(arr[i] / exp) % 10] - 1] = arr[i]
                    count[(arr[i] / exp) % 10]--
                }

                // For every index in arr add the value to arr again
                for (i in arr.indices) {
                    arr[i] = arrOut[i]
                }

                // Increase the exponent by 10 to get the next number
                exp *= 10
            }
        }
    }
}

// Main function
fun sortingMain() {
    // Set the amount of number to short
    print("Numbers in the array: ")
    var n = readln().toInt()

    // Initiate the array with random values
    n *= 2
    val arr = Array(n/2) {(0..n).random()}

    // Select the sorting algorithm
    println("Select sorting type:")
    println("1 -> insertion sort")
    println("2 -> quick sort")
    println("3 -> merge sort")
    println("4 -> radix sort")
    print(": ")
    var sortType = readln().lowercase(Locale.getDefault())

    // Format the input the correct format
    when (sortType) {
        "insertion", "1", "i" -> sortType = "i"
        "quick", "2", "q" -> sortType = "q"
        "merge", "3", "m" -> sortType = "m"
        "radix", "4", "r" -> sortType = "r"
        else -> println("Try again")
    }

    // Select what test to run
    println("Select test:")
    println("1 -> Real run")
    println("2 -> Estimate")
    print(": ")
    val testType = readln().toInt()

    // Switch between running the sorting and estimating the sotring
    if (testType == 1) {
        when (sortType) {
            "i" -> println(Sorting.run(Sorting.insertion(arr)))
            "q" -> println(Sorting.run(Sorting.quick(arr, 0, arr.size - 1)))
            "m" -> println(Sorting.run(Sorting.merge(arr, 0, arr.size - 1)))
            "r" -> println(Sorting.run(Sorting.radix(arr)))
        }

        if (Util.isSorted(arr))
            println("Is sorted")
        else
            println("Is not sorted")

    } else if (testType == 2) {
        when (sortType) {
            "i" -> Sorting.estimate(Sorting::insertion)
            "q" -> Sorting.estimate(Sorting::quick)
            "m" -> Sorting.estimate(Sorting::merge)
            "r" -> Sorting.estimate(Sorting::radix)
        }
    }
}