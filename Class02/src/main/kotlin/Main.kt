/**
 * Notes
 *
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
     * @return the value at the front of the list or `null` if none exists
     */
    fun popFront(): T?

    /**
     * Removes an element from the back of the list. If the list is empty, it is unchanged.
     * @return the value at the back of the list or `null` if none exists
     */
    fun popBack(): T?

    /**
     * @return the value at the front of the list or `null` if none exists
     */
    fun peekFront(): T?

    /**
     * @return the value at the back of the list or `null` if none exists
     */
    fun peekBack(): T?

    /**
     * @return true if the list is empty and false otherwise
     */
    fun isEmpty(): Boolean
}

/** A doubly linked implementation of [LinkedList]. */
class DoublyLinkedList<T> : LinkedList<T> {
    private class Node<T>(
        val data: T,
        var previous: Node<T>? = null,
        var next: Node<T>? = null,
    )

    private var head: Node<T>? = null
    private var tail: Node<T>? = null

    /** Adds [data] to the front of the list. */
    override fun pushFront(data: T) {
        val newNode = Node(data, next = head)
        head?.previous = newNode // do so if head != null
        head = newNode
        if (tail == null) {
            tail = newNode
        } // edge case: original list is empty
    }

    /** Adds [data] to the back of the list. */
    override fun pushBack(data: T) {
        val newNode = Node(data, previous = tail)
        tail?.next = newNode
        tail = newNode
        if (head == null) {
            head = newNode
        }
    }

    /** Removes and returns the front value, or `null` if empty. */
    override fun popFront(): T? {
        val oldHead = head ?: return null // If head exists, store it in oldHead; if head is null, return null.
        head = oldHead.next
        head?.previous = null
        if (head == null) {
            tail = null
        }
        return oldHead.data
    }

    /** Removes and returns the back value, or `null` if empty. */
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

    /** Returns the front value without removing it. */
    override fun peekFront(): T? = head?.data

    /** Returns the back value without removing it. */
    override fun peekBack(): T? = tail?.data

    /** Returns whether the list is empty. */
    override fun isEmpty(): Boolean = head == null
}

/**
 * Exercise 1 -- Stack
 */

interface Stack<T> {
    /**
     * Add [data] to the top of the stack
     */
    fun push(data: T)
    /**
     * Remove the element at the top of the stack.  If the stack is empty, it remains unchanged.
     * @return the value at the top of the stack or `null` if none exists
     */
    fun pop(): T?
    /**
     * @return the value on the top of the stack or `null` if none exists
     */
    fun peek(): T?
    /**
     * @return true if the stack is empty and false otherwise
     */
    fun isEmpty(): Boolean
}

/** A stack backed by a [DoublyLinkedList]. */
class LinkedListStack<T> : Stack<T> {
    private val doublyLinkedList = DoublyLinkedList<T>()

    /** Adds [data] to the top of the stack. */
    override fun push(data: T) = doublyLinkedList.pushFront(data)

    /** Removes and returns the top value, or `null` if empty. */
    override fun pop(): T? = doublyLinkedList.popFront()

    /** Returns the top value without removing it. */
    override fun peek(): T? = doublyLinkedList.peekFront()

    /** Returns whether the stack is empty. */
    override fun isEmpty(): Boolean = doublyLinkedList.isEmpty()
}

/**
 * Exercise 2 -- Queue
 */

interface Queue<T> {
    /**
     * Add [data] to the end of the queue.
     */
    fun enqueue(data: T)
    /**
     * Remove the element at the front of the queue.  If the queue is empty, it remains unchanged.
     * @return the value at the front of the queue or `null` if none exists
     */
    fun dequeue(): T?
    /**
     * @return the value at the front of the queue or `null` if none exists
     */
    fun peek(): T?
    /**
     * @return true if the queue is empty and false otherwise
     */
    fun isEmpty(): Boolean
}

/** A queue backed by a [DoublyLinkedList]. */
class LinkedListQueue<T> : Queue<T> {
    private val doublyLinkedList = DoublyLinkedList<T>()

