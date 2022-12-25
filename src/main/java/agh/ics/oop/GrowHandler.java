package agh.ics.oop;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*

public class GrowHandler {
    EnumeratedDistribution<Vector2d> dist;
    private final Map<Vector2d, Integer> positionToPriorityMap = new HashMap<>();
    AbstractEvolutionMap map;

    public GrowHandler(AbstractEvolutionMap map){
        this.map = map;

        for(int x = 0; x <= map.getRightUpperBound().x; x++){
            for(int y = 0; y <= map.getRightUpperBound().y; y++){
                positionToPriorityMap.put(new Vector2d(x, y), 1);
            }
        }
        //List<Pair<Integer, Double>> list = new ArrayList<>();
        //list.add(new Pair<Integer, Double>(0, 0.5));
        //list.add(new Pair<Integer, Double>(1, 0.3 ));
        //list.add(new Pair<Integer, Double>(2, 0.2));

        //dist = new EnumeratedDistribution<Vector2d>(list);
    }

    public void changePositionPriority(Vector2d position, int priority){
        positionToPriorityMap.remove(position);
        positionToPriorityMap.put(position, priority);
    }

    private List<Pair<Vector2d, Integer>> getPositionToPriorityAsListWithProb(){
        List<Pair<Vector2d, Integer>> list = new ArrayList<>();

        for(var key : positionToPriorityMap.keySet()){
            list.add(new Pair<>(key, positionToPriorityMap.get(key)));
        }
        return list;
    }

    public List<Vector2d> getGrassGrowthPos(int numberOfGrass){
        var list = getPositionToPriorityAsListWithProb();
        var resList = new ArrayList<Vector2d>();

        dist = new EnumeratedDistribution<Vector2d>(list);

        for(int i = 0; i < numberOfGrass; i++){
            var pos = dist.sample();
            resList.add(pos);
        }
        return resList;
    }

    public void get(){
        System.out.println(dist.sample());
    }
     */

