package agh.ics.oop.animal;

import agh.ics.oop.Vector2d;
import agh.ics.oop.map.ExtendedMapDirection;

public record AnimalStateAfterMove(Vector2d position, ExtendedMapDirection direction, int energy) {
}
