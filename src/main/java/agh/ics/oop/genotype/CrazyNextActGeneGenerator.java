package agh.ics.oop.genotype;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CrazyNextActGeneGenerator implements INextActGeneGenerator {
    private final Random rand = new Random();

    public int nextActGeneIdx(int currentGeneIdx, int genomeSize) {

        if (rand.nextInt(100) >= 80) {


            int next = IntStream.range(0, genomeSize)
                    .filter((a) -> a != currentGeneIdx)
                    .boxed()
                    .collect(Collectors.toList())
                    .get(rand.nextInt(genomeSize - 1));

            return next;
        }

        return (currentGeneIdx + 1) % genomeSize;
    }
}
