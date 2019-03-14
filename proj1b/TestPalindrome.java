import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }


    @Test
    public void testIsPalindrome() {


        assertTrue(palindrome.isPalindrome("a"));
        assertTrue(palindrome.isPalindrome(""));
        assertTrue(palindrome.isPalindrome("noon"));
        assertTrue(palindrome.isPalindrome("racecar"));
        assertTrue(palindrome.isPalindrome("sp181ps"));
        assertTrue(palindrome.isPalindrome("A bcb A"));

        assertFalse(palindrome.isPalindrome("cat"));
        assertFalse(palindrome.isPalindrome("aaaaab"));
        assertFalse(palindrome.isPalindrome("noOn"));
        assertFalse(palindrome.isPalindrome("racecaR"));
        assertFalse(palindrome.isPalindrome("Madam I'm Adam"));
        assertFalse(palindrome.isPalindrome("A Toyota"));

    }

    @Test
    public void testNewIsPalindrome() {
        CharacterComparator offByOne = new OffByOne();


        assertTrue(palindrome.isPalindrome("a", offByOne));
        assertTrue(palindrome.isPalindrome("", offByOne));
        assertTrue(palindrome.isPalindrome("mpon", offByOne));
        assertTrue(palindrome.isPalindrome("racebbs", offByOne));
        assertTrue(palindrome.isPalindrome("rac1e2bbs", offByOne));

        assertFalse(palindrome.isPalindrome("cat", offByOne));
        assertFalse(palindrome.isPalindrome("aaaaab", offByOne));
        assertFalse(palindrome.isPalindrome("noOn", offByOne));
        assertFalse(palindrome.isPalindrome("racecaR", offByOne));
        assertFalse(palindrome.isPalindrome("Madam I'm Adam", offByOne));
        assertFalse(palindrome.isPalindrome("A Toyota", offByOne));
        assertFalse(palindrome.isPalindrome("A bbc B", offByOne));
    }
}
