package deque;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        //System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        ArrayDeque<String> ad1 = new ArrayDeque<String>();

        assertTrue("A newly initialized ADeque should be empty", ad1.isEmpty());
        ad1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, ad1.size());
        assertFalse("lld1 should now contain 1 item", ad1.isEmpty());

        ad1.addLast("middle");
        assertEquals(2, ad1.size());

        ad1.addLast("back");
        assertEquals(3, ad1.size());

        System.out.println("Printing out deque: ");
        ad1.printDeque();

    }

    @Test
    /** Adds an item, then removes an item, and ensures that ad is empty afterwards. */
    public void addRemoveTest() {

        //System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        // should be empty
        assertTrue("ad1 should be empty upon initialization", ad1.isEmpty());

        ad1.addFirst(10);
        // should not be empty
        assertFalse("ad1 should contain 1 item", ad1.isEmpty());

        ad1.removeFirst();
        // should be empty
        assertTrue("ad1 should be empty after removal", ad1.isEmpty());

    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        //System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addFirst(3);
        ad1.addFirst(4);
        ad1.addFirst(5);

        assertEquals((int) ad1.removeLast(), 3);
        assertEquals((int) ad1.removeFirst(), 5);
        assertEquals((int) ad1.removeFirst(), 4);
        ad1.removeLast();
        ad1.removeFirst();
        ad1.removeLast();
        ad1.removeFirst();


        int size = ad1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);

    }

    @Test
    /* Check if you can create ArrayDeques with different parameterized types*/
    public void multipleParamTest() {


        ArrayDeque<String> ad1 = new ArrayDeque<String>();
        ArrayDeque<Double> ad2 = new ArrayDeque<Double>();
        ArrayDeque<Boolean> ad3 = new ArrayDeque<Boolean>();

        ad1.addFirst("string");
        ad2.addFirst(3.14159);
        ad3.addFirst(true);

        String s = ad1.removeFirst();
        double d = ad2.removeFirst();
        boolean b = ad3.removeFirst();

    }

    @Test
    /* check if null is return when removing from an empty ArrayDeque. */
    public void emptyNullReturnTest() {

        //System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, ad1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, ad1.removeLast());


    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigADequeTest() {

        //System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        System.out.println("a");
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        System.out.println("before for loop");
        for (int i = 0; i < 40000; i++) {
            //System.out.println(i);
            ad1.addLast(i);
        }
        System.out.println("first finished");

        for (double i = 0; i < 20000; i++) { // 5000 worked but 500000 take forever
            //System.out.println(i);
            assertEquals("Should have the same value", i, (double) ad1.removeFirst(), 0.0);
        }
        System.out.println("second finished");

        for (double i = 39999; i > 20000; i--) {
            //System.out.println(i);

            assertEquals("Should have the same value", i, (double) ad1.removeLast(), 0.0);
        }
        System.out.println("third finished");


    }

    @Test
    /** Adds an item, ensures that dll is not empty afterwards, and then get the item. */
    public void getTest() {

        //System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        // should be empty
        assertTrue("lld1 should be empty upon initialization", ad1.isEmpty());

        ad1.addFirst(10);
        ad1.addFirst(20);
        ad1.addLast(70);
        ad1.addLast(60);
        ad1.addFirst(30);
        // should not be empty
        assertFalse("lld1 should contain 1 item", ad1.isEmpty());


        assertEquals(30, (int) ad1.get(0));
        System.out.println(ad1.get(0));
        assertEquals(20, (int) ad1.get(1));
        System.out.println(ad1.get(1));
        assertEquals(10, (int) ad1.get(2));
        System.out.println(ad1.get(2));
        assertEquals(70, (int) ad1.get(3));
        System.out.println(ad1.get(3));
        assertEquals(60, (int) ad1.get(4));
        System.out.println(ad1.get(4));

    }

    @Test

    public void arrayIteratorTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();

        ad1.addFirst(1);
        ad1.addFirst(2);
        ad1.addLast(5);
        ad1.addLast(6);
        ad1.printDeque();

        Iterator<Integer> ai1 = ad1.iterator();
        while (ai1.hasNext()) {
            int i = ai1.next();
            System.out.println(i);
        }
    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void removeResizeTest() {

        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        for (int i = 0; i < 1000; i++) {
            ad1.addLast(i);
        }


        for (double i = 0; i < 500; i++) { // 5000 worked but 500000 take forever
            System.out.println(i);
            assertEquals("Should have the same value", i, (double) ad1.removeFirst(), 0.0);
        }


        for (double i = 999; i > 500; i--) {
            System.out.println(i);

            assertEquals("Should have the same value", i, (double) ad1.removeLast(), 0.0);
        }

    }

    @Test

    public void sizeTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        for (int i = 0; i < 50; i++) {
            ad1.addLast(i);
        }

        assertEquals(49, (int) ad1.removeLast());
        assertEquals(0, (int) ad1.removeFirst());
    }

    @Test

    public void equalTest() {
        ArrayDeque<String> ad1 = new ArrayDeque<String>();
        ArrayDeque<String> ad2 = new ArrayDeque<String>();
        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();
        for (int i = 0; i < 50; i++) {
            ad1.addLast("a");
            lld1.addLast("a");
            ad2.addLast("a");
        }

        assertTrue(ad1.equals(lld1));
        assertTrue(lld1.equals(ad1));
        assertTrue(ad1.equals(ad2));

    }

}
