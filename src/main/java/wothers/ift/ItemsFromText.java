package wothers.ift;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    public static final String[] SUBFOLDERS = { "resourcepacks", "Items From Text", "assets", MOD_ID, "models",
            "textures", "item", "lang" };

    public static final Path MAIN_FOLDER = Paths.get(SUBFOLDERS[3]);
    public static final Path RESOURCEPACKS_FOLDER = Paths.get(SUBFOLDERS[0], SUBFOLDERS[1]);
    public static final Path MODELS_ITEM_FOLDER = Paths.get(SUBFOLDERS[0], SUBFOLDERS[1], SUBFOLDERS[2], SUBFOLDERS[3],
            SUBFOLDERS[4], SUBFOLDERS[6]);
    public static final Path TEXTURES_ITEM_FOLDER = Paths.get(SUBFOLDERS[0], SUBFOLDERS[1], SUBFOLDERS[2],
            SUBFOLDERS[3], SUBFOLDERS[5], SUBFOLDERS[6]);
    public static final Path LANG_FOLDER = Paths.get(SUBFOLDERS[0], SUBFOLDERS[1], SUBFOLDERS[2], SUBFOLDERS[3],
            SUBFOLDERS[7]);

    public void onInitialize() {
        try {
            if (RESOURCEPACKS_FOLDER.toFile().exists()) {
                deleteDirectory(RESOURCEPACKS_FOLDER.toFile());
            }
            Files.createDirectories(MODELS_ITEM_FOLDER);
            Files.createDirectories(TEXTURES_ITEM_FOLDER);
            Files.createDirectories(LANG_FOLDER);
            mcmetaMake(RESOURCEPACKS_FOLDER);

            String langString = "";

            File[] txtFiles = {};
            if (MAIN_FOLDER.toFile().exists()) {
                txtFiles = MAIN_FOLDER.toFile().listFiles(new FilenameFilter() {
                    public boolean accept(File f, String s) {
                        return s.endsWith(".txt");
                    }
                });
            }

            for (int index = 0; index < txtFiles.length; index++) {
                String fileName = txtFiles[index].getName().replace(".txt", "");
                File image = new File(MAIN_FOLDER + File.separator + fileName + ".png");
                if (image.exists()) {
                    copyFile(image, new File(TEXTURES_ITEM_FOLDER + File.separator + fileName + ".png"));
                }

                Properties p = new Properties();
                p.load(new FileInputStream(txtFiles[index]));

                if (p.getProperty("name") != null) {
                    langString = langString + "\"item." + MOD_ID + "." + fileName + "\":" + "\"" + p.getProperty("name")
                            + "\"";
                    if (index < txtFiles.length - 1) {
                        langString = langString + ",";
                    }
                }

                String parentName = null;
                HyperItem hi = null;

                if (p.getProperty("type") != null) {
                    switch (p.getProperty("type")) {
                    case "food":
                        hi = new HyperFood(null, null, Boolean.parseBoolean(p.getProperty("isHandheld")),
                                Integer.parseInt(p.getProperty("stack")), Integer.parseInt(p.getProperty("hunger")),
                                Float.parseFloat(p.getProperty("saturation")));
                        break;
                    case "tool":
                        hi = new HyperTool(null, null, p.getProperty("toolType"),
                                Float.parseFloat(p.getProperty("miningSpeed")),
                                Integer.parseInt(p.getProperty("miningLevel")),
                                Float.parseFloat(p.getProperty("attackSpeed")) - 4,
                                Integer.parseInt(p.getProperty("attackDamage")),
                                Integer.parseInt(p.getProperty("durability")),
                                Integer.parseInt(p.getProperty("enchantability")), null);
                        break;
                    }
                } else {
                    hi = new HyperItem(null, null, Boolean.parseBoolean(p.getProperty("isHandheld")),
                            Integer.parseInt(p.getProperty("stack")));
                }
                if (hi.isHandheld()) {
                    parentName = "handheld";
                } else {
                    parentName = "generated";
                }

                HyperRegistry.register(new Identifier(MOD_ID, fileName), hi);
                jsonModelMake(MODELS_ITEM_FOLDER, fileName, parentName);
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
        InputStream is = new FileInputStream(source);
        OutputStream os = new FileOutputStream(destination);
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
        fw.write("{\"pack\":{\"pack_format\":6,\"description\":\"Resources for the Items From Text mod.\"}}");
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