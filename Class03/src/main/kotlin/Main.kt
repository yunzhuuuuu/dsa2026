/**
 * A directed, weighted graph.
 */
interface Graph<VertexType> {
    /** Return all vertices currently in the graph. */
    fun getVertices(): Set<VertexType>

    /** Add a directed edge from [from] to [to] with weight [cost]. */
    fun addEdge(from: VertexType, to: VertexType, cost: Double)

    /** Return every outgoing edge from [from] and its weight. */
    fun getEdges(from: VertexType): Map<VertexType, Double>

    /** Remove every edge and vertex from the graph. */
    fun clear()
}

/**
 * A graph stored as an adjacency list.
 */
class AdjacencyListGraph<VertexType> : Graph<VertexType> {
    private val adjacencyList = mutableMapOf<VertexType, MutableMap<VertexType, Double>>()

    override fun getVertices(): Set<VertexType> = adjacencyList.keys.toSet()

    override fun addEdge(from: VertexType, to: VertexType, cost: Double) {
        adjacencyList.getOrPut(from) { mutableMapOf() }[to] = cost
        adjacencyList.getOrPut(to) { mutableMapOf() }
    }

    override fun getEdges(from: VertexType): Map<VertexType, Double> =
        adjacencyList[from]?.toMap() ?: emptyMap()

    override fun clear() {
        adjacencyList.clear()
    }
}

/**
 * ``MinPriorityQueue`` maintains a priority queue where the lower
 *  the priority value, the sooner the element will be removed from
 *  the queue.
 *  @param T the representation of the items in the queue
 */
interface MinPriorityQueue<T> {
    /**
     * @return true if the queue is empty, false otherwise
     */
    fun isEmpty(): Boolean

    /**
     * Add [elem] with at level [priority]
     */
    fun addWithPriority(elem: T, priority: Double)

    /**
     * Get the next (highest priority) element and remove this element from the queue.
     * @return the next element in terms of priority.  If empty, return null.
     */
    fun next(): T?

    /**
     * Adjust the priority of the given element
     * @param elem whose priority should change
     * @param newPriority the priority to use for the element
     *   the lower the priority the earlier the element int
     *   the order.
     */
    fun adjustPriority(elem: T, newPriority: Double)
}

/**
 * Array-based binary min-heap.
 *
 * The index map lets us locate an element in O(1) average time when its
 * priority needs to change. Elements therefore need to be unique in the heap.
 */
class MinHeap<T> {
    private data class Entry<T>(val element: T, var priority: Double)

    private val entries = mutableListOf<Entry<T>>()
    private val indices = mutableMapOf<T, Int>()

    fun isEmpty(): Boolean = entries.isEmpty()

    /**
     * Add an element if it is not already in the heap.
     *
     * @return true when the element was added and false when it was a duplicate
     */
    fun insert(element: T, priority: Double): Boolean {
        if (element in indices) return false

        entries.add(Entry(element, priority))
        val newIndex = entries.lastIndex
        indices[element] = newIndex
        moveUp(newIndex)
        return true
    }

    /** Remove and return the element with the smallest priority. */
    fun removeMin(): T? {
        if (entries.isEmpty()) return null

        val minimum = entries[0].element
        swap(0, entries.lastIndex)
        entries.removeAt(entries.lastIndex)
        indices.remove(minimum)

        if (entries.isNotEmpty()) moveDown(0)
        return minimum
    }

    /** Change an existing element's priority. Missing elements are ignored. */
    fun adjustPriority(element: T, newPriority: Double) {
        val index = indices[element] ?: return
        val oldPriority = entries[index].priority
        entries[index].priority = newPriority

        if (newPriority < oldPriority) {
            moveUp(index)
        } else if (newPriority > oldPriority) {
            moveDown(index)
        }
    }

    private fun moveUp(startIndex: Int) {
        var child = startIndex

        while (child > 0) {
            val parent = (child - 1) / 2
            if (entries[parent].priority <= entries[child].priority) return
            swap(parent, child)
            child = parent
        }
    }

    private fun moveDown(startIndex: Int) {
        var parent = startIndex

        while (true) {
            val leftChild = parent * 2 + 1
            val rightChild = parent * 2 + 2
            if (leftChild >= entries.size) return

            val smallerChild =
                if (rightChild < entries.size &&
                    entries[rightChild].priority < entries[leftChild].priority
                ) {
                    rightChild
                } else {
                    leftChild
                }

            if (entries[parent].priority <= entries[smallerChild].priority) return
            swap(parent, smallerChild)
            parent = smallerChild
        }
    }

