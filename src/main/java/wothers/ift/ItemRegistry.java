package wothers.ift;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wothers.ift.items.ItemProvider;
import java.io.File;
import java.util.*;

public final class ItemRegistry {
    public static final ItemRegistry INSTANCE = new ItemRegistry();

    private final Set<String> namespaces;
    private final Map<String, String> registeredItems;
    private final Map<String, String> langMap;
    private final Map<Item, Integer> fuelMap;

    private ItemRegistry() {
        namespaces = new HashSet<>();
        registeredItems = new HashMap<>();
        langMap = new HashMap<>();
        fuelMap = new HashMap<>();
    }

    public static boolean internalContains(String namespaceName, String itemName) {
        return Registry.ITEM.containsId(new Identifier(namespaceName, itemName));
    }

    public void register(String namespaceName, String itemName, ItemProvider item, String displayName, Boolean isHandheld) {
        Registry.register(Registry.ITEM, new Identifier(namespaceName, itemName), item.getItem());
        namespaces.add(namespaceName);
        registeredItems.put(namespaceName + ":item/" + itemName, isHandheld ? "handheld" : "generated");
        if (displayName != null) langMap.put("item." + namespaceName + "." + itemName, displayName);
    }

    public void addFuel(ItemProvider item, int cookingTime) {
        fuelMap.put(item.getItem(), cookingTime);
    }

    public Set<String> getNamespaces() {
        return Collections.unmodifiableSet(namespaces);
    }

    public Map<String, String> getRegisteredItems() {
        return Collections.unmodifiableMap(registeredItems);
    }

    public Map<String, String> getLangMap() {
        return Collections.unmodifiableMap(langMap);
    }

    public Map<Item, Integer> getFuelMap() {
        return Collections.unmodifiableMap(fuelMap);
    }

    public static final class Recipe {
        public static final Recipe INSTANCE = new Recipe();

        private final Map<Identifier, JsonElement> map;

        private Recipe() {
            map = new HashMap<>();
        }

        public void add(String namespaceName, String itemName, String recipe) {
            String[] strings = recipe.split(",");
            strings = Arrays.copyOfRange(strings, 1, strings.length);
            if (recipe.startsWith("shaped,")) addShaped(namespaceName, itemName, strings);
            if (recipe.startsWith("shapeless,")) addShapeless(namespaceName, itemName, strings);
        }

        private void addShaped(String namespaceName, String itemName, String[] strings) {
            JsonObject mainObject = new JsonObject();
            JsonArray patternArray = new JsonArray();
            JsonObject keysObject = new JsonObject();
            JsonObject resultObject = new JsonObject();

            resultObject.addProperty("item", namespaceName + ":" + itemName);
            for (int i = 0; i < strings.length; i++) {
                if (NumberUtils.isInteger(strings[i])) {
                    resultObject.addProperty("count", Integer.parseInt(strings[i]));
                    for (int j = i + 1; j < strings.length; j++) {
                        JsonObject keyObject = new JsonObject();
                        keyObject.addProperty("item", strings[j].substring(2));
                        keysObject.add(Character.toString(strings[j].charAt(0)), keyObject);
                    }
                    break;
                }
                patternArray.add(strings[i]);
            }

            mainObject.addProperty("type", "minecraft:crafting_shaped");
            mainObject.add("pattern", patternArray);
            mainObject.add("key", keysObject);
            mainObject.add("result", resultObject);

            map.put(new Identifier(namespaceName, itemName), mainObject);
        }

        private void addShapeless(String namespaceName, String itemName, String[] strings) {
            JsonObject mainObject = new JsonObject();
            JsonArray ingredientArray = new JsonArray();
            JsonObject resultObject = new JsonObject();

            resultObject.addProperty("item", namespaceName + ":" + itemName);
            resultObject.addProperty("count", Integer.parseInt(strings[0]));
            for (int i = 1; i < strings.length; i++) {
                JsonObject ingredientObject = new JsonObject();
                ingredientObject.addProperty("item", strings[i]);
                ingredientArray.add(ingredientObject);
            }

            mainObject.addProperty("type", "minecraft:crafting_shapeless");
            mainObject.add("ingredients", ingredientArray);
            mainObject.add("result", resultObject);

            map.put(new Identifier(namespaceName, itemName), mainObject);
        }

        public Map<Identifier, JsonElement> getMap() {
            return Collections.unmodifiableMap(map);
        }
    }

    public static final class Texture {
        public static final Texture INSTANCE = new Texture();

        private final Map<String, File> map;

        private Texture() {
            map = new HashMap<>();
        }

        public void add(String namespaceName, String itemName, File textureFile) {
            if (textureFile.exists()) map.put(namespaceName + ":textures/item/" + itemName + ".png", textureFile);
        }

        public Map<String, File> getMap() {
            return Collections.unmodifiableMap(map);
        }
    }
}
