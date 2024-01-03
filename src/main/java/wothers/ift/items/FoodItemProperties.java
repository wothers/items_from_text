package wothers.ift.items;

import org.apache.logging.log4j.Logger;
import wothers.ift.ItemLoadException;
import wothers.ift.NumberUtils;
import java.util.List;
import java.util.Properties;

public class FoodItemProperties extends ItemProperties {
    public FoodItemProperties(Properties p, String namespaceName, String itemName, Logger logger) {
        super(p, namespaceName, itemName, logger);
    }

    @Override
    public boolean validateRequired() {
        boolean error = super.validateRequired();

        List<String> keys = List.of("hunger", "saturation");
        for (String key : keys) {
            if (checkProperty(key, NumberUtils::isNumeric)) error = true;
        }

        if (effect() != null) {
            List<String> effectKeys = List.of("effectDuration", "effectAmplifier", "effectChance");
            for (String effectKey : effectKeys) {
                if (checkProperty(effectKey, NumberUtils::isNumeric)) error = true;
            }
        }

        if (error) throw new ItemLoadException(namespaceName, itemName);
        return false;
    }

    public int hunger() {
        return Integer.parseInt(p.getProperty("hunger"));
    }

    public float saturation() {
        return Float.parseFloat(p.getProperty("saturation"));
    }

    public boolean isSnack() {
        return Boolean.parseBoolean(p.getProperty("isSnack"));
    }

    public String effect() {
        return p.getProperty("effect");
    }

    public int effectDuration() {
        return Integer.parseInt(p.getProperty("effectDuration"));
    }

    public int effectAmplifier() {
        return Integer.parseInt(p.getProperty("effectAmplifier"));
    }

    public float effectChance() {
        return Float.parseFloat(p.getProperty("effectChance"));
    }
}
