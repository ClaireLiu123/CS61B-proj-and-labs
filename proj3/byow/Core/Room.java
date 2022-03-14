package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;
import java.util.TreeMap;

import static byow.Core.RandomUtils.uniform;

public class Room {
    /* Top left corner of the room */
    private Point topLeft;
    /* Bottom right corner of the room */
    private Point bottomRight;
    private Point connectPoint;
    private final int width;
    private final int height;
    private boolean connected;
    private TreeMap<Double, Room> sortedRooms;

    private final Random seed;

    /**
     * Create a room with a random width and height
     * Width and height range from 5-8 tiles long
     */
    public Room(Random seed) {
        this.seed = seed;
        width = uniform(seed, 5, 9);
        height = uniform(seed, 5, 9);
        connected = false;
    }

    public void drawRoom(TETile[][] board) {
        for (int i = topLeft.x; i <= bottomRight.x; i++) {
            board[i][topLeft.y] = Tileset.WALL;
        }

        for (int j = topLeft.y - 1; j > bottomRight.y; j--) {
            for (int i = topLeft.x; i <= bottomRight.x; i++) {
                if (i == topLeft.x || i == bottomRight.x) {
                    board[i][j] = Tileset.WALL;
                } else {
                    // TODO
                    /** I changed the Tileset.Floor to the tileset.Unlocked_door
                     * and it appears gold coin which I did not expect
                     * feel free to change back. I just feel it is pretty cool
                     */
                    board[i][j] = Tileset.FLOOR;
                }
            }
        }

        for (int i = topLeft.x; i <= bottomRight.x; i++) {
            board[i][bottomRight.y] = Tileset.WALL;
        }

    }

    public Point getStart() {
        return topLeft;
    }

    /**
     * Set the location of the top left corner of the room
     * Location of the bottom right is determined by the width and height
     *
     * @param start
     */
    public void setLocation(Point start) {
        topLeft = start;
        bottomRight = new Point(topLeft.x + width - 1, topLeft.y - height + 1);
    }

    public boolean checkValid(TETile[][] board) {
        if (bottomRight.x >= Engine.WIDTH || bottomRight.y < 0) {
            return false;
        }

        return !overlap(board);
    }

    public boolean overlap(TETile[][] board) {
        for (int i = topLeft.x; i <= bottomRight.x; i++) {
            if (RoomWorld.checkWall(i, topLeft.y, board)) {
                return true;
            }
        }

        for (int j = topLeft.y - 1; j > bottomRight.y; j--) {
            // check inside of room as well to see if theres a room within a room
            if (RoomWorld.checkWall(topLeft.x, j, board)
                    || RoomWorld.checkWall(bottomRight.x, j, board)) {
                return true;
            }
        }

        for (int i = topLeft.x; i <= bottomRight.x; i++) {
            if (RoomWorld.checkWall(i, bottomRight.y, board)) {
                return true;
            }
        }

        return false;
    }

    public void findConnectPoint() {
        int x = uniform(seed, topLeft.x + 1, bottomRight.x);
        int y = uniform(seed, bottomRight.y + 1, topLeft.y);
        connectPoint = new Point(x, y);
    }

    public Point getConnectPoint() {
        return connectPoint;
    }

    public void sortNeighborsByDistance(RoomWorld currWorld) {
        sortedRooms = new TreeMap<>();
        for (Room r : currWorld.getRooms()) {
            sortedRooms.put(RoomWorld.calculateDistance(this, r), r);
        }
        sortedRooms.remove(0.0);
    }

    public Room pickClosestUnconnectedRoom() {
        Double shortestValid = sortedRooms.firstKey();
        Room closestRoom = sortedRooms.get(shortestValid);

        while (closestRoom.isConnected()) {
            shortestValid = sortedRooms.ceilingKey(shortestValid + 0.00001);
            if (shortestValid == null) {
                return null;
            }
            closestRoom = sortedRooms.get(shortestValid);
        }

        return closestRoom;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean flag) {
        connected = flag;
    }
}
