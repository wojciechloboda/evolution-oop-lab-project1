package agh.ics.oop;

import agh.ics.oop.map.AbstractEvolutionMap;
import agh.ics.oop.map.ExtendedMapDirection;

import java.util.ArrayList;
import java.util.List;

public class Animal implements IMapElement, Comparable<Animal>{
    protected Vector2d position;
    protected final AbstractEvolutionMap map;
    private final List<Integer> genome;
    private final INextActGeneGenerator nextActGeneGenerator;
    private final List<IAnimalPositionChangeObserver> positionChangeObservers;
    private ExtendedMapDirection mapDirection;
    private int energy;
    private int activeGeneIdx;
    private int bornAtDay;
    private int numOfChildren;


    public String toString(){
        return "Animal";
    }

    public Animal(INextActGeneGenerator nextActGeneGenerator, List<Integer> genome,
                  Vector2d position, AbstractEvolutionMap map, int bornAtDay, int initEnergy){
        this.map = map;
        this.position = position;
        this.genome = genome;
        this.nextActGeneGenerator = nextActGeneGenerator;
        this.activeGeneIdx = 0;
        this.mapDirection = ExtendedMapDirection.NORTH;
        this.positionChangeObservers = new ArrayList<IAnimalPositionChangeObserver>();
        this.energy = initEnergy;
        this.bornAtDay = bornAtDay;
        this.numOfChildren = 0;
    }

    public void increaseNumOfChildren(){
        this.numOfChildren += 1;
    }

    public int getBornAtDay(){
        return this.bornAtDay;
    }


    public void move() {
        mapDirection = mapDirection.afterRotation(genome.get(activeGeneIdx));

        Vector2d newPosition = this.position.add(mapDirection.toUnitVector());

        PositionAndDirectionPair pair = map.getActualPositionAfterMove(newPosition, mapDirection);
        newPosition = pair.position();
        this.mapDirection = pair.direction();;

        Vector2d oldPosition = this.position;
        this.position = newPosition;
        this.notifyObservers(oldPosition, newPosition);

        activeGeneIdx = nextActGeneGenerator.nextActGeneIdx(activeGeneIdx, genome.size());
    }

    public int getNumOfChildren() {
        return numOfChildren;
    }

    public void addPositionChangeObserver(IAnimalPositionChangeObserver observer){
        this.positionChangeObservers.add(observer);
    }

    public void removePositionChangeObserver(IAnimalPositionChangeObserver observer){
        this.positionChangeObservers.remove(observer);
    }

    private void notifyObservers(Vector2d oldPosition, Vector2d newPosition){
        for(var obs : positionChangeObservers){
            obs.positionChanged(this, oldPosition, newPosition);
        }
    }

    public List<Integer> getGenome() {
        return genome;
    }

    public int getEnergy(){
        return this.energy;
    }


    public void boostEnergy(int value){
        this.energy += value;
    }

    public void decreaseEnergy(int value) {
        this.energy -= value;
    }


    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }

    public Vector2d getPosition(){
        return this.position;
    }

    @Override
    public String getResourcePath() {
        return "src/main/resources/";
    }

    @Override
    public int compareTo(Animal otherAnimal) {
        if(this.energy == otherAnimal.getEnergy()){
            if(this.bornAtDay == otherAnimal.getBornAtDay()){
                return Integer.compare(-1 * this.numOfChildren, -1 * otherAnimal.getNumOfChildren());
            }
            else{
                return Integer.compare(this.bornAtDay, otherAnimal.getBornAtDay());
            }
        }
        else{
            return Integer.compare(-1 * this.energy, -1 * otherAnimal.getEnergy());
        }
    }
}
