package agh.ics.oop;

import java.util.TreeSet;

public class MapBoundry implements IPositionChangeObserver{
    private final TreeSet<PositionAndTypePair> mapElementsYCoordinateSorted;
    private final TreeSet<PositionAndTypePair> mapElementsXCoordinateSorted;

    public MapBoundry(){
        mapElementsXCoordinateSorted = new TreeSet<>((
                PositionAndTypePair o1, PositionAndTypePair o2) ->
                    compareTriplets(o1.position.x, o1.position.y, o1.type, o2.position.x, o2.position.y, o2.type));

        mapElementsYCoordinateSorted = new TreeSet<>((
                PositionAndTypePair o1, PositionAndTypePair o2) ->
                    compareTriplets(o1.position.y, o1.position.x, o1.type, o2.position.y, o2.position.x, o2.type));
    }

    public void addElement(Vector2d mapElementPosition, MapElementTypes type){
        PositionAndTypePair mapElementPair = new PositionAndTypePair(mapElementPosition, type.ordinal());
        mapElementsXCoordinateSorted.add(mapElementPair);
        mapElementsYCoordinateSorted.add(mapElementPair);
    }

    public void removeElement(Vector2d mapElementPosition, MapElementTypes type){
        PositionAndTypePair mapElementPair = new PositionAndTypePair(mapElementPosition, type.ordinal());
        mapElementsXCoordinateSorted.remove(mapElementPair);
        mapElementsYCoordinateSorted.remove(mapElementPair);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        removeElement(oldPosition, MapElementTypes.ANIMAL);
        addElement(newPosition, MapElementTypes.ANIMAL);
    }

    public Vector2d getLeftLowerBound(){
        int lowerX = mapElementsXCoordinateSorted.first().position().x;
        int lowerY = mapElementsYCoordinateSorted.first().position().y;
        return new Vector2d(lowerX, lowerY);
    }

    public Vector2d getRightUpperBound(){
        int upperX = mapElementsXCoordinateSorted.last().position().x;
        int upperY = mapElementsYCoordinateSorted.last().position().y;
        return new Vector2d(upperX, upperY);
    }

    private int compareTriplets(int a1, int b1, int c1, int a2, int b2, int c2){
        if(a1 == a2){
            if(b1 == b2){
                return c1 - c2;
            }
            else{
                return b1 - b2;
            }
        }
        else{
            return a1 - a2;
        }
    }

    private record PositionAndTypePair(Vector2d position, int type) { };
}