    /** Adds [data] to the back of the queue. */
    override fun enqueue(data: T) = doublyLinkedList.pushBack(data)

    /** Removes and returns the front value, or `null` if empty. */
    override fun dequeue(): T? = doublyLinkedList.popFront()

    /** Returns the front value without removing it. */
    override fun peek(): T? = doublyLinkedList.peekFront()

    /** Returns whether the queue is empty. */
    override fun isEmpty(): Boolean = doublyLinkedList.isEmpty()
}

/** Runs a small test */
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

/**
 * Exercise 3 -- reverse a stack
 */
//The following is so wrong, many syntax errors and didn't use previous code. But should be right for python.
//fun reverseStack(stack: LinkedListStack<T>): reversedStack {
//    var currentNode? = stack.tail?: return null
//    var nextNode? = currentNode?.previous?: return stack
//    var previousNode? = null
//    stack.head = currentNode
//    while (nextNode!= null) {
//        currentNode.next = nextNode
//        nextNode = currentNode.next.previous
//        currentNode.previous = previousNode
//        previousNode = currentNode
//        currentNode = currentNode.next
//    }
//    currentNode.next = null
//    currentNode.previous = previousNode
//    stack.tail = currentNode
//    return stack
//}

//The following is correct but returns a new list instead of changing the original one
//fun <T: Any> reverseStack(stack: LinkedListStack<T>): LinkedListStack<T> {
//    val reversedStack = LinkedListStack<T>()
//    while (!stack.isEmpty()){
//        var node = stack.pop() // could also use val here
//        if (node!= null) { // have to do this for Kotlin compile
//            reversedStack.push(node)
//        }
//    }
//    return reversedStack
//}

// The following is fully correct
/** Reverses [stack] in place and returns it. */
fun <T: Any> reverseStack(stack: LinkedListStack<T>): LinkedListStack<T> {
    val tempQueue = LinkedListQueue<T>()
    while (!stack.isEmpty()) {
        val node = stack.pop()
        if (node != null) {
            tempQueue.enqueue(node)
        }
    }

    while(!tempQueue.isEmpty()){
        val node = tempQueue.dequeue()
        if (node != null) {
            stack.push(node)
        }
    }
    return stack
}

/**
 * Exercise 4 -- Valid Parentheses
 * Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.
 */
fun validParentheses(s: String): Boolean {
    if (s.isEmpty()) {
        return true
    }

    val leftParentheses = listOf('(', '[', '{')
    /** Returns whether [char1] and [char2] form a matching pair. */
    fun isMatch(char1: Char?, char2: Char?): Boolean {
        val pair = setOf(char1, char2)
        return pair == setOf('(', ')') || pair == setOf('[', ']') || pair == setOf('{', '}')
    }

    // turn the input string into a linked list of characters
    val chars = DoublyLinkedList<Char>()
    for (char in s) {
        chars.pushBack(char)
    }

    val tempStack = LinkedListStack<Char>()
    while (!chars.isEmpty()) {
        val currentChar = chars.popFront() ?: return false
        if (currentChar in leftParentheses) { // if it's a left parentheses, add to stack
            tempStack.push(currentChar)
        } else { // if it's a right parentheses, check against the newest item in the stack which should be its pair
            if (!isMatch(tempStack.pop(), currentChar)) {
                return false
            }
        }
    }
    return(tempStack.isEmpty())
}

/**
 * Exercise 5 -- copy stack
 * Given a stack return a copy of the original stack (i.e., a new stack with the same values as the original, stored in the same order as the original). Your method should create the new stack and fill it up with the same values that are stored in the original stack.
 * You may use one queue as auxiliary storage.
 */
// This is too similar to exercise 3, so i'm not doing it
// Key idea:
//- copy each element from original stack to a queue
//- move elements from queue to the new stack, now we get a reversed new stack
//- copy the new stack to the queue, get a reversed queue
//- copy the queue to both stacks. it's now reversed back to original order
//- eventually we get unchanged original stack, a copy of it, and a empty queue
