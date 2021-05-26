package wothers.hr.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class HyperItem {
    Item item;
    final int maxStackSize;
    final boolean isFireproof;

    public HyperItem(int maxStackSize, boolean isFireproof) {
        if (maxStackSize < 1 || maxStackSize > 64)
            throw new RuntimeException("Invalid item stack size - should be at least 1 and no more than 64");
        this.maxStackSize = maxStackSize;
        this.isFireproof = isFireproof;
    }

    public Item getItem() {
        if (item == null) {
            Item.Settings settings = new Item.Settings().group(ItemGroup.MISC).maxCount(maxStackSize);
            if (isFireproof)
                settings = settings.fireproof();
            item = new Item(settings);
        }
        return item;
    }
}
