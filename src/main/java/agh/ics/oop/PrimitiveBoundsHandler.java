package agh.ics.oop;

import agh.ics.oop.map.ExtendedMapDirection;

public class PrimitiveBoundsHandler implements IMapBoundsHandler{

    @Override
    public PositionAndDirectionPair getAnimalPosAndDirAfterOutOfBounds(Vector2d position, ExtendedMapDirection dir) {
        return new PositionAndDirectionPair(new Vector2d(0, 0), ExtendedMapDirection.NORTH);
    }
}
