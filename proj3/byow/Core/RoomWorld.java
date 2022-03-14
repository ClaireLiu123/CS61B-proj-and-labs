package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;
import static byow.Core.RandomUtils.*;
import java.util.List;
import java.util.ArrayList;

public class RoomWorld {
    /* Number of rooms to make for this world*/
    private final int numRooms;
    /* Seed for the world */
    private final Random seed;
    /* list of rooms */
    private final List<Room> rooms;

    public RoomWorld(Random seed) {
        this.seed = seed;
        this.numRooms = uniform(seed, 12, 16);
        rooms = new ArrayList<>();
    }

    /**
     * Calls recursive function to draw a random number of rooms ranging from 12-15
     * @source Room generation algorithm idea: https://tinyurl.com/w33tn26a
     *
     * @param board
     */
    public void drawRandRooms(TETile[][] board) {
        drawXRooms(board, numRooms);
        for (Room r : rooms) {
            r.sortNeighborsByDistance(this);
        }
    }

    /**
     * Draw given number of rooms recursively using the brute force approach
     * i.e. creates a room and randomly puts it in different locations and
     * checks if it's valid. Repeat 50 times or until valid.
     *
     * @param board
     * @param numLeft
     */
    private void drawXRooms(TETile[][] board, int numLeft) {
        if (numLeft == 0) {
            return;
        }

        drawXRooms(board, numLeft - 1);

        Room myRoom = new Room(seed);
        for (int i = 0; i < 50; i++) {
            myRoom.setLocation(findRandomLocation());
            if (myRoom.checkValid(board)) {
                myRoom.drawRoom(board);
                /** everytime create a new room, add
                 * the room to the list if the place is
                 * a valid place
                 */
                rooms.add(myRoom);
                myRoom.findConnectPoint();
                break;
            }
        }
    }

    /**
     * Find a random location in the world to put a room
     *
     * @return
     */
    public Point findRandomLocation() {
        int x = uniform(seed, 0, Engine.WIDTH);
        int y = uniform(seed, 0, Engine.HEIGHT);
        return new Point(x, y);
    }


    public static boolean checkWall(int x, int y, TETile[][] board) {
        return board[x][y].equals(Tileset.WALL);
    }

    public List<Room> getRooms() {
        return rooms;
    }

    /** Calculated out the rough distance of two rooms
     */
    public static double calculateDistance(Room r1, Room r2) {
        return Point.distance(r1.getConnectPoint(), r2.getConnectPoint());
    }
}
