package agh.ics.oop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        int genomeLength = firstAnimal.getGenome().size();
        List<Integer> genome;
        if(firstAnimal.getEnergy() < secondAnimal.getEnergy()){
            var tmp = firstAnimal;
            firstAnimal = secondAnimal;
            secondAnimal = tmp;
        }

        int totalEnergy = firstAnimal.getEnergy() + secondAnimal.getEnergy();

        //System.out.print(firstAnimal.getGenome());
        //System.out.println(secondAnimal.getGenome());


        int lim = (int)(((double)firstAnimal.getEnergy() / (double)totalEnergy) * genomeLength);
        //System.out.print("lim: ");
        //System.out.print(firstAnimal.getEnergy());
        //System.out.print(" ");
        //System.out.print(secondAnimal.getEnergy());
        //System.out.print(" ");
        //System.out.println(lim);

        if(rand.nextInt(2) == 0){
            //System.out.println("beginStronger: ");
            genome = firstAnimal.getGenome()
                    .stream()
                    .limit(lim)
                    .collect(Collectors.toList());

            //System.out.print(genome);

            genome.addAll(secondAnimal.getGenome()
                    .stream()
                    .skip(lim)
                    .collect(Collectors.toList()));

            //System.out.println(secondAnimal.getGenome()
             //       .stream()
              //      .skip(lim)
               //     .collect(Collectors.toList()));
        }
        else{
            //System.out.println("endStronger: ");
            genome = secondAnimal.getGenome()
                    .stream()
                    .limit(genomeLength - lim)
                    .collect(Collectors.toList());

            //System.out.print(genome);

            genome.addAll(firstAnimal.getGenome()
                    .stream()
                    .skip(genomeLength - lim)
                    .collect(Collectors.toList()));

            //System.out.println(firstAnimal.getGenome()
             //       .stream()
              //      .skip(genomeLength - lim)
               //     .collect(Collectors.toList()));
        }

        System.out.print("created: ");
        mutationHandler.mutate(genome, minNumOfMutations, maxNumOfMutations);
        System.out.println(genome);

        return genome;
    }
}
