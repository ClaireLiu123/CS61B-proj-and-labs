package byow.Core;

import java.io.Serializable;

/**
 * Represents an (x, y) coordinate in the world
 */
public class Point implements Serializable {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    @Override
    public boolean equals(Object obj) {
        Point other = (Point) obj;
        return (other.x == x && other.y == y);
    }

    @Override
    public int hashCode() {
        return x;
    }

    public static double distance(Point p1, Point p2) {
        double xSquared = Math.pow(p1.x - p2.x, 2);
        double ySquared = Math.pow(p1.y - p2.y, 2);
        return Math.pow(xSquared + ySquared, 0.5);
    }

    public static Point copy(Point p) {
        return new Point(p.x, p.y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

