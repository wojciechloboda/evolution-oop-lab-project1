package agh.ics.oop.gui;

import agh.ics.oop.*;
import agh.ics.oop.map.AbstractEvolutionMap;
import agh.ics.oop.map.MapCreator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class App extends Application{
    /*
    private AbstractWorldMap map;
    private Stage primaryStage;
    private GridPane grid;
    private SimulationEngine engine;
    private final List<GuiElementBox> elementsList = new ArrayList<>();

    private int getMapSizeInRows(){
        return map.getRightUpperBound().subtract(map.getLeftLowerBound()).y + 1;
    }

    private int getMapSizeInColumns(){
        return map.getRightUpperBound().subtract(map.getLeftLowerBound()).x + 1;
    }

    private Label createLabel(String str){
        Label label = new Label(str);
        label.setFont(new Font("Arial", 30));
        GridPane.setHalignment(label, HPos.CENTER);
        return label;
    }

    private void setGridLegend(GridPane grid){
        int gridInsideHeight = getMapSizeInRows();
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

    private void fillGridWithMapElements(GridPane grid){
        int gridInsideHeight = getMapSizeInRows();
        int elementInd = 0;

        for(int x = map.getLeftLowerBound().x; x <= map.getRightUpperBound().x; x++){
            for(int y = map.getLeftLowerBound().y; y <= map.getRightUpperBound().y; y++){
                Vector2d position = new Vector2d(x, y);
                if(map.isOccupied(position)){
                    int currentColumn = x - map.getLeftLowerBound().x + 1;
                    int currentRow = gridInsideHeight - (y - map.getLeftLowerBound().y);
                    grid.add(elementsList.get(elementInd).updateAndGetElementVBox((IMapElement) map.objectAt(position)),
                            currentColumn, currentRow);
                    elementInd += 1;
                }
            }
        }
    }

    private void setGridConstraints(GridPane grid){
        int gridRowsCount = getMapSizeInRows();
        int gridColumnsCount = getMapSizeInColumns();
        int width = 50;
        int height = 50;

        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();

        for(int i = 0; i <= gridRowsCount; i++){
            grid.getRowConstraints().add(new RowConstraints(height));
        }

        for(int i = 0; i <= gridColumnsCount; i++){
            grid.getColumnConstraints().add(new ColumnConstraints(width));
        }
    }
    */

    @Override
    public void start(Stage primaryStage) {
        this.init();
        SimulationParameters simParams = JsonConfigHandler.getParametersFromFile("src/main/resources/params.json");

        //AbstractEvolutionMap evolutionMap = MapCreator.createMap(simParams);
        //SimulationEngine engine = EngineCreator.createEngine(simParams, evolutionMap);

        //GuiSimulation guiSimulation = new GuiSimulation(engine, evolutionMap);

        //AbstractEvolutionMap evolutionMap1 = MapCreator.createMap(simParams);
        //SimulationEngine engine1 = EngineCreator.createEngine(simParams, evolutionMap1);
        //GuiSimulation guiSimulation1 = new GuiSimulation(engine1, evolutionMap1);

        Button start = new Button("start");
        start.setOnAction(e ->{
            AbstractEvolutionMap evolutionMap1 = MapCreator.createMap(simParams);
            SimulationEngine engine1 = EngineCreator.createEngine(simParams, evolutionMap1);
            GuiSimulation guiSimulation1 = new GuiSimulation(engine1, evolutionMap1);
        }
        );

        primaryStage.setScene(new Scene(start));

        primaryStage.show();


        /*
        grid = new GridPane();
        grid.setGridLinesVisible(true);
        setGridConstraints(grid);
        setGridLegend(grid);
        fillGridWithMapElements(grid);

        TextField directionTextField = new TextField();

        Button startButton = new Button("Start");
        startButton.setOnAction(e -> {
            String[] args = directionTextField.getText().split(" ");
            var directionList = OptionsParser.parse(args);
            engine.setDirections(directionList);
            Thread engineThread = new Thread(engine);
            engineThread.start();

        });

        HBox hbox = new HBox(startButton, directionTextField);
        VBox vbox = new VBox(grid, hbox);
        Scene scene = new Scene(vbox);

        this.primaryStage = primaryStage;
        primaryStage.setScene(scene);

        primaryStage.setMaximized(true);
        primaryStage.show();
         */
    }

    @Override
    public void init(){
        /*
        try{

            int grassElementsCount = 10;
            map = new GrassField(10);
            Vector2d[] positions = {new Vector2d(2,2), new Vector2d(3,4)};

            int elementsCount = grassElementsCount + positions.length;
            for(int i = 0; i < elementsCount; i++){
                elementsList.add(new GuiElementBox());
            }

            this.engine = new SimulationEngine(map, positions, 600);
            this.engine.addObserver(this);
        }
        catch(IllegalArgumentException ex){
            System.out.println(ex.getMessage());
        }
         */
    }

    /*

    private void updateGrid(){
        grid.getChildren().retainAll(grid.getChildren().get(0));
        setGridConstraints(grid);
        setGridLegend(grid);
        fillGridWithMapElements(grid);
        primaryStage.show();
    }

    @Override
    public void moveHappened() {
        Platform.runLater(this::updateGrid);
    }

    */


}