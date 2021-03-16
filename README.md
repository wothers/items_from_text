# Items From Text
A mod that allows you to create and add new items to Minecraft using pure text files.

## Mod Setup
Place the jar file in your minecraft directory (usually the .minecraft folder).

## Creating Items
1. Create a new folder called "itemsfromtext" in your .minecraft folder and navigate inside,
2. For each item make a txt file and a 16x16 png image. Both files should be named your item's registry name (e.g. ender_pearl),
3. Inside the txt file complete one of the templates below with your item's data,
4. After loading the game make sure to select the generated resource pack to see the items properly.

# Templates

## Basic Items:
```
name=
stack=
```
"name" is the display name of the item (e.g. Ender Pearl)

"stack" is the max stack size (e.g. 16)

## Food:
```
name=
stack=
type=food
hunger=
saturation=
```
"hunger" is the amount of hunger points refilled when eating (e.g. a value of 4 would be 2 whole hunger bar icons as each point is 1/2 a hunger bar icon)

"saturation" is the amount of saturation replenished (rotten flesh - 0, golden carrot - 1.2)

## Tools:
```
name=
type=
quality=
atkdamage=
atkspeed=
```
"type" is the type of tool (supported are pickaxe, axe, shovel, hoe, sword)

"quality" is the quality preset of the item (can be wood, gold, stone, iron, diamond, or netherite)

"atkdamage" is the damage that the tool deals to entities

"atkspeed" is the swing speed cooldown of the tool (e.g. pickaxes have a value of -2.8)