package agh.ics.oop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Vector2dTest {
    Vector2d a = new Vector2d(0, 0);
    Vector2d b = new Vector2d(0, 0);
    Vector2d c = new Vector2d(1, 1);
    Vector2d d = new Vector2d(0, 1);
    Vector2d e = new Vector2d(-2147483647, 2147483647);
    Vector2d f = new Vector2d(2147483640, -21474830);
    Vector2d g = new Vector2d(2147483647, -2147483647);
    Vector2d h = new Vector2d(2147483647, -2147483647);

    @Test
    public void testVector2d_equals(){
        String str = "str";
        Assertions.assertTrue(a.equals(b));
        Assertions.assertFalse(c.equals(d));
        Assertions.assertFalse(e.equals(f));
        Assertions.assertTrue(g.equals(h));
        Assertions.assertFalse(a.equals(str));
    }

    @Test
    public void testVector2d_toString(){
        Assertions.assertEquals("(0,0)", a.toString());
        Assertions.assertEquals("(-2147483647,2147483647)", e.toString());
    }

    @Test
    public void testVector2d_precedes(){
        Assertions.assertTrue(a.precedes(c));
        Assertions.assertTrue(a.precedes(b));
        Assertions.assertFalse(c.precedes(a));
        Assertions.assertFalse(f.precedes(g));
    }

    @Test
    public void testVector2d_follows(){
        Assertions.assertTrue(c.follows(a));
        Assertions.assertTrue(b.follows(a));
        Assertions.assertFalse(a.follows(c));
        Assertions.assertFalse(g.follows(f));
    }

    @Test
    public void testVector2d_upperRight(){
        Vector2d vec1 = new Vector2d(1, 1);
        Assertions.assertEquals(vec1, a.upperRight(c));

        Vector2d vec2 = new Vector2d(0, 2147483647);
        Assertions.assertEquals(vec2, a.upperRight(e));
        Assertions.assertEquals(vec2, e.upperRight(a));
    }

    @Test
    public void testVector2d_lowerLeft(){
        Vector2d vec1 = new Vector2d(0, 0);
        Assertions.assertEquals(vec1, a.lowerLeft(c));

        Vector2d vec2 = new Vector2d(-2147483647, 0);
        Assertions.assertEquals(vec2, a.lowerLeft(e));
        Assertions.assertEquals(vec2, e.lowerLeft(a));
    }

    @Test
    public void testVector2d_add(){
        Vector2d vec1 = new Vector2d(1, 1);
        Assertions.assertEquals(vec1, a.add(c));

        Vector2d vec2 = new Vector2d(2147483640, -21474829);
        Assertions.assertEquals(vec2, d.add(f));
        Assertions.assertEquals(vec2, f.add(d));
    }

    @Test
    public void testVector2d_subtract(){
        Vector2d vec1 = new Vector2d(-1, -1);
        Assertions.assertEquals(vec1, a.subtract(c));

        Vector2d vec2 = new Vector2d(-2147483640, 21474831);
        Assertions.assertEquals(vec2, d.subtract(f));

        Vector2d vec3 = new Vector2d(2147483640, -21474831);
        Assertions.assertEquals(vec3, f.subtract(d));
    }

    @Test
    public void testVector2d_opposite(){
        Vector2d vec1 = new Vector2d(0, 0);
        Assertions.assertEquals(vec1, a.opposite());

        Vector2d vec2 = new Vector2d(-1, -1);
        Assertions.assertEquals(vec2, c.opposite());

        Vector2d vec3 = new Vector2d(2147483647, -2147483647);
        Assertions.assertEquals(vec3, e.opposite());
    }







}
