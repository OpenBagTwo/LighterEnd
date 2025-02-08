package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks.Material;
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
  protected BlockTagProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
    super(output, registriesFuture);
  }

  @Override
  protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
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

    for (Block block : LighterEndBlocks.VIOLECITE.blocks){
      getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(block);
    }

    getOrCreateTagBuilder(BlockTags.WALLS)
        .add(
            LighterEndBlocks.VIOLECITE.baseWall,
            LighterEndBlocks.VIOLECITE.brickWall,
            LighterEndBlocks.VIOLECITE.polishedWall,
            LighterEndBlocks.VIOLECITE.tileWall
        );

    for (Material jadestone : Arrays.asList(
        LighterEndBlocks.AZURE_JADESTONE,
        LighterEndBlocks.SANDY_JADESTONE,
        LighterEndBlocks.VIRID_JADESTONE
    )) {
      for (Block block : jadestone.blocks){
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(block);
        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL).add(block);
      }
      getOrCreateTagBuilder(BlockTags.WALLS)
          .add(
              jadestone.baseWall,
              jadestone.brickWall,
              jadestone.polishedWall,
              jadestone.tileWall
          );
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
        LighterEndBlocks.TALL_UMBRELLA_FERN
    );

    getOrCreateTagBuilder(BlockTags.AXE_MINEABLE).add(LighterEndBlocks.LUMECORN_STEM);
  }

}
