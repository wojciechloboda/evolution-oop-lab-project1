package agh.ics.oop.map;

import agh.ics.oop.Vector2d;
import agh.ics.oop.animal.Animal;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.*;

public class ToxicDeadMap extends AbstractEvolutionMap {
    private final Map<Vector2d, Integer> deadAnimalsAtPosition = new HashMap<>();
    private final Map<Vector2d, Boolean> activePositions = new HashMap<>();

    public ToxicDeadMap(int width, int height, IMapBoundsHandler boundsHandler, int numOfInitPlants) {
        super(width, height, boundsHandler);

        for (int x = 0; x <= getRightUpperBound().x; x++) {
            for (int y = 0; y <= getRightUpperBound().y; y++) {
                var pos = new Vector2d(x, y);
                deadAnimalsAtPosition.put(pos, 0);
                activePositions.put(pos, true);
            }
        }
        growPlants(numOfInitPlants);
    }

    @Override
    public void growPlants(int numberOfPlants) {
        Set<Vector2d> priorityPositions = new HashSet<>();
        Set<Vector2d> normalPositions = new HashSet<>();

        int minDead = deadAnimalsAtPosition.values().stream().min(Integer::compareTo).get();

        for (var key : deadAnimalsAtPosition.keySet()) {
            if (deadAnimalsAtPosition.get(key) == minDead && activePositions.get(key)) {
                priorityPositions.add(key);
            } else if (deadAnimalsAtPosition.get(key) != minDead && activePositions.get(key)) {
                normalPositions.add(key);
            }
        }

        for (int i = 0; i < numberOfPlants; i++) {
            if (priorityPositions.size() == 0 && normalPositions.size() == 0) {
                return;
            }

            var newPos = getRandomPosition(priorityPositions, normalPositions);
            addGrassToPosition(new Grass(newPos));
        }
    }

    private void addGrassToPosition(Grass grass) {
        grassMap.put(grass.getPosition(), grass);
        activePositions.put(grass.getPosition(), false);
    }

    protected void removeGrassFromPosition(Vector2d position) {
        grassMap.remove(position);
        activePositions.put(position, true);
    }

    @Override
    public void animalDied(Animal animal) {
        int beforeDead = deadAnimalsAtPosition.get(animal.getPosition());
        this.deadAnimalsAtPosition.put(animal.getPosition(), beforeDead + 1);
        this.removeAnimalFromPosition(animal, animal.getPosition());
        this.removeElementNotify(animal);
    }
}
