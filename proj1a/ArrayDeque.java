/**
 * A ArrayDeque is a Array-based "Double Ended Queue".
 * @author Liang Wei
 *
 * @param <T>  A Parameter type
 *
 */
public class ArrayDeque<T> {

    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;


    /** Creates an empty list. */
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 3;
        nextLast = 4;
    }

    /** Deep copy constructor creates an entirely new ArrayDeque<T> object
     * with the exact same items as OTHER. However, they are different objects.
     *
     * @param other An object of LinkedListDeque type to be deep copied
     */
    public ArrayDeque(ArrayDeque<T> other) {
        size = other.size;
        int itemsLength = other.items.length;
        items = (T []) new Object[itemsLength];
        System.arraycopy(other.items, 0, items, 0, itemsLength);
        nextFirst = other.nextFirst;
        nextLast = other.nextLast;
    }

    /** Adds x to the front of the list. */
    public void addFirst(T x) {
        if (size == items.length) {
            resize(size * 2);
        }
        int newLength = items.length;
        items[nextFirst] = x;
        nextFirst = (nextFirst + newLength - 1) % newLength;
        size = size + 1;
    }

    /** Resizes the underlying array to the target capacity.
     *  nextLast = (nextFirst + 1) % items.length
     * @param capacity New length of the array
     */
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int itemsLength = items.length;
        int i = (nextFirst + 1) % itemsLength;
        int j = 1;
        int numItems = 0;
        while (numItems <= size) {
            a[j] = items[i];
            j = j + 1;
            i = (i + 1) % itemsLength;
            numItems = numItems + 1;
        }
        items = a;
        nextFirst = 0;
        nextLast = size + 1;
    }

    /** Adds x to the back of the list. */
    public void addLast(T x) {
        if (size == items.length) {
            resize(size * 2);
        }
        int newLength = items.length;
        items[nextLast] = x;
        nextLast = (nextLast + 1) % newLength;
        size = size + 1;
    }

    /** Checks if the list is empty. */
    public boolean isEmpty() {
        return (size == 0);
    }

    /** Returns the size of the list. */
    public int size() {
        return size;
    }

    /** Returns the length of the array. */
    public int length() {
        return items.length;
    }

    /** Prints each item of the list. */
    public void printDeque() {
        if (size == 0) {
            System.out.println("The Array List is empty");
        }
        int itemsLength = items.length;
        int i = (nextFirst + 1) % itemsLength;
        int numItems = 0;
        while (numItems < size) {
            System.out.print(items[i] + " ");
            i = (i + 1) % itemsLength;
            numItems = numItems + 1;
        }
        System.out.println("\n");
    }

    /** Removes the front item of the list. */
    public T removeFirst() {
        int itemsLength = items.length;
        int firstItem = (nextFirst + 1) % itemsLength;
        T x = items[firstItem];
        items[firstItem] = null;
        nextFirst = firstItem;
        size -= 1;
        if (size < 0.25 * itemsLength && itemsLength >= 16) {
            resize(itemsLength / 2);
        }
        return x;
    }

    /** Removes the back item of the list */
    public T removeLast() {
        int itemsLength = items.length;
        int lastItem = (nextLast + itemsLength - 1) % itemsLength;
        T x = items[lastItem];
        items[lastItem] = null;
        nextLast = lastItem;
        size -= 1;
        if (size < 0.25 * itemsLength && itemsLength >= 16) {
            resize(itemsLength / 2);
        }
        return x;
    }

    /** Gets the INDEX-th item of the list iteratively. */
    public T get(int index) {
        if (index < 0 || index > items.length - 1) {
            System.out.println("Error: index is out of range! ");
            return null;
        }
        int itemIndex = (nextFirst + 1 + index) % items.length;
        return items[itemIndex];
    }

}
