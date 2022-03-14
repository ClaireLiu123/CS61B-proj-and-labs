package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Random;

import static byow.Core.RandomUtils.uniform;

public class Game implements Serializable {
    private Random seed;
    private String characterName;
    private final TETile[][] world = new TETile[Engine.WIDTH][Engine.HEIGHT];
    private Player player;
    private int numSkulls;
    private final TETile[][] smallWorld = new TETile[Engine.WIDTH][Engine.HEIGHT];

    public TETile[][] setUpWorld() {
        fillWorld(world);

        RoomWorld myWorld = new RoomWorld(seed);
        myWorld.drawRandRooms(world);

        Hallways myHalls = new Hallways(myWorld.getRooms(), seed);
        myHalls.connectRooms(world);

        Point playerStart = findRandPoint(seed);
        while (!world[playerStart.x][playerStart.y].equals(Tileset.FLOOR)) {
            playerStart = findRandPoint(seed);
        }

        player = new Player(playerStart, world);

        placeSkulls();
        return world;
    }

    public TETile[][] setUpSmallWorld() {
        fillWorld(smallWorld);
        Point location = getPlayer().getLocation();
        for (int i = Math.max(0, location.getX() - 5);
             i < Math.min(location.getX() + 5, Engine.WIDTH); i++) {
            for (int j = Math.max(0, location.getY() - 5);
                 j < Math.min(location.getY() + 5, Engine.HEIGHT); j++) {
                smallWorld[i][j] = world[i][j];
            }
        }
        return smallWorld;
    }



    public void setSeed(Random random) {
        seed = random;
    }

    public Random getSeed() {
        return seed;
    }

    public void setName(String name) {
        characterName = name;
    }

    public String getName() {
        return characterName;
    }

    public Player getPlayer() {
        return player;
    }

    public TETile[][] getWorld() {
        return world;
    }

    private static void fillWorld(TETile[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = Tileset.NOTHING;
            }
        }
    }

    private static Point findRandPoint(Random seed) {
        int x = uniform(seed, 1, Engine.WIDTH - 1);
        int y = uniform(seed, 1, Engine.HEIGHT - 1);
        return new Point(x, y);
    }

    private void placeSkulls() {
        numSkulls = uniform(seed, 8, 13);
        for (int i = 0; i < numSkulls; i++) {
            while (true) {
                Point randPlace = findRandPoint(seed);
                if (world[randPlace.x][randPlace.y].equals(Tileset.FLOOR)) {
                    world[randPlace.x][randPlace.y] = Tileset.SKULL;
                    break;
                }
            }
        }
    }




}
