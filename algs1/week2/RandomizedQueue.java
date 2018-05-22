import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] arr;
    private int n;

    public RandomizedQueue() {
        arr = (Item[]) new Object[2];
        n = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException("");
        if (n == arr.length)    resize(arr.length * 2);
        arr[n++] = item;
    }

    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("");
        int target = StdRandom.uniform(n);
        Item item = arr[target];
        arr[target] = arr[n -1];
        arr[n -1] = null;
        --n;

        if (n > 0 && n == arr.length /4) resize(arr.length /2);
        return item;
    }

    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException("");
        return arr[StdRandom.uniform(n)];
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private void resize(int capacity) {
        Item[] tmp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; ++i)
            tmp[i] = arr[i];
        arr = tmp;
    }

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

    public static void main(String[] args) {
    }
}
