package agh.ics.oop;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver{
    protected final MapVisualizer mVis;
    protected final Map<Vector2d, IMapElement> elementsMap = new HashMap<>();

    AbstractWorldMap(){
        mVis = new MapVisualizer(this);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        elementsMap.put(newPosition, elementsMap.remove(oldPosition));
    }

    @Override
    public abstract boolean canMoveTo(Vector2d position);

    @Override
    public boolean place (Animal animal){
        return false;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return !(objectAt(position) == null);
    }

    @Override
    public Object objectAt(Vector2d position) {
        return elementsMap.get(position);
    }

    public abstract Vector2d getRightUpperBound();
    public abstract Vector2d getLeftLowerBound();

    public String toString(){
        return mVis.draw(getLeftLowerBound(), getRightUpperBound());
    }
}
