import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int n;
    private Node head, tail;

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

    public boolean isEmpty() {
        return head == null || tail == null;
    }

    public int size() {
        return n;
    }

    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException("");
        Node oldFront = head;
        head = new Node();
        head.item = item;
        head.next = oldFront;
        if (head.next == null)
            tail = head;
        else
            oldFront.prev = head;
        ++n;
    }

    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException("");
        Node oldLast = tail;
        tail = new Node();
        tail.item = item;
        tail.prev = oldLast;
        if (oldLast != null)
            oldLast.next = tail;
        else
            head = tail;
        ++n;
    }

    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("");
        Item item = head.item;
        head = head.next;
        if (head == null)
            tail = null; 
        else
            head.prev = null;
        --n;
        return item;
    }

    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("");
        Item item = tail.item;
        tail = tail.prev;
        if (tail == null)
            head = null;
        else
            tail.next = null;
        --n;
        return item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = head;
        public boolean hasNext() {  return current != null; }
        public void remove()     {  throw new UnsupportedOperationException(); }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
    }
}
