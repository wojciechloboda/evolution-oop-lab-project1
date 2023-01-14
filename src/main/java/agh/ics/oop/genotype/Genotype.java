package agh.ics.oop.genotype;

import java.util.List;

public class Genotype {
    private final List<Integer> actualGenotype;
    private final INextActGeneGenerator nextActGeneGenerator;
    private int currentActiveGeneIdx = 0;
    private final String genotypeRepresentation;

    public Genotype(List<Integer> newGenotype, INextActGeneGenerator nextActGeneGenerator) {
        actualGenotype = newGenotype;
        this.nextActGeneGenerator = nextActGeneGenerator;

        StringBuilder stringBuilder = new StringBuilder();
        for (var i : newGenotype) {
            stringBuilder.append(i);
        }
        genotypeRepresentation = stringBuilder.toString();
    }

    public int getGenotypeSize() {
        return actualGenotype.size();
    }

    public int getActGene() {
        return actualGenotype.get(currentActiveGeneIdx);
    }

    public void nextGene() {
        this.currentActiveGeneIdx = nextActGeneGenerator.nextActGeneIdx(currentActiveGeneIdx, actualGenotype.size());
    }

    public String toString() {
        return this.genotypeRepresentation;
    }

    public List<Integer> getActualGenotype() {
        return this.actualGenotype; // dehermetyzacja
    }


}
