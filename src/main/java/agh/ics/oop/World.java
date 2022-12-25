package agh.ics.oop;
import agh.ics.oop.map.AbstractEvolutionMap;
import agh.ics.oop.map.EarthMap;

public class World {
    public static void main(String[] args) {
        //Application.launch(App.class, args);


        IMapBoundsHandler boundsHandler = new PrimitiveBoundsHandler();
        AbstractEvolutionMap evolutionMap = new EarthMap(6, 6, boundsHandler, 5);

        SimulationParameters simParams = new SimulationParameters(
            2,
            5,
            5,
            5,
            5,
            3,
            0,
            3,
            new RandomMutationHandler(),
            8,
            new StableActGeneGenerator(),
            evolutionMap);

        SimulationEngine engine = new SimulationEngine(simParams);
        Thread engineThread = new Thread(engine);
        engineThread.start();
    }
}

