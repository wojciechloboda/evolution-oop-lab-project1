package agh.ics.oop;

import java.util.concurrent.ThreadLocalRandom;

public class GrassField extends AbstractWorldMap{
    private final int grassElementsCount;
    private final MapBoundry mapBoundry;

    public GrassField(int grassElementsCount){
        super();
        mapBoundry = new MapBoundry();
        this.grassElementsCount = grassElementsCount;
        this.initGrass();
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        if(objectAt(newPosition) instanceof Grass){
            elementsMap.remove(newPosition);
            mapBoundry.removeElement(newPosition, MapElementTypes.GRASS);
            placeGrass();
        }
        elementsMap.put(newPosition, elementsMap.remove(oldPosition));
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        if(objectAt(position) instanceof Animal){
            return false;
        }

        return true;
    }

    @Override
    public boolean place (Animal animal) throws IllegalArgumentException{
        if(!canMoveTo(animal.getPosition())){
            throw new IllegalArgumentException("Position " + animal.getPosition() + " is already taken by another animal");
        }
        animal.addObserver(this);
        animal.addObserver(this.mapBoundry);
        elementsMap.put(animal.getPosition(), animal);
        mapBoundry.addElement(animal.getPosition(), MapElementTypes.ANIMAL);
        return true;
    }

    @Override
    public Vector2d getLeftLowerBound(){
        return mapBoundry.getLeftLowerBound();
    }

    @Override
    public Vector2d getRightUpperBound() {
        return mapBoundry.getRightUpperBound();
    }

    private Vector2d generateRandomPositionForGrass(){
        int x;
        int y;
        x = ThreadLocalRandom.current().nextInt((int)Math.sqrt(this.grassElementsCount * 10));
        y = ThreadLocalRandom.current().nextInt((int)Math.sqrt(this.grassElementsCount * 10));
        return new Vector2d(x, y);
    }

    private void placeGrass(){
        Vector2d generatedPosition;
        do{
            generatedPosition = generateRandomPositionForGrass();
        }
        while(isOccupied(generatedPosition));

        elementsMap.put(generatedPosition, new Grass(generatedPosition));
        mapBoundry.addElement(generatedPosition, MapElementTypes.GRASS);
    }

    private void initGrass(){
        for(int i = 0; i < this.grassElementsCount; i++){
            placeGrass();
        }
    }
}
