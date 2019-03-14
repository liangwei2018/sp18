/** A class for palindrome operations (an off-by-N palindrome).
 * @author Liang Wei
 */

public class OffByN implements CharacterComparator {
    private int N;

    public OffByN(int N) {
        this.N = N;
    }

    /**
     * Compare two characters.
     * @param x A character.
     * @param y A character.
     * @return True if x and y is off by N.
     */
    @Override
    public boolean equalChars(char x, char y) {
        int diff = x - y;
        return (Math.abs(diff) == N);
    }
}
