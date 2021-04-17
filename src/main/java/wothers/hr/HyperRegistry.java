package wothers.hr;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wothers.hr.items.HyperItem;

public class HyperRegistry {
    private static JsonObject langObject = new JsonObject();

    public static void register(String namespaceName, String itemName, HyperItem hi, String displayName) {
        Registry.register(Registry.ITEM, new Identifier(namespaceName, itemName), hi.getItem());
        langObject.addProperty("item." + namespaceName + "." + itemName, displayName);
    }

    public static JsonObject getLangObject() {
        return langObject;
    }

    public static class Recipe {
        private static Map<Identifier, JsonElement> map = new HashMap<Identifier, JsonElement>();

        public static void add(String namespaceName, String itemName, File recipeFile) {
            JsonObject recipeElement = new JsonObject();
            try {
                recipeElement = (JsonObject) new JsonParser().parse(new JsonReader(new FileReader(recipeFile)));
                map.put(new Identifier(namespaceName, itemName), recipeElement);
            } catch (Exception e) {
                System.err.println(
                        "Failed to load crafting recipe for item: " + namespaceName + ":" + itemName + " - " + e);
            }
        }

        public static Map<Identifier, JsonElement> getMap() {
            return map;
        }
    }
}