package byog.Core;

import java.util.ArrayList;


/**
 * A room in the world with a method to check room overlap.
 * width, height are the sizes of the room floor.
 * top/bottom, left/right are the y, x locations of surrounding walls.
 */
public class Room {
    int width;
    int height;
    GameWorld.Pos position;
    static ArrayList<Room> list = new ArrayList<>();
    int top;
    int bottom;
    int left;
    int right;

    Room(GameWorld.Pos p, int w, int h) {
        position = p;
        width = w;
        height = h;
        bottom = position.y - 1;
        top = bottom + height + 1;
        left = position.x - 1;
        right = left + width + 1;

        list.add(this);
    }

    /**
     *  Check if a new room is overlapping the existing rooms.
     * @param p the bottom left position of the new room floor.
     * @param w the width of the new room.
     * @param h the height of the new room.
     * @return  true if overlapping any existing room; false otherwise.
     */
    public static boolean overlap(GameWorld.Pos p, int w, int h) {
        int pxMin = p.x - 1;
        int pyMin = p.y - 1;
        int pxMax = p.x + w;
        int pyMax = p.y + h;

        for (Room r : list) {
            int rx0 = r.left;
            int rx1 = r.right;
            int ry0 = r.bottom;
            int ry1 = r.top;
            if (rx0 < pxMax && rx1 > pxMin && ry0 < pyMax && ry1 > pyMin) {
                return true;
            }
        }
        return false;
    }
}
