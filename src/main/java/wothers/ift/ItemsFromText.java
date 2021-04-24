package wothers.ift;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import net.fabricmc.api.ModInitializer;
import wothers.hr.HyperRegistry;
import wothers.hr.items.HyperFood;
import wothers.hr.items.HyperItem;
import wothers.hr.items.HyperTool;

public class ItemsFromText implements ModInitializer {
    public static final String MOD_ID = "itemsfromtext";
    public static final Path MAIN_FOLDER = Paths.get(MOD_ID);

    public void onInitialize() {
        File[] subdirectories = {};
        if (MAIN_FOLDER.toFile().exists()) {
            subdirectories = MAIN_FOLDER.toFile().listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return file.isDirectory();
                }
            });
        }

        try {
            parseItems(MAIN_FOLDER);
            for (File file : subdirectories) {
                parseItems(file.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseItems(Path path) throws IOException {
        String namespaceName = path.toFile().getName();

        File[] txtFiles = {};
        if (path.toFile().exists()) {
            txtFiles = path.toFile().listFiles(new FilenameFilter() {
                public boolean accept(File f, String s) {
                    return s.endsWith(".txt");
                }
            });
        }

        for (File f : txtFiles) {
            Properties p = new Properties();
            p.load(new FileReader(f));
            String itemName = f.getName().replace(".txt", "");

            HyperItem hi = null;
            try {
                if (p.getProperty("type") != null) {
                    switch (p.getProperty("type")) {
                    case "food":
                        hi = new HyperFood(Integer.parseInt(p.getProperty("stack")),
                                Integer.parseInt(p.getProperty("hunger")),
                                Float.parseFloat(p.getProperty("saturation")),
                                Boolean.parseBoolean(p.getProperty("isHandheld")),
                                Boolean.parseBoolean(p.getProperty("isFireproof")));
                        break;
                    case "tool":
                        hi = new HyperTool(p.getProperty("toolType"), Float.parseFloat(p.getProperty("miningSpeed")),
                                Integer.parseInt(p.getProperty("miningLevel")),
                                Float.parseFloat(p.getProperty("attackSpeed")),
                                Integer.parseInt(p.getProperty("attackDamage")),
                                Integer.parseInt(p.getProperty("durability")),
                                Integer.parseInt(p.getProperty("enchantability")), null,
                                Boolean.parseBoolean(p.getProperty("isFireproof")));
                        break;
                    }
                } else {
                    hi = new HyperItem(Integer.parseInt(p.getProperty("stack")),
                            Boolean.parseBoolean(p.getProperty("isHandheld")),
                            Boolean.parseBoolean(p.getProperty("isFireproof")));
                }
                File recipeFile = new File(path + File.separator + itemName + "_recipe.json");
                if (recipeFile.exists()) {
                    HyperRegistry.Recipe.add(namespaceName, itemName, recipeFile);
                }
                HyperRegistry.register(namespaceName, itemName, hi, p.getProperty("name"));
            } catch (Exception e) {
                System.err.println("Failed to load item: " + namespaceName + ":" + itemName + " - " + e);
            }
        }
    }
}