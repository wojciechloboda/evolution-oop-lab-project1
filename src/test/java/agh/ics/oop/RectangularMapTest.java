package agh.ics.oop;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RectangularMapTest {
    private final RectangularMap map = new RectangularMap(10, 10);

    @Test
    public void TestPlace(){
        OldAnimal a1 = new OldAnimal(map, new Vector2d(2, 2));
        OldAnimal a2 = new OldAnimal(map, new Vector2d(0, 0));
        OldAnimal a3 = new OldAnimal(map, new Vector2d(100, 100));

        Assertions.assertTrue(map.place(a1));
        Assertions.assertTrue(map.place(a2));

        Throwable exception = Assertions.assertThrows(IllegalArgumentException.class, () -> map.place(a3));
        Assertions.assertEquals("Position (100,100) is out of map bounds", exception.getMessage());
    }

    @Test
    public void testIsOccupied(){
        OldAnimal a1 = new OldAnimal(map, new Vector2d(2, 2));
        OldAnimal a2 = new OldAnimal(map, new Vector2d(0, 0));

        map.place(a1);
        map.place(a2);

        Assertions.assertTrue(map.isOccupied(new Vector2d(2, 2)));
        Assertions.assertTrue(map.isOccupied(new Vector2d(0, 0)));
        Assertions.assertFalse(map.isOccupied(new Vector2d(1, 0)));
    }

    @Test
    public void testCanMoveTo(){
        OldAnimal a1 = new OldAnimal(map, new Vector2d(2, 2));

        map.place(a1);

        Assertions.assertFalse(map.canMoveTo(new Vector2d(2, 2)));
        Assertions.assertTrue(map.canMoveTo(new Vector2d(1, 0)));
        Assertions.assertFalse(map.canMoveTo(new Vector2d(10, 10)));
    }

    @Test
    public void testObjectAt(){
        OldAnimal a1 = new OldAnimal(map, new Vector2d(2, 2));

        map.place(a1);

        Assertions.assertEquals(a1, map.objectAt(new Vector2d(2, 2)));
        Assertions.assertNull(map.objectAt(new Vector2d(0, 0)));
    }

    @Test
    public void testWrongAnimalPlacement(){
        OldAnimal a1 = new OldAnimal(map, new Vector2d(0, 0));
        OldAnimal a2 = new OldAnimal(map, new Vector2d(0, 0));

        Assertions.assertTrue(map.place(a1));
        Throwable exception = Assertions.assertThrows(IllegalArgumentException.class, () -> map.place(a2));
        Assertions.assertEquals("Position (0,0) is already taken by another animal", exception.getMessage());
    }


}
