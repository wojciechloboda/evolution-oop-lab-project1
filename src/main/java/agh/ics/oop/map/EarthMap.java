package agh.ics.oop.map;

import agh.ics.oop.Animal;
import agh.ics.oop.Grass;
import agh.ics.oop.IMapBoundsHandler;
import agh.ics.oop.Vector2d;
import agh.ics.oop.map.AbstractEvolutionMap;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.*;

public class EarthMap extends AbstractEvolutionMap {
    private final int equatorBeginYCoord;
    private final int equatorEndYCoord;

    EnumeratedDistribution<Vector2d> dist;
    private final Map<Vector2d, Double> positionToPriorityMap = new HashMap<>();

    public EarthMap(int width, int height, IMapBoundsHandler boundsHandler, int numOfInitPlants) {
        super(width, height, boundsHandler);

        this.equatorBeginYCoord = (int)(height * 0.4);
        this.equatorEndYCoord = (int)(height * 0.6);

        for(int x = 0; x <= getRightUpperBound().x; x++) {
            for (int y = 0; y <= getRightUpperBound().y; y++) {
                if (y >= this.equatorBeginYCoord && y <= this.equatorEndYCoord) {
                    positionToPriorityMap.put(new Vector2d(x, y), 80.0);
                } else {
                    positionToPriorityMap.put(new Vector2d(x, y), 20.0);
                }
            }
        }
        growPlants(numOfInitPlants);
    }

    private List<Pair<Vector2d, Double>> getPositionToPriorityAsListWithProb(){
        List<Pair<Vector2d, Double>> list = new ArrayList<>();

        for(var key : positionToPriorityMap.keySet()){
            list.add(new Pair<>(key, positionToPriorityMap.get(key)));
        }
        return list;
    }

    @Override
    protected void growPlants(int numberOfPlants) {
        var list = getPositionToPriorityAsListWithProb();
        if(list.size() == 0){
            System.out.println("elegancko");
            return;
        }

        dist = new EnumeratedDistribution<Vector2d>(list);

        for(int i = 0; i < numberOfPlants; i++){
            var newPos = dist.sample();
            addGrassToPosition(new Grass(newPos));
        }

    }

    private void addGrassToPosition(Grass grass){
        grassMap.put(grass.getPosition(), grass);
        positionToPriorityMap.remove(grass.getPosition());
    }

    protected void removeGrassFromPosition(Vector2d position){
        grassMap.remove(position);

        if(position.y >= this.equatorBeginYCoord && position.y <= this.equatorEndYCoord){
            positionToPriorityMap.put(position, 80.0);
        }
        else{
            positionToPriorityMap.put(position, 20.0);
        }
    }

    @Override
    public void animalDied(Animal animal) {
        this.removeAnimalFromPosition(animal, animal.getPosition());
    }
}
