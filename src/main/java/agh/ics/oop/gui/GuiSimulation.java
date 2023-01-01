package agh.ics.oop.gui;

import agh.ics.oop.IDayPassedObserver;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.SimulationParameters;
import agh.ics.oop.Vector2d;
import agh.ics.oop.animal.Animal;
import agh.ics.oop.map.AbstractEvolutionMap;
import agh.ics.oop.map.IElementRemovedObserver;
import agh.ics.oop.map.IMapElement;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GuiSimulation implements IDayPassedObserver, IElementRemovedObserver, EventHandler {
    private final Stage simulationStage;
    private final GridPane grid;
    private AnimalStatPanel right;
    private final StatPanel left;
    private final int windowHeight = 600;
    private final int windowWeight = 1000;
    private final AbstractEvolutionMap map;
    private final Thread engineThread;
    private final Map<IMapElement, GuiElementBox> mapElementToRepresentation;
    private final SimulationEngine engine;
    private final BorderPane mainPane;
    private GuiElementBox chosenBox = null;
    private final Set<BorderPane> bestGenomePanes = new HashSet<>();
    private boolean showBestGenomeFlag = false;
    private BorderPane[][] gridSpots;
    private double gridSpotWidth = 0;
    private final EventHandler<ActionEvent> animalStatButtonHandler;
    private final ArrayList<String[]> totalStats= new ArrayList<String[]>();
    private boolean withSaving = false;
    private String pathToStats = null;

    private void setUpGrid(GridPane grid, AbstractEvolutionMap map){
        int gridRowsCount = map.getRightUpperBound().y;
        int gridColumnsCount = map.getRightUpperBound().x;

        double width = (windowWeight* 0.40 / Math.max(gridRowsCount, gridColumnsCount));
        double height = (windowWeight * 0.40 / Math.max(gridRowsCount, gridColumnsCount));
        this.gridSpotWidth = width;

        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();

        for(int i = 0; i <= gridRowsCount; i++){
            grid.getRowConstraints().add(new RowConstraints(height));
        }

        for(int i = 0; i <= gridColumnsCount; i++){
            grid.getColumnConstraints().add(new ColumnConstraints(width));
        }

        gridSpots = new BorderPane[gridColumnsCount + 1][gridRowsCount + 1];

        for(int i = 0; i <= gridColumnsCount; i++){
            for(int j = 0; j <= gridRowsCount; j++){
                gridSpots[i][j] = new BorderPane();
                gridSpots[i][j].setStyle(
                                "-fx-background-color: #9ce738;" +
                                "-fx-border-width:" + Double.toString(width * 0.05) + ";" +
                                "-fx-border-color: #ffffff;"
                );

                grid.add(gridSpots[i][j], i, j);
            }
        }
        grid.setStyle(
                "-fx-background-color: #575951;"
        );
    }

    private GridPane createGrid(AbstractEvolutionMap map){
        GridPane newGrid = new GridPane();
        setUpGrid(newGrid, map);

        return newGrid;
    }

    public GuiSimulation(SimulationEngine engine, AbstractEvolutionMap map, String pathToStats){
        engine.addDayPassedObserver(this);
        simulationStage = new Stage();
        simulationStage.setTitle("SYMULACJA");
        this.map = map;
        this.map.addObserver(this);
        ImageLoader.loadImages();

        if(pathToStats != null){
            withSaving = true;
            this.pathToStats = pathToStats;
            totalStats.add(new String[]{
                    "currentDay",
                    "animalsTotalNum",
                    "plantsTotalNum",
                    "freePositionsNum",
                    "bestGenome",
                    "avgAnimalEnergy",
                    "avgAnimalLifetime"});
            logStats(engine, map);
        }

        this.engine = engine;

        this.grid = createGrid(map);
        this.mapElementToRepresentation = new HashMap<>();

        grid.setAlignment(Pos.CENTER);
        fillGridWithMapElements(grid, map);

        this.left = new StatPanel(engine, map,windowWeight * 0.3);

        BorderPane gridPanel = new BorderPane();
        gridPanel.setMinHeight(windowHeight);
        gridPanel.setMinWidth(windowWeight * 0.6);
        gridPanel.setCenter(grid);
        //gridPanel.setStyle("-fx-background-color: #" + "ffd700");

        var buttonPanel = createButtonPanel();
        var dummyRight = createDummyRight(windowWeight * 0.3);

        mainPane = new BorderPane();
        mainPane.setCenter(gridPanel);
        mainPane.setLeft(left);
        mainPane.setRight(dummyRight);
        mainPane.setBottom(buttonPanel);
        mainPane.setPrefHeight(windowHeight);
        mainPane.setPrefWidth(windowWeight);
        Scene scene = new Scene(mainPane);

        simulationStage.setScene(scene);
        simulationStage.show();

        this.engineThread = new Thread(engine);

        this.simulationStage.setOnCloseRequest(e ->{
            if(engineThread.isAlive()) {
                engineThread.interrupt();
                if(withSaving) {
                    saveStats(pathToStats);
                }
            }
        });

        animalStatButtonHandler = event -> {
            mainPane.setRight(dummyRight);
            chosenBox.removeHighlight();
            chosenBox = null;
        };
    }

    private void fillGridWithMapElements(GridPane grid, AbstractEvolutionMap map){
        int gridInsideHeight = map.getRightUpperBound().y;

        for(int x = 0; x <= map.getRightUpperBound().x; x++){
            for(int y = 0; y <= map.getRightUpperBound().y; y++){
                Vector2d position = new Vector2d(x, y);
                if(map.isOccupied(position)){
                    IMapElement elem = (IMapElement) map.objectAt(position);
                    int currentColumn = x;
                    int currentRow = gridInsideHeight - (y - map.getLeftLowerBound().y);
                    var newGuiElement = new GuiElementBox(grid.getColumnConstraints().get(0).getPrefWidth(), elem);

                    if(mapElementToRepresentation.get(elem) == null){
                        if(elem instanceof Animal){
                            newGuiElement.setOnMouseClicked(this);
                        }
                        mapElementToRepresentation.put(elem, newGuiElement);
                    }

                    if(chosenBox != null && chosenBox.getElement().isAt(position) &&
                            engine.isAnimalAlive((Animal)chosenBox.getElement())){
                        chosenBox.updateElementRepresentation();
                        gridSpots[currentColumn][currentRow].setCenter(chosenBox);
                    }
                    else if(mapElementToRepresentation.get((IMapElement) map.objectAt(position)) != null){
                        mapElementToRepresentation.get((IMapElement) map.objectAt(position)).updateElementRepresentation();
                        gridSpots[currentColumn][currentRow].setCenter(mapElementToRepresentation.get((IMapElement) map.objectAt(position)));
                    }
                }
            }
        }
    }

    private BorderPane createDummyRight(Double width){
        BorderPane pane = new BorderPane();
        pane.setPrefWidth(width);
        var content = new BorderPane();
        content.setCenter(new Label("NO ANIMAL CHOSEN"));
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setCenter(content);
        return pane;
    }

    private void clearGrid(){
        int gridInsideHeight = map.getRightUpperBound().y;
        for(int x = 0; x <= map.getRightUpperBound().x; x++) {
            for (int y = 0; y <= map.getRightUpperBound().y; y++) {
                int currentColumn = x;
                int currentRow = gridInsideHeight - (y - map.getLeftLowerBound().y);
                gridSpots[currentColumn][currentRow].getChildren().clear();
            }
        }
    }

    private BorderPane createButtonPanel(){
        BorderPane buttonPanel = new BorderPane();
        Button startButton = new Button("START");
        Button stopButton = new Button("||");
        Button quitButton = new Button("END");
        Button bestGenome = new Button("MARK BEST GENOME");

        bestGenome.setOnAction(e ->{
            if(!showBestGenomeFlag){
                showBestGenome();
            }
            else{
                unshowBestGenome();
            }
            showBestGenomeFlag = !showBestGenomeFlag;
        });

        startButton.setOnAction(e -> {
            if(engineThread.getState().equals(Thread.State.WAITING)){
                synchronized (engine){
                    engine.notify();
                }
            }
            else if (engineThread.getState().equals(Thread.State.NEW)){
                engineThread.start();
            }
        });

        stopButton.setOnAction(e -> {
            this.engine.setPaused();
        });

        quitButton.setOnAction(e -> {
            engineThread.interrupt();
            if(withSaving){
                saveStats(pathToStats);
            }
            simulationStage.close();
        });

        HBox box = new HBox(startButton, stopButton, quitButton, bestGenome);
        box.setMinWidth(windowWeight);
        box.setAlignment(Pos.CENTER);
        buttonPanel.setCenter(box);
        buttonPanel.setPadding(new Insets(10, 10, 10, 10));

        return buttonPanel;
    }

    private void updateGrid(){
        logStats(engine, map);
        clearGrid();
        unshowBestGenome();
        if(showBestGenomeFlag){
            showBestGenome();
        }
        fillGridWithMapElements(grid, map);
    }

    @Override
    public void dayPassed() {
        Platform.runLater(this::updateGrid);
    }

    @Override
    public void elementRemoved(Object element) {
        this.mapElementToRepresentation.remove((IMapElement) element);
    }

    @Override
    public void handle(Event event) {
        GuiElementBox currentBox = ((GuiElementBox)event.getSource());
        if(chosenBox == null){
            ((GuiElementBox) event.getSource()).highlight();
            this.right = new AnimalStatPanel(this.engine, (Animal)currentBox.getElement(), windowWeight * 0.3, animalStatButtonHandler);
            this.mainPane.setRight(this.right);
            chosenBox = currentBox;
        }
        else{
            chosenBox.removeHighlight();
            ((GuiElementBox) event.getSource()).highlight();
            engine.removeDayPassedObserver(this.right);
            this.right = new AnimalStatPanel(this.engine, (Animal)currentBox.getElement(), windowWeight * 0.3, animalStatButtonHandler);
            this.mainPane.setRight(this.right);
            chosenBox = currentBox;
        }
    }

    private void showBestGenome(){
        int gridInsideHeight = map.getRightUpperBound().y;
        var animals = engine.getAnimalsWithBestGenome();
        bestGenomePanes.clear();
        if(animals == null){
            return;
        }

        for(var animal : animals){
            int currentColumn = animal.getPosition().x;
            int currentRow = gridInsideHeight - (animal.getPosition().y - map.getLeftLowerBound().y);

            bestGenomePanes.add(gridSpots[currentColumn][currentRow]);
            gridSpots[currentColumn][currentRow].setStyle(
                    "-fx-background-color: rgb(16,57,162);" + "-fx-border-width:" + Double.toString(gridSpotWidth * 0.05) + ";" +
                    "-fx-border-color: rgb(255,255,255);"
            );
        }
    }

    private void unshowBestGenome(){
        for(var box : bestGenomePanes){
            if(box != null){
                box.setStyle(
                        "-fx-background-color: #9ce738;" +
                                "-fx-border-width:" + Double.toString(gridSpotWidth * 0.05) + ";" +
                                "-fx-border-color: #ffffff;"
                );
            }
        }
    }

    private void logStats(SimulationEngine engine, AbstractEvolutionMap map){
        var bestGenome = "None";

        if(engine.getSortedGenotypes().size() > 0){
            bestGenome = engine.getSortedGenotypes().get(0).getKey();
        }
        this.totalStats.add(new String[]
                {
                        Integer.toString(engine.getCurrentDay()),
                        Integer.toString(engine.getNumOfAnimals()),
                        Integer.toString(map.getNumOfGrass()),
                        Integer.toString(map.getNumOfFreePositions()),
                        bestGenome,
                        Double.toString(engine.getAvgAnimalEnergy()),
                        Double.toString(engine.getAvgLifetime())
                });
    }

    private void saveStats(String path){
        File csvOutputFile = new File(path);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            totalStats.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
        catch(FileNotFoundException ex){
            System.exit(2);
        }
    }

    public String convertToCSV(String[] data) {
        return String.join(",", data);
    }

}
