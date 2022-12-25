package agh.ics.oop;
import java.util.List;

public class StableActGeneGenerator implements INextActGeneGenerator {

    public int nextActGeneIdx(int currentGeneIdx, int genomeSize) {
        return (currentGeneIdx + 1) % genomeSize;
    }
}
