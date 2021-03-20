package wothers.hr;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wothers.hr.items.HyperItem;

public class HyperRegistry {
    public static void register(Identifier id, HyperItem hi) {
        Registry.register(Registry.ITEM, id, hi.getItem());
    }
}