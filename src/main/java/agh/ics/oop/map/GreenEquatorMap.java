package agh.ics.oop.map;

import agh.ics.oop.animal.Animal;
import agh.ics.oop.Vector2d;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.*;

public class GreenEquatorMap extends AbstractEvolutionMap {
    private final int equatorBeginYCoord;
    private final int equatorEndYCoord;

    private final Map<Vector2d, Boolean> positionToPriorityMap = new HashMap<>();


    public GreenEquatorMap(int width, int height, IMapBoundsHandler boundsHandler, int numOfInitPlants) {
        super(width, height, boundsHandler);

        this.equatorBeginYCoord = (int)(height * 0.4);
        this.equatorEndYCoord = (int)(height * 0.6);

        for(int x = 0; x <= getRightUpperBound().x; x++) {
            for (int y = 0; y <= getRightUpperBound().y; y++) {
                if (y >= this.equatorBeginYCoord && y <= this.equatorEndYCoord) {
                    positionToPriorityMap.put(new Vector2d(x, y), true);
                } else {
                    positionToPriorityMap.put(new Vector2d(x, y), false);
                }
            }
        }
        growPlants(numOfInitPlants);
    }

    @Override
    public void growPlants(int numberOfPlants) {
        Set<Vector2d> priorityPos = new HashSet<>();
        Set<Vector2d> normalPos = new HashSet<>();

        for(var key : positionToPriorityMap.keySet()){
            if(positionToPriorityMap.get(key)){
                priorityPos.add(key);
            }
            else{
                normalPos.add(key);
            }
        }


        for(int i = 0; i < numberOfPlants; i++){
            if(priorityPos.size() == 0 && normalPos.size() == 0){
                return;
            }

            var newPos = this.getRandomPosition(priorityPos, normalPos);
            addGrassToPosition(new Grass(newPos));
        }
    }

    private void addGrassToPosition(Grass grass){
        grassMap.put(grass.getPosition(), grass);
        positionToPriorityMap.remove(grass.getPosition());
    }

    protected void removeGrassFromPosition(Vector2d position){
        grassMap.remove(position);
        removeElementNotify(getGrassAtPosition(position));

        if(position.y >= this.equatorBeginYCoord && position.y <= this.equatorEndYCoord){
            positionToPriorityMap.put(position, true);
        }
        else{
            positionToPriorityMap.put(position, false);
        }
    }

    @Override
    public void animalDied(Animal animal) {
        this.removeElementNotify(animal);
        this.removeAnimalFromPosition(animal, animal.getPosition());
    }
}
