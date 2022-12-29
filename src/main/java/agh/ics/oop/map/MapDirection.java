package agh.ics.oop.map;

import agh.ics.oop.Vector2d;

public enum MapDirection {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    private final Vector2d uNorth = new Vector2d(0,1);
    private final Vector2d uEast = new Vector2d(1,0);
    private final Vector2d uSouth = new Vector2d(0,-1);
    private final Vector2d uWest = new Vector2d(-1,0);

    public String toString(){
        return switch (this) {
            case NORTH -> "Polnoc";
            case SOUTH -> "Poludnie";
            case WEST -> "Zachod";
            case EAST -> "Wschod";
        };
    }

    public MapDirection next(){
        return val[(ordinal() + 1) % val.length];
    }

    public MapDirection previous(){
        return val[Math.floorMod(ordinal() - 1, val.length)];
    }

    public Vector2d toUnitVector(){
        return switch (this) {
            case NORTH -> uNorth;
            case SOUTH -> uSouth;
            case EAST -> uEast;
            case WEST -> uWest;
        };
    }

    private final static MapDirection[] val = values();
}
