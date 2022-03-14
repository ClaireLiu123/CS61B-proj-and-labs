package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import static byow.Core.RandomUtils.uniform;

import java.util.List;
import java.util.Random;

public class Hallways {
    private final List<Room> roomList;
    private final Random seed;

    public Hallways(List<Room> allRooms, Random seed) {
        roomList = allRooms;
        this.seed = seed;
    }

    private Room connect(Room r1, TETile[][] board) {
        Room r2 = r1.pickClosestUnconnectedRoom();

        if (r2 == null) {
            return null;
        }

        Point r1Start = r1.getConnectPoint();
        Point r2Start = r2.getConnectPoint();
        Point drawPoint = Point.copy(r1Start);
        int lastDirection = 2;
        boolean didTurn;

        while (!drawPoint.equals(r2Start)) {
            int xDistance = r2Start.x - drawPoint.x;
            int yDistance = r2Start.y - drawPoint.y;

            int direction = uniform(seed, 0, 2);
            switch (direction) {
                case 0: {
                    if (xDistance == 0) {
                        break;
                    }
                    int sign = Integer.signum(xDistance);
                    int travelDistance = sign * uniform(seed, 1, Math.abs(xDistance) + 1);
                    didTurn = direction != lastDirection;
                    drawXHallway(drawPoint, travelDistance, board, didTurn);
                    drawPoint.x += travelDistance;
                    lastDirection = direction;
                    break;
                }
                case 1: {
                    if (yDistance == 0) {
                        break;
                    }
                    int sign = Integer.signum(yDistance);
                    int travelDistance = sign * uniform(seed, 1, Math.abs(yDistance) + 1);
                    didTurn = direction != lastDirection;
                    drawYHallway(drawPoint, travelDistance, board, didTurn);
                    drawPoint.y += travelDistance;
                    lastDirection = direction;
                    break;
                }
                default: {
                    break;
                }
            }
        }
        r1.setConnected(true);
        return r2;
    }

    private void drawXHallway(Point start, int distance, TETile[][] board, boolean didTurn) {
        int currX = start.x;
        int dir = Integer.signum(distance);
        distance = Math.abs(distance);

        // Places three blocks behind the starting point
        if (didTurn) {
            for (int i = -1; i <= 1; i++) {
                placeHallwayWall(currX - dir, start.y + i, board);
            }
        }

        while (distance != 0) {
            placeHallwayWall(currX, start.y + 1, board);
            board[currX][start.y] = Tileset.FLOOR;
            placeHallwayWall(currX, start.y - 1, board);
            currX += dir;
            distance--;
        }
    }

    private void drawYHallway(Point start, int distance, TETile[][] board, boolean didTurn) {
        int currY = start.y;
        int dir = Integer.signum(distance);
        distance = Math.abs(distance);

        if (didTurn) {
            for (int i = -1; i <= 1; i++) {
                placeHallwayWall(start.x + i, currY - dir, board);
            }
        }

        while (distance != 0) {
            placeHallwayWall(start.x - 1, currY, board);
            board[start.x][currY] = Tileset.FLOOR;
            placeHallwayWall(start.x + 1, currY, board);
            currY += dir;
            distance--;
        }
    }

    private static void placeHallwayWall(int x, int y, TETile[][] board) {
        if (board[x][y].equals(Tileset.NOTHING)) {
            board[x][y] = Tileset.WALL;
        }
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    /**
     * new version of connectRooms
     */
    public void connectRooms(TETile[][] board) {
        Room tracker = roomList.get(0);
        while (tracker != null) {
            tracker = connect(tracker, board);
        }
    }
}


