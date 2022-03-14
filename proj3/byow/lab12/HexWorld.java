package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    public static void addHexagon(int s, int x, int y, TETile newValue, TETile[][] world) {
        int longestLine = (s - 1) * 2 + s;
        int nextLine = s;
        while (nextLine <= longestLine) {
            int emptySpace = (nextLine - s) / 2;
            int startingX = x - emptySpace;
            int startingY = y - emptySpace;
            for (int i = startingX; i < startingX + nextLine; i++) {
                world[i][startingY] = newValue;
            }
            nextLine += 2;
        }
        nextLine -= 2;
        int lastLineYPos = y - (s * 2 - 1);
        while (nextLine >= s) {
            int emptySpace = (nextLine - s) / 2;
            int startingX = x - emptySpace;
            int startingY = lastLineYPos + emptySpace;
            for (int i = startingX; i < startingX + nextLine; i++) {
                world[i][startingY] = newValue;
            }
            nextLine -= 2;
        }

//        int longestLine = (s - 1) * 2 + s;
//        int nextLine = s;
//        while (nextLine < longestLine) {
//            int emptySpace = (longestLine - nextLine) / 2;
//            for (int i = 0; i < emptySpace; i++) {
//                System.out.print(" ");
//            }
//            for (int i = 0; i < nextLine; i++) {
//                System.out.print("a");
//            }
//            nextLine += 2;
//            System.out.println();
//        }
//        for (int i = 0; i < longestLine; i++) {
//            System.out.print("a");
//        }
//        System.out.println();
//        for (int i = 0; i < longestLine; i++) {
//            System.out.print("a");
//        }
//        System.out.println();
//        nextLine -= 2;
//        while (nextLine >= s) {
//            int emptySpace = (longestLine - nextLine) / 2;
//            for (int i = 0; i < emptySpace; i++) {
//                System.out.print(" ");
//            }
//            for (int i = 0; i < nextLine; i++) {
//                System.out.print("a");
//            }
//            nextLine -= 2;
//            System.out.println();
//        }
    }

    public static void draw19Hex(int s, int bigHexS, int x, int y, TETile[][] world){
        int nextColHexNum = bigHexS;
        int longestColHexNum = bigHexS + (bigHexS - 1);
        while (nextColHexNum <= longestColHexNum) {
            int bigHexColStartingYPos = y + (nextColHexNum - bigHexS) * s;
            int bigHexColStartingXPos = x + s + (2 * s - 1) * (nextColHexNum - bigHexS - 1) + s - 1;
            for (int i = 0; i < nextColHexNum; i++) {
                int eachHexStartingY = bigHexColStartingYPos - i * 2 * s;
                TETile randomPattern = randomTile();
                addHexagon(s, bigHexColStartingXPos, eachHexStartingY, randomPattern, world);
            }
            nextColHexNum += 1;
        }
        nextColHexNum -= 1;
        while (nextColHexNum >= bigHexS) {
            int bigHexColStartingYPos = y + (nextColHexNum - bigHexS) * s;
            int bigHexColStartingXPos = x + (s + s + 2 * (s - 1)) * (bigHexS - 1) - (nextColHexNum - bigHexS)*(2*s-1);
            for (int i = 0; i < nextColHexNum; i++) {
                int eachHexStartingY = bigHexColStartingYPos - i * 2 * s;
                TETile randomPattern = randomTile();
                addHexagon(s, bigHexColStartingXPos, eachHexStartingY, randomPattern, world);
            }
            nextColHexNum -= 1;
        }


    }

    private static final long SEED = 2873123;

    private static final Random RANDOM = new Random(SEED);

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(3);
        switch (tileNum) {
            case 0: return Tileset.MOUNTAIN;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            default: return Tileset.WALL;
        }
    }

}




