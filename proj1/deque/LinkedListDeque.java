package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    private class IntNode {
        private final T item;
        private IntNode prev;
        private IntNode next;

        private IntNode(T i) {
            prev = null;
            item = i;
            next = null;
        }
    }

    private final IntNode sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new IntNode(null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    public void addFirst(T item) {
        IntNode a = new IntNode(item);
        size++;
        a.next = sentinel.next;
        sentinel.next = a; //* sentinel is the pointer to the
        // whole IntNode where there is only one IntNode so the */
        a.next.prev = a;
        a.prev = sentinel;
    }

    public void addLast(T item) {
        IntNode a = new IntNode(item);
        size++;
        sentinel.prev.next = a;
        a.prev = sentinel.prev;
        sentinel.prev = a;
        a.next = sentinel;

    }


    public int size() {
        return size;
    }

    public void printDeque() {
        IntNode pointer = sentinel.next;
        while (pointer != sentinel) {
            System.out.print(pointer.item + " ");
            pointer = pointer.next;
        }
        System.out.println();
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        } else {
            IntNode firstValue = sentinel.next;
            sentinel.next.next.prev = sentinel;
            sentinel.next = sentinel.next.next;
            size--;
            return firstValue.item;
        }
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        } else {
            IntNode lastValue = sentinel.prev;
            sentinel.prev.prev.next = sentinel;
            sentinel.prev = sentinel.prev.prev;
            size--;
            return lastValue.item;
        }
    }

    public T get(int index) {
        if (size == 0) {
            return null;
        } else if (index > size) {
            return null;
        } else if (index < 0) {
            return null;
        } else {
            IntNode pointer = sentinel.next;
            for (int i = 0; i < index; i++) {
                pointer = pointer.next;
            }
            return pointer.item;
        }
    }

    public T getRecursive(int index) {
        if (size == 0) {
            return null;
        } else if (index > size) {
            return null;
        } else if (index < 0) {
            return null;
        } else {
            IntNode pointer = sentinel.next;
            return getHelper(index, pointer);
        }
    }

    private T getHelper(int index, IntNode pointer) {
        if (index == 0) {
            return pointer.item;
        } else {
            return getHelper(index - 1, pointer.next);
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
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T> {
        private IntNode pointer;

        // deleted public
        LinkedListIterator() {
            pointer = sentinel.next;
        }

        public boolean hasNext() {
            return pointer != sentinel;
        }

        public T next() {
            T returnItem = pointer.item;
            pointer = pointer.next;
            return returnItem;
        }
    }
}
