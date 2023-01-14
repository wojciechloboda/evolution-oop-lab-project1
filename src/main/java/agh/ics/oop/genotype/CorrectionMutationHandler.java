package agh.ics.oop.genotype;

import agh.ics.oop.genotype.IMutationHandler;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CorrectionMutationHandler implements IMutationHandler {
    private static Random rand = new Random();

    @Override
    public void mutate(List<Integer> genome, int minNumOfMutations, int maxNumOfMutations) {
        List<Integer> indecies = IntStream
                .range(0, genome.size())
                .boxed()
                .collect(Collectors.toList());

        Collections.shuffle(indecies);
        int numOfMutated = minNumOfMutations + rand.nextInt(maxNumOfMutations - minNumOfMutations + 1);

        for (int i = 0; i < numOfMutated; i++) {
            int idx = indecies.get(i);

            if (rand.nextInt(2) == 0) {
                genome.set(idx, Math.floorMod(genome.get(idx) + 1, 8));
            } else {
                genome.set(idx, Math.floorMod(genome.get(idx) - 1, 8));
            }
        }
    }
}
