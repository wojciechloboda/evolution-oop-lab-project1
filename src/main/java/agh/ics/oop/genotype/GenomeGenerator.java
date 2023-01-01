package agh.ics.oop.genotype;

import agh.ics.oop.animal.Animal;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GenomeGenerator {
    private final Random rand = new Random();
    private final IMutationHandler mutationHandler;
    private final int minNumOfMutations;
    private final int maxNumOfMutations;

    public GenomeGenerator(IMutationHandler mutationHandler, int minNumOfMutations, int maxNumOfMutations){
        this.mutationHandler = mutationHandler;
        this.minNumOfMutations = minNumOfMutations;
        this.maxNumOfMutations = maxNumOfMutations;

    }

    public List<Integer> createGenome(Animal firstAnimal, Animal secondAnimal) {
        int genomeLength = firstAnimal.getGenome().getGenotypeSize();
        List<Integer> genome;
        if(firstAnimal.getEnergy() < secondAnimal.getEnergy()){
            var tmp = firstAnimal;
            firstAnimal = secondAnimal;
            secondAnimal = tmp;
        }

        int totalEnergy = firstAnimal.getEnergy() + secondAnimal.getEnergy();


        int lim = (int)(((double)firstAnimal.getEnergy() / (double)totalEnergy) * genomeLength);

        if(rand.nextInt(2) == 0){
            genome = firstAnimal.getGenome().getActualGenotype()
                    .stream()
                    .limit(lim)
                    .collect(Collectors.toList());
            genome.addAll(secondAnimal.getGenome().getActualGenotype()
                    .stream()
                    .skip(lim)
                    .collect(Collectors.toList()));
        }
        else{
            genome = secondAnimal.getGenome().getActualGenotype()
                    .stream()
                    .limit(genomeLength - lim)
                    .collect(Collectors.toList());

            genome.addAll(firstAnimal.getGenome().getActualGenotype()
                    .stream()
                    .skip(genomeLength - lim)
                    .collect(Collectors.toList()));
        }

        mutationHandler.mutate(genome, minNumOfMutations, maxNumOfMutations);

        return genome;
    }
}
