/**
 * Interface VS class
 * - interface is a behavior requirement, an ADT (abstract data type)
 *      - it says: any class (e.g. DoublyLinkedList) that claims to be this (LinkedList) must provide these functions
 * - class is a concrete data type we define, that implements the mechanism for ADT
 *      - DoublyLinkedList<>: LinkedList<> {...} means that this datatype implements the LinkedList interface

 * Why have the interface?
 * - In the stack example, we could later create another implementation:
 *      class ArrayStack<T> : Stack<T> {
 *         // Implements the stack using an array
 *      }
 * - Now both classes satisfy the same interface:
 *      val stack1: Stack<String> = LinkedListStack<String>()
 *      val stack2: Stack<String> = ArrayStack<String>()
 * - Code that only needs normal stack operations can work with either one:
 *      fun addMessage(stack: Stack<String>) {
 *          stack.push("hello")
 *      }
 */

interface LinkedList<T> {
    /**
     * Adds the element [data] to the front of the linked list.
     */
    fun pushFront(data: T)

    /**
     * Adds the element [data] to the back of the linked list.
     */
    fun pushBack(data: T)

    /**
     * Removes an element from the front of the list. If the list is empty, it is unchanged.
     * @return the value at the front of the list or nil if none exists
     */
    fun popFront(): T?

    /**
     * Removes an element from the back of the list. If the list is empty, it is unchanged.
     * @return the value at the back of the list or nil if none exists
     */
    fun popBack(): T?

    /**
     * @return the value at the front of the list or nil if none exists
     */
    fun peekFront(): T?

    /**
     * @return the value at the back of the list or nil if none exists
     */
    fun peekBack(): T?

    /**
     * @return true if the list is empty and false otherwise
     */
    fun isEmpty(): Boolean
}

class DoublyLinkedList<T> : LinkedList<T> {
    private class Node<T>(
        val data: T,
        var previous: Node<T>? = null,
        var next: Node<T>? = null,
    )

    private var head: Node<T>? = null
    private var tail: Node<T>? = null

    override fun pushFront(data: T) {
        val newNode = Node(data, next = head)
        head?.previous = newNode // do so if head != null
        head = newNode
        if (tail == null) {
            tail = newNode
        } // edge case: original list is empty
    }

    override fun pushBack(data: T) {
        val newNode = Node(data, previous = tail)
        tail?.next = newNode
        tail = newNode
        if (head == null) {
            head = newNode
        }
    }

    override fun popFront(): T? {
        val oldHead = head ?: return null // If head exists, store it in oldHead; if head is null, return null.
        head = oldHead.next
        head?.previous = null
        if (head == null) {
            tail = null
        }
        return oldHead.data
    }

    override fun popBack(): T? {
        val oldTail = tail ?: return null // first check if it's empty initially
        tail = oldTail.previous
        tail?.next = null // have to make sure it's not empty after removing the element
        if (tail == null) {
            head = null
        }
        return oldTail.data

//        // alternative (full, longer)
//        if (tail == null) {
//            return null
//        }
//
//        val oldTail = tail
//        tail = oldTail.previous
//        if (tail == null) {
//            head = null
//        } else {
//            tail.next = null
//        } // this logic is correct but won't compile, because Kotlin doesn't read if/else null check, so it thinks tail could still be null and won't compile
//
//        return oldTail.data
    }

    override fun peekFront(): T? = head?.data

    override fun peekBack(): T? = tail?.data

    override fun isEmpty(): Boolean = head == null
}

interface Stack<T> {
    /**
     * Add [data] to the top of the stack
     */
    fun push(data: T)
    /**
     * Remove the element at the top of the stack.  If the stack is empty, it remains unchanged.
     * @return the value at the top of the stack or nil if none exists
     */
    fun pop(): T?
    /**
     * @return the value on the top of the stack or nil if none exists
     */
    fun peek(): T?
    /**
     * @return true if the stack is empty and false otherwise
     */
    fun isEmpty(): Boolean
}

class LinkedListStack<T> : Stack<T> {
    private val doublyLinkedList = DoublyLinkedList<T>()

    override fun push(data: T) = doublyLinkedList.pushFront(data)

    override fun pop(): T? = doublyLinkedList.popFront()

    override fun peek(): T? = doublyLinkedList.peekFront()

    override fun isEmpty(): Boolean = doublyLinkedList.isEmpty()
}

interface Queue<T> {
    /**
     * Add [data] to the end of the queue.
     */
    fun enqueue(data: T)
    /**
     * Remove the element at the front of the queue.  If the queue is empty, it remains unchanged.
     * @return the value at the front of the queue or nil if none exists
     */
    fun dequeue(): T?
    /**
     * @return the value at the front of the queue or nil if none exists
     */
    fun peek(): T?
    /**
     * @return true if the queue is empty and false otherwise
     */
    fun isEmpty(): Boolean
}

class LinkedListQueue<T> : Queue<T> {
    private val doublyLinkedList = DoublyLinkedList<T>()

    override fun enqueue(data: T) = doublyLinkedList.pushBack(data)

    override fun dequeue(): T? = doublyLinkedList.popFront()

    override fun peek(): T? = doublyLinkedList.peekFront()

    override fun isEmpty(): Boolean = doublyLinkedList.isEmpty()
}

// test script
fun main() {
    val stack = LinkedListStack<String>()
    stack.push("first")
    stack.push("second")

    println(stack.pop()) // removes "second"
    println(stack.pop())

    val queue = LinkedListQueue<String>()
    queue.enqueue("first")
    queue.enqueue("second")

    println(queue.dequeue()) // removes "first"
    println(queue.dequeue())
}
