package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
/*

public class OldAnimal extends Animal {
    private MapDirection direction = MapDirection.NORTH;

    private final List<IPositionChangeObserver> observersList;

    public OldAnimal(AbstractWorldMap map) {
        this(map, new Vector2d(2, 2));
    }

    public OldAnimal(IWorldMap map, Vector2d initialPosition){
        super(map, initialPosition);
        this.observersList = new ArrayList<IPositionChangeObserver>();
    }

    public String toString(){
        return switch(direction){
            case EAST -> "E";
            case WEST -> "W";
            case NORTH -> "N";
            case SOUTH -> "S";
        };
    }

    public void move(MoveDirection moveDirection){
        Vector2d newPosition = null;

        switch(moveDirection){
            case LEFT -> this.direction = this.direction.previous();
            case RIGHT -> this.direction = this.direction.next();
            case FORWARD -> newPosition = this.position.add(this.direction.toUnitVector());
            case BACKWARD -> newPosition = this.position.subtract(this.direction.toUnitVector());
        };

        if(newPosition != null){
            if(map.canMoveTo(newPosition)){
                Vector2d oldPosition = this.position;
                this.position = newPosition;
                positionChanged(oldPosition, newPosition);
            }
        }
    }

    public void addObserver(IPositionChangeObserver observer){
        this.observersList.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer){
        this.observersList.remove(observer);
    }

    private void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for(var observer : this.observersList){
            observer.positionChanged(oldPosition, newPosition);
        }
    }

    @Override
    public String getResourcePath() {
        String path = "src/main/resources/";
        return switch(direction){
            case SOUTH -> path + "down.png";
            case NORTH -> path + "up.png";
            case EAST -> path + "right.png";
            case WEST -> path + "left.png";
        };
    }
}

 */
