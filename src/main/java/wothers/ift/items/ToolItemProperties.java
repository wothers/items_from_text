package wothers.ift.items;

import org.apache.logging.log4j.Logger;
import wothers.ift.ItemLoadException;
import wothers.ift.NumberUtils;
import java.util.List;
import java.util.Properties;

public class ToolItemProperties extends ItemProperties {
    public ToolItemProperties(Properties p, String namespaceName, String itemName, Logger logger) {
        super(p, namespaceName, itemName, logger);
    }

    @Override
    public boolean validateRequired() {
        boolean error = super.validateRequired();

        if (toolType() == null) {
            logger.error(String.format("Missing tool type - %s:%s", namespaceName, itemName));
            error = true;
        }

        List<String> keys = List.of("miningSpeed", "miningLevel", "attackSpeed", "attackDamage", "durability", "enchantability");
        for (String key : keys) {
            if (checkProperty(key, NumberUtils::isNumeric)) error = true;
        }

        if (error) throw new ItemLoadException(namespaceName, itemName);
        return false;
    }

    @Override
    public int maxStackSize() {
        return 1;
    }

    @Override
    public boolean isHandheld() {
        return true;
    }

    public String toolType() {
        return p.getProperty("toolType");
    }

    public float miningSpeed() {
        return Float.parseFloat(p.getProperty("miningSpeed"));
    }

    public int miningLevel() {
        return Integer.parseInt(p.getProperty("miningLevel"));
    }

    public float attackSpeed() {
        return Float.parseFloat(p.getProperty("attackSpeed"));
    }

    public int attackDamage() {
        return Integer.parseInt(p.getProperty("attackDamage"));
    }

    public int durability() {
        return Integer.parseInt(p.getProperty("durability"));
    }

    public int enchantability() {
        return Integer.parseInt(p.getProperty("enchantability"));
    }

    public String repairItem() {
        return p.getProperty("repairItem");
    }
}
