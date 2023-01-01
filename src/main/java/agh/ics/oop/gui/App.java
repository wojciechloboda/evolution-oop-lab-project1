package agh.ics.oop.gui;

import agh.ics.oop.*;
import agh.ics.oop.map.AbstractEvolutionMap;
import agh.ics.oop.map.MapCreator;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.shape.Box;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import agh.ics.oop.SimulationParameters;

public class App extends Application{
    private final int windowWidth = 1000;
    private final int windowHeight = 600;
    private BorderPane right;
    private BorderPane left;
    private FileChooser fileChooser;
    private Stage primaryStage;
    private SimulationParameters chosenSimParams = null;
    private BorderPane mainPane;
    private Map<String, Object> paramInputMap = new HashMap<>();
    private Popup pop = new Popup();
    private Button startParamButton;
    private Button startSaveParamButton;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        mainPane = new BorderPane();
        mainPane.setPrefWidth(windowWidth);
        mainPane.setPrefHeight(windowHeight);
        setPopup();

        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("configurations/"));

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

        Button createParamButton = createCreateButton();
        buttonVBox.getChildren().add(createParamButton);

        Button chooseParamButton = createChooseButton();
        buttonVBox.getChildren().add(chooseParamButton);

        this.startParamButton = createStartButton();
        buttonVBox.getChildren().add(startParamButton);
        startParamButton.setDisable(true);

        this.startSaveParamButton = createStartSaveButton();
        buttonVBox.getChildren().add(startSaveParamButton);
        startSaveParamButton.setDisable(true);

        buttonVBox.setAlignment(Pos.CENTER);
        buttonVBox.setSpacing(windowWidth * 0.01);
        left.setCenter(buttonVBox);
        return left;
    }

    private Button createStartButton(){
        Button startParamButton = new Button("RUN SIMULATION");
        startParamButton.setPrefWidth(windowWidth * 0.2);
        //buttonVBox.getChildren().add(startParamButton);
        startParamButton.setDisable(true);
        startParamButton.setOnAction(e -> {
            AbstractEvolutionMap evolutionMap1 = MapCreator.createMap(chosenSimParams);
            SimulationEngine engine1 = EngineCreator.createEngine(chosenSimParams, evolutionMap1);
            GuiSimulation guiSimulation1 = new GuiSimulation(engine1, evolutionMap1, null);
        });
        return startParamButton;
    }

    private Button createCreateButton(){
        Button createParamButton = new Button("CREATE CONFIGURATION");
        createParamButton.setPrefWidth(windowWidth * 0.2);
        createParamButton.setOnAction(e -> {
            mainPane.setRight(createParamGetRight());
        });
        return createParamButton;
    }

    private Button createChooseButton(){
        Button chooseParamButton = new Button("LOAD CONFIGURATION");
        chooseParamButton.setPrefWidth(windowWidth * 0.2);
        chooseParamButton.setOnAction(e -> {
            var file = fileChooser.showOpenDialog(primaryStage);
            if(file != null){
                SimulationParameters simParams = JsonConfigHandler
                        .getParametersFromFile(file.getPath());
                chosenSimParams = simParams;
                mainPane.setRight(createParamVisRight(simParams));
                startParamButton.setDisable(false);
                startSaveParamButton.setDisable(false);
            }
        });
        return chooseParamButton;
    }

    private Button createStartSaveButton(){
        Button startParamButton = new Button("RUN AND SAVE SIMULATION");
        startParamButton.setPrefWidth(windowWidth * 0.2);
        startParamButton.setDisable(true);
        startParamButton.setOnAction(e -> {
            fileChooser.setInitialFileName("simulationStats.csv");
            var path = fileChooser.showSaveDialog(primaryStage).getPath();

            AbstractEvolutionMap evolutionMap1 = MapCreator.createMap(chosenSimParams);
            SimulationEngine engine1 = EngineCreator.createEngine(chosenSimParams, evolutionMap1);
            GuiSimulation guiSimulation1 = new GuiSimulation(engine1, evolutionMap1, path);
        });
        return startParamButton;
    }

    private BorderPane setUpRightPane(){
        var right = new BorderPane();
        right.setPrefWidth(windowWidth * 0.5);
        return right;
    }

    private BorderPane createParamGetRight(){
        this.paramInputMap = new HashMap<>();
        var right = new BorderPane();
        right.setPrefWidth(windowWidth * 0.5);
        var label = new Label("CREATE CONFIGURATION");
        var titlePane = new BorderPane();
        titlePane.setPadding(new Insets(windowHeight * 0.02));
        titlePane.setCenter(label);
        right.setTop(titlePane);

        VBox paramsBox = new VBox();
        paramsBox.setSpacing(windowHeight * 0.005); //HERE

        paramsBox.getChildren().add(createParamInput("name:",  windowWidth * 0.5));
        paramsBox.getChildren().add(createParamInput("map height:",  windowWidth * 0.5));
        paramsBox.getChildren().add(createParamInput("map width:",  windowWidth * 0.5));
        paramsBox.getChildren().add(createParamMenuInput("map variant:",  "HELL PORTAL", "EARTH", windowWidth * 0.5));
        paramsBox.getChildren().add(createParamInput("number of plants at start:", windowWidth * 0.5));
        paramsBox.getChildren().add(createParamInput("energy boost from plant:", windowWidth * 0.5));
        paramsBox.getChildren().add(createParamInput("number of plants growing daily:", windowWidth * 0.5));
        paramsBox.getChildren().add(createParamMenuInput("grass growth variant:", "GREEN EQUATORS", "TOXIC DEAD",windowWidth * 0.5));
        paramsBox.getChildren().add(createParamInput("number of animals at start:", windowWidth * 0.5));
        paramsBox.getChildren().add(createParamInput("animal initial energy:", windowWidth * 0.5));
        paramsBox.getChildren().add(createParamInput("energy needed for recreation:", windowWidth * 0.5));
        paramsBox.getChildren().add(createParamInput("energy loss for new animal:", windowWidth * 0.5));
        paramsBox.getChildren().add(createParamInput("minimum number of mutations:", windowWidth * 0.5));
        paramsBox.getChildren().add(createParamInput("maximum number of mutations:", windowWidth * 0.5));
        paramsBox.getChildren().add(createParamMenuInput("mutation variant:", "RANDOM", "CORRECTION", windowWidth * 0.5));
        paramsBox.getChildren().add(createParamInput("genome length:", windowWidth * 0.5));
        paramsBox.getChildren().add(createParamMenuInput("animal behavior variant:", "CRAZINESS", "PREDESTINATION",windowWidth * 0.5));

        paramsBox.setAlignment(Pos.CENTER);
        right.setCenter(paramsBox);

        right.setBottom(createSaveButtonPane());
        return right;
    }

    private BorderPane createSaveButtonPane(){
        BorderPane pane = new BorderPane();
        Button startButton = new Button("SAVE");
        startButton.setAlignment(Pos.BOTTOM_CENTER);
        startButton.setOnAction(e ->{
            Map<String, String> paramValMap = new HashMap<>();
            for(var key : paramInputMap.keySet()){
                var val = paramInputMap.get(key);
                if(val instanceof TextField){
                    paramValMap.put(key, ((TextField)val).getText());
                }
                if(val instanceof MenuButton){
                    paramValMap.put(key, ((MenuButton)val).getText());
                }
            }
            if(Validation.validate(paramValMap)){
                fileChooser.setInitialFileName(paramValMap.get("name:") + ".json");
                var file = fileChooser.showSaveDialog(primaryStage);
                if(file != null){
                    var path = file.getPath();
                    JsonConfigHandler.saveParametersToFile(paramValMap, path);
                }
            }
            else{
                pop.show(primaryStage);
            }
        });
        pane.setCenter(startButton);
        pane.setPadding(new Insets(windowHeight * 0.01, windowHeight * 0.01,
                windowHeight * 0.01, windowHeight * 0.01));
        return pane;
    }

    private HBox createParamMenuInput(String name, String first, String second, Double width){
        Label nameLabel = new Label(name);
        nameLabel.setPrefWidth(width * 0.4);
        MenuButton menu = new MenuButton("-");
        menu.setPrefWidth(width * 0.4);

        MenuItem firstItem = new MenuItem(first);
        firstItem.setOnAction(e -> menu.setText(first));

        MenuItem secondItem = new MenuItem(second);
        secondItem.setOnAction(e -> menu.setText(second));

        menu.getItems().add(firstItem);
        menu.getItems().add(secondItem);

        HBox box = new HBox(nameLabel, menu);
        box.setAlignment(Pos.CENTER);
        this.paramInputMap.put(name, menu);
        return box;
    }

    private HBox createParamInput(String name, Double width){
        Label nameLabel = new Label(name);
        nameLabel.setPrefWidth(width * 0.4);
        TextField textField = new TextField();
        textField.setPrefWidth(width * 0.4);
        HBox box = new HBox(nameLabel, textField);
        box.setAlignment(Pos.CENTER);
        this.paramInputMap.put(name, textField);
        return box;
    }

    private BorderPane createParamVisRight(SimulationParameters params){
        var right = new BorderPane();
        right.setPrefWidth(windowWidth * 0.5);
        BorderPane titlePane = new BorderPane();
        var label = new Label("LOADED CONFIGURATION: " + chosenSimParams.getParamsName());
        titlePane.setPadding(new Insets(windowHeight * 0.02));
        titlePane.setCenter(label);
        VBox paramsBox = new VBox();
        right.setTop(titlePane);
        paramsBox.setSpacing(windowHeight * 0.01); //HERE

        paramsBox.getChildren().add(createParamEntry("map height:", Integer.toString(chosenSimParams.getHeight()), windowWidth * 0.5));
        paramsBox.getChildren().add(createParamEntry("map width:", Integer.toString(chosenSimParams.getWidth()), windowWidth * 0.5));
        paramsBox.getChildren().add(createParamEntry("map variant:", getBoundsHandlerString(chosenSimParams), windowWidth * 0.5));
        paramsBox.getChildren().add(createParamEntry("number of plants at start:", Integer.toString(chosenSimParams.getNumOfInitGrass()), windowWidth * 0.5));
        paramsBox.getChildren().add(createParamEntry("energy boost from plant:", Integer.toString(chosenSimParams.getEnergyFromGrass()), windowWidth * 0.5));
        paramsBox.getChildren().add(createParamEntry("number of plants growing daily:", Integer.toString(chosenSimParams.getNumOfGrassGrowing()), windowWidth * 0.5));
        paramsBox.getChildren().add(createParamEntry("grass growth variant:", getGrassGrowthString(chosenSimParams), windowWidth * 0.5));
        paramsBox.getChildren().add(createParamEntry("number of animals at start:", Integer.toString(chosenSimParams.getNumOfInitAnimals()), windowWidth * 0.5));
        paramsBox.getChildren().add(createParamEntry("animal initial energy:", Integer.toString(chosenSimParams.getStartAnimalEnergy()), windowWidth * 0.5));
        paramsBox.getChildren().add(createParamEntry("energy needed for recreation:", Integer.toString(chosenSimParams.getEnergyNeededForNewAnimal()), windowWidth * 0.5));
        paramsBox.getChildren().add(createParamEntry("energy loss for new animal:", Integer.toString(chosenSimParams.getEnergyLossForNewAnimal()), windowWidth * 0.5));
        paramsBox.getChildren().add(createParamEntry("minimum number of mutations:", Integer.toString(chosenSimParams.getMinNumOfMutations()), windowWidth * 0.5));
        paramsBox.getChildren().add(createParamEntry("maximum number of mutations:", Integer.toString(chosenSimParams.getMaxNumOfMutations()), windowWidth * 0.5));
        paramsBox.getChildren().add(createParamEntry("mutation variant:", getMutationsString(chosenSimParams), windowWidth * 0.5));
        paramsBox.getChildren().add(createParamEntry("genome length:", Integer.toString(chosenSimParams.getGenomeLength()), windowWidth * 0.5));
        paramsBox.getChildren().add(createParamEntry("animal behavior variant:", getBehaviorString(chosenSimParams), windowWidth * 0.5));

        paramsBox.setAlignment(Pos.CENTER);
        right.setCenter(paramsBox);
        return right;
    }

    private HBox createParamEntry(String name, String val, Double width){
        Label nameLabel = new Label(name);
        nameLabel.setPrefWidth(width * 0.5);
        Label valLabel = new Label(val);
        valLabel.setPrefWidth(width * 0.5);
        HBox box = new HBox(nameLabel, valLabel);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private String getBoundsHandlerString(SimulationParameters params){
        if(params.getBoundsHandlerType().equals(Variations.BoundsHandlerType.EARTH)){
            return "EARTH";
        }
        if(params.getBoundsHandlerType().equals(Variations.BoundsHandlerType.HELL)){
            return "HELL PORTAL";
        }
        return "";
    }

    private String getGrassGrowthString(SimulationParameters params){
        if(params.getGrassGrowthType().equals(Variations.MapGrassGrowthType.GREEN_EQUATOR)){
            return "GREEN EQUATORS";
        }
        if(params.getGrassGrowthType().equals(Variations.MapGrassGrowthType.TOXIC_DEAD)){
            return "TOXIC DEAD";
        }
        return "";
    }

    private String getMutationsString(SimulationParameters params){
        if(params.getMutationHandlerType().equals(Variations.MutationHandlerType.RANDOM)){
            return "RANDOM";
        }
        if(params.getMutationHandlerType().equals(Variations.MutationHandlerType.CORRECTION)){
            return "CORRECTION";
        }
        return "";
    }

    private String getBehaviorString(SimulationParameters params){
        if(params.getNextActGeneGeneratorType().equals(Variations.NextActGeneGeneratorType.CRAZY)){
            return "CRAZINESS";
        }
        if(params.getNextActGeneGeneratorType().equals(Variations.NextActGeneGeneratorType.STABLE)){
            return "PREDESTINATION";
        }
        return "";
    }

    private void setPopup(){
        Label label = new Label("Bad configuration!");
        Button button = new Button("OK");
        button.setOnAction(e -> {
            pop.hide();
        });
        VBox vbox = new VBox(label, button);
        pop.getContent().add(vbox);
    }
}