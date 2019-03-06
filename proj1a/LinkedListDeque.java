/**
 * A LinkedListDeque is a list-based "Double Ended Queue".
 * @author Liang Wei
 *
 * @param <T>  A Parameter type
 *
 */
public class LinkedListDeque<T> {
    private class StuffNode {
        private T item;
        private StuffNode prev;
        private StuffNode next;

        StuffNode(T a, StuffNode p, StuffNode n) {
            item = a;
            prev = p;
            next = n;
        }
        StuffNode(StuffNode c) {
            item = c.item;
            prev = null;
            next = null;
        }
    }

    private int size;
    private StuffNode sentinel;

    /** Creates an empty list. */
    public LinkedListDeque() {
        sentinel = new StuffNode( null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    /** Deep copy constructor creates an entirely new LinkedListDeque<T> object
     * with the exact same items as OTHER. However, they are different objects.
     *
     * @param other An object of LinkedListDeque type to be deep copied
     *
     * An alternative method is to use addLast((T) other.get(i)) to do the deep copy
     */
    public LinkedListDeque(LinkedListDeque other) {
        this.size = other.size;
        this.sentinel = new StuffNode(other.sentinel);


        StuffNode p = other.sentinel.next;
        StuffNode currentNode = this.sentinel;

        while (p != other.sentinel) {
            StuffNode temp = new StuffNode(p);
            currentNode.next = temp;
            temp.prev = currentNode;
            currentNode = currentNode.next;
            p = p.next;
        }
        currentNode.next = sentinel;
        sentinel.prev = currentNode;
    }

    /** Adds ITEM to the front of the list. */
    public void addFirst(T item) {
        sentinel.next = new StuffNode(item, sentinel, sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size += 1;
    }

    /** Adds ITEM to the back of the list. */
    public void addLast(T item) {
        sentinel.prev = new StuffNode(item, sentinel.prev, sentinel);
        sentinel.prev.prev.next = sentinel.prev;
        size += 1;
    }

    /** Checks if the list is empty. */
    public boolean isEmpty() {
        return (sentinel.next == sentinel);
    }

    /** Returns the size of the list. */
    public int size() {
        return size;
    }
    /** Prints each item of the list. */
    public void printDeque() {
        StuffNode p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println("\n");
    }

    /** Removes the front item of the list. */
    public T removeFirst() {
        StuffNode first = sentinel.next;
        if (first == sentinel) {
            return null;
        }
        first.next.prev = sentinel;
        sentinel.next = first.next;
        first.next = null;
        first.prev = null;
        size -= 1;
        return first.item;
    }

    /** Removes the back item of the list */
    public T removeLast() {
        StuffNode last = sentinel.prev;
        if (last == sentinel) {
            return null;
        }
        last.prev.next = sentinel;
        sentinel.prev = last.prev;
        last.next = null;
        last.prev = null;
        size -= 1;
        return last.item;
    }

    /** Gets the INDEX-th item of the list iteratively. */
    public T get(int index) {
        if (index < 0 || index > size - 1) {
            System.out.println("Error: index is out of range! ");
            return null;
        }
        StuffNode p = sentinel.next;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return p.item;
    }

    /** Gets the INDEX-th item of the list recursively. */
    public T getRecursive(int index) {
        if (index < 0 || index > size - 1 || size == 0) {
            System.out.println("Error: index is out of range or Empty List! ");
            return null;
        }

        return getRecursiveHelper(this.sentinel.next, index);
    }

    private T getRecursiveHelper(StuffNode currentNode, int index) {

        if (index == 0) {
            return currentNode.item;
        }

        return getRecursiveHelper(currentNode.next, index - 1);

    }
}
