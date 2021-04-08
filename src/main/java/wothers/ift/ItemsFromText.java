package wothers.ift;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import wothers.hr.HyperRegistry;
import wothers.hr.items.HyperFood;
import wothers.hr.items.HyperItem;
import wothers.hr.items.HyperTool;

public class ItemsFromText implements ModInitializer {
    public static final String MOD_ID = "itemsfromtext";
    public static final String[] SUBFOLDERS = { "resources", "Items From Text", "assets", MOD_ID, "models", "textures",
            "item", "lang" };
    public static final String DESCRIPTION = "Resources for the Items From Text mod.";
    public static final Path MAIN_FOLDER = Paths.get(SUBFOLDERS[3]);
    public static final Path RESOURCES_FOLDER = Paths.get(SUBFOLDERS[0], SUBFOLDERS[1]);
    public static final Path MODELS_ITEM_FOLDER = Paths.get(SUBFOLDERS[0], SUBFOLDERS[1], SUBFOLDERS[2], SUBFOLDERS[3],
            SUBFOLDERS[4], SUBFOLDERS[6]);
    public static final Path TEXTURES_ITEM_FOLDER = Paths.get(SUBFOLDERS[0], SUBFOLDERS[1], SUBFOLDERS[2],
            SUBFOLDERS[3], SUBFOLDERS[5], SUBFOLDERS[6]);
    public static final Path LANG_FOLDER = Paths.get(SUBFOLDERS[0], SUBFOLDERS[1], SUBFOLDERS[2], SUBFOLDERS[3],
            SUBFOLDERS[7]);

    public void onInitialize() {
        try {
            // Make folders
            if (RESOURCES_FOLDER.toFile().exists()) {
                deleteDirectory(RESOURCES_FOLDER.toFile());
            }
            Files.createDirectories(MODELS_ITEM_FOLDER);
            Files.createDirectories(TEXTURES_ITEM_FOLDER);
            Files.createDirectories(LANG_FOLDER);
            mcmetaMake(RESOURCES_FOLDER);
            // Load all txt files into array
            File[] txtFiles = {};
            if (MAIN_FOLDER.toFile().exists()) {
                txtFiles = MAIN_FOLDER.toFile().listFiles(new FilenameFilter() {
                    public boolean accept(File f, String s) {
                        return s.endsWith(".txt");
                    }
                });
            }
            String langString = "";
            for (int index = 0; index < txtFiles.length; index++) {
                Properties p = new Properties();
                p.load(new FileInputStream(txtFiles[index]));
                String fileName = txtFiles[index].getName().replace(".txt", "");
                // Copy image file
                File image = new File(MAIN_FOLDER + File.separator + fileName + ".png");
                if (image.exists()) {
                    copyFile(image, new File(TEXTURES_ITEM_FOLDER + File.separator + fileName + ".png"));
                }
                // Add entry to lang string
                if (p.getProperty("name") != null) {
                    langString = langString + "\"item." + MOD_ID + "." + fileName + "\":" + "\"" + p.getProperty("name")
                            + "\"";
                    if (index < txtFiles.length - 1) {
                        langString = langString + ",";
                    }
                }
                // Register item and make model file
                HyperItem hi = null;
                if (p.getProperty("type") != null) {
                    switch (p.getProperty("type")) {
                    case "food":
                        try {
                            hi = new HyperFood(Boolean.parseBoolean(p.getProperty("isHandheld")),
                                    Integer.parseInt(p.getProperty("stack")), Integer.parseInt(p.getProperty("hunger")),
                                    Float.parseFloat(p.getProperty("saturation")));
                        } catch (Exception e) {
                            System.out.println("Failed to load item: " + fileName);
                        }
                        break;
                    case "tool":
                        try {
                            hi = new HyperTool(p.getProperty("toolType"),
                                    Float.parseFloat(p.getProperty("miningSpeed")),
                                    Integer.parseInt(p.getProperty("miningLevel")),
                                    Float.parseFloat(p.getProperty("attackSpeed")),
                                    Integer.parseInt(p.getProperty("attackDamage")),
                                    Integer.parseInt(p.getProperty("durability")),
                                    Integer.parseInt(p.getProperty("enchantability")), null);
                        } catch (Exception e) {
                            System.out.println("Failed to load item: " + fileName);
                        }
                        break;
                    }
                } else {
                    try {
                        hi = new HyperItem(Boolean.parseBoolean(p.getProperty("isHandheld")),
                                Integer.parseInt(p.getProperty("stack")));
                    } catch (Exception e) {
                        System.out.println("Failed to load item: " + fileName);
                    }

                }
                if (hi != null) {
                    if (hi.isHandheld()) {
                        jsonModelMake(MODELS_ITEM_FOLDER, fileName, "handheld");
                    } else {
                        jsonModelMake(MODELS_ITEM_FOLDER, fileName, "generated");
                    }
                    HyperRegistry.register(new Identifier(MOD_ID, fileName), hi);
                }
            }
            jsonLangMake(LANG_FOLDER, langString);
        } catch (IOException e) {
            e.printStackTrace();
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

    private void jsonModelMake(Path p, String fileName, String parentName) throws IOException {
        FileWriter fw = new FileWriter(p + File.separator + fileName + ".json");
        fw.write("{\"parent\":\"minecraft:item/" + parentName + "\",\"textures\":{\"layer0\":\"" + MOD_ID + ":item/"
                + fileName + "\"}}");
        fw.close();
    }

    private void jsonLangMake(Path p, String s) throws IOException {
        FileWriter fw = new FileWriter(p + File.separator + "en_us.json");
        fw.write("{" + s + "}");
        fw.close();
    }
}