package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.registries.LighterEndBiomes;
import io.github.openbagtwo.lighterend.world.LighterEndConfiguredFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableFeaturePlacerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraft.world.level.material.MapColor;

public class EndMoss extends BonemealableFeaturePlacerBlock {
  /* One of the biggest differences between LighterEnd and BetterEnd in terms of design is that
     there will only be *one* EndTerrainBlock (onto which all LighterEnd plants can be planted).
     Different patternings / colorations / bonemealing outputs, if implemented, will be determined
     by the biome in which the blocks are placed.
   */

  private final ResourceKey<ConfiguredFeature<?, ?>> feature;

  public EndMoss(Properties settings) {
    super(
        LighterEndConfiguredFeatures.END_MOSS_PATCH_BONEMEAL,
        settings
            .mapColor(MapColor.COLOR_CYAN)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(1.5F, 4.5F)
            .sound(SoundType.NYLIUM)
            .ignitedByLava()
            .randomTicks()
    );
    this.feature = LighterEndConfiguredFeatures.END_MOSS_PATCH_BONEMEAL;
  }

  @Override
  protected void randomTick(BlockState state, ServerLevel world, BlockPos pos,
      RandomSource random) {
    if (!stayAlive(state, world, pos)) {
      world.setBlockAndUpdate(pos, Blocks.END_STONE.defaultBlockState());
    }
  }

  private static boolean stayAlive(BlockState state, LevelReader world, BlockPos pos) {
    BlockPos blockPos = pos.above();
    BlockState blockState = world.getBlockState(blockPos);

    int i = LightEngine.getLightBlockInto(
        state,
        blockState,
        Direction.UP,
        blockState.getLightEmission()
    );
    return i < 15;
  }

  protected boolean canGrow(LevelReader world, BlockPos pos) {
    if (world.getBiome(pos).is(BiomeTags.IS_END)) {
      return true;
    }
    if (LighterEnd.CONFIG.endPlantsOnlyGrowInTheEnd()) {
      return true;
    }
    return false;
  }

  @Override
  public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos,
      BlockState state) {
    return canGrow(world, pos);
  }

  @Override
  public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
    return world.getBlockState(pos.above()).isAir() && this.canGrow(world, pos);
  }

  @Override
  public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos,
      BlockState state) {
    if (!canGrow(world, pos)) {
      return;
    }

    ResourceKey<ConfiguredFeature<?, ?>> patch;
    if (world.getBiome(pos).is(LighterEndBiomes.SHADOW_FOREST)) {
      patch = LighterEndConfiguredFeatures.SHADOW_MOSS_PATCH_BONEMEAL;
    } else {
      patch = LighterEndConfiguredFeatures.END_MOSS_PATCH_BONEMEAL;
    }

    world.registryAccess()
        .lookup(Registries.CONFIGURED_FEATURE)
        .flatMap(registry -> registry.get(patch))
        .ifPresent(entry -> entry.value().place(
                world,
                world.getChunkSource().getGenerator(),
                random,
                pos.above()
            )
        );


  }
}
