package byow.TileEngine;

import java.awt.Color;
import java.io.File;

import byow.Core.Utils;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 *
 * @source 0x72 pictures from 0x72 dungeon tileset https://0x72.itch.io/16x16-dungeon-tileset
 * @source rogue pictures from Arachne art thread, 8-color roguelike assets
 * https://forums.tigsource.com/index.php?topic=14166.0
 * https://creativecommons.org/licenses/by-sa/4.0/
 */

public class Tileset {
    public static final File IMAGE_DIR = Utils.join(System.getProperty("user.dir"), "image");
    public static final TETile AVATAR = new TETile('@',
            Color.white, Color.black, "you",
            Utils.join(IMAGE_DIR, "0x72", "dwarf_2.png").getPath());
    public static final TETile AVATAR_LEFT = new TETile('@',
            Color.white, Color.black, "you",
            Utils.join(IMAGE_DIR, "0x72", "dwarf_2_left.png").getPath());
    public static final TETile WALL = new TETile('#',
            new Color(216, 128, 128), Color.darkGray,
            "wall", Utils.join(IMAGE_DIR, "rogue", "blue_wall.png").getPath());
    public static final TETile FLOOR = new TETile('·',
            new Color(128, 192, 128), Color.black,
            "floor", Utils.join(IMAGE_DIR, "rogue", "floor.png").getPath());
    public static final TETile NOTHING = new TETile(' ',
            Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"',
            Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈',
            Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀',
            Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█',
            Color.orange, Color.black, "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢',
            Color.orange, Color.black, "unlocked door");
    public static final TETile SAND = new TETile('▒',
            Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲',
            Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠',
            Color.green, Color.black, "tree");
    public static final TETile SKULL = new TETile('0',
            Color.white, Color.black, "skull",
            Utils.join(IMAGE_DIR, "0x72", "skull.png").getPath());
    public static final TETile PATH = new TETile('·',
            new Color(128, 192, 128), Color.red,
            "floor");
    public static final TETile ENEMY = new TETile('E',
            Color.green, Color.black, "tree");
}



