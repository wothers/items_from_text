package wothers.ift.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ItemWrapper implements ItemProvider {
    final int maxStackSize;
    final boolean isFireproof;
    private Item item;

    public ItemWrapper(int maxStackSize, boolean isFireproof) {
        this.maxStackSize = maxStackSize;
        this.isFireproof = isFireproof;

        if (this.getClass() == ItemWrapper.class) {
            Item.Settings settings = new Item.Settings().group(ItemGroup.MISC).maxCount(maxStackSize);
            if (isFireproof) settings = settings.fireproof();
            item = new Item(settings);
        }
    }

    public Item getItem() {
        return item;
    }
}
