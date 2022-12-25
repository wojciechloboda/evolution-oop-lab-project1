package agh.ics.oop;

import agh.ics.oop.map.AbstractEvolutionMap;

import java.util.*;
import java.util.stream.Collectors;

public class SimulationEngine implements IEngine, Runnable{
    private final List<Animal> animalList = new ArrayList<>();
    private final Random rand = new Random();
    private final AbstractEvolutionMap map;
    private final GenomeGenerator genomeGenerator;
    private final SimulationParameters simParams;

    private void decreaseEnergyAndRemoveDeadAnimals(){
        List<Animal> dead = new ArrayList<>();
        for(var animal : animalList){
            animal.decreaseEnergy(1);
            if(animal.getEnergy() <= 0){
                dead.add(animal);
                map.animalDied(animal);
            }
        }
        animalList.removeAll(dead);
    }

    private void animalsEating(int energyBoost){
        Set<Vector2d> handledPosition = new HashSet<Vector2d>();

        for(var animal : animalList){
            if(!handledPosition.contains(animal.getPosition()) && map.getGrassAtPosition(animal.getPosition()) != null){
                var animalsAtPosition = map.getAnimalsAtPosition(animal.getPosition());
                var bestAnimal = animalsAtPosition.stream()
                        .max(Animal::compareTo).get();

                bestAnimal.boostEnergy(energyBoost);
                map.grassEatenAtPosition(animal.getPosition());

                System.out.print("Eating at position: ");
                System.out.println(animal.getPosition().toString());
                handledPosition.add(animal.getPosition());
            }
        }
    }

    private void animalCreation(int energyLimit, int currentDay){
        Set<Vector2d> handledPosition = new HashSet<Vector2d>();
        ArrayList<Animal> createdAnimals = new ArrayList<Animal>();

        for(var animal : animalList){
            if(!handledPosition.contains(animal.getPosition())){
                //logAnimals(map.getAnimalsAtPosition(animal.getPosition()).stream()
                //        .sorted(Animal::compareTo).collect(Collectors.toList()));

                var animalsFullAtPosition = map.getAnimalsAtPosition(animal.getPosition())
                        .stream()
                        .filter((a) -> a.getEnergy() >= energyLimit)
                        .collect(Collectors.toList());

                if(animalsFullAtPosition.size() >= 2){
                    var firstAnimal = animalsFullAtPosition.stream()
                            .max(Animal::compareTo).get();
                    var secondAnimal = animalsFullAtPosition.stream()
                            .filter((a) -> !a.equals(firstAnimal))
                            .max(Animal::compareTo).get();

                    //reproduce
                    var genome = genomeGenerator.createGenome(firstAnimal, secondAnimal);
                    firstAnimal.decreaseEnergy(this.simParams.energyLossForNewAnimal);
                    secondAnimal.decreaseEnergy(this.simParams.energyLossForNewAnimal);

                    Animal newAnimal = new Animal(this.simParams.nextActGeneGenerator, genome, firstAnimal.getPosition(), map, currentDay,
                            2 * this.simParams.energyLossForNewAnimal);
                    map.place(newAnimal);
                    createdAnimals.add(newAnimal);
                    System.out.print("Animal creation at: ");
                    System.out.println(newAnimal.getPosition());
                }
                handledPosition.add(animal.getPosition());
            }
        }
        animalList.addAll(createdAnimals);
    }

    private List<Integer> createRandomGenome(int genomeLength){
        List<Integer> genome = new ArrayList<>();
        genome.add(4);
        genome.add(0);
        genome.add(0);
        genome.add(0);
        genome.add(0);
        genome.add(0);
        genome.add(2);
        genome.add(2);

        return genome;
    }

    private Vector2d getRandomPosition(AbstractEvolutionMap map){
        int newX = rand.nextInt(map.getRightUpperBound().x + 1);
        int newY = rand.nextInt(map.getRightUpperBound().y + 1);

        return new Vector2d(newX, newY);
    }

    private void setupAnimals(){

        for(int i = 0; i < this.simParams.numOfInitAnimals; i++){
            var genome = createRandomGenome(this.simParams.genomeLength);
            var position = getRandomPosition(map);

            Animal animal = new Animal(this.simParams.nextActGeneGenerator, genome, position, map, 0,
                    this.simParams.startAnimalEnergy);
            map.place(animal);
            animalList.add(animal);
        }
    }


    public SimulationEngine(SimulationParameters simulationParameters){
        this.map = simulationParameters.evolutionMap;
        this.genomeGenerator = new GenomeGenerator(
                simulationParameters.mutationHandler,
                simulationParameters.minNumOfMutations,
                simulationParameters.maxNumOfMutations);

        this.simParams = simulationParameters;
        setupAnimals();
    }

    //tmp
    public void logAnimals(List<Animal> animalList){
        for(int i = 0; i < animalList.size(); i++){
            System.out.print("Animal: ");
            System.out.print(i);
            System.out.print(", Energy: ");
            System.out.print(animalList.get(i).getEnergy());
            System.out.print(", Day: ");
            System.out.print(animalList.get(i).getBornAtDay());
            System.out.print(", Children: ");
            System.out.print(animalList.get(i).getNumOfChildren());
            System.out.print(", Genome: ");
            System.out.println(animalList.get(i).getGenome());
        }
        System.out.println("done");
    }

    @Override
    public void run(){
        System.out.println(map.toString());
        for(int i = 1; i < 10; i++){
            for(var animal : animalList){
                animal.move();
            }
            this.animalsEating(this.simParams.energyFromGrass);
            this.animalCreation(this.simParams.energyNeededForNewAnimal, i);
            map.growPlants(this.simParams.numOfGrassGrowing);

            System.out.println(map.toString());
            logAnimals(animalList);
            this.decreaseEnergyAndRemoveDeadAnimals();
        }
        System.out.println(map.toString());
        logAnimals(animalList);
    }
}
