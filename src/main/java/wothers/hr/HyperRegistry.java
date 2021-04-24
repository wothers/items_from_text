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
    private static Map<String, String> langMap = new HashMap<String, String>();
    private static Map<String, String> registeredItems = new HashMap<String, String>();

    public static void register(String namespaceName, String itemName, HyperItem hi, String displayName) {
        if (displayName == null) {
            throw new RuntimeException("Missing item display name.");
        }
        Identifier id = new Identifier(namespaceName, itemName);
        Registry.register(Registry.ITEM, id, hi.getItem());
        langMap.put("item." + namespaceName + "." + itemName, displayName);
        String modelType;
        if (hi.isHandheld()) {
            modelType = "handheld";
        } else {
            modelType = "generated";
        }
        registeredItems.put(namespaceName + ":item/" + itemName, modelType);
    }

    public static Map<String, String> getLangMap() {
        return langMap;
    }

    public static Map<String, String> getRegisteredItems() {
        return registeredItems;
    }

    public static class Recipe {
        private static Map<Identifier, JsonElement> map = new HashMap<Identifier, JsonElement>();

        public static void add(String namespaceName, String itemName, File recipeFile) {
            JsonElement recipeElement = new JsonObject();
            try {
                recipeElement = new JsonParser().parse(new JsonReader(new FileReader(recipeFile)));
                map.put(new Identifier(namespaceName, itemName), recipeElement);
            } catch (Exception e) {
                System.err.println("Failed to load recipe for item: " + namespaceName + ":" + itemName + " - " + e);
            }
        }

        public static Map<Identifier, JsonElement> getMap() {
            return map;
        }
    }
}