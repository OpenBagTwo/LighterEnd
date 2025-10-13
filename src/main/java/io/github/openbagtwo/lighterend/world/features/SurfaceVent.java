package io.github.openbagtwo.lighterend.world.features;

import static net.minecraft.world.Heightmap.Type.WORLD_SURFACE;

import io.github.openbagtwo.lighterend.blocks.HydrothermalVent;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.github.openbagtwo.lighterend.utils.Flags;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SurfaceVent extends Feature<DefaultFeatureConfig> {

  public SurfaceVent() {
    super(DefaultFeatureConfig.CODEC);
  }

  @Override
  public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
    final Random random = context.getRandom();
    BlockPos pos = context.getOrigin();
    final StructureWorldAccess world = context.getWorld();
    pos = world.getTopPosition(WORLD_SURFACE,
        new BlockPos(pos.getX() + random.nextInt(16), pos.getY(), pos.getZ() + random.nextInt(16))
    );
    if (!world.getBlockState(pos.down(3)).isIn(LighterEndTags.END_STONES)) {
      return false;
    }

    Mutable mut = new Mutable();
    int count = MathHelper.nextInt(random, 15, 30);
    BlockState vent = LighterEndBlocks.HYDROTHERMAL_VENT.getDefaultState()
        .with(HydrothermalVent.WATERLOGGED, false).with(HydrothermalVent.ACTIVATED, true);
    for (int i = 0; i < count; i++) {
      mut.set(pos)
          .move(
              MathHelper.floor(random.nextGaussian() * 2 + 0.5),
              5,
              MathHelper.floor(random.nextGaussian() * 2 + 0.5)
          );
      int dist = MathHelper.floor(2 - Math.sqrt(
          Math.pow(mut.getX() - pos.getX(), 2)
              + Math.pow(mut.getZ() - pos.getZ(), 2)
      )) + random.nextInt(2);
      if (dist > 0) {
        BlockState state = world.getBlockState(mut);
        for (int n = 0; n < 10 && state.isAir(); n++) {
          mut.setY(mut.getY() - 1);
          state = world.getBlockState(mut);
        }
        if (
            state.isIn(LighterEndTags.END_STONES)
                && !world.getBlockState(mut.up()).isOf(LighterEndBlocks.HYDROTHERMAL_VENT)
        ) {
          for (int j = 0; j <= dist; j++) {
            world.setBlockState(
                mut,
                LighterEndBlocks.BORNITE.baseBlock.getDefaultState(),
                Flags.SILENT
            );
            mut.setY(mut.getY() + 1);
          }
          world.setBlockState(mut, vent, Flags.SILENT);
        }
      }
    }

    return true;
  }
}
