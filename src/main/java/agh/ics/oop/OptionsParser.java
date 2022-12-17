package agh.ics.oop;
import java.util.ArrayList;
import java.util.Objects;

public class OptionsParser {
    public static ArrayList<MoveDirection> parse(String[] args) throws IllegalArgumentException{
        var directionList = new ArrayList<MoveDirection>();

        if(args.length == 1 && Objects.equals(args[0], "")){
            return directionList;
        }

        for(String str : args){
            switch (str) {
                case "f", "forward" -> directionList.add(MoveDirection.FORWARD);
                case "b", "backward" -> directionList.add(MoveDirection.BACKWARD);
                case "r", "right" -> directionList.add(MoveDirection.RIGHT);
                case "l", "left" -> directionList.add(MoveDirection.LEFT);
                default -> throw new IllegalArgumentException(str + " is not legal move specification");
            }
        }

        return directionList;
    }
}
