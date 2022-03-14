package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
    public void testSquarePrimesSimple() { //* one prime */
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);
    }
    @Test
    public void testSquarePrimesSimple1() { //* three prime in the end*/
        IntList lst = IntList.of(0, 1, 2, 3, 5);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("0 -> 1 -> 4 -> 9 -> 25", lst.toString());
        assertTrue(changed);
    }
    @Test
    public void testSquarePrimesSimple2() { //* three prime with one non prime in between*/
        IntList lst = IntList.of(1, 2, 3, 4, 5);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("1 -> 4 -> 9 -> 4 -> 25", lst.toString());
        assertTrue(changed);
    }
    @Test
    public void testSquarePrimesSimple3() { //* one non prime in middle */
        IntList lst = IntList.of(3, 2, 6, 7, 5);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("9 -> 4 -> 6 -> 49 -> 25", lst.toString());
        assertTrue(changed);
    }
    @Test
    public void testSquarePrimesSimple4() { //* all prime */
        IntList lst = IntList.of(2, 3, 5, 7, 11);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("4 -> 9 -> 25 -> 49 -> 121", lst.toString());
        assertTrue(changed);
    }
    @Test
    public void testSquarePrimesSimple5() { //* non prime */
        IntList lst = IntList.of(4, 6, 9, 12, 10);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("4 -> 6 -> 9 -> 12 -> 10", lst.toString());
        assertFalse(changed);
    }
}
