package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;


import java.util.Random;


/**
 * Generates and updates the random game world/map, including the position of
 * player, Entry, Exit, FLOOR, WALL.
 *
 * @author Liang Wei
 *
 */

public class GameWorld {

   // Maximum/Minimum room size.
    private static final int LMAX = 6;
    private static final int LMIN = 1;


    // wall Tile color variance maximum.
    private static final int CMAX = 32;

    private static final TETile FLOOR = Tileset.FLOOR;
    private static final TETile WALL = Tileset.WALL;
    private static final TETile PLAYER = Tileset.PLAYER;
    private static final TETile ENTRY = Tileset.LOCKED_DOOR;
    private static final TETile EXIT = Tileset.UNLOCKED_DOOR;


    /**
     * A class of position at (x, y) in the world.
     */
    public class Pos {
        int x;
        int y;
        Pos(int x0, int y0) {
            x = x0;
            y = y0;
        }

        /**
         * Check if the current (this) position and p position collapse.
         * @param p the position to compare.
         * @return true if two position collapse, false otherwise.
         */
        public boolean equals(Pos p) {
            if (this == p) {
                return true;
            }
            if (p == null) {
                return false;
            }
            return this.x == p.x && this.y == p.y;
        }
    }



    /**
     * Draw room floors and walls.
     * @param world the world to draw on
     * @param r the room to draw
     * @param rand random number
     */

    public static void drawRoom(TETile[][] world, Room r, Random rand) {

        int x0 = r.position.x;
        int y0 = r.position.y;
        int xMax = x0 + r.width;
        int yMax = y0 + r.height;

        for (int i = x0; i < xMax; i += 1) {
            for (int j = y0; j < yMax; j += 1) {
                world[i][j] = FLOOR;
            }
        }

        for (int i = r.left; i < r.right + 1; i += 1) {
            world[i][r.bottom] = TETile.colorVariant(WALL,
                    CMAX, CMAX, CMAX, rand);
            world[i][r.top] = TETile.colorVariant(WALL,
                    CMAX, CMAX, CMAX, rand);
        }

        for (int j = r.bottom; j < r.top + 1; j += 1) {
            world[r.left][j] = TETile.colorVariant(WALL,
                    CMAX, CMAX, CMAX, rand);
            world[r.right][j] = TETile.colorVariant(WALL,
                    CMAX, CMAX, CMAX, rand);
        }

    }

    /**
     * Sets the wall tile between the two neighboring rooms to floor.
     * @param world the world to draw on.
     * @param r    the current room.
     * @param next the neighbor room.
     * @param dir the direction from room r to next: 0 right, 1 top, 3 left
     */
    public static void wallToFloor(TETile[][] world, Room r, Room next, int dir) {
        int nx = next.position.x;
        int ny = next.position.y;
        int nw = next.width;
        int rw = r.width;
        int rh = r.height;
        int rt = r.top;
        int rr = r.right;

        if (dir == 0) {
            if (rh == 1) {
                world[nx - 1][rt - 1] = FLOOR;
            } else {
                world[nx - 1][ny] = FLOOR;
            }

        } else if (dir == 1) {
            if (rw == 1) {
                world[rr - 1][ny - 1] = FLOOR;
            } else {
                world[nx][ny - 1] = FLOOR;
            }

        } else if (dir == 2) {
            if (rh == 1) {
                world[nx + nw][rt - 1] = FLOOR;
            } else {
                world[nx + nw][ny] = FLOOR;
            }
        }
    }


    /**
     * Generate and draw a random neighbor room.
     * @param r the original room.
     * @param dir neighbor directions: 0 right, 1 top, 3 left.
     * @param rand random number.
     * @param w the width of the world.
     * @param h the height of the world.
     *
     */
    public static void randomNeighbor(TETile[][] world, Room r, int dir,
                               Random rand, int w, int h) {
        int xStart = r.right + 1;
        int yStart = r.top + 1;
        int randOffset = randRange(1, 5, rand);
        if (dir == 0) {
            if (r.height == 1) {
                yStart = r.top - randOffset;
            } else {
                yStart = randRange(r.bottom + 1, r.top - 1, rand);
            }
        } else if (dir == 1) {
            if (r.width == 1) {
                xStart = r.right - randOffset;
            } else {
                xStart = randRange(r.left + 1, r.right - 1, rand);
            }
        } else if (dir == 2) {
            if (r.height == 1) {
                yStart = r.top - randOffset;
            } else {
                yStart = randRange(r.bottom + 1, r.top - 1, rand);
            }
            xStart = r.left - 1;
        }

        Pos randPos = new GameWorld().new Pos(xStart, yStart);
        int minW = Math.min(randPos.x, w - randPos.x - 1);
        int maxRoomWidth = Math.min(LMAX, minW);
        int minH = Math.min(randPos.y, h - randPos.y - 1);
        int maxRoomHeight = Math.min(LMAX, minH);

        if (maxRoomHeight <= LMIN + 1 || maxRoomWidth <= LMIN + 1) {
            return;
        }

        int randHeight = randRange(LMIN, maxRoomHeight, rand);
        int randWidth = randRange(LMIN, maxRoomWidth, rand);

        if (dir == 0 && r.height > 1) {
            randHeight = 1;
        } else if (dir == 1 && r.width > 1) {
            randWidth = 1;
        } else if (dir == 2 && r.height > 1) {
            randHeight = 1;
        }

        if (r.height == 1 && (dir == 0 || dir == 2)) {
            if (randHeight < randOffset) {
                randHeight += randOffset;
            }
        } else if (r.width == 1 && dir == 1) {
            if (randWidth < randOffset) {
                randWidth += randOffset;
            }
        }


        if (dir == 2) {
            randPos = new GameWorld().new Pos(xStart - randWidth + 1, yStart);
        }

        if (Room.overlap(randPos, randWidth, randHeight)) {
            return;
        }

        Room neighborRoom = new Room(randPos, randWidth, randHeight);

        drawRoom(world, neighborRoom, rand);
        wallToFloor(world, r, neighborRoom, dir);
    }


