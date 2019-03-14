/** A class for palindrome operations.
 * @author Liang Wei
 */

public class Palindrome {

    /**
     * Given a String, wordToDeque returns a Deque where the characters
     * appear in the same order as in the String.
     * @param word A String to be converted.
     * @return A Deque of characters
     */
    public Deque<Character> wordToDeque(String word) {
        if (word == null || word.isEmpty()) {
            return null;
        }
        Deque<Character> charDeque = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            charDeque.addLast(word.charAt(i));
        }
        return charDeque;
    }

    /**
     * Returns true if the given WORD is a palindrome, and false otherwise. Recursion.
     * @param word A string to be tested.
     * @return true if the WORD is a palindrome, and false otherwise.
     */
    public boolean isPalindrome(String word) {
        if (word == null) {
            return false;
        }
        return isPalindromeHelper(wordToDeque(word));
    }
    private boolean isPalindromeHelper(Deque<Character> charDeque) {
        if (charDeque == null || charDeque.size() <= 1) {
            return true;
        }
        return (charDeque.removeFirst() == charDeque.removeLast()) && isPalindromeHelper(charDeque);

    }

    /**
     * The method will return true if the WORD is a palindrome according to
     * the character comparison test provided by the CharacterComparator
     * passed in as argument CC.
     * @param word A string to be tested.
     * @param cc A Charactercomparison test.
     * @return true if the WORD is a palindrome, and false otherwise.
     */
    public boolean isPalindrome(String word, CharacterComparator cc) {

        if (word == null) {
            return false;
        }
        int wLength = word.length();
        if (wLength <= 1) {
            return true;
        }
        boolean eqChar = cc.equalChars(word.charAt(0), word.charAt(wLength - 1));
        return eqChar && isPalindrome(word.substring(1, wLength - 1), cc);
    }

}
