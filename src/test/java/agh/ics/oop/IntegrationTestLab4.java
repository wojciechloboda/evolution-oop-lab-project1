package agh.ics.oop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class IntegrationTestLab4 {

    @Test
    void test1AnimalMove(){
        String[] args = {"f", "f", "f", "f"};
        ArrayList<MoveDirection> directions = OptionsParser.parse(args);

        AbstractWorldMap map = new RectangularMap(5, 5);
        Vector2d[] positions = {new Vector2d(2,2)};
        SimulationEngine engine = new SimulationEngine(directions, map, positions);
        engine.run();

        Assertions.assertTrue(map.isOccupied(new Vector2d(2, 4)));
    }

    @Test
    void test2AnimalsMove(){
        String[] args = {"f", "r", "f", "f"};
        Vector2d[] positions = {new Vector2d(2,2), new Vector2d(2, 3)};

        ArrayList<MoveDirection> directions = OptionsParser.parse(args);
        AbstractWorldMap map = new RectangularMap(5, 5);
        SimulationEngine engine = new SimulationEngine(directions, map, positions);
        engine.run();

        Assertions.assertTrue(map.isOccupied(new Vector2d(2, 2)));
        Assertions.assertTrue(map.isOccupied(new Vector2d(3, 3)));
    }

    @Test
    void test2AnimalsPlacement(){
        String[] args = {""};
        Vector2d[] positions = {new Vector2d(3,3), new Vector2d(3, 3)};

        ArrayList<MoveDirection> directions = OptionsParser.parse(args);
        RectangularMap map = new RectangularMap(5, 5);

        Throwable exception = Assertions.assertThrows(IllegalArgumentException.class,() -> new SimulationEngine(directions, map, positions));
        Assertions.assertEquals( "Position (3,3) is already taken by another animal", exception.getMessage());
    }

    @Test
    void testWithGivenData(){
        String[] args = {"f", "b", "r", "l", "f", "f", "r", "r", "f", "f", "f", "f", "f", "f", "f", "f"};
        ArrayList<MoveDirection> directions = OptionsParser.parse(args);
        AbstractWorldMap map = new RectangularMap(10, 5);
        Vector2d[] positions = {new Vector2d(2,2), new Vector2d(3,4)};
        SimulationEngine engine = new SimulationEngine(directions, map, positions);
        engine.run();

        Assertions.assertTrue(map.isOccupied(new Vector2d(2, 0)));
        Assertions.assertTrue(map.isOccupied(new Vector2d(3, 4)));
    }

    @Test
    void testParserThrowingException(){
        String[] args = {"f", "b", "r", "l", "f", "vololo", "r", "r", "f", "f", "f", "f", "f", "f", "f", "f"};

        Throwable exception = Assertions.assertThrows(IllegalArgumentException.class,() -> OptionsParser.parse(args));
        Assertions.assertEquals( "vololo is not legal move specification", exception.getMessage());
    }
}
