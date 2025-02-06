package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
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
      .add(LighterEndBlocks.MISSING_TILE);

    getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
        .add(LighterEndBlocks.ENDER_BLOCK);

    for (Block block : LighterEndBlocks.VIOLECITE.blocks){
      getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(block);
    }

    getOrCreateTagBuilder(BlockTags.WALLS)
        .add(LighterEndBlocks.VIOLECITE.baseWall)
        .add(LighterEndBlocks.VIOLECITE.brickWall)
        .add(LighterEndBlocks.VIOLECITE.polishedWall)
        .add(LighterEndBlocks.VIOLECITE.tileWall);
  }
}
