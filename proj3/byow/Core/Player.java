package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;

public class Player implements Serializable {
    private Point location;
    private int skullNum = 0;
    private boolean direction = true;

    public Player(Point start, TETile[][] board) {
        location = start;
        board[start.x][start.y] = Tileset.AVATAR;
    }

    public void move(char moveKey, TETile[][] board) {
        board[location.x][location.y] = Tileset.FLOOR;
        switch (moveKey) {
            case 'A': {
                if (validMove(location.x - 1, location.y, board)) {
                    location.x--;
                    direction = false;
                }
                break;
            }
            case 'W': {
                if (validMove(location.x, location.y + 1, board)) {
                    location.y++;
                }
                break;
            }
            case 'S': {
                if (validMove(location.x, location.y - 1, board)) {
                    location.y--;
                }
                break;
            }
            case 'D': {
                if (validMove(location.x + 1, location.y, board)) {
                    location.x++;
                    direction = true;
                }
                break;
            }
            default: {
                break;
            }
        }
        /** check if player is in the same place as the coin
         */
        if (board[location.x][location.y].equals(Tileset.SKULL)) {
            skullNum += 1;
        }

        if (!direction) {
            board[location.x][location.y] = Tileset.AVATAR_LEFT;
        } else {
            board[location.x][location.y] = Tileset.AVATAR;
        }
    }

    public static boolean validMove(int x, int y, TETile[][] board) {
        boolean xBounds = x >= 0 && x < Engine.WIDTH;
        boolean yBounds = y >= 0 && y < Engine.HEIGHT;
        return !RoomWorld.checkWall(x, y, board) && xBounds && yBounds;
    }

    public int getSkulls() {
        return skullNum;
    }

    public Point getLocation() {
        return location;
    }
}
