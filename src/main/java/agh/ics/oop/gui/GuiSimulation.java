package agh.ics.oop.gui;

import agh.ics.oop.IDayPassedObserver;
import agh.ics.oop.SimulationEngine;
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GuiSimulation implements IDayPassedObserver, IElementRemovedObserver, EventHandler {
    private final Stage simulationStage;
    private final GridPane grid;
    private AnimalStatPanel right;
    private final StatPanel left;
    private final int windowHeight = 400;
    private final int windowWeight = 800;
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

    public GuiSimulation(SimulationEngine engine, AbstractEvolutionMap map){
        engine.addDayPassedObserver(this);
        simulationStage = new Stage();
        this.map = map;
        this.map.addObserver(this);
        ImageLoader.loadImages();

        this.engine = engine;

        this.grid = createGrid(map);
        this.mapElementToRepresentation = new HashMap<>();

        grid.setAlignment(Pos.CENTER);
        fillGridWithMapElements(grid, map);

        this.left = new StatPanel(engine, map,windowWeight * 0.2);

        BorderPane gridPanel = new BorderPane();
        gridPanel.setMinHeight(windowHeight);
        gridPanel.setMinWidth(windowWeight * 0.6);
        gridPanel.setCenter(grid);
        //gridPanel.setStyle("-fx-background-color: #" + "ffd700");

        var buttonPanel = createButtonPanel();
        var dummyRight = new BorderPane();
        var label = new Label("Nie wybrano zwierzecia");
        label.setPrefWidth(windowWeight * 0.2);
        label.setAlignment(Pos.CENTER);
        dummyRight.setPadding(new Insets(10, 10, 10, 10));
        dummyRight.setCenter(label);

        mainPane = new BorderPane();
        mainPane.setCenter(gridPanel);
        mainPane.setLeft(left);
        mainPane.setRight(dummyRight);
        mainPane.setTop(createTitle());
        mainPane.setBottom(buttonPanel);
        Scene scene = new Scene(mainPane);

        simulationStage.setScene(scene);
        simulationStage.show();

        this.engineThread = new Thread(engine);

        this.simulationStage.setOnCloseRequest(e ->{
            if(engineThread.isAlive()) {
                engineThread.interrupt();
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
                    else{
                        mapElementToRepresentation.get((IMapElement) map.objectAt(position)).updateElementRepresentation();
                        gridSpots[currentColumn][currentRow].setCenter(mapElementToRepresentation.get((IMapElement) map.objectAt(position)));
                    }
                }
            }
        }
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


    private Label createTitle(){
        var titleLabel = new Label("Symulacja");
        titleLabel.setPrefWidth(windowWeight);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setPadding(new Insets(10, 10, 10, 10));
        return titleLabel;
    }

    private BorderPane createButtonPanel(){
        BorderPane buttonPanel = new BorderPane();
        Button startButton = new Button("START");
        Button stopButton = new Button("PAUZA");
        Button quitButton = new Button("KONIEC");
        Button bestGenome = new Button("Zaznacz najlepszy genom");

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
            this.right = new AnimalStatPanel(this.engine, (Animal)currentBox.getElement(), windowWeight * 0.2, animalStatButtonHandler);
            this.mainPane.setRight(this.right);
            chosenBox = currentBox;
        }
        else{
            chosenBox.removeHighlight();
            ((GuiElementBox) event.getSource()).highlight();
            engine.removeDayPassedObserver(this.right);
            this.right = new AnimalStatPanel(this.engine, (Animal)currentBox.getElement(), windowWeight * 0.2, animalStatButtonHandler);
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
}
