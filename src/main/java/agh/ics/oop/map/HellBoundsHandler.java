package agh.ics.oop.map;

import agh.ics.oop.animal.AnimalStateAfterMove;
import agh.ics.oop.Vector2d;

import java.util.Random;

public class HellBoundsHandler implements IMapBoundsHandler{
    private final int width;
    private final int height;
    private final int energyLoss;
    private final Random rand = new Random();

    public HellBoundsHandler(int width, int height, int energyLoss){
        this.width = width;
        this.height = height;
        this.energyLoss = energyLoss;
    }
    @Override
    public AnimalStateAfterMove getAnimalStateAfterMove(Vector2d position, ExtendedMapDirection dir, int energy) {
        int x = rand.nextInt(width);
        int y = rand.nextInt(height);

        return new AnimalStateAfterMove(new Vector2d(x, y), dir, energy - energyLoss);
    }
}
