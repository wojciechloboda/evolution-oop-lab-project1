package agh.ics.oop.animal;

import agh.ics.oop.Vector2d;
import agh.ics.oop.animal.Animal;

public interface IAnimalPositionChangeObserver {
    void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition);
}
