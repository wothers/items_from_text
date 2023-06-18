package wothers.ift;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wothers.ift.items.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ItemsFromText implements ModInitializer {
    public static final String MOD_ID = "itemsfromtext";
    public static final File MAIN_DIR = new File(MOD_ID);
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        File[] subDirectories = {};
        if (MAIN_DIR.exists()) subDirectories = MAIN_DIR.listFiles(File::isDirectory);

        loadItems(MAIN_DIR);
        for (File dir : subDirectories) {
            loadItems(dir);
        }
    }

    private void loadItems(File dir) {
        String namespaceName = dir.getName();

        File[] txtFiles = {};
        if (dir.exists()) txtFiles = dir.listFiles((file, string) -> string.endsWith(".txt"));

        for (File file : txtFiles) {
            String itemName = file.getName().replace(".txt", "");

            try {
                ItemProperties ip = ItemPropertiesFactory.create(newProperties(file), namespaceName, itemName, LOGGER);
                ItemProvider item = parseItem(ip);
                registerItem(item, ip, namespaceName, itemName, new File(dir, itemName + ".png").getAbsoluteFile());
            } catch (IOException e) {
                LOGGER.error("I/O error while reading from file: " + file.getAbsolutePath());
            } catch (ItemLoadException e) {
                LOGGER.error(e.getMessage());
            } catch (IllegalArgumentException e) {
                LOGGER.error(ItemLoadException.getErrorMessage(namespaceName, itemName, e.getMessage()));
            }
        }
    }

    private Properties newProperties(File file) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader(file));
        return properties;
    }

    private ItemProvider parseItem(ItemProperties ip) {
        if (ip instanceof FoodItemProperties fip) {
            if (fip.effect() != null) return new FoodItemWrapper(fip.maxStackSize(), fip.hunger(), fip.saturation(), fip.isSnack(), fip.effect(), fip.effectDuration(), fip.effectAmplifier(), fip.effectChance(), fip.isFireproof());
            return new FoodItemWrapper(fip.maxStackSize(), fip.hunger(), fip.saturation(), fip.isSnack(), fip.isFireproof());
        }
        if (ip instanceof ToolItemProperties tip) return new ToolItemWrapper(tip.toolType(), tip.miningSpeed(), tip.miningLevel(), tip.attackSpeed(), tip.attackDamage(), tip.durability(), tip.enchantability(), tip.repairItem(), tip.isFireproof());
        return new ItemWrapper(ip.maxStackSize(), ip.isFireproof());
    }

    private void registerItem(ItemProvider item, ItemProperties ip, String namespaceName, String itemName, File textureFile) {
        ItemRegistry.INSTANCE.register(namespaceName, itemName, item, ip.displayName(), ip.isHandheld());

        if (ip.cookingTime() != null) ItemRegistry.INSTANCE.addFuel(item, ip.cookingTime());

        if (ip.recipe() != null) ItemRegistry.Recipe.INSTANCE.add(namespaceName, itemName, ip.recipe());

        ItemRegistry.Texture.INSTANCE.add(namespaceName, itemName, textureFile);
    }
}
