package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.misc.Wood.WoodSet;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks.Material;
import io.github.openbagtwo.lighterend.registries.LighterEndEquipment;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper;

public class RecipeProvider extends FabricRecipeProvider {

  protected RecipeProvider(
      FabricPackOutput output,
      CompletableFuture<Provider> registriesFuture
  ) {
    super(output, registriesFuture);
  }

  @Override
  protected net.minecraft.data.recipes.RecipeProvider createRecipeProvider(
      HolderLookup.Provider registryLookup,
      RecipeOutput exporter
  ) {
    return new net.minecraft.data.recipes.RecipeProvider(registryLookup, exporter) {
      @Override
      public void buildRecipes() {
        twoByTwoPacker(
            RecipeCategory.BUILDING_BLOCKS,
            LighterEndBlocks.AURORA_CRYSTAL,
            LighterEndItems.AURORA_CRYSTAL_SHARD
        );

        twoByTwoPacker(
            RecipeCategory.BUILDING_BLOCKS, LighterEndBlocks.ENDER_BLOCK, Items.ENDER_PEARL
        );

        generateMaterialRecipes(LighterEndBlocks.VIOLECITE);

        shaped(RecipeCategory.BUILDING_BLOCKS, LighterEndBlocks.MISSING_TILE, 4)
            .pattern("VP")
            .pattern("PV")
            .define('V', LighterEndBlocks.VIOLECITE.tiles)
            .define('P', Blocks.PURPUR_BLOCK)
            .unlockedBy(
                getHasName(LighterEndBlocks.VIOLECITE.tiles),
                has(LighterEndBlocks.VIOLECITE.tiles)
            ).save(output);

        for (Material jadestone : Arrays.asList(
            LighterEndBlocks.AZURE_JADESTONE,
            LighterEndBlocks.SANDY_JADESTONE,
            LighterEndBlocks.VIRID_JADESTONE
        )) {
          generateMaterialRecipes(jadestone);
        }

        shaped(RecipeCategory.BUILDING_BLOCKS, LighterEndBlocks.DRAGON_BONE_BLOCK, 8)
            .pattern("BBB")
            .pattern("BDB")
            .pattern("BBB")
            .define('B', Blocks.BONE_BLOCK)
            .define('D', Items.DRAGON_BREATH)
            .unlockedBy(
                getHasName(Items.DRAGON_BREATH),
                has(Items.DRAGON_BREATH)
            ).save(output);
        slab(
            RecipeCategory.BUILDING_BLOCKS,
            LighterEndBlocks.DRAGON_BONE_SLAB,
            LighterEndBlocks.DRAGON_BONE_BLOCK
        );
        stonecutterResultFromBase(
            RecipeCategory.BUILDING_BLOCKS,
            LighterEndBlocks.DRAGON_BONE_SLAB,
            LighterEndBlocks.DRAGON_BONE_BLOCK,
            2
        );
        offerStairsRecipe(LighterEndBlocks.DRAGON_BONE_STAIRS, LighterEndBlocks.DRAGON_BONE_BLOCK);
        stonecutterResultFromBase(
            RecipeCategory.BUILDING_BLOCKS,
            LighterEndBlocks.DRAGON_BONE_STAIRS,
            LighterEndBlocks.DRAGON_BONE_BLOCK
        );

        shapeless(RecipeCategory.MISC, LighterEndBlocks.END_MOSS, 2)
            .requires(Blocks.END_STONE)
            .requires(Blocks.PALE_MOSS_BLOCK)
            .unlockedBy(
                getHasName(Blocks.END_STONE),
                has(Blocks.END_STONE)
            ).save(output);

        shapeless(RecipeCategory.MISC, Items.DYE.cyan())
            .requires(LighterEndBlocks.CREEPING_MOSS)
            .unlockedBy(
                getHasName(LighterEndBlocks.CREEPING_MOSS),
                has(LighterEndBlocks.CREEPING_MOSS)
            ).save(
                output,
                ResourceKey.create(
                    Registries.RECIPE,
                    LighterEnd.of("cyan_dye_from_creeping_moss")
                )
            );

        shapeless(RecipeCategory.MISC, Items.DYE.orange())
            .requires(LighterEndBlocks.UMBRELLA_FERN)
            .unlockedBy(
                getHasName(LighterEndBlocks.UMBRELLA_FERN),
                has(LighterEndBlocks.UMBRELLA_FERN)
            ).save(
                output,
                ResourceKey.create(
                    Registries.RECIPE,
                    LighterEnd.of("orange_dye_from_umbrella_fern")
                )
            );

        generateCookingRecipes(LighterEndItems.LUMECORN_EAR, LighterEndItems.POPPED_LUMECORN);

        generateMaterialRecipes(LighterEndBlocks.UMBRALITH);

        shapeless(RecipeCategory.MISC, Items.DYE.magenta())
            .requires(LighterEndBlocks.TENANEA_FLOWER)
            .unlockedBy(
                getHasName(LighterEndBlocks.TENANEA_FLOWER),
                has(LighterEndBlocks.TENANEA_FLOWER)
            ).save(
                output,
                ResourceKey.create(
                    Registries.RECIPE,
                    LighterEnd.of("magenta_dye_from_tenanea_flower")
                )
            );

        generateWoodRecipes(LighterEndBlocks.TENANEA);

        shapeless(RecipeCategory.MISC, Items.STRING, 2)
            .requires(LighterEndItems.SILK)
            .unlockedBy(
                getHasName(LighterEndItems.SILK),
                has(LighterEndItems.SILK)
            ).save(
                output,
                ResourceKey.create(
                    Registries.RECIPE,
                    LighterEnd.of("string_from_silk")
                )
            );

        shaped(RecipeCategory.MISC, LighterEndItems.SILK_MATRIX)
            .pattern("###")
            .pattern("###")
            .pattern("###")
            .define('#', LighterEndItems.SILK)
            .unlockedBy(
                getHasName(LighterEndItems.SILK),
                has(LighterEndItems.SILK)
            ).save(output);

        shapeless(RecipeCategory.MISC, LighterEndItems.SILK, 9)
            .requires(LighterEndItems.SILK_MATRIX)
            .unlockedBy(
                getHasName(LighterEndItems.SILK_MATRIX),
                has(LighterEndItems.SILK_MATRIX)
            ).save(output);

        shaped(RecipeCategory.DECORATIONS, LighterEndBlocks.SILK_MOTH_NEST)
            .pattern(" P ")
            .pattern("PMP")
            .pattern("PPP")
            .define('M', LighterEndItems.SILK_MATRIX)
            .define('P', LighterEndBlocks.TENANEA.planks)
            .unlockedBy(
                getHasName(LighterEndItems.SILK_MATRIX),
                has(LighterEndItems.SILK_MATRIX)
            ).save(output);

        shaped(RecipeCategory.COMBAT, LighterEndEquipment.SILK_ELYTRA)
            .pattern("P P")
            .pattern("MMM")
            .pattern("MMM")
            .define('M', LighterEndItems.SILK_MATRIX)
            .define('P', Items.PHANTOM_MEMBRANE)
            .unlockedBy(
                getHasName(LighterEndItems.SILK_MATRIX),
                has(LighterEndItems.SILK_MATRIX)
            ).save(output);

        generateWoodRecipes(LighterEndBlocks.UMBRELLA);
        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(LighterEndBlocks.UMBRELLA_MEMBRANE),
                RecipeCategory.MISC,
                CookingBookCategory.MISC,
                Items.SLIME_BALL,
                0.1F,
                200
            ).unlockedBy(getHasName(LighterEndBlocks.UMBRELLA_MEMBRANE),
                this.has(LighterEndBlocks.UMBRELLA_MEMBRANE))
            .save(
                output,
                ResourceKey.create(
                    Registries.RECIPE,
                    LighterEnd.of("slime_balls_from_smelting_membranes")
                )
            );

        generateSmokingSmeltingRecipes(
            LighterEndItems.RAW_END_FISH,
            Items.GLOW_INK_SAC,
            RecipeCategory.MISC,
            CookingBookCategory.MISC
        );

        generateSmokingSmeltingRecipes(
            LighterEndBlocks.CHARNIA_CYAN,
            Items.DYE.cyan(),
            RecipeCategory.MISC,
            CookingBookCategory.MISC
        );
        generateSmokingSmeltingRecipes(
            LighterEndBlocks.CHARNIA_GREEN,
            Items.DYE.green(),
            RecipeCategory.MISC,
            CookingBookCategory.MISC
        );
        generateSmokingSmeltingRecipes(
            LighterEndBlocks.CHARNIA_LIGHT_BLUE,
            Items.DYE.lightBlue(),
            RecipeCategory.MISC,
            CookingBookCategory.MISC
        );
        generateSmokingSmeltingRecipes(
            LighterEndBlocks.CHARNIA_ORANGE,
            Items.DYE.orange(),
            RecipeCategory.MISC,
            CookingBookCategory.MISC
        );
        generateSmokingSmeltingRecipes(
            LighterEndBlocks.CHARNIA_PURPLE,
            Items.DYE.purple(),
            RecipeCategory.MISC,
            CookingBookCategory.MISC
        );
        generateSmokingSmeltingRecipes(
            LighterEndBlocks.CHARNIA_RED,
            Items.DYE.red(),
            RecipeCategory.MISC,
            CookingBookCategory.MISC
        );

        shaped(RecipeCategory.COMBAT, Items.SPECTRAL_ARROW, 4)
            .pattern("X")
            .pattern("#")
            .pattern("Y")
            .define('X', LighterEndItems.GLOW_BARB)
            .define('#', Items.STICK)
            .define('Y', LighterEndTags.FLETCHINGS
            ).unlockedBy(
                getHasName(LighterEndItems.GLOW_BARB),
                has(LighterEndItems.GLOW_BARB)
            ).save(
                output,
                ResourceKey.create(
                    Registries.RECIPE,
                    LighterEnd.of("spectral_arrow")
                )
            );

        generateSmokingSmeltingRecipes(
            LighterEndItems.END_LILY_LEAF,
            LighterEndItems.DRIED_END_LILY_LEAF,
            RecipeCategory.MISC,
            CookingBookCategory.MISC
        );

        shaped(RecipeCategory.MISC, Items.PAPER, 3)
            .pattern("###")
            .define('#', LighterEndItems.DRIED_END_LILY_LEAF)
            .unlockedBy(
                getHasName(LighterEndItems.DRIED_END_LILY_LEAF),
                has(LighterEndItems.DRIED_END_LILY_LEAF)
            ).save(
                output,
                ResourceKey.create(
                    Registries.RECIPE,
                    LighterEnd.of("paper_from_dried_leaves")
                )
            );

        generateWoodRecipes(LighterEndBlocks.LOTUS, 4);
        threeByThreePacker(
            RecipeCategory.BUILDING_BLOCKS,
            LighterEndBlocks.LOTUS.log,
            LighterEndBlocks.END_LOTUS_STEM
        );

        generateWoodRecipes(LighterEndBlocks.GLOWSHROOM);

        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(LighterEndItems.END_CREAM),
                RecipeCategory.BREWING,
                CookingBookCategory.MISC,
                LighterEndItems.END_POWDER,
                0.1F,
                200
            ).unlockedBy(
                getHasName(LighterEndItems.END_CREAM),
                has(LighterEndItems.END_CREAM))
            .save(
                output,
                ResourceKey.create(
                    Registries.RECIPE,
                    LighterEnd.of("end_powder_from_smelting_end_cream")
                )
            );

        shaped(RecipeCategory.TOOLS, Items.SHEARS)
            .pattern(" #")
            .pattern("# ")
            .define('#', LighterEndItems.CRAB_CLAW)
            .unlockedBy(
                getHasName(LighterEndItems.CRAB_CLAW),
                has(LighterEndItems.CRAB_CLAW)
            ).save(
                output,
                ResourceKey.create(
                    Registries.RECIPE,
                    LighterEnd.of("shears_from_claws")
                )
            );
        generateCookingRecipes(LighterEndItems.CRAB_MEAT, LighterEndItems.CRAB_CAKE);

        shaped(RecipeCategory.DECORATIONS, LighterEndBlocks.END_FURNACE)
            .define('#', Blocks.END_STONE)
            .pattern("###")
            .pattern("# #")
            .pattern("###")
            .unlockedBy(getHasName(Blocks.END_STONE), this.has(Blocks.END_STONE))
            .save(output);
        shapeless(RecipeCategory.TRANSPORTATION, Items.FURNACE_MINECART)
            .requires(LighterEndBlocks.END_FURNACE)
            .requires(Items.MINECART)
            .unlockedBy(
                getHasName(LighterEndBlocks.END_FURNACE),
                this.has(LighterEndBlocks.END_FURNACE)
            ).save(
                output,
                ResourceKey.create(
                    Registries.RECIPE,
                    LighterEnd.of("furnace_minecart_from_end_stone_furnace")
                )
            );
        shaped(RecipeCategory.DECORATIONS, LighterEndBlocks.END_SMOKER)
            .define('#', ItemTags.LOGS)
            .define('X', LighterEndBlocks.END_FURNACE)
            .pattern(" # ")
            .pattern("#X#")
            .pattern(" # ")
            .unlockedBy(
                getHasName(LighterEndBlocks.END_FURNACE),
                this.has(LighterEndBlocks.END_FURNACE)
            ).save(output);

        shaped(RecipeCategory.REDSTONE, LighterEndBlocks.END_LEVER)
            .define('#', Blocks.END_STONE)
            .define('X', Items.STICK)
            .pattern("X")
            .pattern("#")
            .unlockedBy(getHasName(Blocks.END_STONE), this.has(Blocks.END_STONE))
            .save(output);

        shaped(RecipeCategory.DECORATIONS, LighterEndBlocks.GOLD_CHANDELIER)
            .define('r', LighterEndItems.LUMECORN_EAR)
            .define('n', Items.GOLD_NUGGET)
            .define('i', Items.GOLD_INGOT)
            .pattern("r r")
            .pattern("n n")
            .pattern(" i ")
            .unlockedBy(
                getHasName(LighterEndItems.LUMECORN_EAR),
                this.has(LighterEndItems.LUMECORN_EAR)
            ).save(output);

        shaped(RecipeCategory.DECORATIONS, LighterEndBlocks.IRON_CHANDELIER)
            .define('r', LighterEndItems.LUMECORN_EAR)
            .define('n', Items.IRON_NUGGET)
            .define('i', Items.IRON_INGOT)
            .pattern("r r")
            .pattern("n n")
            .pattern(" i ")
            .unlockedBy(
                getHasName(LighterEndItems.LUMECORN_EAR),
                this.has(LighterEndItems.LUMECORN_EAR)
            ).save(output);

        shaped(
            RecipeCategory.DECORATIONS,
            LighterEndBlocks.COPPER_CHANDELIERS.weathering().unaffected()
        ).define('r', LighterEndItems.LUMECORN_EAR)
            .define('n', Items.COPPER_NUGGET)
            .define('i', Items.COPPER_INGOT)
            .pattern("r r")
            .pattern("n n")
            .pattern(" i ")
            .unlockedBy(
                getHasName(LighterEndItems.LUMECORN_EAR),
                this.has(LighterEndItems.LUMECORN_EAR)
            ).save(output);
        for (var state : List.of(
            WeatheringCopper.WeatherState.UNAFFECTED,
            WeatheringCopper.WeatherState.EXPOSED,
            WeatheringCopper.WeatherState.WEATHERED,
            WeatheringCopper.WeatherState.OXIDIZED)
        ) {
          var waxed = LighterEndBlocks.COPPER_CHANDELIERS.waxed().pick(state);
          var unwaxed = LighterEndBlocks.COPPER_CHANDELIERS.weathering().pick(state);
          shapeless(RecipeCategory.DECORATIONS, waxed)
              .requires(unwaxed)
              .requires(Items.HONEYCOMB)
              .unlockedBy(getHasName(unwaxed), this.has(unwaxed))
              .save(exporter);
        }

        SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(LighterEndBlocks.FERROUS_ICE),
            RecipeCategory.MISC,
            CookingBookCategory.MISC,
            Items.IRON_NUGGET,
            0.1F,
            200
        ).unlockedBy(
            getHasName(LighterEndBlocks.FERROUS_ICE),
            has(LighterEndBlocks.FERROUS_ICE)
        ).save(
            output,
            ResourceKey.create(
                Registries.RECIPE,
                LighterEnd.of("smelting_iron_from_ice")
            )
        );

        SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(LighterEndBlocks.EMERALD_ICE),
            RecipeCategory.MISC,
            CookingBookCategory.MISC,
            Items.COPPER_NUGGET,
            0.1F,
            200
        ).unlockedBy(
            getHasName(LighterEndBlocks.EMERALD_ICE),
            has(LighterEndBlocks.EMERALD_ICE)
        ).save(
            output,
            ResourceKey.create(
                Registries.RECIPE,
                LighterEnd.of("smelting_copper_from_ice")
            )
        );

        oreSmelting(
            List.of(LighterEndBlocks.END_STONE_REDSTONE_ORE,
                LighterEndBlocks.UMBRALITH_REDSTONE_ORE),
            RecipeCategory.REDSTONE,
            CookingBookCategory.MISC,
            Items.REDSTONE,
            0.7F,
            200,
            "end_redstone"
        );
        oreBlasting(
            List.of(LighterEndBlocks.END_STONE_REDSTONE_ORE,
                LighterEndBlocks.UMBRALITH_REDSTONE_ORE),
            RecipeCategory.REDSTONE,
            CookingBookCategory.MISC,
            Items.REDSTONE,
            0.7F,
            100,
            "end_redstone"
        );

        shaped(RecipeCategory.REDSTONE, Blocks.DROPPER)
            .define('R', Items.REDSTONE)
            .define('#', Blocks.END_STONE)
            .pattern("###")
            .pattern("# #")
            .pattern("#R#")
            .unlockedBy(getHasName(Blocks.END_STONE), this.has(Blocks.END_STONE))
            .save(this.output,
                ResourceKey.create(
                    Registries.RECIPE,
                    LighterEnd.of("dropper_using_end_stone")
                )
            );
        shaped(RecipeCategory.REDSTONE, Blocks.DISPENSER)
            .define('R', Items.REDSTONE)
            .define('#', Blocks.END_STONE)
            .define('X', Items.BOW)
            .pattern("###")
            .pattern("#X#")
            .pattern("#R#")
            .unlockedBy(getHasName(Blocks.END_STONE), this.has(Blocks.END_STONE))
            .save(this.output,
                ResourceKey.create(
                    Registries.RECIPE,
                    LighterEnd.of("dispenser_using_end_stone")
                )
            );
        shaped(RecipeCategory.REDSTONE, Blocks.OBSERVER)
            .define('Q', Items.QUARTZ)
            .define('R', Items.REDSTONE)
            .define('#', Blocks.END_STONE)
            .pattern("###")
            .pattern("RRQ")
            .pattern("###")
            .unlockedBy(getHasName(Blocks.END_STONE), this.has(Blocks.END_STONE))
            .save(this.output,
                ResourceKey.create(
                    Registries.RECIPE,
                    LighterEnd.of("observer_using_end_stone")
                )
            );
        shaped(RecipeCategory.REDSTONE, Blocks.PISTON)
            .define('R', Items.REDSTONE)
            .define('#', Blocks.END_STONE)
            .define('T', ItemTags.PLANKS)
            .define('X', Items.IRON_INGOT)
            .pattern("TTT")
            .pattern("#X#")
            .pattern("#R#")
            .unlockedBy(getHasName(Blocks.END_STONE), this.has(Blocks.END_STONE))
            .save(this.output,
                ResourceKey.create(
                    Registries.RECIPE,
                    LighterEnd.of("piston_using_end_stone")
                )
            );

        generateMaterialRecipes(LighterEndBlocks.BORNITE);

        shaped(RecipeCategory.DECORATIONS, LighterEndItems.MATCHSTICK, 4)
            .define('#', Items.STICK)
            .define('X', LighterEndItems.CRYSTALLINE_SULPHUR)
            .pattern("X")
            .pattern("#")
            .unlockedBy(
                getHasName(LighterEndItems.CRYSTALLINE_SULPHUR),
                this.has(LighterEndItems.CRYSTALLINE_SULPHUR)
            ).save(this.output);

        shapeless(RecipeCategory.MISC, Items.GUNPOWDER, 3)
            .requires(Items.BONE_MEAL)
            .requires(Ingredient.of(Items.COAL, Items.CHARCOAL))
            .requires(LighterEndItems.CRYSTALLINE_SULPHUR)
            .unlockedBy(
                getHasName(LighterEndItems.CRYSTALLINE_SULPHUR),
                this.has(LighterEndItems.CRYSTALLINE_SULPHUR)
            ).save(
                this.output,
                ResourceKey.create(
                    Registries.RECIPE,
                    LighterEnd.of("gunpowder_from_sulphur")
                )
            );

        shapeless(RecipeCategory.MISC, Items.SUGAR, 3)
            .requires(LighterEndItems.UMBRELLA_JUICE)
            .group("sugar")
            .unlockedBy(
                getHasName(LighterEndItems.UMBRELLA_JUICE),
                this.has(LighterEndItems.UMBRELLA_JUICE)
            ).save(this.output,
                getConversionRecipeName(Items.SUGAR, LighterEndItems.UMBRELLA_JUICE));

        generateCookingRecipes(LighterEndItems.SHADOW_BERRY, LighterEndItems.SHADOW_BERRY_COOKED);
        shapeless(RecipeCategory.FOOD, LighterEndItems.SHADOW_BERRY_JAM, 3)
            .requires(LighterEndItems.SHADOW_BERRY_COOKED, 3)
            .requires(Items.SUGAR, 3)
            .requires(Items.GLASS_BOTTLE, 3)
            .unlockedBy(
                getHasName(LighterEndItems.SHADOW_BERRY_COOKED),
                this.has(LighterEndItems.SHADOW_BERRY_COOKED)
            ).save(this.output);

        shapeless(RecipeCategory.MISC, Items.DYE.black())
            .requires(LighterEndBlocks.MURKWEED)
            .unlockedBy(
                getHasName(LighterEndBlocks.MURKWEED),
                has(LighterEndBlocks.MURKWEED)
            ).save(
                output,
                ResourceKey.create(
                    Registries.RECIPE,
                    LighterEnd.of("black_dye_from_murkweed")
                )
            );

        generateWoodRecipes(LighterEndBlocks.DRAGON);

        dyedItem(LighterEndEquipment.SILK_ELYTRA, "dyed_armor");
      }

      public void generateMaterialRecipes(Material material) {

        slab(
            RecipeCategory.BUILDING_BLOCKS, material.baseSlab, material.baseBlock
        );
        stonecutterResultFromBase(
            RecipeCategory.BUILDING_BLOCKS, material.baseSlab, material.baseBlock, 2
        );
        offerStairsRecipe(material.baseStairs, material.baseBlock);
        stonecutterResultFromBase(
            RecipeCategory.BUILDING_BLOCKS, material.baseStairs, material.baseBlock
        );
        wall(RecipeCategory.BUILDING_BLOCKS, material.baseWall, material.baseBlock);
        stonecutterResultFromBase(
            RecipeCategory.BUILDING_BLOCKS, material.baseWall, material.baseBlock
        );
        shaped(RecipeCategory.BUILDING_BLOCKS, material.pillar, 1)
            .pattern("s")
            .pattern("s")
            .define('s', material.baseSlab)
            .unlockedBy(
                getHasName(material.baseSlab),
                has(material.baseSlab)
            ).save(output);
        stonecutterResultFromBase(
            RecipeCategory.BUILDING_BLOCKS, material.pillar, material.baseBlock
        );

        polished(
            RecipeCategory.BUILDING_BLOCKS, material.polished, material.baseBlock
        );
        stonecutterResultFromBase(
            RecipeCategory.BUILDING_BLOCKS, material.polished, material.baseBlock
        );
        slab(
            RecipeCategory.BUILDING_BLOCKS, material.polishedSlab, material.polished
        );
        offerStairsRecipe(material.polishedStairs, material.polished);
        wall(RecipeCategory.BUILDING_BLOCKS, material.polishedWall, material.polished);
        offerButtonRecipe(material.button, material.polished);
        pressurePlate(material.pressurePlate, material.polished);
        for (ItemLike input : Arrays.asList(material.polished, material.baseBlock)) {
          stonecutterResultFromBase(
              RecipeCategory.BUILDING_BLOCKS, material.polishedSlab, input, 2
          );
          stonecutterResultFromBase(
              RecipeCategory.BUILDING_BLOCKS, material.polishedStairs, input
          );
          stonecutterResultFromBase(
              RecipeCategory.BUILDING_BLOCKS, material.polishedWall, input
          );
        }

        polished(
            RecipeCategory.BUILDING_BLOCKS, material.bricks, material.polished
        );
        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, material.bricks,
            material.polished);
        stonecutterResultFromBase(
            RecipeCategory.BUILDING_BLOCKS, material.bricks, material.baseBlock
        );
        slab(RecipeCategory.BUILDING_BLOCKS, material.brickSlab, material.bricks);
        offerStairsRecipe(material.brickStairs, material.bricks);
        wall(RecipeCategory.BUILDING_BLOCKS, material.brickWall, material.bricks);
        for (ItemLike input : Arrays.asList(
            material.bricks, material.polished, material.baseBlock
        )) {
          stonecutterResultFromBase(
              RecipeCategory.BUILDING_BLOCKS, material.brickSlab, input, 2
          );
          stonecutterResultFromBase(
              RecipeCategory.BUILDING_BLOCKS, material.brickStairs, input
          );
          stonecutterResultFromBase(
              RecipeCategory.BUILDING_BLOCKS, material.brickWall, input
          );
        }

        polished(RecipeCategory.BUILDING_BLOCKS, material.tiles, material.bricks);
        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, material.tiles,
            material.polished);
        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, material.tiles,
            material.baseBlock);
        slab(
            RecipeCategory.BUILDING_BLOCKS, material.tileSlab, material.tiles
        );
        offerStairsRecipe(material.tileStairs, material.tiles);
        wall(RecipeCategory.BUILDING_BLOCKS, material.tileWall, material.tiles);
        for (ItemLike input : Arrays.asList(
            material.tiles, material.bricks, material.polished, material.baseBlock
        )) {
          stonecutterResultFromBase(
              RecipeCategory.BUILDING_BLOCKS, material.tileSlab, input, 2
          );
          stonecutterResultFromBase(
              RecipeCategory.BUILDING_BLOCKS, material.tileStairs, input
          );
          stonecutterResultFromBase(
              RecipeCategory.BUILDING_BLOCKS, material.tileWall, input
          );
        }

        shaped(RecipeCategory.DECORATIONS, material.pedestal)
            .pattern("s")
            .pattern("#")
            .pattern("s")
            .define('s', material.polishedSlab)
            .define('#', material.pillar)
            .unlockedBy(getHasName(material.pillar), has(material.pillar))
            .save(output);
      }

      public void generateWoodRecipes(WoodSet wood) {
        generateWoodRecipes(wood, 4);
      }

      public void generateWoodRecipes(WoodSet wood, int planks_per_log) {
        shaped(RecipeCategory.BUILDING_BLOCKS, wood.wood, 3).pattern("ll").pattern("ll")
            .define('l', wood.log).unlockedBy(
                getHasName(wood.log),
                has(wood.log)
            ).save(output);
        shaped(RecipeCategory.BUILDING_BLOCKS, wood.strippedWood, 3).pattern("ll")
            .pattern("ll").define('l', wood.log).unlockedBy(
                getHasName(wood.strippedLog),
                has(wood.strippedLog)
            ).save(output);
        shapeless(RecipeCategory.BUILDING_BLOCKS, wood.planks, planks_per_log)
            .requires(LighterEndTags.LOG_TAGS.get(wood.baseName))
            .unlockedBy(
                getHasName(wood.log),
                this.has(LighterEndTags.LOG_TAGS.get(wood.baseName))
            ).save(output);
        slab(RecipeCategory.BUILDING_BLOCKS, wood.slab, wood.planks);
        offerStairsRecipe(wood.stairs, wood.planks);
        doorBuilder(wood.door, Ingredient.of(wood.planks)).unlockedBy(getHasName(wood.planks),
            has(wood.planks)).save(output);
        trapdoorBuilder(wood.trapdoor, Ingredient.of(wood.planks)).unlockedBy(
            getHasName(wood.planks),
            has(wood.planks)).save(output);
        fenceBuilder(wood.fence, Ingredient.of(wood.planks)).unlockedBy(
            getHasName(wood.planks),
            has(wood.planks)).save(output);
        fenceGateBuilder(wood.gate, Ingredient.of(wood.planks)).unlockedBy(
            getHasName(wood.planks),
            has(wood.planks)).save(output);
        offerButtonRecipe(wood.button, wood.planks);
        pressurePlate(wood.pressurePlate, wood.planks);
        shaped(RecipeCategory.DECORATIONS, wood.ladder, 2)
            .define('#', wood.slab)
            .pattern("#")
            .pattern("#")
            .pattern("#")
            .unlockedBy(getHasName(wood.planks), this.has(wood.planks))
            .save(output);
        signBuilder(wood.sign, Ingredient.of(wood.planks)).unlockedBy(
            getHasName(wood.planks),
            has(wood.planks)).save(output);
        shaped(RecipeCategory.DECORATIONS, wood.hangingSign, 6)
            .group("hanging_sign")
            .define('#', LighterEndTags.STRIPPED_LOG_TAGS.get(wood.baseName))
            .define('X', Items.IRON_CHAIN)
            .pattern("X X")
            .pattern("###")
            .pattern("###")
            .unlockedBy(
                getHasName(wood.strippedLog),
                this.has(LighterEndTags.STRIPPED_LOG_TAGS.get(wood.baseName))
            ).save(this.output);

        shaped(RecipeCategory.DECORATIONS, wood.shelf, 6)
            .define('#', LighterEndTags.STRIPPED_LOG_TAGS.get(wood.baseName))
            .pattern("###")
            .pattern("   ")
            .pattern("###")
            .group("shelf")
            .unlockedBy(
                getHasName(wood.strippedLog),
                this.has(LighterEndTags.STRIPPED_LOG_TAGS.get(wood.baseName))
            ).save(this.output);
      }

      // seems odd these aren't already implemented
      public void offerStairsRecipe(ItemLike product, ItemLike input) {
        stairBuilder(
            product, Ingredient.of(input)
        ).unlockedBy(getHasName(input), this.has(input)).save(this.output);
      }

      public void offerButtonRecipe(ItemLike product, ItemLike input) {
        buttonBuilder(
            product, Ingredient.of(input)
        ).unlockedBy(getHasName(input), this.has(input)).save(this.output);
      }

      public void generateCookingRecipes(ItemLike input, ItemLike product) {
        generateSmokingSmeltingRecipes(input, product, RecipeCategory.FOOD,
            CookingBookCategory.FOOD);

        SimpleCookingRecipeBuilder.campfireCooking(
            Ingredient.of(input),
            RecipeCategory.FOOD,
            product,
            0.35F,
            600
        ).unlockedBy(getHasName(input), this.has(input)
        ).save(
            this.output,
            ResourceKey.create(
                Registries.RECIPE,
                LighterEnd.of(
                    BuiltInRegistries.ITEM.getKey(product.asItem()).getPath()
                        + "_campfire_from_"
                        + BuiltInRegistries.ITEM.getKey(input.asItem()).getPath())
            )
        );
      }

      public void generateSmokingSmeltingRecipes(
          ItemLike input,
          ItemLike product,
          RecipeCategory recipeCategory,
          CookingBookCategory cookingCategory
      ) {
        String output_key = BuiltInRegistries.ITEM.getKey(product.asItem()).getPath();
        SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(input),
            recipeCategory,
            cookingCategory,
            product,
            0.35F,
            200
        ).unlockedBy(getHasName(input), this.has(input)
        ).save(
            this.output,
            ResourceKey.create(Registries.RECIPE,
                LighterEnd.of(
                    output_key
                        + "_smelting_"
                        + BuiltInRegistries.ITEM.getKey(input.asItem()).getPath()
                )
            )
        );
        SimpleCookingRecipeBuilder.smoking(
            Ingredient.of(input),
            recipeCategory,
            product,
            0.35F,
            100
        ).unlockedBy(getHasName(input),
            has(input)
        ).save(
            this.output,
            ResourceKey.create(Registries.RECIPE,
                LighterEnd.of(
                    output_key
                        + "_smoking_"
                        + BuiltInRegistries.ITEM.getKey(input.asItem()).getPath()
                )
            )
        );
      }
    };
  }

  @Override
  public String getName() {
    return "LighterEndRecipeProvider";
  }
}
