import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class RadixSortTester {


    private static String[] someStr = {"California", "AB", "Banana", "US", "Canada", "Iowa", "New York"};
    private static String[] oneStr = {"California"};
    private static String[] emptyStr = {""};


    public static void assertIsSorted(String[] s) {
        if (s == null || s.length == 0) {
            return;
        }
        String previous = s[0];
        for (String x : s) {
            assertTrue(x.compareTo(previous) >= 0);
            previous = x;
        }
    }

    @Test
    public void testRadixSortWithSomeStr() {
        String[] sortedSomeStr = RadixSort.sort(someStr);
        assertIsSorted(sortedSomeStr);
    }

    @Test
    public void testRadixSortWithOneStr() {
        String[] sortedSomeStr = RadixSort.sort(oneStr);
        assertIsSorted(sortedSomeStr);
    }

    @Test
    public void testRadixSortWithEmptyStr() {
        String[] sortedSomeStr = RadixSort.sort(emptyStr);
        assertIsSorted(sortedSomeStr);
    }
}
