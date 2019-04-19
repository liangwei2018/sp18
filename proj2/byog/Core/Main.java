package byog.Core;

import byog.TileEngine.TETile;

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byog.Core.Game class take over
 *  in either keyboard or input string mode.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println("Can only have one argument - the input string");
            System.exit(0);
        } else if (args.length == 1) {
            Game game = new Game();
            TETile[][] worldState = game.playWithInputString(args[0]);
            System.out.println(TETile.toString(worldState));
            /*
            game.startGame(worldState);
            TETile[][] worldState1 = game.playWithInputString("n3277701080089882166s");
            game.startGame(worldState1);
            System.out.println(TETile.toString(worldState1));
            for (int i = 0; i < worldState.length; i += 1) {
                for (int j = 0; j < worldState[i].length; j += 1) {
                    if (!worldState[i][j].equals(worldState1[i][j])) {
                        System.out.println("Difference: " + worldState[i][j].character()
                                + " : " + worldState1[i][j].description());
                    }
                }
            }
            */
        } else {
            Game game = new Game();
            game.playWithKeyboard();
        }
    }
}
