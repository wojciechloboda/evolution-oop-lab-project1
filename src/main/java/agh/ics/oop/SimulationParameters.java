package agh.ics.oop;

import java.math.BigDecimal;
import java.util.Map;

public class SimulationParameters {
    private final Map<String, Object> paramsMap;

    public SimulationParameters(Map<String, Object> paramsMap){
        this.paramsMap = paramsMap;
    }

    public String getParamsName(){
        return (String) paramsMap.get("paramsName");
    }

    public int getWidth(){
        return Integer.parseInt((String) paramsMap.get("map width:"));
    }

    public int getHeight(){
        return Integer.parseInt((String) paramsMap.get("map height:"));
    }

    public int getNumOfInitGrass(){
        return Integer.parseInt((String) paramsMap.get("number of animals at start:"));
    }

    public Variations.BoundsHandlerType getBoundsHandlerType(){
        return (Variations.BoundsHandlerType) paramsMap.get("map variant:");
    }

    public Variations.MapGrassGrowthType getGrassGrowthType(){
        return (Variations.MapGrassGrowthType) paramsMap.get("grass growth variant:");
    }

    public int getEnergyFromGrass(){
        return Integer.parseInt((String) paramsMap.get("energy boost from plant:"));
    }

    public int getNumOfGrassGrowing(){
        return Integer.parseInt((String) paramsMap.get("number of plants growing daily:"));
    }

    public int getNumOfInitAnimals(){
        return Integer.parseInt((String) paramsMap.get("number of animals at start:"));
    }

    public int getStartAnimalEnergy(){
        return Integer.parseInt((String) paramsMap.get("animal initial energy:"));
    }

    public int getEnergyNeededForNewAnimal(){
        return Integer.parseInt((String) paramsMap.get("energy needed for recreation:"));
    }

    public int getEnergyLossForNewAnimal(){
        return Integer.parseInt((String) paramsMap.get("energy loss for new animal:"));
    }

    public int getMinNumOfMutations(){
        return Integer.parseInt((String) paramsMap.get("minimum number of mutations:"));
    }

    public int getMaxNumOfMutations(){
        return Integer.parseInt((String) paramsMap.get("maximum number of mutations:"));
    }

    public Variations.MutationHandlerType getMutationHandlerType(){
        return (Variations.MutationHandlerType) paramsMap.get("mutation variant:");
    }

    public int getGenomeLength(){
        return Integer.parseInt((String) paramsMap.get("genome length:"));
    }

    public Variations.NextActGeneGeneratorType getNextActGeneGeneratorType(){
        return (Variations.NextActGeneGeneratorType) paramsMap.get("animal behavior variant:");
    }
}
