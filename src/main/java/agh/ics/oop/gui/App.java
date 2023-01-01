package agh.ics.oop.gui;

import agh.ics.oop.*;
import agh.ics.oop.map.AbstractEvolutionMap;
import agh.ics.oop.map.MapCreator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App extends Application{
    private final int windowWidth = 1000;
    private final int windowHeight = 600;
    private BorderPane right;
    private BorderPane left;
    private FileChooser fileChooser;
    private Stage primaryStage;
    private SimulationParameters chosenSimParams = null;
    /*

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
        this.primaryStage = primaryStage;
        BorderPane mainPane = new BorderPane();
        mainPane.setPrefWidth(windowWidth);
        mainPane.setPrefHeight(windowHeight);
        SimulationParameters simParams = JsonConfigHandler.getParametersFromFile("src/main/resources/params.json");

        //TEMPORARY
        AbstractEvolutionMap evolutionMap1 = MapCreator.createMap(simParams);
        SimulationEngine engine1 = EngineCreator.createEngine(simParams, evolutionMap1);
        GuiSimulation guiSimulation1 = new GuiSimulation(engine1, evolutionMap1);
        //TEMPORARY

        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("src/"));

        right = setUpRightPane();
        mainPane.setRight(setUpRightPane());
        left = setUpLeftPane();
        mainPane.setLeft(left);

        Scene mainScene = new Scene(mainPane);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private BorderPane setUpLeftPane(){
        var left = new BorderPane();
        left.setPrefWidth(windowWidth * 0.5);
        VBox buttonVBox = new VBox();

        Button createParamButton = new Button("STWORZ KONFIGURACJE");
        createParamButton.setPrefWidth(windowWidth * 0.2);
        buttonVBox.getChildren().add(createParamButton);

        Button chooseParamButton = createChooseButton();
        buttonVBox.getChildren().add(chooseParamButton);

        Button startParamButton = new Button("ROZPOCZNIJ SYMULACJE");
        startParamButton.setPrefWidth(windowWidth * 0.2);
        buttonVBox.getChildren().add(startParamButton);
        startParamButton.setDisable(true);

        buttonVBox.setAlignment(Pos.CENTER);
        buttonVBox.setSpacing(windowWidth * 0.01);
        left.setCenter(buttonVBox);
        return left;
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

    private Button createChooseButton(){
        Button chooseParamButton = new Button("WYBIERZ KONFIGURACJE");
        chooseParamButton.setPrefWidth(windowWidth * 0.2);
        chooseParamButton.setOnAction(e -> {
            SimulationParameters simParams = JsonConfigHandler
                    .getParametersFromFile(fileChooser.showOpenDialog(primaryStage).getPath());
            this.right = createParamVisRight(simParams);
            //handlowanie errorow
            chosenSimParams = simParams;
        });
        return chooseParamButton;
    }

    private BorderPane setUpRightPane(){
        var right = new BorderPane();
        right.setPrefWidth(windowWidth * 0.5);
        return right;
    }

    private BorderPane createParamVisRight(SimulationParameters params){
        var right = new BorderPane();
        right.setPrefWidth(windowWidth * 0.5);
        return right;
    }

}