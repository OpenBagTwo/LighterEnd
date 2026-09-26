package io.github.openbagtwo.lighterend.world.features;

import static net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE;

import io.github.openbagtwo.lighterend.blocks.HydrothermalVent;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.github.openbagtwo.lighterend.utils.Flags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SurfaceVent extends Feature<NoneFeatureConfiguration> {

  public SurfaceVent() {
    super(NoneFeatureConfiguration.CODEC);
  }

  @Override
  public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
    final RandomSource random = context.random();
    BlockPos pos = context.origin();
    final WorldGenLevel world = context.level();
    pos = world.getHeightmapPos(WORLD_SURFACE,
        new BlockPos(pos.getX() + random.nextInt(16), pos.getY(), pos.getZ() + random.nextInt(16))
    );
    if (!world.getBlockState(pos.below(3)).is(LighterEndTags.END_STONES)) {
      return false;
    }

    MutableBlockPos mut = new MutableBlockPos();
    int count = Mth.nextInt(random, 15, 30);
    BlockState vent = LighterEndBlocks.HYDROTHERMAL_VENT.defaultBlockState()
        .setValue(HydrothermalVent.WATERLOGGED, false)
        .setValue(HydrothermalVent.ACTIVATED, true);
    for (int i = 0; i < count; i++) {
      mut.set(pos)
          .move(
              Mth.floor(random.nextGaussian() * 2 + 0.5),
              5,
              Mth.floor(random.nextGaussian() * 2 + 0.5)
          );
      int dist = Mth.floor(2 - Math.sqrt(
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
            state.is(LighterEndTags.END_STONES)
                && !world.getBlockState(mut.above()).is(LighterEndBlocks.HYDROTHERMAL_VENT)
        ) {
          for (int j = 0; j <= dist; j++) {
            world.setBlock(
                mut,
                LighterEndBlocks.BORNITE.baseBlock.defaultBlockState(),
                Flags.SILENT
            );
            mut.setY(mut.getY() + 1);
          }
          world.setBlock(mut, vent, Flags.SILENT);
        }
      }
    }

    return true;
  }
}
