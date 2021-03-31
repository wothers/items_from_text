# Items From Text

A mod that allows you to create and add new items to Minecraft using pure text files.

## Mod Setup

Place the jar file in your minecraft directory (usually the .minecraft folder).

## Creating Items

1. Create a new folder called "itemsfromtext" in your .minecraft folder and navigate inside,
2. For each item make a txt file and a 16x16 png image. Both files should be named your item's registry name (e.g. ender_pearl),
3. Inside the txt file complete one of the example templates below with your own item's data,

# Templates

Each item has a name field that represents the display name of the item.

Most items (apart from tools) have a stack field which represents the max stack size.

Optional for non tool items: "isHandheld" field (true or false) - is the item held like a stick or tool.

## Basic Items:

Example:

```
name=Ender Pearl
stack=16
```

## Food:

Example:

```
name=Apple
stack=64
type=food
hunger=4
saturation=2.4
```

## Tools:

Example:

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
