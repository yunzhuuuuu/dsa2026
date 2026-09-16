import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DoublyLinkedListTest {
    @Test
    fun `new list is empty`() {
        val list = DoublyLinkedList<Int>()

        assertTrue(list.isEmpty())
        assertNull(list.peekFront())
        assertNull(list.peekBack())
    }

    @Test
    fun `push front adds values to the front`() {
        val list = DoublyLinkedList<Int>()

        list.pushFront(1)
        list.pushFront(2)

        assertFalse(list.isEmpty())
        assertEquals(2, list.peekFront())
        assertEquals(1, list.peekBack())
    }

    @Test
    fun `push back adds values to the back`() {
        val list = DoublyLinkedList<String>()

        list.pushBack("first")
        list.pushBack("second")

        assertEquals("first", list.peekFront())
        assertEquals("second", list.peekBack())
    }

    @Test
    fun `pop front removes values from the front`() {
        val list = DoublyLinkedList<Int>()
        list.pushBack(1)
        list.pushBack(2)

        assertEquals(1, list.popFront())
        assertEquals(2, list.popFront())
        assertNull(list.popFront())
        assertTrue(list.isEmpty())
    }

    @Test
    fun `pop back removes values from the back`() {
        val list = DoublyLinkedList<Int>()
        list.pushFront(1)
        list.pushFront(2)

        assertEquals(1, list.popBack())
        assertEquals(2, list.popBack())
        assertNull(list.popBack())
        assertTrue(list.isEmpty())
    }

    @Test
    fun `list can be reused after becoming empty`() {
        val list = DoublyLinkedList<Int>()
        list.pushBack(1)
        assertEquals(1, list.popFront())

        list.pushFront(2)

        assertEquals(2, list.peekFront())
        assertEquals(2, list.peekBack())
    }
}

class LinkedListStackTest {
    @Test
    fun `new stack is empty`() {
        val stack = LinkedListStack<Int>()

        assertTrue(stack.isEmpty())
        assertNull(stack.peek())
        assertNull(stack.pop())
    }

    @Test
    fun `push places an item on top`() {
        val stack = LinkedListStack<String>()

        stack.push("bottom")
        stack.push("top")

        assertFalse(stack.isEmpty())
        assertEquals("top", stack.peek())
    }

    @Test
    fun `pop removes items in last in first out order`() {
        val stack = LinkedListStack<Int>()
        stack.push(1)
        stack.push(2)
        stack.push(3)

        assertEquals(3, stack.pop())
        assertEquals(2, stack.pop())
        assertEquals(1, stack.pop())
        assertNull(stack.pop())
        assertTrue(stack.isEmpty())
    }

    @Test
    fun `peek does not remove the top item`() {
        val stack = LinkedListStack<Int>()
        stack.push(7)

        assertEquals(7, stack.peek())
        assertEquals(7, stack.peek())
        assertFalse(stack.isEmpty())
    }
}

class LinkedListQueueTest {
    @Test
    fun `new queue is empty`() {
        val queue = LinkedListQueue<Int>()

        assertTrue(queue.isEmpty())
        assertNull(queue.peek())
        assertNull(queue.dequeue())
    }

    @Test
    fun `queue removes items in first in first out order`() {
        val queue = LinkedListQueue<String>()
        queue.enqueue("first")
        queue.enqueue("second")

        assertFalse(queue.isEmpty())
        assertEquals("first", queue.dequeue())
        assertEquals("second", queue.dequeue())
        assertNull(queue.dequeue())
        assertTrue(queue.isEmpty())
    }

    @Test
    fun `peek does not remove the front item`() {
        val queue = LinkedListQueue<Int>()
        queue.enqueue(7)

        assertEquals(7, queue.peek())
        assertEquals(7, queue.peek())
        assertFalse(queue.isEmpty())
    }
}
