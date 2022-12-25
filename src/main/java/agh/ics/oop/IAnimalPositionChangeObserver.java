package agh.ics.oop;

public interface IAnimalPositionChangeObserver {
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition);
}
