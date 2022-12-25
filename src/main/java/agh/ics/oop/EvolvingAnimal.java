package agh.ics.oop;
import java.util.ArrayList;
import java.util.List;
/*
public class EvolvingAnimal extends Animal {
    private ExtendedMapDirection mapDirection;
    private final List<Integer> genome;
    private final INextActGeneGenerator nextActGeneGenerator;
    private final List<IAnimalPositionChangeObserver> positionChangeObservers;
    private final IWorldMap map;

    private int energy;
    private int activeGeneIdx;

    public EvolvingAnimal(INextActGeneGenerator nextActGeneGenerator, List<Integer> genome, Vector2d position, IWorldMap map){
        this.genome = genome;
        this.nextActGeneGenerator = nextActGeneGenerator;
        this.activeGeneIdx = 0;
        this.mapDirection = ExtendedMapDirection.NORTH;
        this.positionChangeObservers = new ArrayList<IAnimalPositionChangeObserver>();
        this.map = map;
        this.position = position;
    }

    @Override
    public void move(MoveDirection moveDirection) {
        activeGeneIdx = nextActGeneGenerator.nextActGeneIdx(activeGeneIdx, genome.size());
        mapDirection = mapDirection.afterRotation(genome.get(activeGeneIdx));

        Vector2d newPosition = this.position.add(mapDirection.toUnitVector());
        //get actual new position and direction



        //
        //
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

    public void boostEnergy(int value){

    }

    public void decreaseEnergy(int value){

    }
}

 */
