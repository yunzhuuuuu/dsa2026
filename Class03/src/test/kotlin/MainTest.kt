import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MainTest {
    @Test
    fun graphWorks() {
        val graph = AdjacencyListGraph<String>()
        graph.addEdge("A", "B", 3.5)

        assertEquals(setOf("A", "B"), graph.getVertices())
        assertEquals(mapOf("B" to 3.5), graph.getEdges("A"))
        assertTrue(graph.getEdges("B").isEmpty())

        graph.clear()
        assertTrue(graph.getVertices().isEmpty())
    }

    @Test
    fun queueOrder() {
        val queue = PriorityQueue<String>()
        queue.addWithPriority("low", 8.0)
        queue.addWithPriority("high", 2.0)
        queue.addWithPriority("middle", 5.0)

        assertEquals("high", queue.next())
        assertEquals("middle", queue.next())
        assertEquals("low", queue.next())
        assertNull(queue.next())
    }

    @Test
    fun priorityChanges() {
        val queue = PriorityQueue<String>()
        queue.addWithPriority("A", 1.0)
        queue.addWithPriority("B", 2.0)
        queue.addWithPriority("C", 3.0)

        queue.adjustPriority("A", 10.0)
        queue.adjustPriority("C", 0.5)

        assertEquals("C", queue.next())
        assertEquals("B", queue.next())
        assertEquals("A", queue.next())
    }

    @Test
    fun shortestPath() {
        val graph = AdjacencyListGraph<String>()
        graph.addEdge("A", "B", 4.0)
        graph.addEdge("A", "C", 2.0)
        graph.addEdge("C", "B", 1.0)
        graph.addEdge("B", "D", 2.0)
        graph.addEdge("C", "D", 8.0)

        assertEquals(listOf("A", "C", "B", "D"), dijkstra(graph, "A", "D"))
        assertEquals(listOf("A"), dijkstra(graph, "A", "A"))
    }

    @Test
    fun noPath() {
        val graph = AdjacencyListGraph<String>()
        graph.addEdge("A", "B", 1.0)
        graph.addEdge("C", "D", 1.0)

        assertNull(dijkstra(graph, "A", "D"))
        assertNull(dijkstra(graph, "missing", "A"))
    }

    @Test
    fun negativeEdge() {
        val graph = AdjacencyListGraph<String>()
        graph.addEdge("A", "B", -1.0)

        assertFailsWith<IllegalArgumentException> {
            dijkstra(graph, "A", "B")
        }
    }
}
