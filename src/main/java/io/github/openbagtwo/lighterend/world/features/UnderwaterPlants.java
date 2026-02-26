package io.github.openbagtwo.lighterend.world.features;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class UnderwaterPlants extends Feature<NoneFeatureConfiguration> {

  public UnderwaterPlants() {
    super(NoneFeatureConfiguration.CODEC);
  }

  @Override
  public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
    final RandomSource random = featureConfig.random();
    final BlockPos blockPos = featureConfig.origin();
    final WorldGenLevel world = featureConfig.level();

    label80:
    for (int i = 0; i < 128; i++) {
      BlockPos blockPos2 = blockPos;
      BlockState blockState = LighterEndBlocks.CHARNIA_CYAN.defaultBlockState();

      for (int j = 0; j < i / 16; j++) {
        blockPos2 = blockPos2.offset(random.nextInt(3) - 1,
            (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
        if (world.getBlockState(blockPos2).isCollisionShapeFullBlock(world, blockPos2)) {
          continue label80;
        }
      }

      blockState = BuiltInRegistries.BLOCK
          .getRandomElementOf(LighterEndTags.AQUATIC_END_VEGETATION, random)
          .map(blockEntry -> (blockEntry.value()).defaultBlockState())
          .orElse(blockState);

      if (blockState.canSurvive(world, blockPos2)) {
        BlockState blockState2 = world.getBlockState(blockPos2);
        if (blockState2.is(Blocks.WATER)
            && world.getFluidState(blockPos2).getAmount() == 8) {
          world.setBlock(blockPos2, blockState, Block.UPDATE_ALL);
        }
      }
    }
    return true;
  }

}
