package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    private T[] items;
    private int size;
    private int nextFirst; // make a variable that track the place to put next first item
    private int nextLast; // make a variable that track the place to put the next Last item


    //creates an empty array deque
    public ArrayDeque() {
        size = 0;
        items = (T[]) new Object[8];
        nextFirst = 7; // current first is 0 and next first is one before.
        // So it will equal to the items.length-1
        nextLast = 0; // current last is 7 and next last is one after.
        // So it will equal to 0
    }


    private void addResize() {
        int currFirst;
        int currLast;
        T[] holder = (T[]) new Object[size * 2]; //create a new array with double size boxes.

        if (nextFirst == items.length - 1) { // equal to the last item
            currFirst = 0;
        } else {
            currFirst = nextFirst + 1; // currFirst is always the one after nextFirst
        }

        if (nextLast == 0) {
            currLast = items.length - 1;
        } else {
            currLast = nextLast - 1; // currLast is always the one before nextLast
        }

        //* Invariants: two situation
        // 1. when currLast < currFirst this is the time the list
        //end in the middle and new item is going to add in middle
        // 2. when currLast > currFirst this is the time the
        //list end in two side. The new item is going to add in
        // the last item of the array.

        if (currLast < currFirst) {
            //copy from the 0 item to currLast
            //copy to the holder's index 0 from 0 to currLast-0 items
            //nextLast won't change
            System.arraycopy(items, 0, holder, 0, currLast + 1);

            // copy from the currFirst item for items and
            // copy length of the last item - currFirst
            // copy to the currFirst+size
            System.arraycopy(items, currFirst, holder, currFirst + size,
                    (items.length - 1) - currFirst + 1);
            // nextFirst change to the place just created
            nextFirst = currFirst + size - 1;
            items = holder; // assign the holder array to the items

        } else if (currFirst < currLast) {
            //copy from the currFirst item and copy size items
            System.arraycopy(items, currFirst, holder, currFirst, size);
            nextFirst = holder.length - 1;
            nextLast = size;
            items = holder;

        }

    }


    public void addFirst(T item) {
        if (items.length == size) {
            addResize();
        }
        items[nextFirst] = item;
        // assign nextFirst to the place before nextFirst
        // where the next item will be add in the front
        // nextFirst place become currFirst

        if (nextFirst == 0) { // equal to the last item
            nextFirst = items.length - 1;
        } else {
            nextFirst = nextFirst - 1;
            // currFirst is always the one after nextFirst
        }

        // then the nextFirst need
        // to move to one before
        size++; //add one to size
    }

    public void addLast(T item) {
        if (items.length == size) {
            addResize();
        }
        items[nextLast] = item;
        //assign nextLast to the place after nextLast
        // because it is the next place to add the item for addLast

        if (nextLast == items.length - 1) {
            nextLast = 0;
        } else {
            nextLast = nextLast + 1; // currLast is
            // always the one before nextLast
        }

        size++;

    }


    public int size() {
        return size;
    }

    public void printDeque() {
        //check if the array deque is empty

        if (isEmpty()) {
            return; // to exit a void function
        }
        int currFirst;
        int currLast;

        //check where is the currLast and currFirst
        if (nextLast == 0) {
            currLast = items.length - 1;
        } else {
            currLast = nextLast - 1;
        }

        if (nextFirst == items.length - 1) {
            currFirst = 0;
        } else {
            currFirst = nextFirst + 1;
        }

        //check if the currFirst < or > than currLast
        if (currLast < currFirst) {
            for (int i = currFirst; i < items.length; i++) {
                System.out.print(items[i] + " ");
            }
            for (int i = 0; i <= currLast; i++) {
                System.out.print(items[i] + " ");
            }
            System.out.println();
        } else if (currLast > currFirst) {
            for (int i = currFirst; i <= currLast; i++) {
                System.out.print(items[i] + " ");
            }
            System.out.println();
        }
    }

