package agh.ics.oop.map;

import agh.ics.oop.Vector2d;

public interface IMapElement {
    /*
    TODO
     */
    boolean isAt(Vector2d position);

    /*
    TODO
     */
    Vector2d getPosition();

    /*
    TODO
     */
    String toString();

    String getResourcePath();
}
