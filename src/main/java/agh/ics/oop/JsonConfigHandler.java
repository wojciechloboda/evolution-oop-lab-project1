package agh.ics.oop;

import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class JsonConfigHandler {
    public static SimulationParameters getParametersFromFile(String path){
        try {
            Reader reader = Files.newBufferedReader(Paths.get(path));
            Variations.BoundsHandlerType boundsHandlerType;
            Variations.MapGrassGrowthType grassGrowthType;
            Variations.MutationHandlerType mutationHandlerType;
            Variations.NextActGeneGeneratorType nextActGeneGeneratorType;

            // create parser
            JsonObject parser = (JsonObject) Jsoner.deserialize(reader);
            HashMap<String, Object> map = new HashMap<>();

            for(var key : parser.keySet()){
                switch(key){
                    case "boundsHandlerType" -> {
                        switch((String)parser.get(key)){
                            case "hell" -> map.put("boundsHandlerType", Variations.BoundsHandlerType.HELL);
                            case "earth" -> map.put("boundsHandlerType", Variations.BoundsHandlerType.EARTH);
                            default -> System.out.println("ERROR");
                        }
                    }
                    case "grassGrowthType" -> {
                        switch((String)parser.get(key)){
                            case "green" -> map.put("grassGrowthType", Variations.MapGrassGrowthType.GREEN_EQUATOR);
                            case "toxic" -> map.put("grassGrowthType", Variations.MapGrassGrowthType.TOXIC_DEAD);
                            default -> System.out.println("ERROR");
                        }
                    }
                    case "mutationHandlerType" -> {
                        switch((String)parser.get(key)){
                            case "correction" -> map.put("mutationHandlerType", Variations.MutationHandlerType.CORRECTION);
                            case "random" -> map.put("mutationHandlerType", Variations.MutationHandlerType.RANDOM);
                            default -> System.out.println("ERROR");
                        }
                    }
                    case "nextActGeneGeneratorType" -> {
                        switch((String)(parser.get(key))){
                            case "stable" -> map.put("nextActGeneGeneratorType", Variations.NextActGeneGeneratorType.STABLE);
                            case "crazy" -> map.put("nextActGeneGeneratorType", Variations.NextActGeneGeneratorType.CRAZY);
                            default -> System.out.println("ERROR");
                        }
                    }
                    default -> map.put(key, parser.get(key));
                }
            }

            return new SimulationParameters(map);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
