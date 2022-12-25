package agh.ics.oop.map;

import agh.ics.oop.Vector2d;

public enum ExtendedMapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    private final Vector2d uNorth = new Vector2d(0,1);
    private final Vector2d uNorthEast = new Vector2d(1, 1);
    private final Vector2d uEast = new Vector2d(1,0);
    private final Vector2d uSouthEast = new Vector2d(1, -1);
    private final Vector2d uSouth = new Vector2d(0,-1);
    private final Vector2d uSouthWest = new Vector2d(-1, -1);
    private final Vector2d uWest = new Vector2d(-1,0);
    private final Vector2d uNorthWest = new  Vector2d(-1, 1);

    public String toString(){
        return switch (this) {
            case NORTH -> "Polnoc";
            case NORTHEAST -> "Polnocny wschod";
            case NORTHWEST -> "Polnocny zachod";
            case SOUTH -> "Poludnie";
            case SOUTHEAST -> "Polduniowy wschod";
            case SOUTHWEST -> "Poludniowy zachod";
            case WEST -> "Zachod";
            case EAST -> "Wschod";
        };
    }

    public ExtendedMapDirection next(){
        return val[(ordinal() + 1) % val.length];
    }

    public ExtendedMapDirection previous(){
        return val[Math.floorMod(ordinal() - 1, val.length)];
    }

    public Vector2d toUnitVector(){
        return switch (this) {
            case NORTH -> uNorth;
            case NORTHEAST -> uNorthEast;
            case NORTHWEST -> uNorthWest;
            case SOUTH -> uSouth;
            case SOUTHEAST -> uSouthEast;
            case SOUTHWEST -> uSouthWest;
            case EAST -> uEast;
            case WEST -> uWest;
        };
    }

    public ExtendedMapDirection afterRotation(int turn){
        return val[(ordinal() + turn) % val.length];
    }

    private final static ExtendedMapDirection[] val = values();
}
