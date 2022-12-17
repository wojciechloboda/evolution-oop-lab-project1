package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine implements IEngine, Runnable{
    private ArrayList<MoveDirection> directionList;
    private final List<Animal> animalList = new ArrayList<>();
    private final List<IMoveObserver> observersList = new ArrayList<>();
    private final int moveDelay;

    private void setupAnimals(AbstractWorldMap map, Vector2d[] initPositions){
        for(var pos : initPositions){
            Animal newAnimal = new Animal(map, pos);

            if(map.place(newAnimal)){
                animalList.add(newAnimal);
            }
        }
    }

    public void setDirections(ArrayList<MoveDirection> directionList){
        this.directionList = directionList;
    }

    public SimulationEngine(AbstractWorldMap map, Vector2d[] initPositions, int moveDelay){
        this.directionList = new ArrayList<>();
        this.moveDelay = moveDelay;
        setupAnimals(map, initPositions);
    }

    public SimulationEngine(ArrayList<MoveDirection> directionList, AbstractWorldMap map, Vector2d[] initPositions){
        this.directionList = directionList;
        moveDelay = 0;
        setupAnimals(map, initPositions);
    }

    @Override
    public void run(){
        for(int i = 0; i < directionList.size(); i++){
            animalList.get(i % animalList.size()).move(directionList.get(i));

            notifyObservers();

            try{
                Thread.sleep(moveDelay);
            }
            catch(InterruptedException ex){
                Thread.currentThread().interrupt();
            }
        }
    }

    public void addObserver(IMoveObserver observer){
        this.observersList.add(observer);
    }

    public void removeObserver(IMoveObserver observer){
        this.observersList.remove(observer);
    }

    private void notifyObservers(){
        for(var observer : this.observersList){
            observer.moveHappened();
        }
    }
}
