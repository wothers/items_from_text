package wothers.ift.mixins;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Lifecycle;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Mixin(SimpleRegistry.class)
public abstract class RegistryMixin {

    @Unique
    private final Map<RegistryKey<?>, FoodComponent> itemsfromtext$foodComponentMap = new HashMap<>();

    @Inject(method = "add", at = @At(value = "HEAD"))
    private void interceptAddItem(RegistryKey<?> key, Object entry, Lifecycle lifecycle, CallbackInfoReturnable<RegistryEntry<?>> cir) {
        if (!this.equals(Registry.ITEM)) return;
        itemsfromtext$addFoodContainingEffects(key, (Item) entry);
    }

    @Unique
    private void itemsfromtext$addFoodContainingEffects(RegistryKey<?> key, Item item) {
        if (!item.isFood()) return;
        FoodComponent foodComponent = item.getFoodComponent();
        if (foodComponent.getStatusEffects().isEmpty()) return;
        itemsfromtext$foodComponentMap.put(key, foodComponent);
    }

    @Inject(method = "freeze", at = @At(value = "HEAD"))
    private void validateOnFreeze(CallbackInfoReturnable<Registry<?>> cir) {
        if (!this.equals(Registry.ITEM)) return;
        itemsfromtext$validateFoodEffects();
    }

    @Unique
    private void itemsfromtext$validateFoodEffects() {
        for (Map.Entry<RegistryKey<?>, FoodComponent> entry : itemsfromtext$foodComponentMap.entrySet()) {
            Iterator<Pair<StatusEffectInstance, Float>> iterator = entry.getValue().getStatusEffects().iterator();
            while (iterator.hasNext()) {
                Pair<StatusEffectInstance, Float> pair = iterator.next();
                if (pair.getFirst().getEffectType() == null) {
                    iterator.remove();
                    LogUtils.getLogger().error("Food item {} has invalid status effect(s)", entry.getKey().getValue());
                }
            }
        }
    }
}
