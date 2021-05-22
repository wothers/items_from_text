package wothers.hr.items;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class HyperFood extends HyperItem {
    private final int hunger;
    private final float saturation;
    private final boolean isSnack;

    public HyperFood(int maxStackSize, int hunger, float saturation, boolean isSnack, boolean isHandheld, boolean isFireproof) {
        super(maxStackSize, isHandheld, isFireproof);
        this.hunger = hunger;
        this.saturation = saturation;
        this.isSnack = isSnack;
    }

    @Override
    public Item getItem() {
        Item.Settings settings = new Item.Settings().group(ItemGroup.FOOD).maxCount(maxStackSize);
        if (isFireproof)
            settings = settings.fireproof();
        FoodComponent.Builder builder = new FoodComponent.Builder().hunger(hunger).saturationModifier(saturation);
        if (isSnack)
            builder = builder.snack();
        return new Item(settings.food(builder.build()));
    }
}
