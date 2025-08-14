package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks.Material;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks.Wood;
import io.github.openbagtwo.lighterend.registries.LighterEndEquipment;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.ItemTags;

public class RecipeProvider extends FabricRecipeProvider {

  protected RecipeProvider(FabricDataOutput output,
      CompletableFuture<WrapperLookup> registriesFuture) {
    super(output, registriesFuture);
  }

  @Override
  protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup,
      RecipeExporter exporter) {
    return new RecipeGenerator(registryLookup, exporter) {
      @Override
      public void generate() {
        offer2x2CompactingRecipe(
            RecipeCategory.BUILDING_BLOCKS,
            LighterEndBlocks.AURORA_CRYSTAL,
            LighterEndItems.AURORA_CRYSTAL_SHARD
        );

        offer2x2CompactingRecipe(
            RecipeCategory.BUILDING_BLOCKS, LighterEndBlocks.ENDER_BLOCK, Items.ENDER_PEARL
        );

        generateMaterialRecipes(LighterEndBlocks.VIOLECITE);

        createShaped(RecipeCategory.BUILDING_BLOCKS, LighterEndBlocks.MISSING_TILE, 4)
            .pattern("VP")
            .pattern("PV")
            .input('V', LighterEndBlocks.VIOLECITE.tiles)
            .input('P', Blocks.PURPUR_BLOCK)
            .criterion(
                hasItem(LighterEndBlocks.VIOLECITE.tiles),
                conditionsFromItem(LighterEndBlocks.VIOLECITE.tiles)
            ).offerTo(exporter);

        for (Material jadestone : Arrays.asList(
            LighterEndBlocks.AZURE_JADESTONE,
            LighterEndBlocks.SANDY_JADESTONE,
            LighterEndBlocks.VIRID_JADESTONE
        )) {
          generateMaterialRecipes(jadestone);
        }

        createShaped(RecipeCategory.BUILDING_BLOCKS, LighterEndBlocks.DRAGON_BONE_BLOCK, 8)
            .pattern("BBB")
            .pattern("BDB")
            .pattern("BBB")
            .input('B', Blocks.BONE_BLOCK)
            .input('D', Items.DRAGON_BREATH)
            .criterion(
                hasItem(Items.DRAGON_BREATH),
                conditionsFromItem(Items.DRAGON_BREATH)
            ).offerTo(exporter);
        offerSlabRecipe(
            RecipeCategory.BUILDING_BLOCKS,
            LighterEndBlocks.DRAGON_BONE_SLAB,
            LighterEndBlocks.DRAGON_BONE_BLOCK
        );
        offerStonecuttingRecipe(
            RecipeCategory.BUILDING_BLOCKS,
            LighterEndBlocks.DRAGON_BONE_SLAB,
            LighterEndBlocks.DRAGON_BONE_BLOCK,
            2
        );
        offerStairsRecipe(LighterEndBlocks.DRAGON_BONE_STAIRS, LighterEndBlocks.DRAGON_BONE_BLOCK);
        offerStonecuttingRecipe(
            RecipeCategory.BUILDING_BLOCKS,
            LighterEndBlocks.DRAGON_BONE_STAIRS,
            LighterEndBlocks.DRAGON_BONE_BLOCK
        );

        createShapeless(RecipeCategory.MISC, LighterEndBlocks.END_MOSS, 2)
            .input(Blocks.END_STONE)
            .input(Blocks.PALE_MOSS_BLOCK)
            .criterion(
                hasItem(Blocks.END_STONE),
                conditionsFromItem(Blocks.END_STONE)
            ).offerTo(exporter);

        createShapeless(RecipeCategory.MISC, Items.CYAN_DYE)
            .input(LighterEndBlocks.CREEPING_MOSS)
            .criterion(
                hasItem(LighterEndBlocks.CREEPING_MOSS),
                conditionsFromItem(LighterEndBlocks.CREEPING_MOSS)
            ).offerTo(
                exporter,
                RegistryKey.of(
                    RegistryKeys.RECIPE,
                    LighterEnd.of("cyan_dye_from_creeping_moss")
                )
            );

        createShapeless(RecipeCategory.MISC, Items.ORANGE_DYE)
            .input(LighterEndBlocks.UMBRELLA_FERN)
            .criterion(
                hasItem(LighterEndBlocks.UMBRELLA_FERN),
                conditionsFromItem(LighterEndBlocks.UMBRELLA_FERN)
            ).offerTo(
                exporter,
                RegistryKey.of(
                    RegistryKeys.RECIPE,
                    LighterEnd.of("orange_dye_from_umbrella_fern")
                )
            );

        generateCookingRecipes(LighterEndItems.LUMECORN_EAR, LighterEndItems.POPPED_LUMECORN);

        generateMaterialRecipes(LighterEndBlocks.UMBRALITH);

        createShapeless(RecipeCategory.MISC, Items.MAGENTA_DYE)
            .input(LighterEndBlocks.TENANEA_FLOWER)
            .criterion(
                hasItem(LighterEndBlocks.TENANEA_FLOWER),
                conditionsFromItem(LighterEndBlocks.TENANEA_FLOWER)
            ).offerTo(
                exporter,
                RegistryKey.of(
                    RegistryKeys.RECIPE,
                    LighterEnd.of("magenta_dye_from_tenanea_flower")
                )
            );

        generateWoodRecipes(LighterEndBlocks.TENANEA);

        createShapeless(RecipeCategory.MISC, Items.STRING, 2)
            .input(LighterEndItems.SILK)
            .criterion(
                hasItem(LighterEndItems.SILK),
                conditionsFromItem(LighterEndItems.SILK)
            ).offerTo(
                exporter,
                RegistryKey.of(
                    RegistryKeys.RECIPE,
                    LighterEnd.of("string_from_silk")
                )
            );

        createShaped(RecipeCategory.MISC, LighterEndItems.SILK_MATRIX)
            .pattern("###")
            .pattern("###")
            .pattern("###")
            .input('#', LighterEndItems.SILK)
            .criterion(
                hasItem(LighterEndItems.SILK),
                conditionsFromItem(LighterEndItems.SILK)
            ).offerTo(exporter);

        createShapeless(RecipeCategory.MISC, LighterEndItems.SILK, 9)
            .input(LighterEndItems.SILK_MATRIX)
            .criterion(
                hasItem(LighterEndItems.SILK_MATRIX),
                conditionsFromItem(LighterEndItems.SILK_MATRIX)
            ).offerTo(exporter);

        createShaped(RecipeCategory.DECORATIONS, LighterEndItems.SILK_MOTH_NEST)
            .pattern("PPP")
            .pattern("MMM")
            .pattern("PPP")
            .input('M', LighterEndItems.SILK_MATRIX)
            .input('P', LighterEndBlocks.TENANEA.planks)
            .criterion(
                hasItem(LighterEndItems.SILK_MATRIX),
                conditionsFromItem(LighterEndItems.SILK_MATRIX)
            ).offerTo(exporter);

        createShaped(RecipeCategory.COMBAT, LighterEndEquipment.SILK_ELYTRA)
            .pattern("P P")
            .pattern("MMM")
            .pattern("MMM")
            .input('M', LighterEndItems.SILK_MATRIX)
            .input('P', Items.PHANTOM_MEMBRANE)
            .criterion(
                hasItem(LighterEndItems.SILK_MATRIX),
                conditionsFromItem(LighterEndItems.SILK_MATRIX)
            ).offerTo(exporter);

        generateWoodRecipes(LighterEndBlocks.UMBRELLA);
        CookingRecipeJsonBuilder.createSmelting(
                Ingredient.ofItem(LighterEndBlocks.UMBRELLA_MEMBRANE), RecipeCategory.MISC,
                Items.SLIME_BALL, 0.1F, 200)
            .criterion(hasItem(LighterEndBlocks.UMBRELLA_MEMBRANE),
                this.conditionsFromItem(LighterEndBlocks.UMBRELLA_MEMBRANE))
            .offerTo(
                exporter,
                RegistryKey.of(
                    RegistryKeys.RECIPE,
                    LighterEnd.of("slime_balls_from_smelting_membranes")
                )
            );

        generateSmokingSmeltingRecipes(LighterEndItems.RAW_END_FISH, Items.GLOW_INK_SAC,
            RecipeCategory.MISC);

        generateSmokingSmeltingRecipes(LighterEndBlocks.CHARNIA_CYAN, Items.CYAN_DYE,
            RecipeCategory.MISC);
        generateSmokingSmeltingRecipes(LighterEndBlocks.CHARNIA_GREEN, Items.GREEN_DYE,
            RecipeCategory.MISC);
        generateSmokingSmeltingRecipes(LighterEndBlocks.CHARNIA_LIGHT_BLUE, Items.LIGHT_BLUE_DYE,
            RecipeCategory.MISC);
        generateSmokingSmeltingRecipes(LighterEndBlocks.CHARNIA_ORANGE, Items.ORANGE_DYE,
            RecipeCategory.MISC);
        generateSmokingSmeltingRecipes(LighterEndBlocks.CHARNIA_PURPLE, Items.PURPLE_DYE,
            RecipeCategory.MISC);
        generateSmokingSmeltingRecipes(LighterEndBlocks.CHARNIA_RED, Items.RED_DYE,
            RecipeCategory.MISC);

        createShaped(RecipeCategory.COMBAT, Items.SPECTRAL_ARROW, 4)
            .pattern("X")
            .pattern("#")
            .pattern("Y")
            .input('X', LighterEndItems.GLOW_BARB)
            .input('#', Items.STICK)
            .input('Y', LighterEndTags.FLETCHINGS
            ).criterion(
                hasItem(LighterEndItems.GLOW_BARB),
                conditionsFromItem(LighterEndItems.GLOW_BARB)
            ).offerTo(
                exporter,
                RegistryKey.of(
                    RegistryKeys.RECIPE,
                    LighterEnd.of("spectral_arrow")
                )
            );

        generateSmokingSmeltingRecipes(LighterEndItems.END_LILY_LEAF,
            LighterEndItems.DRIED_END_LILY_LEAF, RecipeCategory.MISC);

        createShaped(RecipeCategory.MISC, Items.PAPER, 3)
            .pattern("###")
            .input('#', LighterEndItems.DRIED_END_LILY_LEAF)
            .criterion(
                hasItem(LighterEndItems.DRIED_END_LILY_LEAF),
                conditionsFromItem(LighterEndItems.DRIED_END_LILY_LEAF)
            ).offerTo(
                exporter,
                RegistryKey.of(
                    RegistryKeys.RECIPE,
                    LighterEnd.of("paper_from_dried_leaves")
                )
            );

        generateWoodRecipes(LighterEndBlocks.LOTUS, 4);
        offerCompactingRecipe(
            RecipeCategory.BUILDING_BLOCKS,
            LighterEndBlocks.LOTUS.log,
            LighterEndBlocks.END_LOTUS_STEM
        );

        generateWoodRecipes(LighterEndBlocks.GLOWSHROOM);

        CookingRecipeJsonBuilder.createSmelting(
                Ingredient.ofItem(LighterEndItems.END_CREAM), RecipeCategory.BREWING,
                LighterEndItems.END_POWDER, 0.1F, 200)
            .criterion(hasItem(LighterEndBlocks.UMBRELLA_MEMBRANE),
                conditionsFromItem(LighterEndBlocks.UMBRELLA_MEMBRANE))
            .offerTo(
                exporter,
                RegistryKey.of(
                    RegistryKeys.RECIPE,
                    LighterEnd.of("end_powder_from_smelting_end_cream")
                )
            );

        createShaped(RecipeCategory.TOOLS, Items.SHEARS)
            .pattern(" #")
            .pattern("# ")
            .input('#', LighterEndItems.CRAB_CLAW)
            .criterion(
                hasItem(LighterEndItems.CRAB_CLAW),
                conditionsFromItem(LighterEndItems.CRAB_CLAW)
            ).offerTo(
                exporter,
                RegistryKey.of(
                    RegistryKeys.RECIPE,
                    LighterEnd.of("shears_from_claws")
                )
            );
        generateSmokingSmeltingRecipes(
            LighterEndItems.CRAB_MEAT,
            LighterEndItems.CRAB_CAKE,
            RecipeCategory.FOOD
        );

        createShaped(RecipeCategory.DECORATIONS, LighterEndBlocks.END_FURNACE)
            .input('#', Blocks.END_STONE)
            .pattern("###")
            .pattern("# #")
            .pattern("###")
            .criterion(hasItem(Blocks.END_STONE), this.conditionsFromItem(Blocks.END_STONE))
            .offerTo(this.exporter);
        this.createShapeless(RecipeCategory.TRANSPORTATION, Items.FURNACE_MINECART)
            .input(LighterEndBlocks.END_FURNACE)
            .input(Items.MINECART)
            .criterion(
                hasItem(LighterEndBlocks.END_FURNACE),
                this.conditionsFromItem(LighterEndBlocks.END_FURNACE)
            ).offerTo(
                exporter,
                RegistryKey.of(
                    RegistryKeys.RECIPE,
                    LighterEnd.of("furnace_minecart_from_end_stone_furnace")
                )
            );
        this.createShaped(RecipeCategory.DECORATIONS, LighterEndBlocks.END_SMOKER)
            .input('#', ItemTags.LOGS)
            .input('X', LighterEndBlocks.END_FURNACE)
            .pattern(" # ")
            .pattern("#X#")
            .pattern(" # ")
            .criterion(
                hasItem(LighterEndBlocks.END_FURNACE),
                this.conditionsFromItem(LighterEndBlocks.END_FURNACE)
            ).offerTo(this.exporter);

        this.createShaped(RecipeCategory.REDSTONE, LighterEndBlocks.END_LEVER)
            .input('#', Blocks.END_STONE)
            .input('X', Items.STICK)
            .pattern("X")
            .pattern("#")
            .criterion(hasItem(Blocks.END_STONE), this.conditionsFromItem(Blocks.END_STONE))
            .offerTo(this.exporter);
      }

      public void generateMaterialRecipes(Material material) {

        offerSlabRecipe(
            RecipeCategory.BUILDING_BLOCKS, material.baseSlab, material.baseBlock
        );
        offerStonecuttingRecipe(
            RecipeCategory.BUILDING_BLOCKS, material.baseSlab, material.baseBlock, 2
        );
        offerStairsRecipe(material.baseStairs, material.baseBlock);
        offerStonecuttingRecipe(
            RecipeCategory.BUILDING_BLOCKS, material.baseStairs, material.baseBlock
        );
        offerWallRecipe(RecipeCategory.BUILDING_BLOCKS, material.baseWall, material.baseBlock);
        offerStonecuttingRecipe(
            RecipeCategory.BUILDING_BLOCKS, material.baseWall, material.baseBlock
        );
        createShaped(RecipeCategory.BUILDING_BLOCKS, material.pillar, 1)
            .pattern("s")
            .pattern("s")
            .input('s', material.baseSlab)
            .criterion(
                hasItem(material.baseSlab),
                conditionsFromItem(material.baseSlab)
            ).offerTo(exporter);
        offerStonecuttingRecipe(
            RecipeCategory.BUILDING_BLOCKS, material.pillar, material.baseBlock
        );

        offerPolishedStoneRecipe(
            RecipeCategory.BUILDING_BLOCKS, material.polished, material.baseBlock
        );
        offerStonecuttingRecipe(
            RecipeCategory.BUILDING_BLOCKS, material.polished, material.baseBlock
        );
        offerSlabRecipe(
            RecipeCategory.BUILDING_BLOCKS, material.polishedSlab, material.polished
        );
        offerStairsRecipe(material.polishedStairs, material.polished);
        offerWallRecipe(RecipeCategory.BUILDING_BLOCKS, material.polishedWall, material.polished);
        offerButtonRecipe(material.button, material.polished);
        offerPressurePlateRecipe(material.pressurePlate, material.polished);
        for (ItemConvertible input : Arrays.asList(material.polished, material.baseBlock)) {
          offerStonecuttingRecipe(
              RecipeCategory.BUILDING_BLOCKS, material.polishedSlab, input, 2
          );
          offerStonecuttingRecipe(
              RecipeCategory.BUILDING_BLOCKS, material.polishedStairs, input
          );
          offerStonecuttingRecipe(
              RecipeCategory.BUILDING_BLOCKS, material.polishedWall, input
          );
        }

        offerPolishedStoneRecipe(
            RecipeCategory.BUILDING_BLOCKS, material.bricks, material.polished
        );
        offerStonecuttingRecipe(RecipeCategory.BUILDING_BLOCKS, material.bricks, material.polished);
        offerStonecuttingRecipe(
            RecipeCategory.BUILDING_BLOCKS, material.bricks, material.baseBlock
        );
        offerSlabRecipe(RecipeCategory.BUILDING_BLOCKS, material.brickSlab, material.bricks);
        offerStairsRecipe(material.brickStairs, material.bricks);
        offerWallRecipe(RecipeCategory.BUILDING_BLOCKS, material.brickWall, material.bricks);
        for (ItemConvertible input : Arrays.asList(
            material.bricks, material.polished, material.baseBlock
        )) {
          offerStonecuttingRecipe(
              RecipeCategory.BUILDING_BLOCKS, material.brickSlab, input, 2
          );
          offerStonecuttingRecipe(
              RecipeCategory.BUILDING_BLOCKS, material.brickStairs, input
          );
          offerStonecuttingRecipe(
              RecipeCategory.BUILDING_BLOCKS, material.brickWall, input
          );
        }

        offerPolishedStoneRecipe(RecipeCategory.BUILDING_BLOCKS, material.tiles, material.bricks);
        offerStonecuttingRecipe(RecipeCategory.BUILDING_BLOCKS, material.tiles, material.polished);
        offerStonecuttingRecipe(RecipeCategory.BUILDING_BLOCKS, material.tiles, material.baseBlock);
        offerSlabRecipe(
            RecipeCategory.BUILDING_BLOCKS, material.tileSlab, material.tiles
        );
        offerStairsRecipe(material.tileStairs, material.tiles);
        offerWallRecipe(RecipeCategory.BUILDING_BLOCKS, material.tileWall, material.tiles);
        for (ItemConvertible input : Arrays.asList(
            material.tiles, material.bricks, material.polished, material.baseBlock
        )) {
          offerStonecuttingRecipe(
              RecipeCategory.BUILDING_BLOCKS, material.tileSlab, input, 2
          );
          offerStonecuttingRecipe(
              RecipeCategory.BUILDING_BLOCKS, material.tileStairs, input
          );
          offerStonecuttingRecipe(
              RecipeCategory.BUILDING_BLOCKS, material.tileWall, input
          );
        }
      }

      public void generateWoodRecipes(Wood wood) {
        generateWoodRecipes(wood, 4);
      }

      public void generateWoodRecipes(Wood wood, int planks_per_log) {
        createShaped(RecipeCategory.BUILDING_BLOCKS, wood.wood, 3).pattern("ll").pattern("ll")
            .input('l', wood.log).criterion(
                hasItem(wood.log),
                conditionsFromItem(wood.log)
            ).offerTo(exporter);
        createShaped(RecipeCategory.BUILDING_BLOCKS, wood.strippedWood, 3).pattern("ll")
            .pattern("ll").input('l', wood.log).criterion(
                hasItem(wood.strippedLog),
                conditionsFromItem(wood.strippedLog)
            ).offerTo(exporter);
        createShapeless(RecipeCategory.BUILDING_BLOCKS, wood.planks, planks_per_log).input(
                Ingredient.ofItems(wood.log, wood.strippedLog, wood.wood, wood.strippedWood))
            .criterion(hasItem(wood.log), this.conditionsFromItem(wood.log)).offerTo(exporter);
        offerSlabRecipe(RecipeCategory.BUILDING_BLOCKS, wood.slab, wood.planks);
        offerStairsRecipe(wood.stairs, wood.planks);
        createDoorRecipe(wood.door, Ingredient.ofItem(wood.planks)).criterion(hasItem(wood.planks),
            conditionsFromItem(wood.planks)).offerTo(exporter);
        createDoorRecipe(wood.trapdoor, Ingredient.ofItem(wood.planks)).criterion(
            hasItem(wood.planks),
            conditionsFromItem(wood.planks)).offerTo(exporter);
        createFenceRecipe(wood.fence, Ingredient.ofItem(wood.planks)).criterion(
            hasItem(wood.planks),
            conditionsFromItem(wood.planks)).offerTo(exporter);
        createFenceGateRecipe(wood.gate, Ingredient.ofItem(wood.planks)).criterion(
            hasItem(wood.planks),
            conditionsFromItem(wood.planks)).offerTo(exporter);
        offerButtonRecipe(wood.button, wood.planks);
        offerPressurePlateRecipe(wood.pressurePlate, wood.planks);
        createShaped(RecipeCategory.DECORATIONS, wood.ladder, 2)
            .input('#', wood.slab)
            .pattern("#")
            .pattern("#")
            .pattern("#")
            .criterion(hasItem(wood.planks), this.conditionsFromItem(wood.planks))
            .offerTo(exporter);
        createSignRecipe(wood.sign, Ingredient.ofItem(wood.planks)).criterion(
            hasItem(wood.planks),
            conditionsFromItem(wood.planks)).offerTo(exporter);
        offerHangingSignRecipe(wood.hangingSign, wood.strippedLog);

      }

      // seems odd these aren't already implemented
      public void offerStairsRecipe(ItemConvertible output, ItemConvertible input) {
        createStairsRecipe(
            output, Ingredient.ofItem(input)
        ).criterion(hasItem(input), this.conditionsFromItem(input)).offerTo(exporter);
      }

      public void offerButtonRecipe(ItemConvertible output, ItemConvertible input) {
        createButtonRecipe(
            output, Ingredient.ofItem(input)
        ).criterion(hasItem(input), this.conditionsFromItem(input)).offerTo(exporter);
      }

      public void generateCookingRecipes(ItemConvertible input, ItemConvertible output) {
        generateSmokingSmeltingRecipes(input, output, RecipeCategory.FOOD);

        CookingRecipeJsonBuilder.createCampfireCooking(
            Ingredient.ofItem(input),
            RecipeCategory.FOOD,
            output,
            0.35F,
            600
        ).criterion(hasItem(input), this.conditionsFromItem(input)
        ).offerTo(
            exporter,
            RegistryKey.of(
                RegistryKeys.RECIPE,
                LighterEnd.of(
                    Registries.ITEM.getId(output.asItem()).getPath()
                        + "_campfire_from_"
                        + Registries.ITEM.getId(input.asItem()).getPath())
            )
        );
      }

      public void generateSmokingSmeltingRecipes(ItemConvertible input, ItemConvertible output,
          RecipeCategory category) {
        String output_key = Registries.ITEM.getId(output.asItem()).getPath();
        CookingRecipeJsonBuilder.createSmelting(
            Ingredient.ofItem(input),
            category,
            output,
            0.35F,
            200
        ).criterion(hasItem(input), this.conditionsFromItem(input)
        ).offerTo(
            exporter,
            RegistryKey.of(RegistryKeys.RECIPE,
                LighterEnd.of(
                    output_key
                        + "_smelting_"
                        + Registries.ITEM.getId(input.asItem()).getPath()
                )
            )
        );
        CookingRecipeJsonBuilder.createSmoking(
            Ingredient.ofItem(input),
            category,
            output,
            0.35F,
            100
        ).criterion(hasItem(input),
            conditionsFromItem(input)
        ).offerTo(
            exporter,
            RegistryKey.of(RegistryKeys.RECIPE,
                LighterEnd.of(
                    output_key
                        + "_smoking_"
                        + Registries.ITEM.getId(input.asItem()).getPath()
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
