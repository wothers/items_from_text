# Items From Text

A mod that allows you to create and add new items to Minecraft using pure text files.

## Mod Setup

Place the jar file in your minecraft directory (usually the .minecraft folder).

## Creating Items

1. Create a new folder called "itemsfromtext" in your .minecraft folder and navigate inside
2. For each item make a txt file and a png image. Both files should be named your item's registry name (e.g. ender_pearl)
3. Inside the txt file complete one of the example templates below with your own item's data
4. For custom namespaces, make a new folder within the main folder and put items in that folder

## Crafting Recipes

Crafting recipes can either be shaped or shapeless.

To give your item a custom crafting recipe, add a recipe property.

Recipes must be defined like this:

recipe=shaped,TOP ROW,MIDDLE ROW,BOTTOM ROW,QUANTITY,KEYS
or
recipe=shapeless,QUANTITY,ITEMS

### Examples:

Recipe for a Furnace

```
recipe=shaped,XXX,X X,XXX,1,X-minecraft:cobblestone
```

Recipe for Flint and Steel

```
recipe=shapeless,1,minecraft:flint,minecraft:iron_ingot
```

# Templates

Each item has a name field that represents the display name of the item.

Most items (apart from tools) have a stack field which represents the max stack size.

Optional for all items: "isFireproof" field (true or false) - is the item immune to burning in fire or lava.

Optional for all items: "cookingTime" field (integer value) - marks the item as a fuel that cooks for the amount of ticks specified.

Optional for non tool items: "isHandheld" field (true or false) - is the item held like a stick or tool.

Optional for food items: "isSnack" field (true or false) - is the food consumed quickly.

Optional for tool items: "repairItem" field - the item that can repair the tool with an Anvil.

## Basic Items:

```
name=Ender Pearl
stack=16
```

## Food:

```
name=Apple
stack=64
type=food
hunger=4
saturation=2.4
```

## Tools:

```
name=Ruby Pickaxe
type=tool
toolType=pickaxe
attackDamage=9
attackSpeed=1.2
miningSpeed=10
miningLevel=4
durability=5000
enchantability=20
```

"toolType" - the type of tool (supported are pickaxe, axe, shovel, hoe, sword)
