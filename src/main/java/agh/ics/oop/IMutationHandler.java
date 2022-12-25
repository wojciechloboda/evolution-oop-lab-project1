package agh.ics.oop;

import java.util.List;

public interface IMutationHandler {
    void mutate(List<Integer> genome, int minNumOfMutations,int maxNumOfMutation);
}
