import edu.princeton.cs.algs4.Queue;

public class QuickSort {
    /**
     * Returns a new queue that contains the given queues catenated together.
     *
     * The items in q2 will be catenated after all of the items in q1.
     */
    private static <Item extends Comparable> Queue<Item> catenate(Queue<Item> q1, Queue<Item> q2) {
        Queue<Item> catenated = new Queue<Item>();
        for (Item item : q1) {
            catenated.enqueue(item);
        }
        for (Item item: q2) {
            catenated.enqueue(item);
        }
        return catenated;
    }

    /** Returns a random item from the given queue. */
    private static <Item extends Comparable> Item getRandomItem(Queue<Item> items) {
        int pivotIndex = (int) (Math.random() * items.size());
        Item pivot = null;
        // Walk through the queue to find the item at the given index.
        for (Item item : items) {
            if (pivotIndex == 0) {
                pivot = item;
                break;
            }
            pivotIndex--;
        }
        return pivot;
    }

    /**
     * Partitions the given unsorted queue by pivoting on the given item.
     *
     * @param unsorted  A Queue of unsorted items
     * @param pivot     The item to pivot on
     * @param less      An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are less than the given pivot.
     * @param equal     An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are equal to the given pivot.
     * @param greater   An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are greater than the given pivot.
     */
    private static <Item extends Comparable> void partition(
            Queue<Item> unsorted, Item pivot,
            Queue<Item> less, Queue<Item> equal, Queue<Item> greater) {
        // Your code here!

        for (Item item : unsorted) {
            int comp = item.compareTo(pivot);
            if (comp == 0) {
                equal.enqueue(item);
            } else if (comp < 0) {
                less.enqueue(item);
            } else {
                greater.enqueue(item);
            }
        }
        //partition(less, getRandomItem(less), less, equal, greater);
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> quickSort(
            Queue<Item> items) {
        // Your code here!
        if (items == null || items.isEmpty()) {
            return null;
        }
        if (items.size() == 1) {
            return items;
        }
        Queue<Item> less = new Queue<>();
        Queue<Item> equal = new Queue<>();
        Queue<Item> greater = new Queue<>();
        Queue<Item> sortedQueue = new Queue<>();
        Item randPivotItem = getRandomItem(items);

        partition(items, randPivotItem, less, equal, greater);
        if (!less.isEmpty()) {
            sortedQueue = catenate(sortedQueue, quickSort(less));
        }
        sortedQueue = catenate(sortedQueue, equal);
        if (!greater.isEmpty()) {
            sortedQueue = catenate(sortedQueue, quickSort(greater));
        }
        return sortedQueue;
    }

    public static void main(String[] args) {
        Queue<String> students = new Queue<String>();
        students.enqueue("Alice");
        students.enqueue("Vanessa");
        students.enqueue("Ethan");
        students.enqueue("Alex");
        students.enqueue("Will");
        students.enqueue("Liam");
        students.enqueue("Ethan");

        System.out.println("original order: \n");
        for (String s : students) {
            System.out.println(s);
        }
        System.out.println("new order: \n");
        Queue<String> sortedStudents = QuickSort.quickSort(students);
        for (String s : sortedStudents) {
            System.out.println(s);
        }

    }
}
