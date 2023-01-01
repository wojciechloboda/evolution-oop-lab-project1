package agh.ics.oop.gui;

import agh.ics.oop.IDayPassedObserver;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.map.AbstractEvolutionMap;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.apache.commons.math3.analysis.function.Abs;

import java.util.HashMap;
import java.util.Map;

public class StatPanel extends BorderPane implements IDayPassedObserver {
    private final Map<String, Label> parameterValMap = new HashMap<>();
    private final Double width;
    private final SimulationEngine engine;
    private final AbstractEvolutionMap map;
    private final Label genomeRankedContent = new Label();

    private HBox createStatEntry(String key, String name){
        var parameterName = new Label(name);
        var parameterVal = new Label("0");
        parameterName.setPrefWidth(width * 0.75);
        parameterVal.setPrefWidth(width * 0.25);
        parameterValMap.put(key, parameterVal);
        HBox entry = new HBox(parameterName, parameterVal);
        return entry;
    }

    private BorderPane createGenomeRanking(){
        BorderPane pane = new BorderPane();
        Label title = new Label("\n Top 5 genomes: \n");
        title.setMinWidth(width);
        title.setAlignment(Pos.TOP_CENTER);
        pane.setTop(title);
        StringBuilder bld = new StringBuilder();

        for(int i = 1; i <= 5; i++){
            bld.append(i).append(". --- \n");
        }

        genomeRankedContent.setText(bld.toString());
        pane.setCenter(genomeRankedContent);
        return pane;
    }

    public StatPanel(SimulationEngine engine, AbstractEvolutionMap map, Double width){
        super();
        this.width = width;
        this.engine = engine;
        this.map = map;
        engine.addDayPassedObserver(this);

        var content = new VBox();

        this.setStyle("-fx-background-color: #" + "ffffff");

        var title = new Label("STATISTICS");
        title.setPadding(new Insets(10,10,10,10));
        title.setMinWidth(width);
        title.setAlignment(Pos.TOP_CENTER);
        this.setTop(title);

        content.getChildren().add(createStatEntry("currentDay", "Day:"));
        content.getChildren().add(createStatEntry("numOfAnimals", "Animals:"));
        content.getChildren().add(createStatEntry("numOfGrass", "Plants:"));
        content.getChildren().add(createStatEntry("numOfFreePos", "Free spots:"));
        content.getChildren().add(createStatEntry("avgEnergy", "Avg energy:"));
        content.getChildren().add(createStatEntry("avgLifeTime","Avf lifetime:"));

        content.getChildren().add(createGenomeRanking());
        content.setSpacing(10.0);
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setCenter(content);
    }

    @Override
    public void dayPassed() {
        Platform.runLater(this::updateStats);
        Platform.runLater(this::updateGenotypeRanking);
    }

    private void updateStats(){
        parameterValMap.get("currentDay").setText(Integer.toString(engine.getCurrentDay()));
        parameterValMap.get("numOfAnimals").setText(Integer.toString(engine.getNumOfAnimals()));
        parameterValMap.get("numOfGrass").setText(Integer.toString(map.getNumOfGrass()));
        parameterValMap.get("numOfFreePos").setText(Integer.toString(map.getNumOfFreePositions()));
        parameterValMap.get("avgEnergy").setText(String.format("%.2f", engine.getAvgAnimalEnergy()));
        parameterValMap.get("avgLifeTime").setText(String.format("%.2f", engine.getAvgLifetime()));
    }

    private void updateGenotypeRanking(){
        StringBuilder bld = new StringBuilder();
        if(this.engine.getSortedGenotypes() == null){
            return;
        }

        for(int i = 1; i <= 5; i++){
            if(i > this.engine.getSortedGenotypes().size()){
                bld.append(i).append(". --- \n");
            }
            else{
                var genome = this.engine.getSortedGenotypes().get(i - 1);
                bld.append(i).append(". ").append(genome.getKey()).append("\n");
            }
        }
        this.genomeRankedContent.setText(bld.toString());
    }
}
