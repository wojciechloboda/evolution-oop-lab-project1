package agh.ics.oop.gui;
import agh.ics.oop.IMapElementType;
import agh.ics.oop.animal.Animal;
import agh.ics.oop.map.Grass;
import agh.ics.oop.map.IMapElement;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox extends BorderPane {
    private final ImageView imageView;
    private final IMapElement element;
    private final int topEnergy = 50;
    private final double size;

    public IMapElement getElement(){
        return this.element;
    }

    public GuiElementBox(double size, IMapElement element) {
        this.size = size;
        imageView = new ImageView();
        this.element = element;
        if(element instanceof Grass){
            imageView.setImage(ImageLoader.getImage(IMapElementType.GRASS));
        }
        else{
            imageView.setImage(ImageLoader.getImage(IMapElementType.ANIMAL));
        }

        imageView.setFitWidth(size * 0.8);
        imageView.setFitHeight(size * 0.8);
        this.setCenter(imageView);
    }

    public void updateElementRepresentation(){
        if(element instanceof Animal){
            imageView.setEffect(getColorAdjust(((Animal) element).getEnergy()));
        }
    }

    private ColorAdjust getColorAdjust(int energy){
        ColorAdjust adj = new ColorAdjust();
        adj.setBrightness((-2.0 * (Math.min(energy, topEnergy)) / ((double)topEnergy)) + 1.0);
        return adj;
    }

    public void highlight(){
        //this.setStyle("-fx-background-color: #" + "ff0000");
        //this.setStyle("-fx-background-fill: black, white");
        //this.setStyle( "-fx-background-insets: 0, 1");
        //"-fx-background-fill: black, white"
        //"-fx-background-insets: 0, 1" ;
        this.setStyle(
                "-background-color: transparent, #f4f4f4;" +
                "-fx-border-width:" + size * 0.1 + ";" +
                "-fx-border-color: lightgrey;"
        );
    }

    public void removeHighlight(){
        this.setStyle("-fx-background-color: #" + "ffd700");
    }

    public void highlightBestGenome(){
        this.setStyle("-fx-background-color: #" + "00d0ff");
    }

    public void removeHighlightBestGenome(){
        this.setStyle("-fx-background-color: #" + "ffd700");
    }
}
