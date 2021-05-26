package wothers.ift.mixins;

import java.util.Map;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.Item;
import wothers.hr.HyperRegistry;

@Mixin(AbstractFurnaceBlockEntity.class)
public class FuelLoaderMixin {
    @ModifyVariable(method = "createFuelTimeMap", at = @At(value = "TAIL"), ordinal = 0)
    private static Map<Item, Integer> appendMap(Map<Item, Integer> map) {
        map.putAll(HyperRegistry.INSTANCE.getFuelMap());
        return map;
    }
}
