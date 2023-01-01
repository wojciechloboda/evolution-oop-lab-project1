package agh.ics.oop;

import agh.ics.oop.genotype.IMutationHandler;
import agh.ics.oop.genotype.INextActGeneGenerator;
import agh.ics.oop.map.AbstractEvolutionMap;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class SimulationParameters {
    //MAPA
    public final int width;
    public final int height;
    public final int numOfInitGrass;
    public final Variations.BoundsHandlerType boundsHandlerType;
    public final Variations.MapGrassGrowthType grassGrowthType;
    public final int energyFromGrass;
    public final int numOfGrassGrowing;
    public final int numOfInitAnimals;
    public final int startAnimalEnergy;
    public final int energyNeededForNewAnimal;
    public final int energyLossForNewAnimal;
    public final int minNumOfMutations;
    public final int maxNumOfMutations;
    public final Variations.MutationHandlerType mutationHandlerType;
    public final int genomeLength;
    public final Variations.NextActGeneGeneratorType nextActGeneGeneratorType;

    private final Map<String, Object> paramsMap = new HashMap<>();

    public SimulationParameters(Map<String, Object> paramsMap){
        this.width = ((BigDecimal) paramsMap.get("width")).intValue();
        System.out.println("?????");
        this.height = ((BigDecimal) paramsMap.get("height")).intValue();
        this.numOfInitGrass = ((BigDecimal) paramsMap.get("numOfInitGrass")).intValue();
        this.boundsHandlerType = (Variations.BoundsHandlerType) paramsMap.get("boundsHandlerType");
        this.grassGrowthType = (Variations.MapGrassGrowthType) paramsMap.get("grassGrowthType");
        this.energyFromGrass = ((BigDecimal) paramsMap.get("energyFromGrass")).intValue();
        this.numOfGrassGrowing = ((BigDecimal) paramsMap.get("numOfGrassGrowing")).intValue();
        this.numOfInitAnimals = ((BigDecimal) paramsMap.get("numOfInitAnimals")).intValue();
        this.startAnimalEnergy = ((BigDecimal) paramsMap.get("startAnimalEnergy")).intValue();
        this.energyNeededForNewAnimal = ((BigDecimal) paramsMap.get("energyNeededForNewAnimal")).intValue();
        this.energyLossForNewAnimal = ((BigDecimal) paramsMap.get("energyLossForNewAnimal")).intValue();
        this.minNumOfMutations = ((BigDecimal) paramsMap.get("minNumOfMutations")).intValue();
        this.maxNumOfMutations = ((BigDecimal) paramsMap.get("maxNumOfMutations")).intValue();
        this.mutationHandlerType = (Variations.MutationHandlerType) paramsMap.get("mutationHandlerType");
        this.genomeLength = ((BigDecimal) paramsMap.get("genomeLength")).intValue();
        this.nextActGeneGeneratorType = (Variations.NextActGeneGeneratorType) paramsMap.get("nextActGeneGeneratorType");
    }

    public SimulationParameters(
            int width,
            int height,
            int numOfInitGrass,
            Variations.BoundsHandlerType boundsHandlerType,
            Variations.MapGrassGrowthType grassGrowthType,
            int energyFromGrass,
            int numOfGrassGrowing,
            int numOfInitAnimals,
            int startAnimalEnergy,
            int energyNeededForNewAnimal,
            int energyLossForNewAnimal,
            int minNumOfMutations,
            int maxNumOfMutations,
            Variations.MutationHandlerType mutationHandlerType,
            int genomeLength,
            Variations.NextActGeneGeneratorType nextActGeneGeneratorType
    ){
        this.width = width;
        this.height = height;
        this.numOfInitGrass = numOfInitGrass;
        this.boundsHandlerType = boundsHandlerType;
        this.grassGrowthType = grassGrowthType;
        this.energyFromGrass = energyFromGrass;
        this.numOfGrassGrowing = numOfGrassGrowing;
        this.numOfInitAnimals = numOfInitAnimals;
        this.startAnimalEnergy = startAnimalEnergy;
        this.energyNeededForNewAnimal = energyNeededForNewAnimal;
        this.energyLossForNewAnimal = energyLossForNewAnimal;
        this.minNumOfMutations = minNumOfMutations;
        this.maxNumOfMutations = maxNumOfMutations;
        this.mutationHandlerType = mutationHandlerType;
        this.genomeLength = genomeLength;
        this.nextActGeneGeneratorType = nextActGeneGeneratorType;
    }

}
