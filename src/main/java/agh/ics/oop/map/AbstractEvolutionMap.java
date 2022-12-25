package agh.ics.oop.map;

import agh.ics.oop.*;

import java.util.*;

public abstract class AbstractEvolutionMap
        implements IWorldMap, IAnimalPositionChangeObserver, IAnimalDeathObserver {
    protected final Map<Vector2d, List<Animal>> animalsMap = new HashMap<>();
    protected final Map<Vector2d, Grass> grassMap = new HashMap<>();
    private final Vector2d rightUpperBound;
    private final Vector2d leftLowerBound;
    private final IMapBoundsHandler boundsHandler; //Hell or earth
    private final MapVisualizer mVis = new MapVisualizer(this);


    public AbstractEvolutionMap(int width, int height, IMapBoundsHandler boundsHandler){
        this.leftLowerBound = new Vector2d(0, 0);
        this.rightUpperBound = new Vector2d(width - 1, height - 1);
        this.boundsHandler = boundsHandler;
    }

    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
        removeAnimalFromPosition(animal, oldPosition);
        addAnimalToPosition(animal);
    }

    protected void removeAnimalFromPosition(Animal animal, Vector2d position){
        Object animals = objectAt(position);
        if(animals != null){
            List<IMapElement> listOfElementsOnPosition = (List<IMapElement>) animals;
            listOfElementsOnPosition.remove(animal);
            if(listOfElementsOnPosition.size() == 0){
                animalsMap.remove(position);
            }
        }
    }

    private void addAnimalToPosition(Animal currentAnimal){
        var animalsInPosition = animalsMap.get(currentAnimal.getPosition());
        if(animalsInPosition != null){
            ((List<Animal>) animalsInPosition).add(currentAnimal);
        }
        else{
            List<Animal> newElements = new ArrayList<Animal>();
            newElements.add(currentAnimal);
            animalsMap.put(currentAnimal.getPosition(), newElements);
        }
    }

    private void addGrassToPosition(Grass grass){

    }

    protected abstract void removeGrassFromPosition(Vector2d position);

    @Override
    public boolean canMoveTo(Vector2d position) {
        return true;
    }

    @Override
    public boolean place(Animal animal) {
        addAnimalToPosition(animal);
        animal.addPositionChangeObserver(this);
        return true;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public Object objectAt(Vector2d position) {
        if(animalsMap.get(position) == null){
            return grassMap.get(position);
        }
        return animalsMap.get(position);
    }

    public PositionAndDirectionPair getActualPositionAfterMove(Vector2d position, ExtendedMapDirection direction) {
        if(!position.follows(this.leftLowerBound) || !position.precedes(this.rightUpperBound)){
            return boundsHandler.getAnimalPosAndDirAfterOutOfBounds(position, direction);
        }
        else {
            return new PositionAndDirectionPair(position, direction);
        }
    }

    protected Vector2d getRightUpperBound(){
        return this.rightUpperBound;
    }

    protected Vector2d getLeftLowerBound(){
        return this.leftLowerBound;
    }

    public String toString(){
        return mVis.draw(getLeftLowerBound(), getRightUpperBound());
    }

    public List<Animal> getAnimalsAtPosition(Vector2d position){
        return new ArrayList<>(animalsMap.get(position));
    }

    public Grass getGrassAtPosition(Vector2d position){
        return grassMap.get(position);
    }

    public void grassEatenAtPosition(Vector2d position) {
        this.removeGrassFromPosition(position);
    }

    protected abstract void growPlants(int numberOfPlants);
    public abstract void animalDied(Animal animal);

}
