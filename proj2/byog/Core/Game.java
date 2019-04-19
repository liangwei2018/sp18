package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

import edu.princeton.cs.introcs.StdDraw;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;



public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 20;
    public static final int HEIGHT = 10;
    //public static boolean gameOver = false;






    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
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
        long seed = 0L;
        char c0 = input.charAt(0);
        String moves = "";
        TETile[][] world = null;
        GameWorld.Pos playerPos = null;

        ter.initialize(WIDTH, HEIGHT);

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
            ter.renderFrame(world);
        } else if (c0 == 'L' || c0 == 'l') {
            // load a saved game
            moves = input.substring(1);

            world = loadFile("savefile.txt");
            ter.renderFrame(world);

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
                ter.renderFrame(world);
                StdDraw.pause(500);
            }

            if (c == ':' && (moves.charAt(i + 1) == 'Q'
                    || moves.charAt(i + 1) == 'q')) {
                //gameOver = true;

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

/*
    public TETile[][] startGame(boolean newGame, long seed, String s) {


        TETile[][] world = GameWorld.generateWorld(ter, seed, WIDTH, HEIGHT);
        while (!gameOver) {
            if (newGame) {
                GameWorld.playerInitialize(world);
                ter.renderFrame(world);
            } else {
                try
                {
                    //FileInputStream file = new FileInputStream("savefile" + seed + ".txt");
                    FileInputStream file = new FileInputStream("savefile.txt");
                    ObjectInputStream in = new ObjectInputStream(file);

                    world = (TETile[][]) in.readObject();

                    in.close();
                    file.close();

                }

                catch(IOException e)
                {
                    e.printStackTrace();
                }

                catch(ClassNotFoundException e)
                {
                    System.out.println("ClassNotFoundException!");
                }
            }


            for (int i = 0; i < s.length(); i += 1) {
                Character c = s.charAt(i);
                if (c == ':' && (s.charAt(i + 1) == 'Q' || s.charAt(i + 1) == 'q')) {
                    gameOver = true;
                    try
                    {

                        //FileOutputStream file = new FileOutputStream("savefile" + seed + ".txt");
                        FileOutputStream file = new FileOutputStream("savefile.txt");
                        ObjectOutputStream save = new ObjectOutputStream(file);

                        save.writeObject(world);


                        save.close();
                        file.close();
                    }

                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                } else {
                    world = GameWorld.movement(world, c);
                    ter.renderFrame(world);
                    StdDraw.pause(500);
                }
            }
        }
        return world;
    }
    */

}
