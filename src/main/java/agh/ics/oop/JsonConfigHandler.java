package agh.ics.oop;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class JsonConfigHandler {
    public static void saveParametersToFile(Map<String, String> paramMap, String path) {
        JsonObject object = new JsonObject();

        for (var key : paramMap.keySet()) {
            object.put(key, paramMap.get(key));
        }

        try (FileWriter file = new FileWriter(path)) {
            file.write(object.toJson());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SimulationParameters getParametersFromFile(String path) {
        try {
            File file = new File(path);
            Reader reader = Files.newBufferedReader(Paths.get(path));

            JsonObject parser = (JsonObject) Jsoner.deserialize(reader);
            HashMap<String, Object> map = new HashMap<>();

            map.put("paramsName", file.getName());

            for (var key : parser.keySet()) {
                switch (key) {
                    case "map variant:" -> {
                        switch ((String) parser.get(key)) {
                            case "HELL PORTAL" -> map.put("map variant:", Variations.BoundsHandlerType.HELL);
                            case "EARTH" -> map.put("map variant:", Variations.BoundsHandlerType.EARTH);
                            default -> System.out.println("ERROR");
                        }
                    }
                    case "grass growth variant:" -> {
                        switch ((String) parser.get(key)) {
                            case "GREEN EQUATORS" ->
                                    map.put("grass growth variant:", Variations.MapGrassGrowthType.GREEN_EQUATOR);
                            case "TOXIC DEAD" ->
                                    map.put("grass growth variant:", Variations.MapGrassGrowthType.TOXIC_DEAD);
                            default -> System.out.println("ERROR");
                        }
                    }
                    case "mutation variant:" -> {
                        switch ((String) parser.get(key)) {
                            case "CORRECTION" ->
                                    map.put("mutation variant:", Variations.MutationHandlerType.CORRECTION);
                            case "RANDOM" -> map.put("mutation variant:", Variations.MutationHandlerType.RANDOM);
                            default -> System.out.println("ERROR");
                        }
                    }
                    case "animal behavior variant:" -> {
                        switch ((String) (parser.get(key))) {
                            case "PREDESTINATION" ->
                                    map.put("animal behavior variant:", Variations.NextActGeneGeneratorType.STABLE);
                            case "CRAZINESS" ->
                                    map.put("animal behavior variant:", Variations.NextActGeneGeneratorType.CRAZY);
                            default -> System.out.println("ERROR");
                        }
                    }
                    default -> map.put(key, parser.get(key));
                }
            }

            return new SimulationParameters(map);
        } catch (Exception ex) { // nie wolno łapać Exception
            System.out.println(ex.getMessage());
            System.exit(2);
        }
        return null;
    }
}
