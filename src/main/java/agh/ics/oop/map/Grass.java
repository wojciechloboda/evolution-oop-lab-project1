package agh.ics.oop.map;

import agh.ics.oop.Vector2d;
import agh.ics.oop.map.IMapElement;

public class Grass implements IMapElement {
    private final Vector2d position;

    public Grass(Vector2d position) {
        this.position = position;
    }

    @Override
    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public String getResourcePath() {
        return "src/main/resources/grass.png";
    }
}