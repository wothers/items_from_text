package wothers.hr.items;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class HyperFood extends HyperItem {
    public HyperFood(int maxStackSize, int hunger, float saturation, boolean isHandheld, boolean isFireproof) {
        super(maxStackSize, isHandheld, isFireproof);
        Item.Settings settings = new Item.Settings().group(ItemGroup.FOOD).maxCount(maxStackSize);
        if (isFireproof) {
            settings = settings.fireproof();
        }
        item = new Item(
                settings.food(new FoodComponent.Builder().hunger(hunger).saturationModifier(saturation).build()));
    }
}