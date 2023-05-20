package wothers.hr.items;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class HyperFood extends HyperItem {
    private final int hunger;
    private final float saturation;
    private final boolean isSnack;
    private final String effect;
    private final int effectDuration;
    private final int effectAmplifier;
    private final float effectChance;

    public HyperFood(int maxStackSize, int hunger, float saturation, boolean isSnack, boolean isFireproof) {
        this(maxStackSize, hunger, saturation, isSnack, null, 0, 0, 0f, isFireproof);
    }

    public HyperFood(int maxStackSize, int hunger, float saturation, boolean isSnack, String effect, int effectDuration, int effectAmplifier, float effectChance, boolean isFireproof) {
        super(maxStackSize, isFireproof);
        this.hunger = hunger;
        this.saturation = saturation;
        this.isSnack = isSnack;
        this.effect = effect;
        this.effectDuration = effectDuration;
        this.effectAmplifier = effectAmplifier;
        this.effectChance = effectChance;
    }

    @Override
    public Item getItem() {
        if (item == null) {
            Item.Settings settings = new Item.Settings().group(ItemGroup.FOOD).maxCount(maxStackSize);
            if (isFireproof)
                settings = settings.fireproof();
            FoodComponent.Builder builder = new FoodComponent.Builder().hunger(hunger).saturationModifier(saturation);
            if (isSnack)
                builder = builder.snack();
            if (effect != null)
                builder = builder.statusEffect(new StatusEffectInstance(Registry.STATUS_EFFECT.get(new Identifier(effect)), effectDuration, effectAmplifier), effectChance);
            item = new Item(settings.food(builder.build()));
        }
        return item;
    }
}
