package wothers.hr.items;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class HyperFood extends HyperItem {
    public HyperFood(String displayName, String textureName, boolean isHandheld, int maxStackSize, int hunger,
            float saturation) {
        super(displayName, textureName, isHandheld, maxStackSize);
        item = new Item(new Item.Settings().group(ItemGroup.FOOD).maxCount(maxStackSize)
                .food(new FoodComponent.Builder().hunger(hunger).saturationModifier(saturation).build()));
    }
}