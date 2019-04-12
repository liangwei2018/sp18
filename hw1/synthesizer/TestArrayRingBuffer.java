package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer arb = new ArrayRingBuffer(10);
    }

    @Test
    public void moreTest() {

        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(4);

        assertTrue(arb.isEmpty());

        arb.enqueue(5);
        arb.enqueue(10);
        arb.enqueue(15);
        arb.enqueue(20);

        assertTrue(arb.isFull());

        arb.dequeue();
        arb.enqueue(25);

        assertEquals(10, (int) arb.peek());


        int j = 2;
        for (int i : arb) {
            assertEquals(j * 5, i);
            j += 1;
        }

    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {

        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);

        int[] someInts = new int[]{1, 2, 3};
        for (int x : someInts) {
            for (int y: someInts) {
                System.out.println("x: " + x +  ", y:" + y);
            }
        }
    }
} 
