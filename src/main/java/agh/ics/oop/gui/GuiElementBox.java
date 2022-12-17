package agh.ics.oop.gui;
import agh.ics.oop.Grass;
import agh.ics.oop.IMapElement;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox{
    private final ImageView imageView;
    private final VBox elementVBox;
    private final Label elementLabel;

    public GuiElementBox() {
        imageView = new ImageView();
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        elementLabel = new Label();
        elementVBox = new VBox(imageView, elementLabel);
        elementVBox.setAlignment(Pos.CENTER);
    }

    private static Image loadImage(IMapElement element){
        Image image;

        try{
            image = new Image(new FileInputStream(element.getResourcePath()));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(2);
            return null;
        }
        return image;
    }

    public VBox updateAndGetElementVBox(IMapElement element){
        if(element instanceof Grass){
            elementLabel.setText("Grass");
        }
        else{
            elementLabel.setText(element.getPosition().toString());
        }
        imageView.setImage(loadImage(element));

        return this.elementVBox;
    }
}
