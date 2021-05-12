package wothers.hr.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class HyperItem {
    public final boolean isHandheld;
    protected Item item;

    public HyperItem(int maxStackSize, boolean isHandheld, boolean isFireproof) {
        this.isHandheld = isHandheld;
        if (maxStackSize < 1 || maxStackSize > 64)
            throw new RuntimeException("Invalid item stack size - should be at least 1 and no more than 64");
        else {
            Item.Settings settings = new Item.Settings().group(ItemGroup.MISC).maxCount(maxStackSize);
            if (isFireproof)
                settings = settings.fireproof();
            item = new Item(settings);
        }
    }

    public Item getItem() {
        return item;
    }
}
