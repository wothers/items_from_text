package wothers.ift;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.fabricmc.api.ModInitializer;
import wothers.hr.HyperRegistry;
import wothers.hr.items.HyperFood;
import wothers.hr.items.HyperItem;
import wothers.hr.items.HyperTool;

public class ItemsFromText implements ModInitializer {
    public static final String MOD_ID = "itemsfromtext";
    public static final Path MAIN_FOLDER = Paths.get(MOD_ID);
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        File[] subDirectories = {};
        if (MAIN_FOLDER.toFile().exists())
            subDirectories = MAIN_FOLDER.toFile().listFiles(File::isDirectory);

        try {
            parseItems(MAIN_FOLDER);
            for (File file : subDirectories) {
                parseItems(file.toPath());
            }
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    private void parseItems(Path path) throws IOException {
        String namespaceName = path.toFile().getName();

        File[] txtFiles = {};
        if (path.toFile().exists())
            txtFiles = path.toFile().listFiles((file, string) -> string.endsWith(".txt"));

        for (File file : txtFiles) {
            Properties properties = new Properties();
            properties.load(new FileReader(file));
            String itemName = file.getName().replace(".txt", "");

            HyperItem item = null;
            try {
                boolean isFireproof = Boolean.parseBoolean(properties.getProperty("isFireproof"));
                boolean isHandheld = Boolean.parseBoolean(properties.getProperty("isHandheld"));
                if (properties.getProperty("type") != null) {
                    switch (properties.getProperty("type")) {
                        case "food":
                            int hunger = Integer.parseInt(properties.getProperty("hunger"));
                            float saturation = Float.parseFloat(properties.getProperty("saturation"));
                            boolean isSnack = Boolean.parseBoolean(properties.getProperty("isSnack"));
                            item = new HyperFood(Integer.parseInt(properties.getProperty("stack")), hunger, saturation, isSnack, isFireproof);
                            break;
                        case "tool":
                            float miningSpeed = Float.parseFloat(properties.getProperty("miningSpeed"));
                            int miningLevel = Integer.parseInt(properties.getProperty("miningLevel"));
                            float attackSpeed = Float.parseFloat(properties.getProperty("attackSpeed"));
                            int attackDamage = Integer.parseInt(properties.getProperty("attackDamage"));
                            int durability = Integer.parseInt(properties.getProperty("durability"));
                            int enchantability = Integer.parseInt(properties.getProperty("enchantability"));
                            item = new HyperTool(properties.getProperty("toolType"), miningSpeed, miningLevel, attackSpeed, attackDamage, durability, enchantability, properties.getProperty("repairItem"), isFireproof);
                            isHandheld = true;
                            break;
                    }
                } else {
                    item = new HyperItem(Integer.parseInt(properties.getProperty("stack")), isFireproof);
                }
                HyperRegistry.INSTANCE.register(namespaceName, itemName, item, properties.getProperty("name"), isHandheld);
                if (properties.getProperty("cookingTime") != null)
                    try {
                        HyperRegistry.INSTANCE.addFuel(item, Short.parseShort(properties.getProperty("cookingTime")));
                    } catch (NumberFormatException e) {
                        LOGGER.warn("Error parsing cooking time for item: " + namespaceName + ":" + itemName);
                    }
                if (properties.getProperty("recipe") != null)
                    HyperRegistry.Recipe.INSTANCE.add(namespaceName, itemName, properties.getProperty("recipe"));
                HyperRegistry.Texture.INSTANCE.add(namespaceName, itemName, new File(path + File.separator + itemName + ".png"));
            } catch (Exception e) {
                LOGGER.error("Failed to load item: " + namespaceName + ":" + itemName + " - " + e);
            }
        }
    }
}
