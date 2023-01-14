package agh.ics.oop.genotype;

import agh.ics.oop.genotype.INextActGeneGenerator;

public class StableActGeneGenerator implements INextActGeneGenerator {

    public int nextActGeneIdx(int currentGeneIdx, int genomeSize) {
        return (currentGeneIdx + 1) % genomeSize;
    }
}
