package wothers.ift;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
    public static final File MAIN_FOLDER = Paths.get(MOD_ID).toFile();
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        File[] subDirectories = {};
        if (MAIN_FOLDER.exists())
            subDirectories = MAIN_FOLDER.listFiles(File::isDirectory);

        parseItems(MAIN_FOLDER);
        for (File dir : subDirectories) {
            parseItems(dir);
        }
    }

    private void parseItems(File dir) {
        String namespaceName = dir.getName();

        File[] txtFiles = {};
        if (dir.exists())
            txtFiles = dir.listFiles((file, string) -> string.endsWith(".txt"));

        for (File file : txtFiles) {
            String itemName = file.getName().replace(".txt", "");

            try {
                Properties p = new Properties();
                p.load(new FileReader(file));
                parseItem(namespaceName, itemName, p);
                HyperRegistry.Texture.INSTANCE.add(namespaceName, itemName, new File(dir.getAbsolutePath() + File.separator + itemName + ".png"));
            } catch (IOException e) {
                LOGGER.error("I/O error while reading from file: " + file.getAbsolutePath());
            }
        }
    }

    private void parseItem(String namespaceName, String itemName, Properties p) {
        try {
            HyperItem item = null;
            boolean isFireproof = Boolean.parseBoolean(p.getProperty("isFireproof"));
            boolean isHandheld = Boolean.parseBoolean(p.getProperty("isHandheld"));
            if (p.getProperty("type") != null) {
                switch (p.getProperty("type")) {
                    case "food":
                        int hunger = Integer.parseInt(p.getProperty("hunger"));
                        float saturation = Float.parseFloat(p.getProperty("saturation"));
                        boolean isSnack = Boolean.parseBoolean(p.getProperty("isSnack"));
                        item = new HyperFood(Integer.parseInt(p.getProperty("stack")), hunger, saturation, isSnack, isFireproof);
                        break;
                    case "tool":
                        float miningSpeed = Float.parseFloat(p.getProperty("miningSpeed"));
                        int miningLevel = Integer.parseInt(p.getProperty("miningLevel"));
                        float attackSpeed = Float.parseFloat(p.getProperty("attackSpeed"));
                        int attackDamage = Integer.parseInt(p.getProperty("attackDamage"));
                        int durability = Integer.parseInt(p.getProperty("durability"));
                        int enchantability = Integer.parseInt(p.getProperty("enchantability"));
                        item = new HyperTool(p.getProperty("toolType"), miningSpeed, miningLevel, attackSpeed, attackDamage, durability, enchantability, p.getProperty("repairItem"), isFireproof);
                        isHandheld = true;
                        break;
                }
            } else {
                item = new HyperItem(Integer.parseInt(p.getProperty("stack")), isFireproof);
            }

            HyperRegistry.INSTANCE.register(namespaceName, itemName, item, p.getProperty("name"), isHandheld);
            if (p.getProperty("cookingTime") != null)
                try {
                    HyperRegistry.INSTANCE.addFuel(item, Short.parseShort(p.getProperty("cookingTime")));
                } catch (NumberFormatException e) {
                    LOGGER.warn("Error parsing cooking time for item: " + namespaceName + ":" + itemName);
                }
            if (p.getProperty("recipe") != null)
                HyperRegistry.Recipe.INSTANCE.add(namespaceName, itemName, p.getProperty("recipe"));
        } catch (RuntimeException e) {
            LOGGER.error("Failed to load item: " + namespaceName + ":" + itemName + " - " + e);
        }
    }
}
