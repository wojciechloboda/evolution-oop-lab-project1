package agh.ics.oop.gui;

import agh.ics.oop.IMapElementType;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ImageLoader {
    private static Image grassImage;
    private static Image animalImage;

    public static void loadImages() {
        try {
            grassImage = new Image(new FileInputStream("src/main/resources/grass.png"));
            animalImage = new Image(new FileInputStream("src/main/resources/animal.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

    public static Image getImage(IMapElementType type) {
        return switch (type) {
            case GRASS -> grassImage;
            case ANIMAL -> animalImage;
        };
    }
}
