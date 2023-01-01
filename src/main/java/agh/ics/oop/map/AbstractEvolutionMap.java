package agh.ics.oop.map;

import agh.ics.oop.*;
import agh.ics.oop.animal.Animal;
import agh.ics.oop.animal.AnimalStateAfterMove;
import agh.ics.oop.animal.IAnimalPositionChangeObserver;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractEvolutionMap
        implements IWorldMap, IAnimalPositionChangeObserver {
    protected final Map<Vector2d, List<Animal>> animalsMap = new ConcurrentHashMap<>();
    protected final Map<Vector2d, Grass> grassMap = new ConcurrentHashMap<>();
    private final Vector2d rightUpperBound;
    private final Vector2d leftLowerBound;
    private final IMapBoundsHandler boundsHandler; //Hell or earth
    private final Random rand = new Random();
    private final List<IElementRemovedObserver> elementInMapObservers = new ArrayList<>();


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
        animalsMap.get(position).remove(animal);

        if(animalsMap.get(position).size() == 0){
            animalsMap.remove(position);
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
    public synchronized Object objectAt(Vector2d position) {
        if(animalsMap.get(position) == null){
            return grassMap.get(position);
        }
        return animalsMap.get(position).stream().max(Comparator.comparingInt(Animal::getEnergy)).get();
    }

    public AnimalStateAfterMove getAnimalStateAfterMove(Vector2d position, ExtendedMapDirection direction, int energy) {
        if(!position.follows(this.leftLowerBound) || !position.precedes(this.rightUpperBound)){
            return boundsHandler.getAnimalStateAfterMove(position, direction, energy);
        }
        else {
            return new AnimalStateAfterMove(position, direction, energy);
        }
    }

    public Vector2d getRightUpperBound(){
        return this.rightUpperBound;
    }

    public Vector2d getLeftLowerBound(){
        return this.leftLowerBound;
    }

    public String toString(){
        return "Animal";
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

    public abstract void growPlants(int numberOfPlants);
    public abstract void animalDied(Animal animal);

    protected Vector2d getRandomPosition(Set<Vector2d> priority, Set<Vector2d> normal){
        Vector2d generatedPos;
        if(priority.size() > 0 && normal.size() > 0){
            if(rand.nextInt(100) < 80){
                generatedPos = priority.stream().skip(rand.nextInt(priority.size())).findFirst().get();
                priority.remove(generatedPos);
            }
            else{
                generatedPos = normal.stream().skip(rand.nextInt(normal.size())).findFirst().get();
                normal.remove(generatedPos);
            }
        }
        else if(priority.size() > 0){
            generatedPos = priority.stream().skip(rand.nextInt(priority.size())).findFirst().get();
            priority.remove(generatedPos);
        }
        else{
            generatedPos = normal.stream().skip(rand.nextInt(normal.size())).findFirst().get();
            normal.remove(generatedPos);
        }

        return generatedPos;
    }

    public synchronized int getNumOfGrass(){
        return this.grassMap.size();
    }

    public synchronized int getNumOfFreePositions(){
            Set<Vector2d> combined = new HashSet<>();
            combined.addAll(animalsMap.keySet());
            combined.addAll(grassMap.keySet());
            combined.addAll(animalsMap.keySet());
            combined.addAll(grassMap.keySet());
            return (this.rightUpperBound.x + 1) * (this.rightUpperBound.y + 1) - combined.size();
    }

    public void addObserver(IElementRemovedObserver obs){
        this.elementInMapObservers.add(obs);
    }

    public void removeObserver(IElementRemovedObserver obs){
        this.elementInMapObservers.remove(obs);
    }

    protected void removeElementNotify(IMapElement element){
        for(var obs : this.elementInMapObservers){
            obs.elementRemoved(element);
        }
    }

}
