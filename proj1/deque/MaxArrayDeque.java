package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private final Comparator<T> c; // create a comparator c

    public MaxArrayDeque(Comparator<T> c) {
        super();
        this.c = c;
    }

    public T max() {
        return this.max(c);
    }

    public T max(Comparator<T> c1) { // changed c to c1
        if (this.isEmpty()) {
            return null;
        } else {
            T maxValue = this.get(0);
            //Changed the i = 0 to i = 1
            for (int i = 1; i < this.size(); i++) {
                T nextValue = this.get(i);
                if (c1.compare(maxValue, nextValue) < 0) {
                    //if grater is positive (> 0), if lesser is negative (< 0).
                    maxValue = nextValue;
                }
            }
            return maxValue;

        }
    }
}
