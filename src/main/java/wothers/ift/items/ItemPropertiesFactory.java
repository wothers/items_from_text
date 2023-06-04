package wothers.ift.items;

import org.apache.logging.log4j.Logger;
import java.util.Properties;

public final class ItemPropertiesFactory {
    private ItemPropertiesFactory() {}

    public static ItemProperties create(Properties p, String namespaceName, String itemName, Logger logger) {
        String type = p.getProperty("type");
        if (type != null) switch (type) {
            case "food":
                return new FoodItemProperties(p, namespaceName, itemName, logger);
            case "tool":
                return new ToolItemProperties(p, namespaceName, itemName, logger);
            default:
                logger.warn(String.format("Invalid type \"%s\" - %s:%s", type, namespaceName, itemName));
        }
        return new ItemProperties(p, namespaceName, itemName, logger);
    }
}
