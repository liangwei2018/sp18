/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // TO DO: Implement LSD Sort
        if (asciis == null || asciis.length <= 1) {
            return asciis;
        }
        int strLen = asciis.length;
        int maxLength = Integer.MIN_VALUE;
        for (String s : asciis) {
            int len = s.length();
            if (len > maxLength) {
                maxLength = len;
            }
        }
        String[] padded = new String[strLen];

        for (int i = 0; i < strLen; i += 1) {
            padded[i] = String.format("%-" + maxLength + "s", asciis[i]);
            //System.out.println("old string:" + asciis[i] + " padded:" + padded[i]
            //        + " length:" + padded[i].length());
        }
        for (int i = maxLength - 1; i >= 0; i -= 1) {
            sortHelperLSD(padded, i);
        }
        return padded;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        int strLen = asciis.length;
        int[] num = new int[strLen];
        for (int i = 0; i < strLen; i += 1) {
            num[i] = asciis[i].charAt(index);
        }
        //int[] sorted = CountingSort.naiveCountingSort(num);

        // find max
        int max = Integer.MIN_VALUE;
        for (int i : num) {
            max = max > i ? max : i;
        }

        // gather all the counts for each value
        int[] counts = new int[max + 1];
        for (int i : num) {
            counts[i]++;
        }

        // however, below is a more proper, generalized implementation of
        // counting sort that uses start position calculation
        int[] starts = new int[max + 1];
        int pos = 0;
        for (int i = 0; i < starts.length; i += 1) {
            starts[i] = pos;
            pos += counts[i];
        }

        String[] sorted2 = new String[num.length];
        for (int i = 0; i < num.length; i += 1) {
            int item = num[i];
            int place = starts[item];
            sorted2[place] = asciis[i];
            starts[item] += 1;
        }
        for (int i = 0; i < strLen; i += 1) {
            asciis[i] = sorted2[i];
        }

    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }
}
