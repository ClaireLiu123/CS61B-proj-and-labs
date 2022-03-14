package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.Random;
import java.io.File;


public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 25;
    private static final int CANVAS_WIDTH = 40;
    private static final int CANVAS_HEIGHT = 30;
    private static final int TILE_SIZE = 16;
    private static final boolean PRINT_TYPED_KEYS = false;
    private static final File SAVE_FILE = new File("savedGame.txt");
    private Game currentGame;

    public Engine() {
        currentGame = new Game();
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {

        StdDraw.setCanvasSize(CANVAS_WIDTH * TILE_SIZE, CANVAS_HEIGHT * TILE_SIZE);
        StdDraw.setXscale(0, CANVAS_WIDTH);
        StdDraw.setYscale(0, CANVAS_HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.enableDoubleBuffering();

        StringBuilder input = new StringBuilder();
        drawStartingPage("", "");
        while (true) {
            char c = getNextKey();
            if (c == 'N') {
                input.append(c);
                StdDraw.clear();
                drawStartingPage("Enter a seed: ", input.toString());
            } else if (c == 'L') {
                loadWorld(true);
            } else if (c == 'C') {
                processName();
            } else if (c == 'Q') {
                System.exit(0);
            } else if (Character.isDigit(c)) {
                input.append(c);
                drawStartingPage("Enter a seed: ", input.toString());
            } else if (c == 'S') {
                input.append(c);
                drawStartingPage("Enter a seed: ", input.toString());
                TETile[][] board = interactWithInputString(input.toString());
                playWorld(board);
            }
        }
    }

    private void saveWorld() {
        Utils.writeObject(SAVE_FILE, currentGame);
    }

    private void loadWorld(boolean play) {
        if (!SAVE_FILE.exists()) {
            drawStartingPage("No saved game", "");
            return;
        }
        currentGame = Utils.readObject(SAVE_FILE, Game.class);
        if (play) {
            playWorld(currentGame.getWorld());
        }
    }

    public static char getNextKey() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (PRINT_TYPED_KEYS) {
                    System.out.print(c);
                }
                return c;
            }
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        input = input.toUpperCase();
        String extraSequence;

        if (input.indexOf('N') == 0) {
            String s = input.substring(1, input.indexOf('S'));
            long seed = Long.parseLong(s);
            currentGame.setSeed(new Random(seed));
            currentGame.setUpWorld();
            extraSequence = input.substring(input.indexOf('S') + 1);
        } else {
            loadWorld(false);
            extraSequence = input.substring(1);
        }

        boolean quit = false;
        Player player = currentGame.getPlayer();
        TETile[][] world = currentGame.getWorld();

        for (int i = 0; i < extraSequence.length(); i++) {
            char c = extraSequence.charAt(i);
            if (c == ':') {
                quit = true;
            } else if (Character.toUpperCase(c) == 'Q') {
                if (quit) {
                    saveWorld();
                    break;
                }
            }
            player.move(c, world);
        }

        return world;
    }

    /** A method used to draw the starting page of the game
     */
    public static void drawStartingPage(String prompt, String input) {
        StdDraw.clear(Color.BLACK);

        Font font1 = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font1);
        StdDraw.text(20, 21, "CS61B: The Game");

        Font font2 = new Font("Monaco", Font.BOLD, 18);
        StdDraw.setFont(font2);
        StdDraw.text(20, 17, "New Game (N)");
        StdDraw.text(20, 15, "Load Game (L)");
        StdDraw.text(20, 13, "Name Your Character (C)");
        StdDraw.text(20, 11, "Quit (Q)");
        StdDraw.text(20, 7, prompt);
        StdDraw.text(20, 5, input);

        StdDraw.show();
    }

    private void processName() {
        StringBuilder name = new StringBuilder();
        while (true) {
            drawStartingPage("Enter a name (press . to finish): ", name.toString());
            char c = getNextKey();
            if (c == '.') {
                break;
            }
            name.append(c);
        }
        drawStartingPage("", "");
        currentGame.setName(name.toString());
    }

    /**
     * place character
     * while true
     * process input
     * move
     * redraw
     *
     * @param board
     */
    public void playWorld(TETile[][] board) {
        boolean isSmall = false;
        ter.initialize(WIDTH, CANVAS_HEIGHT);
        StdDraw.setPenColor(Color.WHITE);

        boolean gameOver = false;

        Player player = currentGame.getPlayer();

        drawFrame(board);
        while (!gameOver) {
            if (StdDraw.hasNextKeyTyped()) {
                char inputKey = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (inputKey == ':') {
                    if (Character.toUpperCase(getNextKey()) == 'Q') {
                        saveWorld();
                        gameOver = true;
                    }
                } else if (inputKey == 'T') {
                    isSmall = !isSmall;
                }  else {
                    player.move(inputKey, board);
                }
            }
            if (isSmall) {
                drawFrame(currentGame.setUpSmallWorld());
            } else {
                drawFrame(board);
            }
        }
        System.exit(0);
    }

    private void drawHUD(TETile[][] board) {
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();

        TETile hoverTile;
        if (mouseY >= HEIGHT) {
            hoverTile = Tileset.NOTHING;
        } else {
            hoverTile = board[mouseX][mouseY];
        }

        String name = currentGame.getName();
        if (name == null) {
            name = "ANON HERO";
        }

        StdDraw.text(6.0, CANVAS_HEIGHT - 1, name);
        StdDraw.text(6.0, CANVAS_HEIGHT - 2, hoverTile.description());
        StdDraw.text(40.0, CANVAS_HEIGHT - 1, "Collect all the skulls!");
        StdDraw.text(6.0, CANVAS_HEIGHT - 3, "Skulls collected: "
                + currentGame.getPlayer().getSkulls());
    }

    /**
     *
     * @param board
     */
    private void drawFrame(TETile[][] board) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        drawHUD(board);
        ter.renderFrame(board);
    }
}
