package agh.ics.oop.map;

import agh.ics.oop.animal.AnimalStateAfterMove;
import agh.ics.oop.Vector2d;

public interface IMapBoundsHandler {
    AnimalStateAfterMove getAnimalStateAfterMove(Vector2d position, ExtendedMapDirection dir, int energy);
}
