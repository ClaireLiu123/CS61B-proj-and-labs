package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    @Test
    public void testThreeAddThreeRemove(){
        BuggyAList<Integer> bList1 = new BuggyAList<>();
        AListNoResizing<Integer> aList1 = new AListNoResizing<>();
        bList1.addLast(4);
        aList1.addLast(4);
        bList1.addLast(5);
        aList1.addLast(5);
        bList1.addLast(6);
        aList1.addLast(6);

        assertEquals(bList1.size(), aList1.size());

        assertEquals(bList1.removeLast(), aList1.removeLast());
        assertEquals(bList1.removeLast(), aList1.removeLast());
        assertEquals(bList1.removeLast(), aList1.removeLast());
    }

    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();


        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int lSize = L.size();
                int bSize = B.size();
                System.out.println("size: " + lSize);
                System.out.println("size: " + bSize);
            }

            int a = 0;
            int b = 0;
            int a1 = 0;
            int b1 = 0;

            if (L.size() > 0) {
                a1 = L.getLast();
                a = L.removeLast();
            }

            if (B.size() > 0) {
                b1 = B.getLast();
                b = B.removeLast();
            }

            assertEquals(a, b);
            assertEquals(a1, b1);
            assertEquals(b, b1);

        }

    }

}
