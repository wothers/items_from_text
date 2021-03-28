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

    public HyperTool(String displayName, String textureName, String toolType, float miningSpeed, int miningLevel,
            float attackSpeed, int attackDamage, int durability, int enchantability, Ingredient repairIngredient) {
        super(displayName, textureName, true, 1);

        class CustomToolMaterial implements ToolMaterial {
            public int getDurability() {
                return durability;
            }

            public float getMiningSpeedMultiplier() {
                return miningSpeed;
            }

            public float getAttackDamage() {
                if (attackDamage % 2 == 0) {
                    return (attackDamage / 2) - 1;
                } else {
                    return (attackDamage / 2);
                }
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

        switch (toolType) {
        case PICKAXE:
            item = new CustomPickaxeItem(new CustomToolMaterial(), attackDamage / 2, attackSpeed,
                    new Item.Settings().group(ItemGroup.TOOLS));
            break;
        case AXE:
            item = new CustomAxeItem(new CustomToolMaterial(), attackDamage / 2, attackSpeed,
                    new Item.Settings().group(ItemGroup.TOOLS));
            break;
        case SHOVEL:
            item = new ShovelItem(new CustomToolMaterial(), attackDamage / 2, attackSpeed,
                    new Item.Settings().group(ItemGroup.TOOLS));
            break;
        case HOE:
            item = new CustomHoeItem(new CustomToolMaterial(), attackDamage / 2, attackSpeed,
                    new Item.Settings().group(ItemGroup.TOOLS));
            break;
        case SWORD:
            item = new SwordItem(new CustomToolMaterial(), attackDamage / 2, attackSpeed,
                    new Item.Settings().group(ItemGroup.COMBAT));
            break;
        default:
            throw new InvalidToolTypeException();
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

    private class InvalidToolTypeException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        private InvalidToolTypeException() {
            super("Invalid tool type - only pickaxe, axe, shovel, hoe or sword are permitted");
        }
    }
}