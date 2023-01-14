package agh.ics.oop;

import agh.ics.oop.animal.Animal;
import agh.ics.oop.genotype.GenomeGenerator;
import agh.ics.oop.genotype.Genotype;
import agh.ics.oop.genotype.IMutationHandler;
import agh.ics.oop.genotype.INextActGeneGenerator;
import agh.ics.oop.map.AbstractEvolutionMap;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SimulationEngine implements IEngine, Runnable {
    private final Set<Animal> animalSet = new HashSet<>();
    private final Random rand = new Random();
    private final AbstractEvolutionMap map;
    private final GenomeGenerator genomeGenerator;
    private final SimulationParameters simParams;
    private final Map<String, Integer> genotypes = new ConcurrentHashMap<>();
    private double avgAnimalEnergy = 0;
    private int sumOfDaysLivedByDeadAnimals = 0;
    private int numOfDeadAnimals = 0;
    private final INextActGeneGenerator nextActGeneGenerator;
    private final List<IDayPassedObserver> observersList = new ArrayList<>();
    private boolean isPaused = false;
    private int currentDay = 0;

    private void removeDeadAnimals(int currentDay) {
        List<Animal> dead = new ArrayList<>();

        for (var animal : animalSet) {
            if (animal.getEnergy() <= 0) {
                sumOfDaysLivedByDeadAnimals += currentDay - animal.getBornAtDay();
                numOfDeadAnimals += 1;
                dead.add(animal);
                map.animalDied(animal);
                removeGenotype(animal.getGenome());
            }
        }
        dead.forEach(animalSet::remove);

    }

    private void decreaseAnimalEnergy() {
        for (var animal : animalSet) {
            animal.decreaseEnergy(1);
        }
    }

    private void animalsEating(int energyBoost) {
        Set<Vector2d> handledPosition = new HashSet<Vector2d>();

        for (var animal : animalSet) {
            if (!handledPosition.contains(animal.getPosition()) && map.getGrassAtPosition(animal.getPosition()) != null) {
                var animalsAtPosition = map.getAnimalsAtPosition(animal.getPosition());
                var bestAnimal = animalsAtPosition.stream()
                        .max(Animal::compareTo).get();

                bestAnimal.boostEnergy(energyBoost);
                map.grassEatenAtPosition(animal.getPosition());

                handledPosition.add(animal.getPosition());
            }
        }
    }

    private void animalCreation(int energyLimit, int currentDay) {
        Set<Vector2d> handledPosition = new HashSet<Vector2d>();
        ArrayList<Animal> createdAnimals = new ArrayList<Animal>();

        for (var animal : animalSet) {
            if (!handledPosition.contains(animal.getPosition())) {
                var animalsFullAtPosition = map.getAnimalsAtPosition(animal.getPosition())
                        .stream()
                        .filter((a) -> a.getEnergy() >= energyLimit)
                        .collect(Collectors.toList());

                if (animalsFullAtPosition.size() >= 2) {
                    var firstAnimal = animalsFullAtPosition.stream()
                            .max(Animal::compareTo).get();
                    var secondAnimal = animalsFullAtPosition.stream()
                            .filter((a) -> !a.equals(firstAnimal))
                            .max(Animal::compareTo).get();

                    var genome = genomeGenerator.createGenome(firstAnimal, secondAnimal);
                    firstAnimal.decreaseEnergy(this.simParams.getEnergyLossForNewAnimal());
                    firstAnimal.increaseNumOfChildren();
                    secondAnimal.decreaseEnergy(this.simParams.getEnergyLossForNewAnimal());
                    secondAnimal.increaseNumOfChildren();

                    Genotype genotype = new Genotype(genome, this.nextActGeneGenerator);
                    addGenotype(genotype);

                    Animal newAnimal = new Animal(genotype, firstAnimal.getPosition(), map, currentDay,
                            2 * this.simParams.getEnergyLossForNewAnimal());
                    map.place(newAnimal);
                    createdAnimals.add(newAnimal);
                }
                handledPosition.add(animal.getPosition());
            }
        }
        animalSet.addAll(createdAnimals);
    }

    private List<Integer> createRandomGenome(int genomeLength) {
        List<Integer> genome = new ArrayList<>();
        for (int i = 0; i < genomeLength; i++) {
            genome.add(rand.nextInt(8));
        }
        return genome;
    }

    private Vector2d getRandomPosition(AbstractEvolutionMap map) {
        int newX = rand.nextInt(map.getRightUpperBound().x + 1);
        int newY = rand.nextInt(map.getRightUpperBound().y + 1);

        return new Vector2d(newX, newY);
    }

    private void setupAnimals() {

        for (int i = 0; i < this.simParams.getNumOfInitAnimals(); i++) {
            var genotype = new Genotype(createRandomGenome(this.simParams.getGenomeLength()),
                    this.nextActGeneGenerator);
            addGenotype(genotype);
            var position = getRandomPosition(map);

            Animal animal = new Animal(genotype, position, map, 0,
                    this.simParams.getStartAnimalEnergy());
            map.place(animal);
            animalSet.add(animal);
        }
    }


    public SimulationEngine(AbstractEvolutionMap map, SimulationParameters simulationParameters,
                            IMutationHandler mutationHandler, INextActGeneGenerator nextActGeneGenerator) {
        this.map = map;
        this.genomeGenerator = new GenomeGenerator(
                mutationHandler,
                simulationParameters.getMinNumOfMutations(),
                simulationParameters.getMaxNumOfMutations());
        this.nextActGeneGenerator = nextActGeneGenerator;

        this.simParams = simulationParameters;
        setupAnimals();
    }

    @Override
    public void run() {
        currentDay = 1;
        while (!Thread.interrupted()) {
            if (!isPaused) {
                for (var animal : animalSet) {
                    animal.move();
                }
                this.removeDeadAnimals(currentDay);
                this.animalsEating(this.simParams.getEnergyFromGrass());
                this.animalCreation(this.simParams.getEnergyNeededForNewAnimal(), currentDay);
                map.growPlants(this.simParams.getNumOfGrassGrowing());

                updateAvgAnimalEnergy();
                this.decreaseAnimalEnergy();
                this.removeDeadAnimals(currentDay);

                this.notifyObservers();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                if (animalSet.size() == 0) {
                    Thread.currentThread().interrupt();
                }
                currentDay += 1;
            } else {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    } finally {
                        isPaused = false;
                    }
                }
            }
        }
    }

    public synchronized int getNumOfAnimals() {
        return this.animalSet.size();
    }

    public synchronized List<Pair<String, Integer>> getSortedGenotypes() {

        List<Pair<String, Integer>> genomeList = new ArrayList<>();
        for (var key : this.genotypes.keySet()) {
            genomeList.add(new Pair<>(key, this.genotypes.get(key)));
        }
        return genomeList.stream().sorted((a, b) -> b.getValue() - a.getValue()).collect(Collectors.toList());
    }

    public List<Animal> getAnimalsWithBestGenome() {
        if (this.getSortedGenotypes().size() == 0) {
            return null;
        }
        List<Animal> res = new ArrayList<>();
        var bestGenome = this.getSortedGenotypes().get(0).getKey();
        for (var animal : animalSet) {
            if (animal.getGenome().toString().equals(bestGenome)) {
                res.add(animal);
            }
        }
        return res;
    }

    private void addGenotype(Genotype genotype) {
        if (this.genotypes.get(genotype.toString()) != null) {
            int current = this.genotypes.get(genotype.toString());
            this.genotypes.put(genotype.toString(), current + 1);
        } else {
            this.genotypes.put(genotype.toString(), 1);
        }
    }

    private void removeGenotype(Genotype genotype) {
        if (this.genotypes.get(genotype.toString()) > 1) {
            int current = this.genotypes.get(genotype.toString());
            this.genotypes.put(genotype.toString(), current - 1);
        } else {
            this.genotypes.remove(genotype.toString());
        }
    }

    private void updateAvgAnimalEnergy() {
        if (animalSet.size() == 0) {
            this.avgAnimalEnergy = 0;
            return;
        }
        int sum = 0;
        for (var animal : animalSet) {
            sum += animal.getEnergy();
        }
        this.avgAnimalEnergy = (double) sum / (double) animalSet.size();
    }

    public synchronized double getAvgAnimalEnergy() {
        return this.avgAnimalEnergy;
    }

    public synchronized double getAvgLifetime() {
        if (numOfDeadAnimals == 0) {
            return 0;
        }

        return (double) sumOfDaysLivedByDeadAnimals / (double) numOfDeadAnimals;
    }

    public void addDayPassedObserver(IDayPassedObserver obs) {
        this.observersList.add(obs);
    }

    public void removeDayPassedObserver(IDayPassedObserver obs) {
        this.observersList.remove(obs);
    }

    private synchronized void notifyObservers() {
        for (var obs : observersList) {
            obs.dayPassed();
        }
    }

    public synchronized void setPaused() {
        this.isPaused = true;
    }

    public synchronized int getCurrentDay() {
        return this.currentDay;
    }

    public synchronized boolean isAnimalAlive(Animal animal) {
        return animalSet.contains(animal);
    }
}
