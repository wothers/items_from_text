package wothers.hr.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class HyperItem {
    protected Item item;
    private boolean isHandheld;

    public HyperItem(boolean isHandheld, int maxStackSize) {
        this.isHandheld = isHandheld;
        if (maxStackSize < 1 || maxStackSize > 64) {
            throw new InvalidStackSizeException();
        } else {
            item = new Item(new Item.Settings().group(ItemGroup.MISC).maxCount(maxStackSize));
        }
    }

    public Item getItem() {
        return item;
    }

    public boolean isHandheld() {
        return isHandheld;
    }

    private class InvalidStackSizeException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        private InvalidStackSizeException() {
            super("Invalid item stack size, should be between at least 1 and no more than 64");
        }
    }
}