//    private void removeResize() {
//        int currFirst;
//        int currLast;
//        // Create a new array of size items
//        T[] newArray = (T[]) new Object[size];
//
//        if (nextFirst == items.length - 1) {
//            currFirst = 0;
//        } else {
//            currFirst = nextFirst + 1;
//        }
//
//        if (nextLast == 0) {
//            currLast = items.length - 1;
//        } else {
//            currLast = nextLast - 1;
//        }
//
//        if (currFirst <= currLast) {
//            System.arraycopy(items, currFirst, newArray, 0, size);
//            items = newArray;
//            nextLast = 0;
//            nextFirst = items.length - 1;
//        } else {
//            System.arraycopy(items, 0, newArray, 0, currLast + 1);
//            System.arraycopy(items, currFirst, newArray,
//                    currLast + 1, (items.length - 1) - currFirst + 1);
//            nextFirst = currLast;
//            nextLast = currLast + 1;
//            items = newArray;
//        }
//
//
//    }

    private void removeResize() {
        int currFirst;
        int currLast;
        // Create a new array of size items
        T[] newArray = (T[]) new Object[size * 2];

        if (nextFirst == items.length - 1) {
            currFirst = 0;
        } else {
            currFirst = nextFirst + 1;
        }

        if (nextLast == 0) {
            currLast = items.length - 1;
        } else {
            currLast = nextLast - 1;
        }

        if (currFirst <= currLast) {
            System.arraycopy(items, currFirst, newArray, 0, size);
            items = newArray;
            nextLast = size;
            nextFirst = items.length - 1;
        } else {
            System.arraycopy(items, 0, newArray, 0, currLast + 1);
            System.arraycopy(items, currFirst, newArray,
                    currLast + 1 + size, (items.length - 1) - currFirst + 1);
            nextFirst = currLast + size;
            nextLast = currLast + 1;
            items = newArray;
        }


    }


    public T removeFirst() {
        if (items.length >= 16 && size < items.length / 4) {
            removeResize();
        }

        int currFirst;
        int currLast;
        T firstValue;

        if (nextFirst == items.length - 1) {
            currFirst = 0;
        } else {
            currFirst = nextFirst + 1;
        }

        if (nextLast == 0) {
            currLast = items.length - 1;
        } else {
            currLast = nextLast - 1;
        }


        if (size == 0) { // if the list is empty
            return null;
        } else if (items[currFirst] == null) { // if the item at currFirst is empty
            return null;
        } else {
            firstValue = items[currFirst];
            nextFirst = currFirst;
            size--;
            return firstValue;
        }

    }

    public T removeLast() {
        if (items.length > 16 && size / items.length < 0.25) {
            removeResize();
        }

        int currFirst;
        int currLast;
        T lastValue;

        if (nextFirst == items.length - 1) {
            currFirst = 0;
        } else {
            currFirst = nextFirst + 1;
        }

        if (nextLast == 0) {
            currLast = items.length - 1;
        } else {
            currLast = nextLast - 1;
        }

        if (size == 0) {
            return null;
        } else if (items[currLast] == null) {
            return null;
        } else {
            lastValue = items[currLast];
            nextLast = currLast;
            size--;
            return lastValue;
        }


    }

    public T get(int index) {

        int currFirst;
        int currLast;

        if (nextFirst == items.length - 1) {
            currFirst = 0;
        } else {
            currFirst = nextFirst + 1;
        }

        if (nextLast == 0) {
            currLast = items.length - 1;
        } else {
            currLast = nextLast - 1;
        }


        if (index < 0) {
            return null; // the smallest index is 0
        } else if (index > items.length - 1) {
            return null; // the largest index is items.length - 1
        } else if (currFirst <= currLast) {
            return items[currFirst + index];
        } else {
            if (index + currFirst > items.length - 1) {
                int numbersInLast = (items.length - 1) - currFirst + 1;
                int returnIndex = index - numbersInLast;
                T returnValue = items[returnIndex];
                return returnValue;
            }
            return items[currFirst + index];
        }
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        if (size != ((Deque<T>) o).size()) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (!((Deque<T>) o).get(i).equals(this.get(i))) {
                return false;
            }
        }

        return true;
    }


    public Iterator<T> iterator() {
        return new ArrayDeque.ArrayIterator();
    }

    private class ArrayIterator implements Iterator<T> {
        private int index;

        //deleted public
        ArrayIterator() {
            index = 0;
        }

        public boolean hasNext() {
            return index < size;
        }

        public T next() {
            T returnItem = get(index);
            index++;
            return returnItem;
        }
    }


}
