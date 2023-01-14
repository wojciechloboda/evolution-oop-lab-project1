package agh.ics.oop.animal;

import agh.ics.oop.Vector2d;
import agh.ics.oop.genotype.Genotype;
import agh.ics.oop.map.AbstractEvolutionMap;
import agh.ics.oop.map.ExtendedMapDirection;
import agh.ics.oop.map.IMapElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal implements IMapElement, Comparable<Animal> {
    protected Vector2d position;
    protected final AbstractEvolutionMap map;
    private final Genotype genotype;
    private final List<IAnimalPositionChangeObserver> positionChangeObservers;
    private ExtendedMapDirection mapDirection;
    private int energy;
    private int bornAtDay;
    private int numOfChildren;
    private final Random rand = new Random();
    private int grassEaten;


    public String toString() {
        return "Animal";
    }

    public Animal(Genotype genotype,
                  Vector2d position, AbstractEvolutionMap map, int bornAtDay, int initEnergy) {
        this.map = map;
        this.position = position;
        this.genotype = genotype;
        this.mapDirection = ExtendedMapDirection.values()[rand.nextInt(8)];
        this.positionChangeObservers = new ArrayList<IAnimalPositionChangeObserver>();
        this.energy = initEnergy;
        this.bornAtDay = bornAtDay;
        this.numOfChildren = 0;
        this.grassEaten = 0;
    }

    public void increaseNumOfChildren() {
        this.numOfChildren += 1;
    }

    public int getBornAtDay() {
        return this.bornAtDay;
    }


    public void move() {
        mapDirection = mapDirection.afterRotation(genotype.getActGene());

        Vector2d newPosition = this.position.add(mapDirection.toUnitVector());

        AnimalStateAfterMove state = map.getAnimalStateAfterMove(newPosition, mapDirection, this.energy);
        newPosition = state.position();
        this.mapDirection = state.direction();
        ;
        this.energy = state.energy();

        Vector2d oldPosition = this.position;
        this.position = newPosition;
        this.notifyObservers(oldPosition, newPosition);

        genotype.nextGene();
    }

    public int getNumOfChildren() {
        return numOfChildren;
    }

    public void addPositionChangeObserver(IAnimalPositionChangeObserver observer) {
        this.positionChangeObservers.add(observer);
    }

    public void removePositionChangeObserver(IAnimalPositionChangeObserver observer) {
        this.positionChangeObservers.remove(observer);
    }

    private void notifyObservers(Vector2d oldPosition, Vector2d newPosition) {
        for (var obs : positionChangeObservers) {
            obs.positionChanged(this, oldPosition, newPosition);
        }
    }

    public Genotype getGenome() {
        return this.genotype;
    }

    public int getEnergy() {
        return this.energy;
    }


    public void boostEnergy(int value) {
        this.grassEaten += 1;
        this.energy += value;
    }

    public void decreaseEnergy(int value) {
        this.energy -= value;
    }


    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public ExtendedMapDirection getDirection() {
        return this.mapDirection;
    }

    @Override
    public String getResourcePath() {
        return "src/main/resources/";
    }

    @Override
    public int compareTo(Animal otherAnimal) {
        if (this.energy == otherAnimal.getEnergy()) {
            if (this.bornAtDay == otherAnimal.getBornAtDay()) {
                return Integer.compare(-1 * this.numOfChildren, -1 * otherAnimal.getNumOfChildren());
            } else {
                return Integer.compare(this.bornAtDay, otherAnimal.getBornAtDay());
            }
        } else {
            return Integer.compare(-1 * this.energy, -1 * otherAnimal.getEnergy());
        }
    }

    public int getNumOfGrassEaten() {
        return this.grassEaten;
    }
}
