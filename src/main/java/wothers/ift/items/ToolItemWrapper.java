package wothers.ift.items;

import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ToolItemWrapper extends ItemWrapper {
    private final Item toolItem;

    public ToolItemWrapper(String toolType, float miningSpeed, int miningLevel, float attackSpeed, int attackDamage, int durability, int enchantability, String repairItem, boolean isFireproof) {
        super(1, isFireproof);
        attackSpeed -= 4;

        ToolMaterial customToolMaterial = new ToolMaterial() {
            public int getDurability() {
                return durability;
            }

            public float getMiningSpeedMultiplier() {
                return miningSpeed;
            }

            public float getAttackDamage() {
                return attackDamage % 2 == 0 ? (attackDamage / 2) - 1 : attackDamage / 2;
            }

            public int getMiningLevel() {
                return miningLevel;
            }

            public int getEnchantability() {
                return enchantability;
            }

            public Ingredient getRepairIngredient() {
                return repairItem != null ? Ingredient.ofItems(Registry.ITEM.get(new Identifier(repairItem))) : Ingredient.EMPTY;
            }
        };

        Item.Settings settings = new Item.Settings().group(ItemGroup.TOOLS);
        if (isFireproof) settings = settings.fireproof();
        switch (toolType) {
            case "pickaxe" -> toolItem = new CustomPickaxeItem(customToolMaterial, attackDamage / 2, attackSpeed, settings);
            case "axe" -> toolItem = new CustomAxeItem(customToolMaterial, attackDamage / 2, attackSpeed, settings);
            case "shovel" -> toolItem = new ShovelItem(customToolMaterial, attackDamage / 2, attackSpeed, settings);
            case "hoe" -> toolItem = new CustomHoeItem(customToolMaterial, attackDamage / 2, attackSpeed, settings);
            case "sword" -> {
                settings = settings.group(ItemGroup.COMBAT);
                toolItem = new SwordItem(customToolMaterial, attackDamage / 2, attackSpeed, settings);
            }
            default -> throw new RuntimeException("Invalid tool type - only pickaxe, axe, shovel, hoe or sword are permitted");
        }
    }

    @Override
    public Item getItem() {
        return toolItem;
    }

    private static class CustomPickaxeItem extends PickaxeItem {
        private CustomPickaxeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
            super(material, attackDamage, attackSpeed, settings);
        }
    }

    private static class CustomAxeItem extends AxeItem {
        private CustomAxeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
            super(material, attackDamage, attackSpeed, settings);
        }
    }

    private static class CustomHoeItem extends HoeItem {
        private CustomHoeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
            super(material, attackDamage, attackSpeed, settings);
        }
    }
}