    /** Generate a random int number in a range.
     *
     * @param xMin the minimum int number (inclusive).
     * @param xMax the maxmium int number (exclusive).
     * @param rand random number.
     * @return the random int number between xMin and xMax.
     */
    public static int randRange(int xMin, int xMax, Random rand) {
        if (xMin >= xMax || (long) xMax - xMin >= Integer.MAX_VALUE) {
            throw new IllegalArgumentException("invalid range: [" + xMin
                    + ", " + xMax + ")");
        }
        return rand.nextInt(xMax - xMin) + xMin;
    }

    /**
     * Generate the game world
     *
     * @param seed parameter gives seed,
     * @param w the width of the world.
     * @param h the height of the world.
     * @return a game World.
     */
    public static TETile[][] generateWorld(long seed, int w, int h) {

        Random rand = new Random(seed);

        TETile[][] world = new TETile[w][h];
        for (int x = 0; x < w; x += 1) {
            for (int y = 0; y < h; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        int randX0 = randRange(w / 5, w * 2 / 3, rand);
        int randY0 = randRange(3, 8, rand);
        int randWidth = randRange(LMIN, LMAX, rand);
        int randHeight = randRange(LMIN, LMAX, rand);
        GameWorld.Pos randPos = new GameWorld().new Pos(randX0, randY0);
        Room rm0 = new Room(randPos, randWidth, randHeight);
        drawRoom(world, rm0, rand);

        int maxTop = rm0.top;
        int minBottom = rm0.bottom;
        int maxIndex = 0;
        int minIndex = 0;
        int i = 0;
        while (i < Room.list.size()) {
            Room r = Room.list.get(i);

            if (r.right < w - randRange(10, 15, rand)) {
                randomNeighbor(world, r, 0, rand, w, h);
            }

            if (r.top < h - randRange(10, 15, rand)) {
                randomNeighbor(world, r, 1, rand, w, h);
            }

            if (r.left > randRange(10, 15, rand)) {
                randomNeighbor(world, r, 2, rand, w, h);
            }

            int top = r.top;
            if (top > maxTop) {
                maxTop = top;
                maxIndex = i;
            }
            int bottom = r.bottom;
            if (bottom < minBottom) {
                minBottom = bottom;
                minIndex = i;
            }
            i += 1;
        }

        for (int j = i; j < Room.list.size(); j += 1) {
            Room r = Room.list.get(j);
            int top = r.top;
            if (top > maxTop) {
                maxTop = top;
                maxIndex = j;
            }
            int bottom = r.bottom;
            if (bottom < minBottom) {
                minBottom = bottom;
                minIndex = j;
            }
        }
        Room topRoom = Room.list.get(maxIndex);
        world[topRoom.position.x + (topRoom.width / 2)][topRoom.top] = EXIT;

        Room bottomRoom = Room.list.get(minIndex);
        int bx = bottomRoom.position.x + bottomRoom.width / 2;
        int by = bottomRoom.bottom;
        world[bx][by] = ENTRY;
        world[bx][by + 1] = PLAYER;

        Room.list.clear();
        return world;
    }


    /**
     * Gets the player position.
     * @param world the world to play on.
     * @return the player position.
     */
    public static Pos getPlayerPos(TETile[][] world) {
        for (int i = 0; i < world.length; i += 1) {
            for (int j = 0; j < world[i].length; j += 1) {
                if (world[i][j].equals(PLAYER)) {
                    return new GameWorld().new Pos(i, j);
                }
            }
        }
        return null;
    }

    /**
     * Gets the player position.
     * @param world the world to play on.
     * @return the EXIT position.
     */
    public static Pos getExitPos(TETile[][] world) {
        for (int i = 0; i < world.length; i += 1) {
            for (int j = 0; j < world[i].length; j += 1) {
                if (world[i][j].equals(EXIT)) {
                    return new GameWorld().new Pos(i, j);
                }
            }
        }
        return null;
    }


    /**
     * Simulate movement based on the input character.
     * @param world the world to play on.
     * @param ch the input character.
     * @param playerPos the old position of the player.
     * @return the new position of the player.
     */
    public static Pos movement(TETile[][] world, char ch, Pos playerPos) {

        int x = playerPos.x;
        int y = playerPos.y;
        TETile nextTile = null;

        switch (ch) {
            case 'A':
            case 'a':
                x -= 1;
                break;

            case 'S':
            case 's':
                y -= 1;
                break;

            case 'D':
            case 'd':
                x += 1;
                break;

            case 'W':
            case 'w':
                y += 1;
                break;

            default:
        }
        nextTile = world[x][y];
        if (nextTile.equals(FLOOR) || nextTile.equals(EXIT)) {
            return new GameWorld().new Pos(x, y);
        }
        return new GameWorld().new Pos(playerPos.x, playerPos.y);
    }

    /**
     * Updates the world.
     * @param world the world to play on
     * @param oldPos the old position of the player.
     * @param newPos the new position of the player.
     * @return the updated world.
     */
    public static TETile[][] updateWorld(TETile[][] world, Pos oldPos, Pos newPos) {
        world[oldPos.x][oldPos.y] = FLOOR;
        world[newPos.x][newPos.y] = PLAYER;
        return world;
    }

}
