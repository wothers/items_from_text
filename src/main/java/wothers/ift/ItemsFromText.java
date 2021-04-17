package wothers.ift;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.api.ModInitializer;
import wothers.hr.HyperRegistry;
import wothers.hr.items.HyperFood;
import wothers.hr.items.HyperItem;
import wothers.hr.items.HyperTool;

public class ItemsFromText implements ModInitializer {
    public static final String MOD_ID = "itemsfromtext";
    public static final String[] FOLDER_NAMES = { "resources", "Items From Text", "assets", MOD_ID, "models",
            "textures", "item", "lang" };
    public static final String DESCRIPTION = "Resources for the Items From Text mod.";
    public static final Path MAIN_FOLDER = Paths.get(FOLDER_NAMES[3]);
    public static final Path RESOURCES_FOLDER = Paths.get(FOLDER_NAMES[0], FOLDER_NAMES[1]);
    public static final Path LANG_FOLDER = Paths.get(FOLDER_NAMES[0], FOLDER_NAMES[1], FOLDER_NAMES[2], FOLDER_NAMES[3],
            FOLDER_NAMES[7]);

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
            // Make folders
            if (RESOURCES_FOLDER.toFile().exists()) {
                deleteDirectory(RESOURCES_FOLDER.toFile());
            }
            Files.createDirectories(LANG_FOLDER);
            mcmetaMake(RESOURCES_FOLDER);
            // Load all items
            parseItems(MAIN_FOLDER);
            for (File file : subdirectories) {
                parseItems(file.toPath());
            }
            // Make language file for display names
            Gson gson = new GsonBuilder().create();
            FileWriter fw = new FileWriter(LANG_FOLDER + File.separator + "en_us.json");
            fw.write(gson.toJson(HyperRegistry.getLangObject()));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseItems(Path path) throws IOException {
        String namespaceName = path.toFile().getName();
        final Path MODELS_ITEM_FOLDER = Paths.get(FOLDER_NAMES[0], FOLDER_NAMES[1], FOLDER_NAMES[2], namespaceName,
                FOLDER_NAMES[4], FOLDER_NAMES[6]);
        final Path TEXTURES_ITEM_FOLDER = Paths.get(FOLDER_NAMES[0], FOLDER_NAMES[1], FOLDER_NAMES[2], namespaceName,
                FOLDER_NAMES[5], FOLDER_NAMES[6]);
        Files.createDirectories(MODELS_ITEM_FOLDER);
        Files.createDirectories(TEXTURES_ITEM_FOLDER);

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

            File image = new File(path + File.separator + itemName + ".png");
            if (image.exists()) {
                copyFile(image, new File(TEXTURES_ITEM_FOLDER + File.separator + itemName + ".png"));
            }

            HyperItem hi = null;
            try {
                if (p.getProperty("type") != null) {
                    switch (p.getProperty("type")) {
                    case "food":
                        hi = new HyperFood(Boolean.parseBoolean(p.getProperty("isHandheld")),
                                Integer.parseInt(p.getProperty("stack")), Integer.parseInt(p.getProperty("hunger")),
                                Float.parseFloat(p.getProperty("saturation")));
                        break;
                    case "tool":
                        hi = new HyperTool(p.getProperty("toolType"), Float.parseFloat(p.getProperty("miningSpeed")),
                                Integer.parseInt(p.getProperty("miningLevel")),
                                Float.parseFloat(p.getProperty("attackSpeed")),
                                Integer.parseInt(p.getProperty("attackDamage")),
                                Integer.parseInt(p.getProperty("durability")),
                                Integer.parseInt(p.getProperty("enchantability")), null);
                        break;
                    }
                } else {
                    hi = new HyperItem(Boolean.parseBoolean(p.getProperty("isHandheld")),
                            Integer.parseInt(p.getProperty("stack")));
                }
                jsonModelMake(MODELS_ITEM_FOLDER, namespaceName, itemName, hi.isHandheld());
                File recipeFile = new File(path + File.separator + itemName + "_recipe.json");
                if (recipeFile.exists()) {
                    HyperRegistry.Recipe.add(namespaceName, itemName, recipeFile);
                }
                HyperRegistry.register(namespaceName, itemName, hi, p.getProperty("name"));
                System.out.println("Loaded item: " + namespaceName + ":" + itemName);
            } catch (Exception e) {
                System.err.println("Failed to load item: " + namespaceName + ":" + itemName + " - " + e);
            }
        }
    }

    private boolean deleteDirectory(File f) {
        File[] allContents = f.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return f.delete();
    }

    private void copyFile(File source, File destination) throws IOException {
        FileInputStream is = new FileInputStream(source);
        FileOutputStream os = new FileOutputStream(destination);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.close();
    }

    private void mcmetaMake(Path p) throws IOException {
        FileWriter fw = new FileWriter(p + File.separator + "pack.mcmeta");
        fw.write("{\"pack\":{\"pack_format\":6,\"description\":\"" + DESCRIPTION + "\"}}");
        fw.close();
    }

    private void jsonModelMake(Path p, String namespaceName, String itemName, boolean b) throws IOException {
        String parentName;
        if (b) {
            parentName = "handheld";
        } else {
            parentName = "generated";
        }
        FileWriter fw = new FileWriter(p + File.separator + itemName + ".json");
        fw.write("{\"parent\":\"minecraft:item/" + parentName + "\",\"textures\":{\"layer0\":\"" + namespaceName
                + ":item/" + itemName + "\"}}");
        fw.close();
    }
}