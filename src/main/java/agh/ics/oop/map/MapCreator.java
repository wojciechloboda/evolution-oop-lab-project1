package agh.ics.oop.map;

import agh.ics.oop.SimulationParameters;
import agh.ics.oop.Vector2d;
import agh.ics.oop.animal.Animal;

public class MapCreator {
    public static AbstractEvolutionMap createMap(SimulationParameters simParams){

        IMapBoundsHandler boundsHandler = switch(simParams.boundsHandlerType){
            case HELL -> new HellBoundsHandler(simParams.width, simParams.height, simParams.energyLossForNewAnimal);
            case EARTH -> new EarthBoundsHandler(simParams.width, simParams.height);
        };

        return switch (simParams.grassGrowthType){
            case GREEN_EQUATOR -> new GreenEquatorMap(simParams.width, simParams.height,
                    boundsHandler, simParams.numOfInitGrass);
            case TOXIC_DEAD -> new ToxicDeadMap(simParams.width, simParams.height,
                    boundsHandler, simParams.numOfInitGrass);
        };
    }
}