    private fun swap(firstIndex: Int, secondIndex: Int) {
        if (firstIndex == secondIndex) return

        val temporary = entries[firstIndex]
        entries[firstIndex] = entries[secondIndex]
        entries[secondIndex] = temporary

        indices[entries[firstIndex].element] = firstIndex
        indices[entries[secondIndex].element] = secondIndex
    }
}

/** A thin priority-queue wrapper around [MinHeap]. */
class PriorityQueue<T> : MinPriorityQueue<T> {
    private val heap = MinHeap<T>()

    override fun isEmpty(): Boolean = heap.isEmpty()

    override fun addWithPriority(elem: T, priority: Double) {
        heap.insert(elem, priority)
    }

    override fun next(): T? = heap.removeMin()

    override fun adjustPriority(elem: T, newPriority: Double) {
        heap.adjustPriority(elem, newPriority)
    }
}


/**
 * Searching + Problem 3 (Graph with costs)
 * Find the shortest path from [start] to [target] with Dijkstra's algorithm.
 */
fun <VertexType> dijkstra(
    graph: Graph<VertexType>,
    start: VertexType,
    target: VertexType,
): List<VertexType>? {
    val vertices = graph.getVertices()
    if (start !in vertices || target !in vertices) return null

    // Dijkstra's algorithm only works when every edge has a nonnegative cost.
    for (vertex in vertices) {
        for (cost in graph.getEdges(vertex).values) {
            require(cost.isFinite() && cost >= 0.0) {
                "Dijkstra's algorithm requires finite, nonnegative edge costs."
            }
        }
    }

    val distances = mutableMapOf<VertexType, Double>()
    val previous = mutableMapOf<VertexType, VertexType>()
    val visited = mutableSetOf<VertexType>()
    val inQueue = mutableSetOf<VertexType>()
    val queue = PriorityQueue<VertexType>()

    distances[start] = 0.0
    queue.addWithPriority(start, 0.0)
    inQueue.add(start)

    while (!queue.isEmpty()) {
        val current = queue.next() ?: break
        inQueue.remove(current)

        // The first time a vertex leaves the min-priority queue, its shortest
        // distance is final.
        if (!visited.add(current)) continue
        if (current == target) {
            return generateSequence(target) { vertex -> previous[vertex] }
                .toList()
                .asReversed()
        }

        val currentDistance = distances.getValue(current)
        for ((neighbor, edgeCost) in graph.getEdges(current)) {
            if (neighbor in visited) continue

            val newDistance = currentDistance + edgeCost
            val oldDistance = distances[neighbor] ?: Double.POSITIVE_INFINITY

            if (newDistance < oldDistance) {
                distances[neighbor] = newDistance
                previous[neighbor] = current

                if (neighbor in inQueue) {
                    queue.adjustPriority(neighbor, newDistance)
                } else {
                    queue.addWithPriority(neighbor, newDistance)
                    inQueue.add(neighbor)
                }
            }
        }
    }

    return null
}

/** Add the same weighted connection in both directions. */
private fun Graph<String>.addTwoWayEdge(first: String, second: String, cost: Double) {
    addEdge(first, second, cost)
    addEdge(second, first, cost)
}

/** Add the edge costs along an already constructed path. */
private fun <VertexType> pathCost(graph: Graph<VertexType>, path: List<VertexType>): Double =
    path.zipWithNext().sumOf { (from, to) ->
        graph.getEdges(from)[to]
            ?: error("The supplied path contains an edge that is not in the graph.")
    }

fun main() {
    //These example costs are only demonstration weights.
    val cities = AdjacencyListGraph<String>()
    cities.addTwoWayEdge("Boston", "Worcester", 47.0)
    cities.addTwoWayEdge("Boston", "Providence", 50.0)
    cities.addTwoWayEdge("Worcester", "Springfield", 54.0)
    cities.addTwoWayEdge("Worcester", "Hartford", 64.0)
    cities.addTwoWayEdge("Providence", "Hartford", 73.0)
    cities.addTwoWayEdge("Springfield", "Albany", 87.0)
    cities.addTwoWayEdge("Hartford", "Albany", 98.0)

    val route = dijkstra(cities, "Boston", "Albany")
    if (route == null) {
        println("There is no route from Boston to Albany.")
    } else {
        println("Shortest route: ${route.joinToString(" -> ")}")
        println("Total cost: ${pathCost(cities, route)}")
    }
}
