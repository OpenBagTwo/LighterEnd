package io.github.openbagtwo.lighterend.world.features;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class UnderwaterPlants extends Feature<DefaultFeatureConfig> {

  public UnderwaterPlants() {
    super(DefaultFeatureConfig.CODEC);
  }

  @Override
  public boolean generate(FeatureContext<DefaultFeatureConfig> featureConfig) {
    final Random random = featureConfig.getRandom();
    final BlockPos blockPos = featureConfig.getOrigin();
    final StructureWorldAccess world = featureConfig.getWorld();

    label80:
    for (int i = 0; i < 128; i++) {
      BlockPos blockPos2 = blockPos;
      BlockState blockState = LighterEndBlocks.CHARNIA_CYAN.getDefaultState();

      for (int j = 0; j < i / 16; j++) {
        blockPos2 = blockPos2.add(random.nextInt(3) - 1,
            (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
        if (world.getBlockState(blockPos2).isFullCube(world, blockPos2)) {
          continue label80;
        }
      }

      blockState = Registries.BLOCK
          .getRandomEntry(LighterEndTags.AQUATIC_END_VEGETATION, random)
          .map(blockEntry -> (blockEntry.value()).getDefaultState())
          .orElse(blockState);

      if (blockState.canPlaceAt(world, blockPos2)) {
        BlockState blockState2 = world.getBlockState(blockPos2);
        if (blockState2.isOf(Blocks.WATER)
            && world.getFluidState(blockPos2).getLevel() == 8) {
          world.setBlockState(blockPos2, blockState, Block.NOTIFY_ALL);
        }
      }
    }
    return true;
  }

}
