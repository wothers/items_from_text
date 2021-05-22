package wothers.hr;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wothers.hr.items.HyperItem;
import wothers.ift.ItemsFromText;

public class HyperRegistry {
    public static HyperRegistry INSTANCE = new HyperRegistry();

    private Map<String, String> langMap;
    private Map<String, String> registeredItems;

    public HyperRegistry() {
        langMap = new HashMap<>();
        registeredItems = new HashMap<>();
    }

    public void register(String namespaceName, String itemName, HyperItem item, String displayName) {
        if (registeredItems.containsKey(namespaceName + ":item/" + itemName))
            throw new RuntimeException("Duplicate item");
        if (displayName == null)
            throw new RuntimeException("Missing item display name");
        Registry.register(Registry.ITEM, new Identifier(namespaceName, itemName), item.getItem());
        langMap.put("item." + namespaceName + "." + itemName, displayName);
        registeredItems.put(namespaceName + ":item/" + itemName, item.isHandheld ? "handheld" : "generated");
    }

    public Map<String, String> getLangMap() {
        return new HashMap<>(langMap);
    }

    public Map<String, String> getRegisteredItems() {
        return new HashMap<>(registeredItems);
    }

    public static class Recipe {
        public static Recipe INSTANCE = new Recipe();

        private Map<Identifier, JsonElement> map;

        public Recipe() {
            map = new HashMap<>();
        }

        public void add(String namespaceName, String itemName, String recipe) {
            try {
                if (recipe.startsWith("shaped,"))
                    resolveShapedRecipe(namespaceName, itemName, recipe.replace("shaped,", ""));
                else if (recipe.startsWith("shapeless,"))
                    resolveShapelessRecipe(namespaceName, itemName, recipe.replace("shapeless,", ""));
                else
                    throw new RuntimeException();
            } catch (Exception e) {
                ItemsFromText.LOGGER.warn("Failed to resolve recipe for: " + namespaceName + ":" + itemName);
            }
        }

        private void resolveShapedRecipe(String namespaceName, String itemName, String recipe) {
            JsonObject mainObject = new JsonObject();
            JsonArray patternArray = new JsonArray();
            JsonObject keysObject = new JsonObject();
            JsonObject resultObject = new JsonObject();

            List<String> subStrings = getSubStrings(recipe);

            resultObject.addProperty("item", namespaceName + ":" + itemName);
            for (int i = 0; i < subStrings.size(); i++) {
                if (isNumeric(subStrings.get(i))) {
                    resultObject.addProperty("count", Integer.parseInt(subStrings.get(i)));
                    for (int j = i + 1; j < subStrings.size(); j++) {
                        JsonObject keyObject = new JsonObject();
                        keyObject.addProperty("item", subStrings.get(j).substring(2));
                        keysObject.add(Character.toString(subStrings.get(j).charAt(0)), keyObject);
                    }
                    break;
                }
                patternArray.add(subStrings.get(i));
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

            List<String> subStrings = getSubStrings(recipe);

            resultObject.addProperty("item", namespaceName + ":" + itemName);
            resultObject.addProperty("count", Integer.parseInt(subStrings.get(0)));
            for (int i = 1; i < subStrings.size(); i++) {
                JsonObject ingredientObject = new JsonObject();
                ingredientObject.addProperty("item", subStrings.get(i));
                ingredientArray.add(ingredientObject);
            }

            mainObject.addProperty("type", "minecraft:crafting_shapeless");
            mainObject.add("ingredients", ingredientArray);
            mainObject.add("result", resultObject);

            map.put(new Identifier(namespaceName, itemName), mainObject);
        }

        private List<String> getSubStrings(String input) {
            List<String> result = new ArrayList<>();
            int length = input.length();
            int index = 0;
            int start = 0;
            while (index < length) {
                if (input.charAt(index) == ',') {
                    result.add(input.substring(start, index));
                    start = index + 1;
                } else if (index == length - 1) {
                    result.add(input.substring(start, index + 1));
                    break;
                }
                index++;
            }
            return result;
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

    public static class Texture {
        public static Texture INSTANCE = new Texture();

        private Map<String, File> map;

        public Texture() {
            map = new HashMap<>();
        }

        public void add(String namespaceName, String itemName, File textureFile) {
            if (textureFile.exists())
                map.put(namespaceName + ":textures/item/" + itemName + ".png", textureFile);
        }

        public Map<String, File> getMap() {
            return new HashMap<>(map);
        }
    }
}
