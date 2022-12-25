package agh.ics.oop;

import agh.ics.oop.map.AbstractEvolutionMap;

public class SimulationParameters {
    public final int energyFromGrass;
    public final int numOfGrassGrowing;
    public final int numOfInitAnimals;
    public final int startAnimalEnergy;
    public final int energyNeededForNewAnimal;
    public final int energyLossForNewAnimal;
    public final int minNumOfMutations;
    public final int maxNumOfMutations;
    public final IMutationHandler mutationHandler;
    public final int genomeLength;
    public final INextActGeneGenerator nextActGeneGenerator;
    public final AbstractEvolutionMap evolutionMap;

    public SimulationParameters(
            int energyFromGrass,
            int numOfGrassGrowing,
            int numOfInitAnimals,
            int startAnimalEnergy,
            int energyNeededForNewAnimal,
            int energyLossForNewAnimal,
            int minNumOfMutations,
            int maxNumOfMutations,
            IMutationHandler mutationHandler,
            int genomeLength,
            INextActGeneGenerator nextActGeneGenerator,
            AbstractEvolutionMap evolutionMap
    ){
        this.energyFromGrass = energyFromGrass;
        this.numOfGrassGrowing = numOfGrassGrowing;
        this.numOfInitAnimals = numOfInitAnimals;
        this.startAnimalEnergy = startAnimalEnergy;
        this.energyNeededForNewAnimal = energyNeededForNewAnimal;
        this.energyLossForNewAnimal = energyLossForNewAnimal;
        this.minNumOfMutations = minNumOfMutations;
        this.maxNumOfMutations = maxNumOfMutations;
        this.mutationHandler = mutationHandler;
        this.genomeLength = genomeLength;
        this.nextActGeneGenerator = nextActGeneGenerator;
        this.evolutionMap = evolutionMap;
    }

}
