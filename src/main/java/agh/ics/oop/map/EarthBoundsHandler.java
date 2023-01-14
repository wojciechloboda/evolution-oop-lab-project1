package agh.ics.oop.map;

import agh.ics.oop.animal.AnimalStateAfterMove;
import agh.ics.oop.Vector2d;

public class EarthBoundsHandler implements IMapBoundsHandler {
    private final int width;
    private final int height;


    public EarthBoundsHandler(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public AnimalStateAfterMove getAnimalStateAfterMove(Vector2d position, ExtendedMapDirection dir, int energy) {
        int x = Math.floorMod(position.x, width);
        int y = getYCoord(position.y, height - 1);
        ExtendedMapDirection newDir = dir;

        if (y != position.y) {
            newDir = dir.afterRotation(4);
        }

        return new AnimalStateAfterMove(new Vector2d(x, y), newDir, energy);
    }

    private int getYCoord(int y, int boundY) {
        if (y > boundY) {
            return boundY;
        } else return Math.max(y, 0);
    }
}
