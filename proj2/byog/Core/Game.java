package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;


import java.awt.Font;
import java.awt.Color;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;



public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;


    private static long seed = 0L;
    private static String moves = "";






    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT);
        drawMenu();


    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        //
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        //long seed = 0L;
        char c0 = input.charAt(0);
        TETile[][] world = null;
        GameWorld.Pos playerPos = null;
        //GameWorld GameWorld = new GameWorld();

        //ter.initialize(WIDTH, HEIGHT);

        if (c0 == 'N' || c0 == 'n') {
            // start a new game
            for (int i = 1; i < input.length(); i += 1) {
                if (Character.isDigit(input.charAt(i))) {
                    continue;
                }
                char ci = input.charAt(i);
                if (ci == 'S' || ci == 's') {
                    if (i > 1) {
                        String numberOnly = input.substring(1, i);
                        seed = Long.parseLong(numberOnly, 10);
                        moves = input.substring(i + 1);
                        break;
                    } else {
                        throw new IllegalArgumentException("No seed specified!");
                    }
                }
            }
            world = GameWorld.generateWorld(seed, WIDTH, HEIGHT);
            playerPos = GameWorld.playerInitialize(world);
            if (playerPos == null) {
                throw new IllegalArgumentException("No Player!");
            }
        //   ter.renderFrame(world);
        } else if (c0 == 'L' || c0 == 'l') {
            // load a saved game
            moves = input.substring(1);

            world = loadFile("savefile.txt");
         //   ter.renderFrame(world);

            playerPos = GameWorld.getPlayerPos(world);

            if (playerPos == null) {
                throw new IllegalArgumentException("No Player!");
            }

        } else {
            throw new IllegalArgumentException("invalid arguments: ["
                    + input + ")");
        }

        for (int i = 0; i < moves.length(); i += 1) {
            char c = moves.charAt(i);
            if (c == 'A' || c == 'a' || c == 'S' || c == 's'
                    || c == 'D' || c == 'd' || c == 'W' || c == 'w') {
                GameWorld.Pos newPos = GameWorld.movement(world, c, playerPos);
                world = GameWorld.updateWorld(world, playerPos, newPos);
                playerPos = newPos;
            }

            if (c == ':' && (moves.charAt(i + 1) == 'Q'
                    || moves.charAt(i + 1) == 'q')) {

                saveFile(world, "savefile.txt");
                break;
            }

        }

        return world;
    }

    /**
     * Saves the current world to a file.
     * @param world the current game world.
     * @param filename the name of the file.
     */
    public static void saveFile(TETile[][] world, String filename) {
        try {
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream save = new ObjectOutputStream(file);

            //save.writeObject(GameWorld.getPlayerPos(world));
            save.writeObject(world);
            save.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the world from a file.
     * @param filename the name of the file.
     * @return the game world
     */
    public static TETile[][] loadFile(String filename) {
        try {
            //FileInputStream file = new FileInputStream("savefile" + seed + ".txt");
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);
            //long seed = in.readLong();
            TETile[][] world = (TETile[][]) in.readObject();
            in.close();
            file.close();
            return world;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException!");
            return null;
        }
    }

    public void startGame(TETile[][] world) {
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world);
        //StdDraw.pause(500);

    }

    public void drawMenu() {
        /*
         * Take the string and display it in the center of the screen
         * If game is not over, display relevant game information at the top of the screen
         */
        /*
        if (s == null) {
            throw new IllegalArgumentException("String is null or empty.");
        }
*/
        int midWidthh = WIDTH / 2;
        int midHeight = HEIGHT / 2;

        /* Clears the drawing window. */
        StdDraw.clear(Color.BLACK);

        /* Draw the GUI */

        Font fontTop = new Font("Arial", Font.BOLD, 40);
        StdDraw.setFont(fontTop);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(midWidthh,   0.8 * HEIGHT,  "CS61B:  THE GAME");



        /* Draw the actual text */
        Font font = new Font("Arial MS Unicode", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);


        StdDraw.text(midWidthh, midHeight, "New Game (N)");
        StdDraw.text(midWidthh, midHeight - 2, "Load Game (L)");
        StdDraw.text(midWidthh, midHeight - 4, "Quit (N)");
        StdDraw.show();
    }


}
