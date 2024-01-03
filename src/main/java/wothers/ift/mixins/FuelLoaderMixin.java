package wothers.ift.mixins;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import wothers.ift.ItemRegistry;
import java.util.Map;

@Mixin(AbstractFurnaceBlockEntity.class)
public class FuelLoaderMixin {
    @ModifyVariable(method = "createFuelTimeMap", at = @At(value = "TAIL"), ordinal = 0)
    private static Map<Item, Integer> appendMap(Map<Item, Integer> map) {
        map.putAll(ItemRegistry.INSTANCE.getFuelMap());
        return map;
    }
}
