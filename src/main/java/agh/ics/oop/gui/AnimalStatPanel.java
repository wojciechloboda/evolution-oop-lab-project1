package agh.ics.oop.gui;

import agh.ics.oop.IDayPassedObserver;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.animal.Animal;
import agh.ics.oop.map.AbstractEvolutionMap;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class AnimalStatPanel extends BorderPane implements IDayPassedObserver {
    private final Map<String, Label> parameterValMap = new HashMap<>();
    private final Animal animal;
    private final Double width;
    private final SimulationEngine engine;
    private boolean isAlive = true;

    private HBox createStatEntry(String key, String name){
        var parameterName = new Label(name);
        var parameterVal = new Label("0");
        parameterName.setPrefWidth(width * 0.50);
        parameterVal.setPrefWidth(width * 0.50);
        parameterValMap.put(key, parameterVal);
        HBox entry = new HBox(parameterName, parameterVal);
        return entry;
    }

    public AnimalStatPanel(SimulationEngine engine, Animal animal, Double width, EventHandler<ActionEvent> ev){
        super();
        this.width = width;
        this.engine = engine;
        engine.addDayPassedObserver(this);
        this.animal = animal;

        var content = new VBox();

        this.setStyle("-fx-background-color: #" + "ffffff");

        var title = new Label("CHOSEN ANIMAL:");
        title.setPadding(new Insets(10,10,10,10));
        title.setMinWidth(width);
        title.setAlignment(Pos.TOP_CENTER);
        this.setTop(title);


        content.getChildren().add(createStatEntry("status", "Status:"));
        content.getChildren().add(createStatEntry("genome", "Genome:"));
        content.getChildren().add(createStatEntry("actGenomePart", "Active part:"));
        content.getChildren().add(createStatEntry("energy", "Energy:"));
        content.getChildren().add(createStatEntry("grassEaten", "Plants eaten:"));
        content.getChildren().add(createStatEntry("children","Number of children:"));
        content.getChildren().add(createStatEntry("lifeTime","Lifetime:"));
        content.getChildren().add(createStatEntry("death", "Death at:"));

        content.setSpacing(10.0);
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setCenter(content);

        var button = new Button("STOP FOLLOWING");
        button.setPrefWidth(width * 0.9);
        button.setOnAction(ev);
        content.getChildren().add(button);


        updateStats();
    }

    @Override
    public void dayPassed() {
        if(isAlive){
            Platform.runLater(this::updateStats);
            if(!engine.isAnimalAlive(animal)){
                isAlive = false;
            }
        }
    }

    private void updateStats(){
        parameterValMap.get("status").setText("Alive");
        parameterValMap.get("genome").setText(animal.getGenome().toString());
        parameterValMap.get("actGenomePart").setText(Integer.toString(animal.getGenome().getActGene()));
        parameterValMap.get("energy").setText(Integer.toString(animal.getEnergy()));
        parameterValMap.get("grassEaten").setText(Integer.toString(animal.getNumOfGrassEaten()));
        parameterValMap.get("children").setText(Integer.toString(animal.getNumOfChildren()));
        parameterValMap.get("lifeTime").setText(Integer.toString(engine.getCurrentDay() - animal.getBornAtDay()));
        parameterValMap.get("death").setText(" - ");

        if(!engine.isAnimalAlive(animal)){
            parameterValMap.get("status").setText("Dead");
            parameterValMap.get("actGenomePart").setText(" - ");
            parameterValMap.get("energy").setText("0");
            parameterValMap.get("death").setText(Integer.toString(engine.getCurrentDay()));
        }
    }
}
