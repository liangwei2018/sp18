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

/**
 * Contains the two main methods that allow playing of the game:
 * 1) playWithKeyBoard() method is used for playing a fresh game.
 * 2) playWithInputString(String input) takes input as a series of
 * keyboard inputs, and returns a 2D TETile array representing the
 * state of the universe after processing all the key presses.
 */

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;

    private static long seed = 0L;
    private static String moves = "";
    private static boolean gameOver = false;
    private static long lastElapsedTime = 0L;


    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {

        TETile[][] world = null;
        GameWorld.Pos playerPos = null;
        ter.initialize(WIDTH, HEIGHT);

        while (!gameOver) {
            String input = "";
            drawMenu("menu0");
            StdDraw.show();
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            if (key == 'N' || key == 'n') {
                drawMenu("menu1");
                StdDraw.show();

                while (!StdDraw.hasNextKeyTyped()) {
                    continue;
                }
                char ci = StdDraw.nextKeyTyped();
                input += String.valueOf(ci);
                drawMenu("menu1");
                StdDraw.text(WIDTH / 2, HEIGHT / 2 - 4, input);
                StdDraw.show();
                long sum = 0L;
                while (Character.isDigit(ci)) {
                    int a = Character.getNumericValue(ci);
                    sum = 10 * sum + a;
                    while (!StdDraw.hasNextKeyTyped()) {
                        continue;
                    }
                    ci = StdDraw.nextKeyTyped();
                    input += String.valueOf(ci);
                    drawMenu("menu1");
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 - 4, input);
                    StdDraw.show();
                }
                StdDraw.pause(1000);
                seed = sum;

                if (ci == 'S' || ci == 's') {
                    world = GameWorld.generateWorld(seed, WIDTH, HEIGHT);
                    playerPos = GameWorld.getPlayerPos(world);
                    if (playerPos == null) {
                        throw new IllegalArgumentException("No Player!");
                    }
                    startGame(world, playerPos);
                }
            }

            if (key == 'L' || key == 'l') {
                world = loadFile("savefile.txt");
                playerPos = GameWorld.getPlayerPos(world);
                if (playerPos == null) {
                    throw new IllegalArgumentException("No Player!");
                }
                startGame(world, playerPos);
            }

            if (key == 'Q' || key == 'q') {
                gameOver = true;
                StdDraw.clear(Color.black);
                StdDraw.show();
            }
        }
    }

    /**
     * Starts the game.
     * @param world the world to play on.
     * @param playerPos the position of the player.
     */
    public void startGame(TETile[][] world, GameWorld.Pos playerPos) {
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world);
        drawMenu("menu2");
        StdDraw.show();

        GameWorld.Pos exitPos = GameWorld.getExitPos(world);

        boolean toMainMenu = false;
        long startTime = System.nanoTime();
        long elapsedTime = System.nanoTime() - startTime + lastElapsedTime;
        drawTime(elapsedTime);

        while (!toMainMenu) {
            while (!StdDraw.hasNextKeyTyped()) {
                drawFrame(world);
                elapsedTime = System.nanoTime() - startTime + lastElapsedTime;
                drawTime(elapsedTime);
                drawMenu("menu2");


                int mx = (int) (StdDraw.mouseX());
                int my = (int) (StdDraw.mouseY());

                if (mx < WIDTH && my < HEIGHT) {
                    TETile mTile = world[mx][my];
                    if (!mTile.description().equals("nothing")) {
                        drawMenu(mTile.description());
                    }
                }
                StdDraw.show();
            }
            char c = StdDraw.nextKeyTyped();
            if (c == 'A' || c == 'a' || c == 'S' || c == 's'
                    || c == 'D' || c == 'd' || c == 'W' || c == 'w') {
                GameWorld.Pos newPos = GameWorld.movement(world, c, playerPos);
                world = GameWorld.updateWorld(world, playerPos, newPos);
                playerPos = newPos;
                drawFrame(world);
                elapsedTime = System.nanoTime() - startTime + lastElapsedTime;
                drawTime(elapsedTime);
                drawMenu("menu2");
                StdDraw.show();

                if (playerPos.equals(exitPos)) {
                    toMainMenu = true;
                    elapsedTime = System.nanoTime() - startTime + lastElapsedTime;
                    drawSuccess(elapsedTime);
                    StdDraw.show();
                    StdDraw.pause(3000);
                }
            }

            if (c == ':') {
                while (!StdDraw.hasNextKeyTyped()) {
                    continue;
                }
                char cNext = StdDraw.nextKeyTyped();
                if (cNext == 'Q' || cNext == 'q') {

                    toMainMenu = true;
                    lastElapsedTime = System.nanoTime() - startTime + lastElapsedTime;
                    saveFile(world, lastElapsedTime, "savefile.txt");
                }
            }
        }
    }

    /**
     * Draws the elapsed time on the top left corner.
     * @param elapsedTime the elapsed time in nanoseconds.
     */

    public void drawTime(long elapsedTime) {
        int totalSeconds = (int) (elapsedTime / 1_000_000_000);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds - minutes * 60;
        StdDraw.setFont(StdDraw.getFont());
        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.textLeft(5, HEIGHT - 1, "Elapsed Time(m:s): "
                + minutes + ":" + seconds);
    }

    /**
     * Draws the the message including the time you used when you win the game.
     * @param nanoSecs
     */
    public void drawSuccess(long nanoSecs) {
        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;
        int totalSeconds = (int) (nanoSecs / 1_000_000_000);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds - minutes * 60;

        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.text(midWidth, midHeight, "Congratulations! You win!");
        StdDraw.text(midWidth, midHeight - 4, "Elapsed Time(m:s): "
                + minutes + ":" + seconds);
    }

    /**
     * Draws the menus and the relevant game information
     * @param s input string.
     */

    public void drawMenu(String s) {

        if (s == null) {
            throw new IllegalArgumentException("String is null or empty.");
        }

        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;

        switch (s) {
            case "menu0":
                StdDraw.clear(Color.BLACK);
                /* Draw the GUI */
                Font fontTop = new Font("Arial", Font.BOLD, 40);
                StdDraw.setFont(fontTop);
                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.text(midWidth, 0.8 * HEIGHT, "CS61B:  THE GAME");

                /* Draw the actual text */
                Font font = new Font("Arial MS Unicode", Font.BOLD, 30);
                StdDraw.setFont(font);
                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.text(midWidth, midHeight, "New Game (N)");
                StdDraw.text(midWidth, midHeight - 2, "Load Game (L)");
                StdDraw.text(midWidth, midHeight - 4, "Quit (Q)");
                break;

            case "menu1":
                StdDraw.clear(Color.BLACK);
                font = new Font("Arial MS Unicode", Font.BOLD, 30);
                StdDraw.setFont(font);
                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.text(midWidth, midHeight, "Please enter a seed "
                        + "number:");
                StdDraw.text(midWidth, midHeight - 2, "(enter 's' after "
                        + "entering the final seed digit.)");
                break;

            case "menu2":
                StdDraw.setFont(StdDraw.getFont());
                StdDraw.setPenColor(StdDraw.YELLOW);
                StdDraw.textRight(WIDTH - 1, HEIGHT - 1, "Press "
                        + "\":Q\" to Quit ");
                break;

            default:
                StdDraw.setFont(StdDraw.getFont());
                StdDraw.setPenColor(StdDraw.YELLOW);
                StdDraw.text(midWidth, HEIGHT - 1, s);
        }
    }

    /**
     * Draws the tiles in the world.
     * @param world the world to draw on.
     */
    public void drawFrame(TETile[][] world) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        StdDraw.clear(new Color(0, 0, 0));
        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                if (world[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position "
                            + "x=" + x + ", y=" + y + " is null.");
                }
                world[x][y].draw(x, y);
            }
        }
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
            playerPos = GameWorld.getPlayerPos(world);
            if (playerPos == null) {
                throw new IllegalArgumentException("No Player!");
            }
        } else if (c0 == 'L' || c0 == 'l') {
            // load a saved game
            moves = input.substring(1);
            world = loadFile("savefile.txt");
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

                saveFile(world, lastElapsedTime, "savefile.txt");
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
    public static void saveFile(TETile[][] world, long savedTime, String filename) {
        try {
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream save = new ObjectOutputStream(file);
            save.writeLong(savedTime);
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
            lastElapsedTime = in.readLong();
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
}
