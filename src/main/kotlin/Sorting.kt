class Util {
    companion object {
        @JvmStatic fun swap(arr: Array<Int>, i: Int, j:Int) {
            val tmp = arr[i]
            arr[i] = arr[j]
            arr[j] = tmp
        }
    }
}

class Sorting {
    companion object {
        @JvmStatic fun insertion(arr: Array<Int>) {
            for (i in 1 until arr.size) {
                val key = arr[i]
                var j = i - 1

                while (j >= 0 && arr[j] > key) {
                    arr[j + 1] = arr[j]
                    j -= 1
                }
                arr[j + 1] = key
            }
        }

        @JvmStatic fun quick(arr: Array<Int>, low: Int, high: Int) {
            if (low < high) {
                val pi = partion(arr, low, high)
                quick(arr, low, pi - 1)
                quick(arr, pi + 1, high)
            }
        }

        @JvmStatic fun partion(arr: Array<Int>, low: Int, high: Int): Int {
            val pivot = arr[high]
            var i = (low - 1)

            for (j in low until high) {
                if (arr[j] < pivot) {
                    i++
                    Util.swap(arr, i, j)
                }
            }
            Util.swap(arr, i + 1, high)

            return (i + 1)
        }

        @JvmStatic fun m(arr: Array<Int>, left: Int, mid: Int, right:Int) {
            val tArr1 = mid - left + 1
            val tArr2 = right - mid

            val leftArr = Array(tArr1) {i -> arr[left + i]}
            val rightArr = Array(tArr2) {i -> arr[mid + 1 + i]}

            var idxTArr1 = 0
            var idxTArr2 = 0
            var idxMerge = left

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

        @JvmStatic fun merge(arr: Array<Int>, start: Int, end: Int) {
            if (start >= end) {
                return
            }

            val mid = start + (end - start) / 2
            merge(arr, start, mid)
            merge(arr, mid + 1, end)
            m(arr, start, mid, end)
        }

        @JvmStatic fun radix(arr: Array<Int>) {
            val max = arr.maxOrNull() ?: return

            var exp = 1
            while ((max / exp) > 0) {
                val arrOut = Array(arr.size) { 0}
                val count = Array(10) { 0}

                for (i in arr.indices)
                    count[(arr[i] / exp) % 10]++

                for (i in 1 until 10)
                    count[i] += count[i - 1]

                for (i in arr.size-1 downTo 0) {
                    arrOut[count[(arr[i] / exp) % 10] - 1] = arr[i]
                    count[(arr[i] / exp) % 10]--
                }

                for (i in arr.indices) {
                    arr[i] = arrOut[i]
                }

                exp *= 10
            }
        }
    }
}

fun sortingMain() {
    val arr = arrayOf(1, 5, 3, 6, 2, 8)

    arr.shuffle()
    Sorting.insertion(arr)
    arr.forEach { i -> print("$i, ") }

    println()

    arr.shuffle()
    Sorting.quick(arr, 0, arr.size - 1)
    arr.forEach { i -> print("$i, ") }

    println()

    arr.shuffle()
    Sorting.merge(arr, 0, arr.size - 1)
    arr.forEach { i -> print("$i, ") }

    println()

    arr.shuffle()
    Sorting.radix(arr)
    arr.forEach { i -> print("$i, ") }

}