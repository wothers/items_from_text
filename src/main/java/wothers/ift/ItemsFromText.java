package wothers.ift;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FilenameFilter;
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
        if (MAIN_FOLDER.toFile().exists()) {
            subDirectories = MAIN_FOLDER.toFile().listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isDirectory();
                }
            });
        }

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
        if (path.toFile().exists()) {
            txtFiles = path.toFile().listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File f, String s) {
                    return s.endsWith(".txt");
                }
            });
        }

        for (File file : txtFiles) {
            Properties properties = new Properties();
            properties.load(new FileReader(file));
            String itemName = file.getName().replace(".txt", "");

            HyperItem item = null;
            try {
                if (properties.getProperty("type") != null) {
                    switch (properties.getProperty("type")) {
                        case "food":
                            item = new HyperFood(Integer.parseInt(properties.getProperty("stack")), Integer.parseInt(properties.getProperty("hunger")), Float.parseFloat(properties.getProperty("saturation")), Boolean.parseBoolean(properties.getProperty("isSnack")), Boolean.parseBoolean(properties.getProperty("isHandheld")), Boolean.parseBoolean(properties.getProperty("isFireproof")));
                            break;
                        case "tool":
                            item = new HyperTool(properties.getProperty("toolType"), Float.parseFloat(properties.getProperty("miningSpeed")), Integer.parseInt(properties.getProperty("miningLevel")), Float.parseFloat(properties.getProperty("attackSpeed")), Integer.parseInt(properties.getProperty("attackDamage")), Integer.parseInt(properties.getProperty("durability")), Integer.parseInt(properties.getProperty("enchantability")), Boolean.parseBoolean(properties.getProperty("isFireproof")));
                            break;
                    }
                } else {
                    item = new HyperItem(Integer.parseInt(properties.getProperty("stack")), Boolean.parseBoolean(properties.getProperty("isHandheld")), Boolean.parseBoolean(properties.getProperty("isFireproof")));
                }
                HyperRegistry.INSTANCE.register(namespaceName, itemName, item, properties.getProperty("name"));
                if (properties.getProperty("recipe") != null)
                    HyperRegistry.Recipe.INSTANCE.add(namespaceName, itemName, properties.getProperty("recipe"));
                HyperRegistry.Texture.INSTANCE.add(namespaceName, itemName, new File(path + File.separator + itemName + ".png"));
            } catch (Exception e) {
                LOGGER.error("Failed to load item: " + namespaceName + ":" + itemName + " - " + e);
            }
        }
    }
}
