package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks.Material;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks.Wood;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
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
import net.minecraft.util.Identifier;

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
            ).offerTo(exporter);

        createShapeless(RecipeCategory.MISC, Items.ORANGE_DYE)
            .input(LighterEndBlocks.UMBRELLA_FERN)
            .criterion(
                hasItem(LighterEndBlocks.UMBRELLA_FERN),
                conditionsFromItem(LighterEndBlocks.UMBRELLA_FERN)
            ).offerTo(exporter);

        generateCookingRecipes(LighterEndItems.LUMECORN_EAR, LighterEndItems.POPPED_LUMECORN);

        generateMaterialRecipes(LighterEndBlocks.UMBRALITH);

        createShapeless(RecipeCategory.MISC, Items.MAGENTA_DYE)
            .input(LighterEndBlocks.TENANEA_FLOWER)
            .criterion(
                hasItem(LighterEndBlocks.TENANEA_FLOWER),
                conditionsFromItem(LighterEndBlocks.TENANEA_FLOWER)
            ).offerTo(exporter);

        generateWoodRecipes(LighterEndBlocks.TENANEA);
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
        createShapeless(RecipeCategory.BUILDING_BLOCKS, wood.planks, 4).input(
                Ingredient.ofItems(wood.log, wood.strippedLog, wood.wood, wood.strippedWood))
            .criterion(hasItem(wood.log), conditionsFromItem(wood.log)).offerTo(exporter);
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
        ).criterion(hasItem(input), conditionsFromItem(input)).offerTo(exporter);
      }

      public void offerButtonRecipe(ItemConvertible output, ItemConvertible input) {
        createButtonRecipe(
            output, Ingredient.ofItem(input)
        ).criterion(hasItem(input), conditionsFromItem(input)).offerTo(exporter);
      }

      public void generateCookingRecipes(ItemConvertible input, ItemConvertible output) {
        Identifier output_key = Registries.ITEM.getId(output.asItem());
        CookingRecipeJsonBuilder.createSmelting(
            Ingredient.ofItem(input),
            RecipeCategory.FOOD,
            output,
            0.35F,
            200
        ).criterion(hasItem(input), conditionsFromItem(input)
        ).offerTo(
            exporter,
            RegistryKey.of(RegistryKeys.RECIPE, output_key.withSuffixedPath("_smelting"))
        );
        CookingRecipeJsonBuilder.createSmoking(
            Ingredient.ofItem(input),
            RecipeCategory.FOOD,
            output,
            0.35F,
            100
        ).criterion(hasItem(input), conditionsFromItem(input)
        ).offerTo(
            exporter,
            RegistryKey.of(RegistryKeys.RECIPE, output_key.withSuffixedPath("_smoking"))
        );
        CookingRecipeJsonBuilder.createCampfireCooking(
            Ingredient.ofItem(input),
            RecipeCategory.FOOD,
            output,
            0.35F,
            600
        ).criterion(hasItem(input), conditionsFromItem(input)
        ).offerTo(
            exporter,
            RegistryKey.of(RegistryKeys.RECIPE, output_key.withSuffixedPath("_campfire"))
        );
      }
    };
  }

  @Override
  public String getName() {
    return "LighterEndRecipeProvider";
  }
}
