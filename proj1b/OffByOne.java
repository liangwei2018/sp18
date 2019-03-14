/** A class for palindrome operations (an off-by-1 palindrome).
 * @author Liang Wei
 */

public class OffByOne implements CharacterComparator {

    /**
     * Compare two characters.
     * @param x A character.
     * @param y A character.
     * @return True if x and y is off by 1.
     */

    @Override
    public boolean equalChars(char x, char y) {
        int diff = x - y;
        return (Math.abs(diff) == 1);
    }
}
