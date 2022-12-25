package agh.ics.oop;

/*


public class RectangularMap extends AbstractWorldMap{
    private final Vector2d boundsLowerLeft;
    private final Vector2d boundsUpperRight;

    public RectangularMap(int width, int height){
        super();
        boundsLowerLeft = new Vector2d(0, 0);
        boundsUpperRight = new Vector2d(width - 1, height - 1);
    }

    @Override
    public boolean place (OldAnimal oldAnimal) throws IllegalArgumentException{
        if(!canMoveTo(oldAnimal.getPosition())){
            if(!oldAnimal.getPosition().follows(boundsLowerLeft) || !oldAnimal.getPosition().precedes(boundsUpperRight)){
                throw new IllegalArgumentException("Position " + oldAnimal.getPosition() + " is out of map bounds");
            }
            throw new IllegalArgumentException("Position " + oldAnimal.getPosition() + " is already taken by another animal");
        }
        oldAnimal.addObserver(this);
        elementsMap.put(oldAnimal.getPosition(), oldAnimal);
        return true;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        if(!position.follows(boundsLowerLeft) || !position.precedes(boundsUpperRight)){
            return false;
        }

        return !isOccupied(position);
    }

    @Override
    public Vector2d getLeftLowerBound(){
        return boundsLowerLeft;
    }

    @Override
   public Vector2d getRightUpperBound(){
        return boundsUpperRight;
    }
}


 */