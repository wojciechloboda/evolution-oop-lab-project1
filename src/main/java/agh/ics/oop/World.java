package agh.ics.oop;

import agh.ics.oop.genotype.CorrectionMutationHandler;
import agh.ics.oop.genotype.CrazyNextActGeneGenerator;
import agh.ics.oop.gui.App;
import agh.ics.oop.map.*;
import javafx.application.Application;

public class World {
    public static void main(String[] args) {
        Application.launch(App.class, args);

        /*

        SimulationParameters simParams = JsonConfigHandler.getParametersFromFile("src/main/resources/params.json");

        AbstractEvolutionMap evolutionMap = MapCreator.createMap(simParams);
        SimulationEngine engine = EngineCreator.createEngine(simParams, evolutionMap);
        Thread engineThread = new Thread(engine);
        engineThread.start();
         */


    }
}

