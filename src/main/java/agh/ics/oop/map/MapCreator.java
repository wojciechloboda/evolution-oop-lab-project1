package agh.ics.oop.map;

import agh.ics.oop.SimulationParameters;

public class MapCreator {
    public static AbstractEvolutionMap createMap(SimulationParameters simParams) {

        IMapBoundsHandler boundsHandler = switch (simParams.getBoundsHandlerType()) {
            case HELL ->
                    new HellBoundsHandler(simParams.getWidth(), simParams.getHeight(), simParams.getEnergyLossForNewAnimal());
            case EARTH -> new EarthBoundsHandler(simParams.getWidth(), simParams.getHeight());
        };

        return switch (simParams.getGrassGrowthType()) {
            case GREEN_EQUATOR -> new GreenEquatorMap(simParams.getWidth(), simParams.getHeight(),
                    boundsHandler, simParams.getNumOfInitGrass());
            case TOXIC_DEAD -> new ToxicDeadMap(simParams.getWidth(), simParams.getHeight(),
                    boundsHandler, simParams.getNumOfInitGrass());
        };
    }
}
