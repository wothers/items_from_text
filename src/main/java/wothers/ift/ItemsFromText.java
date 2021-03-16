package wothers.ift;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.AxeItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemsFromText implements ModInitializer {
    public static final String modID = "itemsfromtext";
    public static final String[] subfolders = { "resourcepacks", "Items From Text", "assets", modID, "models",
            "textures", "item", "lang" };

    public static final Path main_folder = Paths.get(subfolders[3]);
    public static final Path resourcepacks_folder = Paths.get(subfolders[0], subfolders[1]);
    public static final Path models_item_folder = Paths.get(subfolders[0], subfolders[1], subfolders[2], subfolders[3],
            subfolders[4], subfolders[6]);
    public static final Path textures_item_folder = Paths.get(subfolders[0], subfolders[1], subfolders[2],
            subfolders[3], subfolders[5], subfolders[6]);
    public static final Path lang_folder = Paths.get(subfolders[0], subfolders[1], subfolders[2], subfolders[3],
            subfolders[7]);

    public void onInitialize() {
        try {
            ArrayList<String> filepaths = getTxtFilePaths(main_folder);
            ArrayList<String> filenames = getTxtFileNames(main_folder);

            if (resourcepacks_folder.toFile().exists()) {
                deleteDirectory(resourcepacks_folder.toFile());
            }
            Files.createDirectories(models_item_folder);
            Files.createDirectories(textures_item_folder);
            Files.createDirectories(lang_folder);
            mcmetaMake(resourcepacks_folder);

            String langString = "";

            for (int index = 0; index < filepaths.size(); index++) {
                File image = new File(main_folder + File.separator + filenames.get(index) + ".png");
                if (image.exists()) {
                    copyFile(image, new File(textures_item_folder + File.separator + filenames.get(index) + ".png"));
                }

                Properties p = new Properties();
                p.load(new FileInputStream(filepaths.get(index)));

                int maxCount;
                if (p.getProperty("stack") == null || Integer.parseInt(p.getProperty("stack")) < 1
                        || Integer.parseInt(p.getProperty("stack")) > 64) {
                    maxCount = 64;
                } else {
                    maxCount = Integer.parseInt(p.getProperty("stack"));
                }

                if (p.getProperty("name") != null) {
                    langString = langString + "\"item." + modID + "." + filenames.get(index) + "\":" + "\""
                            + p.getProperty("name") + "\"";
                    if (index < filepaths.size() - 1) {
                        langString = langString + ",";
                    }
                }

                String parentname = null;
                Item i = null;

                if (p.getProperty("type") != null) {
                    ToolMaterial tm = ToolMaterials.WOOD;
                    int atkdamage = 0;
                    float atkspeed = 0;

                    if (p.getProperty("type").equals("pickaxe") || p.getProperty("type").equals("axe")
                            || p.getProperty("type").equals("shovel") || p.getProperty("type").equals("hoe")
                            || p.getProperty("type").equals("sword")) {
                        parentname = "handheld";
                        atkdamage = Integer.parseInt(p.getProperty("atkdamage"));
                        atkspeed = Float.parseFloat(p.getProperty("atkspeed"));

                        if (p.getProperty("quality") != null) {
                            switch (p.getProperty("quality")) {
                            case "gold":
                                tm = ToolMaterials.GOLD;
                                break;
                            case "stone":
                                tm = ToolMaterials.STONE;
                                break;
                            case "iron":
                                tm = ToolMaterials.IRON;
                                break;
                            case "diamond":
                                tm = ToolMaterials.DIAMOND;
                                break;
                            case "netherite":
                                tm = ToolMaterials.NETHERITE;
                                break;
                            }
                        }
                    }

                    switch (p.getProperty("type")) {
                    case "food":
                        i = new Item(new Item.Settings().group(ItemGroup.FOOD).maxCount(maxCount)
                                .food(new FoodComponent.Builder().hunger(Integer.parseInt(p.getProperty("hunger")))
                                        .saturationModifier(Float.parseFloat(p.getProperty("saturation"))).build()));
                        parentname = "generated";
                        break;
                    case "pickaxe":
                        i = new CustomPickaxeItem(tm, atkdamage, atkspeed, new Item.Settings().group(ItemGroup.TOOLS));
                        break;
                    case "axe":
                        i = new CustomAxeItem(tm, atkdamage, atkspeed, new Item.Settings().group(ItemGroup.TOOLS));
                        break;
                    case "shovel":
                        i = new ShovelItem(tm, atkdamage, atkspeed, new Item.Settings().group(ItemGroup.TOOLS));
                        break;
                    case "hoe":
                        i = new CustomHoeItem(tm, atkdamage, atkspeed, new Item.Settings().group(ItemGroup.TOOLS));
                        break;
                    case "sword":
                        i = new SwordItem(tm, atkdamage, atkspeed, new Item.Settings().group(ItemGroup.COMBAT));
                        break;
                    }
                } else {
                    i = new Item(new Item.Settings().group(ItemGroup.MISC).maxCount(maxCount));
                    parentname = "generated";
                }

                Registry.register(Registry.ITEM, new Identifier(modID, filenames.get(index)), i);
                jsonModelMake(models_item_folder, filenames.get(index), parentname);
            }

            jsonLangMake(lang_folder, langString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<String> getTxtFilePaths(Path p) {
        ArrayList<String> filepaths = new ArrayList<String>();
        try (Stream<Path> sp = Files.walk(p, 1)) {
            filepaths = (ArrayList<String>) sp.filter(Files::isRegularFile).map(pa -> pa.toString())
                    .collect(Collectors.toList());
            for (int i = 0; i < filepaths.size(); i++) {
                if (!filepaths.get(i).endsWith(".txt")) {
                    filepaths.remove(i);
                    i--;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filepaths;
    }

    private static ArrayList<String> getTxtFileNames(Path p) {
        ArrayList<String> filenames = new ArrayList<String>();
        for (int i = 0; i < getTxtFilePaths(p).size(); i++) {
            filenames.add(getTxtFilePaths(p).get(i).replace(p + File.separator, "").replace(".txt", ""));
        }
        return filenames;
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

    private void jsonModelMake(Path p, String filename, String parentname) throws IOException {
        FileWriter fw = new FileWriter(p + File.separator + filename + ".json");
        fw.write("{\"parent\":\"minecraft:item/" + parentname + "\",\"textures\":{\"layer0\":\"" + modID + ":item/"
                + filename + "\"}}");
        fw.close();
    }

    private void jsonLangMake(Path p, String s) throws IOException {
        FileWriter fw = new FileWriter(p + File.separator + "en_us.json");
        fw.write("{" + s + "}");
        fw.close();
    }

    private class CustomPickaxeItem extends PickaxeItem {
        private CustomPickaxeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
            super(material, attackDamage, attackSpeed, settings);
        }
    }

    private class CustomAxeItem extends AxeItem {
        private CustomAxeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
            super(material, attackDamage, attackSpeed, settings);
        }
    }

    private class CustomHoeItem extends HoeItem {
        private CustomHoeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
            super(material, attackDamage, attackSpeed, settings);
        }
    }
}