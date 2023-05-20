package wothers.ift.items;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FoodItemWrapper extends ItemWrapper {
    private final Item foodItem;

    public FoodItemWrapper(int maxStackSize, int hunger, float saturation, boolean isSnack, boolean isFireproof) {
        this(maxStackSize, hunger, saturation, isSnack, null, 0, 0, 0f, isFireproof);
    }

    public FoodItemWrapper(int maxStackSize, int hunger, float saturation, boolean isSnack, String effect, int effectDuration, int effectAmplifier, float effectChance, boolean isFireproof) {
        super(maxStackSize, isFireproof);
        Item.Settings settings = new Item.Settings().group(ItemGroup.FOOD).maxCount(maxStackSize);
        if (isFireproof) settings = settings.fireproof();
        FoodComponent.Builder builder = new FoodComponent.Builder().hunger(hunger).saturationModifier(saturation);
        if (isSnack) builder = builder.snack();
        if (effect != null) builder = builder.statusEffect(new StatusEffectInstance(Registry.STATUS_EFFECT.get(new Identifier(effect)), effectDuration, effectAmplifier), effectChance);
        foodItem = new Item(settings.food(builder.build()));
    }

    @Override
    public Item getItem() {
        return foodItem;
    }
}
