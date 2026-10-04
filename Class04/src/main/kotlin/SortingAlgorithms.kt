/**
 * Heap sort (max heap)
 */
fun heapSort(values: List<Int>): List<Int> {
    val result = values.toMutableList()

    // Rearrange the list into a max heap.
    for (parent in result.size / 2 - 1 downTo 0) {
        moveDown(result, parent, result.size)
    }

    // Move the maximum to the end, then repair the smaller heap.
    for (end in result.lastIndex downTo 1) {
        val temporary = result[0]
        result[0] = result[end]
        result[end] = temporary
        moveDown(result, 0, end)
    }

    return result
}

private fun moveDown(heap: MutableList<Int>, start: Int, heapSize: Int) {
    var parent = start

    while (true) {
        val leftChild = parent * 2 + 1
        if (leftChild >= heapSize) return

        val rightChild = leftChild + 1
        val largerChild =
            if (rightChild < heapSize && heap[rightChild] > heap[leftChild]) {
                rightChild
            } else {
                leftChild
            }

        if (heap[parent] >= heap[largerChild]) return

        val temporary = heap[parent]
        heap[parent] = heap[largerChild]
        heap[largerChild] = temporary
        parent = largerChild
    }
}

/**
 * Selection sort
 */
fun selectionSort(values: List<Int>): List<Int> {
    val result = values.toMutableList()

    for (start in 0 until result.lastIndex) {
        var smallest = start
        for (index in start + 1 until result.size) {
            if (result[index] < result[smallest]) smallest = index
        }

        if (smallest != start) {
            val temporary = result[start]
            result[start] = result[smallest]
            result[smallest] = temporary
        }
    }

    return result
}

/**
 * Merge sort
 */
fun mergeSort(values: List<Int>): List<Int> {
    if (values.size <= 1) return values.toList()

    val middle = values.size / 2
    val left = mergeSort(values.subList(0, middle))
    val right = mergeSort(values.subList(middle, values.size))
    return merge(left, right)
}

private fun merge(left: List<Int>, right: List<Int>): List<Int> {
    val result = ArrayList<Int>(left.size + right.size)
    var leftIndex = 0
    var rightIndex = 0

    while (leftIndex < left.size && rightIndex < right.size) {
        if (left[leftIndex] <= right[rightIndex]) {
            result.add(left[leftIndex])
            leftIndex++
        } else {
            result.add(right[rightIndex])
            rightIndex++
        }
    }

    while (leftIndex < left.size) result.add(left[leftIndex++])
    while (rightIndex < right.size) result.add(right[rightIndex++])
    return result
}

/**
 * Quick sort (middle element as the pivot)
 */
fun quickSort(values: List<Int>): List<Int> {
    val result = values.toMutableList()
    quickSortRange(result, 0, result.lastIndex)
    return result
}

private fun quickSortRange(values: MutableList<Int>, low: Int, high: Int) {
    if (low >= high) return

    val pivot = values[(low + high) / 2]
    var left = low
    var right = high

    while (left <= right) {
        while (values[left] < pivot) left++
        while (values[right] > pivot) right--

        if (left <= right) {
            val temporary = values[left]
            values[left] = values[right]
            values[right] = temporary
            left++
            right--
        }
    }

    if (low < right) quickSortRange(values, low, right)
    if (left < high) quickSortRange(values, left, high)
}
