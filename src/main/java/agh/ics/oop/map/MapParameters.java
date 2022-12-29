package agh.ics.oop.map;

import agh.ics.oop.map.IMapBoundsHandler;

import java.util.Map;

public class MapParameters {
    public final int width;
    public final int height;
    public final int numOfInitPlants;
    public final IMapBoundsHandler boundsHandler;
    public final MapGrassGrowthType growthType;

    public MapParameters(
            int width,
            int height,
            int numOfInitPlants,
            IMapBoundsHandler boundsHandler,
            MapGrassGrowthType growthType
    )
    {
        this.width = width;
        this.height = height;
        this.numOfInitPlants = numOfInitPlants;
        this.boundsHandler = boundsHandler;
        this.growthType = growthType;
    }
}
