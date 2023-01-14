package agh.ics.oop;

import java.util.Map;

public class Validation {
    public static boolean validate(Map<String, String> paramMap) {
        try {
            var height = Integer.parseInt((String) paramMap.get("map height:"));
            var width = Integer.parseInt((String) paramMap.get("map width:"));
            var numOfInitPlants = Integer.parseInt((String) paramMap.get("number of plants at start:"));
            var energyBoost = Integer.parseInt((String) paramMap.get("energy boost from plant:"));
            var numOfGrowing = Integer.parseInt((String) paramMap.get("number of plants growing daily:"));
            var numOfInitAnimals = Integer.parseInt((String) paramMap.get("number of animals at start:"));
            var numOfInitEnergy = Integer.parseInt((String) paramMap.get("animal initial energy:"));
            var energyNeeded = Integer.parseInt((String) paramMap.get("energy needed for recreation:"));
            var energyLoss = Integer.parseInt((String) paramMap.get("energy loss for new animal:"));
            var minNumMutations = Integer.parseInt((String) paramMap.get("minimum number of mutations:"));
            var maxNumMutations = Integer.parseInt((String) paramMap.get("maximum number of mutations:"));
            var genomeLength = Integer.parseInt((String) paramMap.get("genome length:"));

            if (paramMap.get("map variant:").equals("-") ||
                    paramMap.get("grass growth variant:").equals("-") ||
                    paramMap.get("mutation variant:").equals("-") ||
                    paramMap.get("animal behavior variant:").equals("-") ||
                    height <= 0 || width <= 0 ||
                    numOfInitPlants < 0 || numOfInitPlants > height * width ||
                    energyLoss < 0 ||
                    energyBoost < 0 ||
                    numOfGrowing < 0 ||
                    numOfInitAnimals <= 0 ||
                    numOfInitEnergy <= 0 ||
                    energyNeeded < 0 ||
                    minNumMutations < 0 || maxNumMutations < 0 ||
                    minNumMutations > maxNumMutations ||
                    maxNumMutations > genomeLength ||
                    genomeLength <= 0
            ) {
                return false;
            }
        } catch (NumberFormatException ex) {
            return false;
        }

        return true;
    }
}
