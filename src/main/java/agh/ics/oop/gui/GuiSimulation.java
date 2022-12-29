package agh.ics.oop.gui;

import agh.ics.oop.IDayPassedObserver;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.Vector2d;
import agh.ics.oop.animal.Animal;
import agh.ics.oop.map.AbstractEvolutionMap;
import agh.ics.oop.map.IElementRemovedObserver;
import agh.ics.oop.map.IMapElement;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
    private final int windowHeight = 600;
    private final int windowWeight = 1200;
    private final AbstractEvolutionMap map;
    private final Thread engineThread;
    private final Map<IMapElement, GuiElementBox> mapElementToRepresentation;
    private final SimulationEngine engine;
    private final BorderPane mainPane;
    private GuiElementBox chosenBox = null;
    private final Set<GuiElementBox> bestGenomeBoxes = new HashSet<>();
    private boolean showBestGenomeFlag = false;

    private Label createLabel(String str){
        Label label = new Label(str);
        GridPane.setHalignment(label, HPos.CENTER);
        return label;
    }

    private void setGridLegend(GridPane grid, AbstractEvolutionMap map){
        int gridInsideHeight = map.getRightUpperBound().y + 1;
        Label legendLabel;

        for(int x = map.getLeftLowerBound().x; x <= map.getRightUpperBound().x; x++){
            legendLabel = createLabel(Integer.toString(x));
            int currentColumn = x - map.getLeftLowerBound().x + 1;
            grid.add(legendLabel, currentColumn, 0);
        }

        for(int y = map.getLeftLowerBound().y; y <= map.getRightUpperBound().y; y++){
            legendLabel = createLabel(Integer.toString(y));
            int currentRow = gridInsideHeight - (y - map.getLeftLowerBound().y);
            grid.add(legendLabel, 0, currentRow);
        }

        legendLabel = createLabel("y\\x");
        grid.add(legendLabel, 0, 0);
    }

    private void setGridConstraints(GridPane grid, AbstractEvolutionMap map){
        int gridRowsCount = map.getRightUpperBound().y + 1;
        int gridColumnsCount = map.getRightUpperBound().x + 1;

        double width = (windowWeight* 0.40 / Math.max(gridRowsCount, gridColumnsCount));
        double height = (windowWeight * 0.40 / Math.max(gridRowsCount, gridColumnsCount));

        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();

        for(int i = 0; i <= gridRowsCount; i++){
            grid.getRowConstraints().add(new RowConstraints(height));
        }

        for(int i = 0; i <= gridColumnsCount; i++){
            grid.getColumnConstraints().add(new ColumnConstraints(width));
        }
    }

    private GridPane createGrid(AbstractEvolutionMap map){
        GridPane newGrid = new GridPane();
        //newGrid.setGridLinesVisible(true);

        //experimenting
        newGrid.setHgap(1);
        newGrid.setVgap(1);
        newGrid.setStyle("-fx-background-color: palegreen; -fx-padding: 2; -fx-hgap: 2; -fx-vgap: 2;");
        //
        setGridConstraints(newGrid, map);

        return newGrid;
    }

    public GuiSimulation(SimulationEngine engine, AbstractEvolutionMap map){
        engine.addDayPassedObserver(this);
        simulationStage = new Stage();
        this.map = map;
        ImageLoader.loadImages();

        this.engine = engine;

        this.grid = createGrid(map);
        this.mapElementToRepresentation = new HashMap<>();

        grid.setAlignment(Pos.CENTER);
        setGridLegend(grid, map);
        fillGridWithMapElements(grid, map);

        this.left = new StatPanel(engine, map,windowWeight * 0.2);

        BorderPane gridPanel = new BorderPane();
        gridPanel.setMinHeight(windowHeight);
        gridPanel.setMinWidth(windowWeight * 0.6);
        gridPanel.setCenter(grid);
        gridPanel.setStyle("-fx-background-color: #" + "ffd700");

        var buttonPanel = createButtonPanel();

        mainPane = new BorderPane();
        mainPane.setCenter(gridPanel);
        mainPane.setLeft(left);
        //mainPane.setRight(right);
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
    }

    private void logElements(){
        for(var elem : mapElementToRepresentation.keySet()){
            System.out.print(elem.getPosition());
            System.out.println(elem);
        }
        System.out.println("END LOGGING");
    }

    private void fillGridWithMapElements(GridPane grid, AbstractEvolutionMap map){
        int gridInsideHeight = map.getRightUpperBound().y + 1;

        for(int x = 0; x <= map.getRightUpperBound().x; x++){
            for(int y = 0; y <= map.getRightUpperBound().y; y++){
                Vector2d position = new Vector2d(x, y);
                if(map.isOccupied(position)){
                    IMapElement elem = (IMapElement) map.objectAt(position);
                    int currentColumn = x + 1;
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
                        grid.add(chosenBox,
                                currentColumn, currentRow);
                    }
                    else{
                        mapElementToRepresentation.get((IMapElement) map.objectAt(position)).updateElementRepresentation();
                        grid.add(mapElementToRepresentation.get((IMapElement) map.objectAt(position)),
                                currentColumn, currentRow);
                    }
                }
            }
        }
    }

    private Label createTitle(){
        var titleLabel = new Label("Title");
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
            System.out.println("CLICKED");
            if(engineThread.getState().equals(Thread.State.WAITING)){
                synchronized (engine){
                    System.out.println("is waiting");
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
        grid.getChildren().retainAll(grid.getChildren().get(0));
        unshowBestGenome();
        setGridConstraints(grid, map);
        setGridLegend(grid, map);
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
            this.right = new AnimalStatPanel(this.engine, (Animal)currentBox.getElement(), windowWeight * 0.2);
            this.mainPane.setRight(this.right);
            chosenBox = currentBox;
        }
        else{
            chosenBox.removeHighlight();
            ((GuiElementBox) event.getSource()).highlight();
            engine.removeDayPassedObserver(this.right);
            this.right = new AnimalStatPanel(this.engine, (Animal)currentBox.getElement(), windowWeight * 0.2);
            this.mainPane.setRight(this.right);
            chosenBox = currentBox;
        }
    }

    private void showBestGenome(){
        var animals = engine.getAnimalsWithBestGenome();
        bestGenomeBoxes.clear();
        if(animals == null){
            return;
        }

        for(var animal : animals){
            if(mapElementToRepresentation.get(animal) == null){
                var newGuiElement = new GuiElementBox(grid.getColumnConstraints().get(0).getPrefWidth(), animal);
                if(animal instanceof Animal){
                    newGuiElement.setOnMouseClicked(this);
                }
                mapElementToRepresentation.put(animal, newGuiElement);
            }

            mapElementToRepresentation.get((IMapElement)animal).highlightBestGenome();
            bestGenomeBoxes.add(mapElementToRepresentation.get((IMapElement) animal));
        }
    }

    private void unshowBestGenome(){
        for(var box : bestGenomeBoxes){
            if(box != null){
                box.removeHighlightBestGenome();
            }
        }
    }
}
