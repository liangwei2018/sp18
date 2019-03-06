/**
 * Performs some basic Array list tests.
 * @author Liang Wei
 *
 */
public class ArrayDequeTest {
    /* Utility method for printing out empty checks. */
    public static boolean checkEmpty(boolean expected, boolean actual) {
        if (expected != actual) {
            System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Utility method for printing out size checks. */
    public static boolean checkSize(int expected, int actual) {
        if (expected != actual) {
            System.out.println("size() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }


    /* Utility method for printing out item checks. */
    public static boolean checkStringItem(String expected, String actual) {
        if (!expected.equals(actual)) {
            System.out.println("item returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }


    /* Prints a nice message based on whether a test passed.
     * The \n means newline. */
    public static void printTestStatus(boolean passed) {
        if (passed) {
            System.out.println("Test passed!\n");
        } else {
            System.out.println("Test failed!\n");
        }
    }

    /** Adds a few things to the list, checking isEmpty(), size(),
     * deep copy constructor, get() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */


    public static void addIsEmptySizeTest() {
        System.out.println("Running add/isEmpty/Size/deep copy/get()"
                            + "/removeLast/resize/usage factor test.");

        ArrayDeque<String> lld1 = new ArrayDeque<>();

        boolean passed = checkEmpty(true, lld1.isEmpty());

        lld1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments
        // true, and false otherwise.
        passed = checkSize(1, lld1.size()) && passed;
        passed = checkEmpty(false, lld1.isEmpty()) && passed;

        lld1.addLast("middle");
        passed = checkSize(2, lld1.size()) && passed;

        lld1.addLast("back");
        passed = checkSize(3, lld1.size()) && passed;

        lld1.addLast("back2");
        passed = checkSize(4, lld1.size()) && passed;

        lld1.addLast("back3");
        passed = checkSize(5, lld1.size()) && passed;

        lld1.addLast("back4");
        passed = checkSize(6, lld1.size()) && passed;

        lld1.addLast("back5");
        passed = checkSize(7, lld1.size()) && passed;

        lld1.addLast("back6");
        passed = checkSize(8, lld1.size()) && passed;

        lld1.addLast("back7");
        passed = checkSize(9, lld1.size()) && passed;

        lld1.addLast("back8");
        passed = checkSize(10, lld1.size()) && passed;

        lld1.addFirst("front0");
        passed = checkSize(11, lld1.size()) && passed;

        passed = checkStringItem("front0", lld1.get(0)) && passed;
        passed = checkStringItem("middle", lld1.get(2)) && passed;
        passed = checkStringItem("back8", lld1.get(10)) && passed;
        System.out.println("get() test passed!");

        /** check resize() and usage factor
         * For arrays of length 16 or more, your usage factor should always be at least 25%. */

        passed = checkSize(16, lld1.length()) && passed;
        for (int i = 0; i < 8; i++) {
            lld1.removeLast();
        }
        passed = checkSize(8, lld1.length()) && passed;

        System.out.println("Resize()/usage factor test passed!");

        System.out.println("Printing out lld1 deque: ");
        lld1.printDeque();

        ArrayDeque<String> lld2 = new ArrayDeque<>(lld1);

        // test lld2 and lld1 have exact same items, but they are different objects.
        for (int i = 0; i < lld2.size(); i++) {
            passed = checkStringItem(lld1.get(i), lld2.get(i)) && passed;
        }
        passed = (lld1 != lld2) && passed;

        System.out.println("Deep copy constructor test passed!");
        System.out.println("Printing out lld2 deque: ");
        lld2.printDeque();

        printTestStatus(passed);
    }

    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public static void addRemoveTest() {

        System.out.println("Running add/remove test.");


        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        // should be empty
        boolean passed = checkEmpty(true, lld1.isEmpty());

        lld1.addFirst(10);
        // should not be empty
        passed = checkEmpty(false, lld1.isEmpty()) && passed;

        lld1.removeFirst();
        // should be empty
        passed = checkEmpty(true, lld1.isEmpty()) && passed;

        lld1.addLast(15);
        // should not be empty
        passed = checkEmpty(false, lld1.isEmpty()) && passed;

        lld1.removeLast();
        // should be empty
        passed = checkEmpty(true, lld1.isEmpty()) && passed;


        printTestStatus(passed);

    }

    public static void main(String[] args) {
        System.out.println("Running tests.\n");
        addIsEmptySizeTest();
        addRemoveTest();
    }
}
