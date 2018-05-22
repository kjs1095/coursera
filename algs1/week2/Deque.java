/*----------------------------------------------------------------
 *  Author:        Jyun-Sheng Kao
 *  Written:       5/19/2018
 *  Last updated:  5/22/2018
 *
 *  Compilation:   javac-coursera Deque.java
 *  Execution:     java-coursera Deque < input.txt
 *  Dependencies:  StdIn.java StdOut.java
 *
 *  % more input.txt
 *  <+ A <+ B >+ C >+ D <- >- <+ E
 *
 *  % java-coursera Deque < input.txt
 *  D A (3 left on deque
 *
 *----------------------------------------------------------------*/

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private int n;      // number of elements on deque
    private Node head;  // head pointer of this deque
    private Node tail;  // tail pointer of this deque

    // helper linked list class
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    /**
     * Initializes an empty deque.
     */
    public Deque() {
        head = null;
        tail = null;
        n = 0;
    }

    /**
     * Is this deque empty?
     *
     * @return true if this deque is empty; false otherwise
     */
    public boolean isEmpty() {
        return head == null || tail == null;
    }

    /**
     * Returns the number of items in the deque.
     * 
     * @return the number of items in the deque
     */
    public int size() {
        return n;
    }

    /**
     * Adds the item to the front of this deque.
     * 
     * @param item the item to add
     * @throws java.lang.IllegalArgumentException if the item is null
     */
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException("item is null");

        Node oldFront = head;
        head = new Node();
        head.item = item;
        head.next = oldFront;

        if (head.next == null)  // only one item in the deque now
            tail = head;
        else
            oldFront.prev = head;
        ++n;
    }

    /**
     * Adds the item to the end of this deque.
     *
     * @param item the item to add
     * @throws java.lang.IllegalArgumentException if the item is null
     */
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException("item is null");

        Node oldLast = tail;
        tail = new Node();
        tail.item = item;
        tail.prev = oldLast;
        
        if (oldLast != null)    // only one item in the deque now
            oldLast.next = tail;
        else
            head = tail;
        ++n;
    }

    /**
     * Removes and returns the item most recently added to the front of this 
     * deque.
     *
     * @return the item most recently added to the front of deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("Deque underflow");
        
        Item item = head.item;
        head = head.next;

        if (head == null)   // empty deque
            tail = null; 
        else
            head.prev = null;
        --n;
        return item;
    }

    /**
     * Removes and returns the item most recently added to the end of this
     * deque.
     *
     * @return the item most recently added to the end of deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("Deque underflow");

        Item item = tail.item;
        tail = tail.prev;
        
        if (tail == null)   // empty deque
            head = null;
        else
            tail.next = null;
        --n;
        return item;
    }

    /**
     * Returns an iterator to this deque that iterates through the items from 
     * head to tail.
     *
     * @return an iterator to this deque that iterates through the items from
     *         head to tail 
     */
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class DequeIterator implements Iterator<Item> {
        private Node current = head;

        public boolean hasNext() {  
            return current != null; 
        }
        
        public void remove() {
            throw new UnsupportedOperationException(); 
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    /**
     * Unit tests the {@code Deque} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String cmd = StdIn.readString();
            if (cmd.equals("<-"))
                StdOut.print(deque.removeLast() + " ");
            else if (cmd.equals(">-"))
                StdOut.print(deque.removeFirst() + " ");
            else if (cmd.equals("<+")) {
                String item = StdIn.readString();
                deque.addLast(item);
            } else if (cmd.equals(">+")) {
                String item = StdIn.readString();
                deque.addFirst(item);
            }
        }
        StdOut.println("(" + deque.size() + " left on deque");
    }
}
