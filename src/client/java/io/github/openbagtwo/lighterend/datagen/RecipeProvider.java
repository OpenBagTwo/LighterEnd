package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks.Material;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class RecipeProvider extends FabricRecipeProvider {
  protected RecipeProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
    super(output, registriesFuture);
  }

  @Override
  protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
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
        createShapeless(RecipeCategory.TOOLS, Items.ENDER_PEARL, 4)
            .input(LighterEndBlocks.ENDER_BLOCK)
            .criterion(
                hasItem(LighterEndBlocks.ENDER_BLOCK),
                conditionsFromItem(Items.ENDER_PEARL)
            ).offerTo(exporter);

        generateMaterialRecipes(LighterEndBlocks.VIOLECITE);

        createShaped(RecipeCategory.BUILDING_BLOCKS, LighterEndBlocks.MISSING_TILE, 4)
            .pattern("VP")
            .pattern("PV")
            .input('V', LighterEndBlocks.VIOLECITE.tiles)
            .input('P', Blocks.PURPUR_BLOCK)
            .criterion(
                hasItem(LighterEndBlocks.VIOLECITE.tiles),
                conditionsFromItem(LighterEndBlocks.MISSING_TILE)
            ).offerTo(exporter);
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
                conditionsFromItem(material.pillar)
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
        for (ItemConvertible input : Arrays.asList(material.polished, material.baseBlock)){
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
        )){
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
        )){
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

      // seems odd these aren't already implemented
      public void offerStairsRecipe(ItemConvertible output, ItemConvertible input){
        createStairsRecipe(
            output, Ingredient.ofItem(input)
        ).criterion(hasItem(input),conditionsFromItem(output)).offerTo(exporter);
      }
      public void offerButtonRecipe(ItemConvertible output, ItemConvertible input){
        createButtonRecipe(
            output, Ingredient.ofItem(input)
        ).criterion(hasItem(input),conditionsFromItem(output)).offerTo(exporter);
      }
    };
  }

  @Override
  public String getName() {
    return "LighterEndRecipeProvider";
  }
}
