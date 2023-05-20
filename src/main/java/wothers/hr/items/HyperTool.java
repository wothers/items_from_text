package wothers.hr.items;

import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class HyperTool extends HyperItem {
    private final String toolType;
    private final int attackDamage;
    private final float attackSpeed;
    private final ToolMaterial customToolMaterial;

    public HyperTool(String toolType, float miningSpeed, int miningLevel, float attackSpeed, int attackDamage, int durability, int enchantability, String repairItem, boolean isFireproof) {
        super(1, isFireproof);
        this.toolType = toolType;
        this.attackDamage = attackDamage;
        attackSpeed -= 4;
        this.attackSpeed = attackSpeed;

        customToolMaterial = new ToolMaterial() {
            @Override
            public int getDurability() {
                return durability;
            }

            @Override
            public float getMiningSpeedMultiplier() {
                return miningSpeed;
            }

            @Override
            public float getAttackDamage() {
                return attackDamage % 2 == 0 ? (attackDamage / 2) - 1 : attackDamage / 2;
            }

            @Override
            public int getMiningLevel() {
                return miningLevel;
            }

            @Override
            public int getEnchantability() {
                return enchantability;
            }

            @Override
            public Ingredient getRepairIngredient() {
                return repairItem != null ? Ingredient.ofItems(Registry.ITEM.get(new Identifier(repairItem))) : Ingredient.EMPTY;
            }
        };
    }

    @Override
    public Item getItem() {
        if (item == null) {
            Item.Settings settings = new Item.Settings().group(ItemGroup.TOOLS);
            if (isFireproof) settings = settings.fireproof();
            switch (toolType) {
                case "pickaxe":
                    item = new CustomPickaxeItem(customToolMaterial, attackDamage / 2, attackSpeed, settings);
                    break;
                case "axe":
                    item = new CustomAxeItem(customToolMaterial, attackDamage / 2, attackSpeed, settings);
                    break;
                case "shovel":
                    item = new ShovelItem(customToolMaterial, attackDamage / 2, attackSpeed, settings);
                    break;
                case "hoe":
                    item = new CustomHoeItem(customToolMaterial, attackDamage / 2, attackSpeed, settings);
                    break;
                case "sword":
                    settings = settings.group(ItemGroup.COMBAT);
                    item = new SwordItem(customToolMaterial, attackDamage / 2, attackSpeed, settings);
                    break;
                default:
                    throw new RuntimeException("Invalid tool type - only pickaxe, axe, shovel, hoe or sword are permitted");
            }
        }
        return item;
    }

    private class CustomPickaxeItem extends PickaxeItem {
        private CustomPickaxeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
            super(material, attackDamage, attackSpeed, settings);
        }
    }

    private class CustomAxeItem extends AxeItem {
        private CustomAxeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
            super(material, attackDamage, attackSpeed, settings);
        }
    }

    private class CustomHoeItem extends HoeItem {
        private CustomHoeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
            super(material, attackDamage, attackSpeed, settings);
        }
    }
}
