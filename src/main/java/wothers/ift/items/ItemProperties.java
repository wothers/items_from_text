package wothers.ift.items;

import org.apache.logging.log4j.Logger;
import wothers.ift.NumberUtils;
import java.util.Properties;
import java.util.function.Function;

public class ItemProperties {
    final Properties p;
    final String namespaceName, itemName;
    final Logger logger;

    public ItemProperties(Properties p, String namespaceName, String itemName, Logger logger) {
        this.p = p;
        this.namespaceName = namespaceName;
        this.itemName = itemName;
        this.logger = logger;

        validateOptional();
        validateRequired();
    }

    public void validateOptional() {
        if (displayName() == null) logger.warn(String.format("Missing display name - %s:%s", namespaceName, itemName));

        if (!"tool".equals(p.getProperty("type"))) {
            String stack = p.getProperty("stack");
            if (stack != null && !NumberUtils.isNumeric(stack)) logger.warn(String.format("Non-numeric stack specified - %s:%s", namespaceName, itemName));
        }

        String cookingTime = p.getProperty("cookingTime");
        if (cookingTime != null && !NumberUtils.isNumeric(cookingTime)) logger.warn(String.format("Non-numeric cooking time specified - %s:%s", namespaceName, itemName));

        if (initialRecipeCheck()) {
            p.remove("recipe");
            logger.warn(String.format("Invalid recipe structure - %s:%s", namespaceName, itemName));
        }
    }

    public boolean validateRequired() {
        if (maxStackSize() < 1 || maxStackSize() > 64) {
            logger.error(String.format("Stack not in range 1-64 - %s:%s", namespaceName, itemName));
            return true;
        }
        return false;
    }

    private boolean initialRecipeCheck() {
        if (recipe() == null) return false;
        String[] strings = recipe().split(",");
        if (strings.length < 3) return true;
        if (recipe().startsWith("shaped")) return initialShapedRecipeCheck(strings);
        if (recipe().startsWith("shapeless")) return initialShapelessRecipeCheck(strings);
        return true;
    }

    private boolean initialShapedRecipeCheck(String[] strings) {
        if (NumberUtils.isInteger(strings[1])) return true;
        if (strings[1].length() < 1 || strings[1].length() > 3) return true;
        for (int i = 2; i < 4; i++) {
            if (NumberUtils.isInteger(strings[i])) return false;
            if (strings[i].length() != strings[1].length()) return true;
        }
        return !NumberUtils.isInteger(strings[4]);
    }

    private boolean initialShapelessRecipeCheck(String[] strings) {
        return !NumberUtils.isInteger(strings[1]);
    }

    boolean checkProperty(String key, Function<String, Boolean> isValid) {
        boolean error = !isValid.apply(p.getProperty(key));
        if (error) logger.error(String.format("Missing or invalid %s value - %s:%s", key, namespaceName, itemName));
        return error;
    }

    public String displayName() {
        return p.getProperty("name");
    }

    public int maxStackSize() {
        return NumberUtils.tryParse(p.getProperty("stack"), 64);
    }

    public boolean isHandheld() {
        return Boolean.parseBoolean(p.getProperty("isHandheld"));
    }

    public boolean isFireproof() {
        return Boolean.parseBoolean(p.getProperty("isFireproof"));
    }

    public Integer cookingTime() {
        return NumberUtils.tryParse(p.getProperty("cookingTime"), null);
    }

    public String recipe() {
        return p.getProperty("recipe");
    }
}
