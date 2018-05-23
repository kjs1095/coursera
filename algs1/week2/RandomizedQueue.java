/*----------------------------------------------------------------
 *  Author:        Jyun-Sheng Kao
 *  Written:       5/19/2018
 *  Last updated:  5/23/2018
 *
 *  Compilation:   javac-coursera RandomizedQueue.java
 *  Execution:     java-coursera RandomizedQueue < input.txt
 *  Dependencies:  StdIn.java StdOut.java StdRandom.java
 *
 *  % more input.txt
 *  A B C D - E - - F
 *
 *  % java-coursera RandomizedQueue < input.txt
 *  A C F(3 left on randomized queue 
 *
 *----------------------------------------------------------------*/

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] arr; // array of items
    private int n;      // number of elements in randomized queue

    /**
     * Initializes an empty randomized queue.
     */
    public RandomizedQueue() {
        arr = (Item[]) new Object[2];
        n = 0;
    }

    /**
     * Is this randomized queue empty?
     * @return true if this randomized queue is empty; false otherwize
     */
    public boolean isEmpty() {
        return n == 0;
    }

    /**
     * Returns the number of items in the randomized queue.
     * @return the number of items in the randomized queue
     */
    public int size() {
        return n;
    }

    /**
     * Adds the item to this randomized queue
     * @param item the item to add
     * @throws java.lang.IllegalArgumentException if the item is null
     */
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException("item is null");

        if (n == arr.length)    // double size of array if necessary   
            resize(arr.length * 2);
        arr[n++] = item;
    }

    /**
     * Removes and returns the item in the randomized queue randomly.
     * @return the item in the randomized queue
     * @throws java.util.NoSuchElementException if this randomized queue 
     *         is empty
     */
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Randomized queue underflow");
        int target = StdRandom.uniform(n);
        Item item = arr[target];
        arr[target] = arr[n -1];
        arr[n -1] = null;
        --n;

        // shrink size of array if necessary
        if (n > 0 && n == arr.length /4) resize(arr.length /2);
        return item;
    }

    /**
     * Returns (but does not remove) the item in ths randomized queue randomly.
     * @return the item in the randomized queue
     * @throws java.util.NoSuchElementException if this randomized queue
     *         is empty
     */
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException("");
        return arr[StdRandom.uniform(n)];
    }

    /**
     * Returns an iterator to this randomized queue that iterate through 
     * the item in random order. 
     * @return an iterator to this randomized queue that iterate through
     *         the item in random order 
     */
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // resize the underlying array holding the elements
    private void resize(int capacity) {
        assert capacity >= n;

        Item[] tmp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; ++i)
            tmp[i] = arr[i];
        arr = tmp;
    }

    // an iterator, doesn't implement remove() since it's optional
    private class RandomizedQueueIterator implements Iterator<Item> {
        private int cap;
        private Item[] tmp;
        public RandomizedQueueIterator() { 
            cap = n;
            tmp = (Item[]) new Object[cap];
            for (int i = 0; i < cap; ++i)
                tmp[i] = arr[i];
        }

        public boolean hasNext()    {  return cap > 0; }
        public void remove()        {  throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            int target = StdRandom.uniform(cap);
            Item item = tmp[target];
            tmp[target] = tmp[cap -1];
            tmp[cap -1] = null;
            --cap;
            return item;
        }
    }

    /**
     * Unit tests the {@code RandomizedQueue} data type
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-"))  rq.enqueue(item);
            else                    StdOut.print(rq.dequeue() + " ");
        }

        StdOut.println("(" + rq.size() + " left on randomized queue");
    }
}
