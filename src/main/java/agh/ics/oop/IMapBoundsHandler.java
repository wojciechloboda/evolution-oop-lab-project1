package agh.ics.oop;

import agh.ics.oop.map.ExtendedMapDirection;

public interface IMapBoundsHandler {
    PositionAndDirectionPair getAnimalPosAndDirAfterOutOfBounds(Vector2d position, ExtendedMapDirection dir);
}
