package agh.ics.oop;

import java.math.BigDecimal;
import java.util.Map;

public class Parameters {
    private final Map<String, Object> paramsMap;

    public Parameters(Map<String, Object> paramsMap){
        this.paramsMap = paramsMap;
    }

    public int getWidth(){
        return ((BigDecimal) paramsMap.get("width")).intValue();
    }

    public int getHeight(){
        return ((BigDecimal) paramsMap.get("height")).intValue();
    }

    public int getNumOfInitGrass(){
        return ((BigDecimal) paramsMap.get("numOfInitGrass")).intValue();
    }

    public Variations.BoundsHandlerType getBoundsHandlerType(){
        return (Variations.BoundsHandlerType) paramsMap.get("boundsHandlerType");
    }

    public Variations.MapGrassGrowthType getGrassGrowthType(){
        return (Variations.MapGrassGrowthType) paramsMap.get("grassGrowthType");
    }

    public int getEnergyFromGrass(){
        return ((BigDecimal) paramsMap.get("energyFromGrass")).intValue();
    }

    public int numOfGrassGrowing(){
        return ((BigDecimal) paramsMap.get("numOfGrassGrowing")).intValue();
    }

    public int numOfInitAnimals(){
        return ((BigDecimal) paramsMap.get("numOfInitAnimals")).intValue();
    }

    public int startAnimalEnergy(){
        return ((BigDecimal) paramsMap.get("startAnimalEnergy")).intValue();
    }

    public int getEnergyNeededForNewAnimal(){
        return ((BigDecimal) paramsMap.get("energyNeededForNewAnimal")).intValue();
    }

    public int getEnergyLossForNewAnimal(){
        return ((BigDecimal) paramsMap.get("energyLossForNewAnimal")).intValue();
    }

    public int getMinNumOfMutations(){
        return ((BigDecimal) paramsMap.get("minNumOfMutations")).intValue();
    }

    public int getMaxNumOfMutations(){
        return ((BigDecimal) paramsMap.get("maxNumOfMutations")).intValue();
    }

    public Variations.MutationHandlerType getMutationHandlerType(){
        return (Variations.MutationHandlerType) paramsMap.get("mutationHandlerType");
    }

    public int getGenomeLength(){
        return ((BigDecimal) paramsMap.get("genomeLength")).intValue();
    }

    public Variations.NextActGeneGeneratorType getNextActGeneGeneratorType(){
        return (Variations.NextActGeneGeneratorType) paramsMap.get("nextActGeneGeneratorType");
    }
}
