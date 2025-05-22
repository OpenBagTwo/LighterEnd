package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks.Material;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks.Wood;
import io.github.openbagtwo.lighterend.tags.LighterEndTags;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.BlockTags;

public class BlockTagProvider extends FabricTagProvider.BlockTagProvider {

  protected BlockTagProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> future) {
    super(output, future);
  }

  @Override
  protected void configure(RegistryWrapper.WrapperLookup lookup) {
    for (Material material : Arrays.asList(
        LighterEndBlocks.VIOLECITE,
        LighterEndBlocks.AZURE_JADESTONE,
        LighterEndBlocks.SANDY_JADESTONE,
        LighterEndBlocks.VIRID_JADESTONE, LighterEndBlocks.UMBRALITH
    )) {
      for (Block block : material.blocks) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(block);
        getOrCreateTagBuilder(BlockTags.WALLS)
            .add(
                material.baseWall,
                material.brickWall,
                material.polishedWall,
                material.tileWall
            );
        getOrCreateTagBuilder(BlockTags.STONE_BUTTONS).add(material.button);
        getOrCreateTagBuilder(BlockTags.STONE_PRESSURE_PLATES).add(material.pressurePlate);
      }
    }

    for (Wood wood : Arrays.asList(
        LighterEndBlocks.TENANEA
    )) {
      for (Block block : wood.blocks) {
        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE).add(block);
      }
      getOrCreateTagBuilder(BlockTags.AXE_MINEABLE).add(wood.wallSign, wood.wallHangingSign);
      getOrCreateTagBuilder(BlockTags.PLANKS).add(wood.planks);
      getOrCreateTagBuilder(BlockTags.WOODEN_BUTTONS).add(wood.button);
      getOrCreateTagBuilder(BlockTags.WOODEN_DOORS).add(wood.door);
      getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS).add(wood.stairs);
      getOrCreateTagBuilder(BlockTags.WOODEN_SLABS).add(wood.slab);
      getOrCreateTagBuilder(BlockTags.WOODEN_FENCES).add(wood.fence);
      getOrCreateTagBuilder(BlockTags.FENCE_GATES).add(wood.gate);
      getOrCreateTagBuilder(BlockTags.WOODEN_PRESSURE_PLATES).add(wood.pressurePlate);
      getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN).add(
          wood.log,
          wood.strippedLog,
          wood.wood,
          wood.strippedWood
      );
      getOrCreateTagBuilder(BlockTags.WOODEN_TRAPDOORS).add(wood.trapdoor);
      getOrCreateTagBuilder(BlockTags.STANDING_SIGNS).add(wood.sign);
      getOrCreateTagBuilder(BlockTags.WALL_SIGNS).add(wood.wallSign);
      getOrCreateTagBuilder(BlockTags.CEILING_HANGING_SIGNS).add(wood.hangingSign);
      getOrCreateTagBuilder(BlockTags.WALL_HANGING_SIGNS).add(wood.wallHangingSign);
      getOrCreateTagBuilder(BlockTags.CLIMBABLE).add(wood.ladder);
    }

    getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
        .add(LighterEndBlocks.ENDER_BLOCK)
        .add(LighterEndBlocks.MISSING_TILE)
        .add(
            LighterEndBlocks.DRAGON_BONE_BLOCK,
            LighterEndBlocks.DRAGON_BONE_STAIRS,
            LighterEndBlocks.DRAGON_BONE_SLAB,
            LighterEndBlocks.END_MOSS
        );

    getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
        .add(LighterEndBlocks.ENDER_BLOCK);

    for (Material jadestone : Arrays.asList(
        LighterEndBlocks.AZURE_JADESTONE,
        LighterEndBlocks.SANDY_JADESTONE,
        LighterEndBlocks.VIRID_JADESTONE
    )) {
      for (Block block : jadestone.blocks) {

        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL).add(block);
      }

    }

    getOrCreateTagBuilder(BlockTags.ENDERMAN_HOLDABLE).add(LighterEndBlocks.END_MOSS);
    getOrCreateTagBuilder(BlockTags.ANIMALS_SPAWNABLE_ON).add(LighterEndBlocks.END_MOSS);
    getOrCreateTagBuilder(BlockTags.REPLACEABLE_BY_TREES).add(LighterEndBlocks.END_MOSS);
    getOrCreateTagBuilder(BlockTags.SCULK_REPLACEABLE).add(LighterEndBlocks.END_MOSS);

    getOrCreateTagBuilder(LighterEndTags.END_MOSS_REPLACEABLE).add(Blocks.END_STONE);

    getOrCreateTagBuilder(LighterEndTags.END_SOIL).add(
        Blocks.END_STONE, LighterEndBlocks.END_MOSS
    );

    getOrCreateTagBuilder(BlockTags.FLOWERS).add(
        LighterEndBlocks.CREEPING_MOSS,
        LighterEndBlocks.UMBRELLA_FERN,
        LighterEndBlocks.TALL_UMBRELLA_FERN,
        LighterEndBlocks.TENANEA_FLOWER
    );

    getOrCreateTagBuilder(BlockTags.AXE_MINEABLE).add(LighterEndBlocks.LUMECORN_STEM);

    getOrCreateTagBuilder(BlockTags.SAPLINGS).add(LighterEndBlocks.TENANEA_SAPLING);

    getOrCreateTagBuilder(LighterEndTags.END_MOSS_REPLACEABLE)
        .add(LighterEndBlocks.UMBRALITH.baseBlock);
    getOrCreateTagBuilder(LighterEndTags.END_SOIL)
        .add(LighterEndBlocks.UMBRALITH.baseBlock);


  }

}
