package wothers.hr.items;

import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class HyperTool extends HyperItem {
    public static final String PICKAXE = "pickaxe";
    public static final String AXE = "axe";
    public static final String SHOVEL = "shovel";
    public static final String HOE = "hoe";
    public static final String SWORD = "sword";

    public HyperTool(String toolType, float miningSpeed, int miningLevel, float attackSpeed, int attackDamage,
            int durability, int enchantability, Ingredient repairIngredient, boolean isFireproof) {
        super(1, true, isFireproof);
        attackSpeed -= 4;

        class CustomToolMaterial implements ToolMaterial {
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
                return repairIngredient;
            }
        }

        Item.Settings settings = new Item.Settings().group(ItemGroup.TOOLS);
        if (isFireproof) {
            settings = settings.fireproof();
        }

        switch (toolType) {
        case PICKAXE:
            item = new CustomPickaxeItem(new CustomToolMaterial(), attackDamage / 2, attackSpeed, settings);
            break;
        case AXE:
            item = new CustomAxeItem(new CustomToolMaterial(), attackDamage / 2, attackSpeed, settings);
            break;
        case SHOVEL:
            item = new ShovelItem(new CustomToolMaterial(), attackDamage / 2, attackSpeed, settings);
            break;
        case HOE:
            item = new CustomHoeItem(new CustomToolMaterial(), attackDamage / 2, attackSpeed, settings);
            break;
        case SWORD:
            settings = settings.group(ItemGroup.COMBAT);
            item = new SwordItem(new CustomToolMaterial(), attackDamage / 2, attackSpeed, settings);
            break;
        default:
            throw new RuntimeException("Invalid tool type - only pickaxe, axe, shovel, hoe or sword are permitted");
        }
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