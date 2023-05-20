package wothers.hr;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wothers.hr.items.HyperItem;
import wothers.ift.ItemsFromText;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class HyperRegistry {
    public static final HyperRegistry INSTANCE = new HyperRegistry();

    private Map<String, String> langMap;
    private Map<String, String> registeredItems;
    private Map<Item, Integer> fuelMap;

    private HyperRegistry() {
        langMap = new HashMap<>();
        registeredItems = new HashMap<>();
        fuelMap = new HashMap<>();
    }

    public void register(String namespaceName, String itemName, HyperItem item, String displayName, Boolean isHandheld) {
        if (registeredItems.containsKey(namespaceName + ":item/" + itemName)) throw new RuntimeException("Duplicate item");
        if (displayName == null) throw new RuntimeException("Missing item display name");
        Registry.register(Registry.ITEM, new Identifier(namespaceName, itemName), item.getItem());
        langMap.put("item." + namespaceName + "." + itemName, displayName);
        registeredItems.put(namespaceName + ":item/" + itemName, isHandheld ? "handheld" : "generated");
    }

    public void addFuel(HyperItem item, short cookingTime) {
        fuelMap.put(item.getItem(), (int) cookingTime);
    }

    public Map<String, String> getLangMap() {
        return new HashMap<>(langMap);
    }

    public Map<String, String> getRegisteredItems() {
        return new HashMap<>(registeredItems);
    }

    public Map<Item, Integer> getFuelMap() {
        return new HashMap<>(fuelMap);
    }

    public static final class Recipe {
        public static final Recipe INSTANCE = new Recipe();

        private Map<Identifier, JsonElement> map;

        private Recipe() {
            map = new HashMap<>();
        }

        public void add(String namespaceName, String itemName, String recipe) {
            try {
                if (recipe.startsWith("shaped,")) resolveShapedRecipe(namespaceName, itemName, recipe.replace("shaped,", ""));
                else if (recipe.startsWith("shapeless,")) resolveShapelessRecipe(namespaceName, itemName, recipe.replace("shapeless,", ""));
                else throw new RuntimeException();
            } catch (Exception e) {
                ItemsFromText.LOGGER.warn("Failed to resolve recipe for: " + namespaceName + ":" + itemName);
            }
        }

        private void resolveShapedRecipe(String namespaceName, String itemName, String recipe) {
            JsonObject mainObject = new JsonObject();
            JsonArray patternArray = new JsonArray();
            JsonObject keysObject = new JsonObject();
            JsonObject resultObject = new JsonObject();

            String[] subStrings = recipe.split(",");

            resultObject.addProperty("item", namespaceName + ":" + itemName);
            for (int i = 0; i < subStrings.length; i++) {
                if (isNumeric(subStrings[i])) {
                    resultObject.addProperty("count", Integer.parseInt(subStrings[i]));
                    for (int j = i + 1; j < subStrings.length; j++) {
                        JsonObject keyObject = new JsonObject();
                        keyObject.addProperty("item", subStrings[j].substring(2));
                        keysObject.add(Character.toString(subStrings[j].charAt(0)), keyObject);
                    }
                    break;
                }
                patternArray.add(subStrings[i]);
            }

            mainObject.addProperty("type", "minecraft:crafting_shaped");
            mainObject.add("pattern", patternArray);
            mainObject.add("key", keysObject);
            mainObject.add("result", resultObject);

            map.put(new Identifier(namespaceName, itemName), mainObject);
        }

        private void resolveShapelessRecipe(String namespaceName, String itemName, String recipe) {
            JsonObject mainObject = new JsonObject();
            JsonArray ingredientArray = new JsonArray();
            JsonObject resultObject = new JsonObject();

            String[] subStrings = recipe.split(",");

            resultObject.addProperty("item", namespaceName + ":" + itemName);
            resultObject.addProperty("count", Integer.parseInt(subStrings[0]));
            for (int i = 1; i < subStrings.length; i++) {
                JsonObject ingredientObject = new JsonObject();
                ingredientObject.addProperty("item", subStrings[i]);
                ingredientArray.add(ingredientObject);
            }

            mainObject.addProperty("type", "minecraft:crafting_shapeless");
            mainObject.add("ingredients", ingredientArray);
            mainObject.add("result", resultObject);

            map.put(new Identifier(namespaceName, itemName), mainObject);
        }

        private boolean isNumeric(String input) {
            try {
                Integer.parseInt(input);
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        }

        public Map<Identifier, JsonElement> getMap() {
            return new HashMap<>(map);
        }
    }

    public static final class Texture {
        public static final Texture INSTANCE = new Texture();

        private Map<String, File> map;

        private Texture() {
            map = new HashMap<>();
        }

        public void add(String namespaceName, String itemName, File textureFile) {
            if (textureFile.exists()) map.put(namespaceName + ":textures/item/" + itemName + ".png", textureFile);
        }

        public Map<String, File> getMap() {
            return new HashMap<>(map);
        }
    }
}
