package io.github.openbagtwo.lighterend.world.features;

import com.google.common.collect.Sets;
import io.github.openbagtwo.lighterend.BlockFixer;
import io.github.openbagtwo.lighterend.blocks.HydrothermalVent;
import io.github.openbagtwo.lighterend.blocks.TubeWorm;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.github.openbagtwo.lighterend.utils.Flags;
import io.github.openbagtwo.lighterend.utils.MiscUtils;
import io.github.openbagtwo.lighterend.utils.math.MathUtils;
import io.github.openbagtwo.lighterend.world.gen.noise.OpenSimplexNoise;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SulphurCave extends Feature<NoneFeatureConfiguration> {

  public SulphurCave() {
    super(NoneFeatureConfiguration.CODEC);
  }

  @Override
  public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
    final RandomSource random = context.random();
    BlockPos pos = context.origin();
    final WorldGenLevel world = context.level();
    int radius = Mth.nextInt(random, 10, 30);

    int top = world.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ());
    MutableBlockPos bpos = new MutableBlockPos();
    bpos.setX(pos.getX());
    bpos.setZ(pos.getZ());
    bpos.setY(top - 1);

    BlockState state = world.getBlockState(bpos);
    while (!state.is(LighterEndTags.END_STONES) && bpos.getY() > 5) {
      bpos.setY(bpos.getY() - 1);
      state = world.getBlockState(bpos);
    }
    if (bpos.getY() < 10) {
      return false;
    }
    top = (int) (bpos.getY() - (radius * 1.3F + 5));

    while (state.is(LighterEndTags.END_STONES)
        || !state.getFluidState().isEmpty() && bpos.getY() > 5) {
      bpos.setY(bpos.getY() - 1);
      state = world.getBlockState(bpos);
    }
    int bottom = (int) (bpos.getY() + radius * 1.3F + 5);

    if (top <= bottom) {
      return false;
    }

    MutableBlockPos mut = new MutableBlockPos();
    pos = new BlockPos(pos.getX(), Mth.nextInt(random, bottom, top), pos.getZ());

    OpenSimplexNoise noise = new OpenSimplexNoise(MathUtils.getSeed(534, pos.getX(), pos.getZ()));

    int x1 = pos.getX() - radius - 5;
    int z1 = pos.getZ() - radius - 5;
    int x2 = pos.getX() + radius + 5;
    int z2 = pos.getZ() + radius + 5;
    int y1 = Mth.floor(pos.getY() - (radius + 5) / 1.6);
    int y2 = Mth.floor(pos.getY() + (radius + 5) / 1.6);

    double hr = radius * 0.75;
    double nr = radius * 0.25;

    Set<BlockPos> brimstone = Sets.newHashSet();
    BlockState rock = LighterEndBlocks.BORNITE.baseBlock.defaultBlockState();
    int waterLevel = pos.getY()
        + Mth.nextInt(random, Mth.floor(radius * 0.8), radius);
    for (int x = x1; x <= x2; x++) {
      int xsq = x - pos.getX();
      xsq *= xsq;
      mut.setX(x);
      for (int z = z1; z <= z2; z++) {
        int zsq = z - pos.getZ();
        zsq *= zsq;
        mut.setZ(z);
        for (int y = y1; y <= y2; y++) {
          int ysq = y - pos.getY();
          ysq *= 1.6;
          ysq *= ysq;
          mut.setY(y);
          double r = noise.eval(x * 0.1, y * 0.1, z * 0.1) * nr + hr;
          double r2 = r + 5;
          double dist = xsq + ysq + zsq;
          if (dist < r * r) {
            state = world.getBlockState(mut);
            if (isReplaceable(state)) {
              world.setBlock(
                  mut,
                  y < waterLevel ? Blocks.WATER.defaultBlockState()
                      : Blocks.CAVE_AIR.defaultBlockState(),
                  Flags.SILENT
              );
            }
          } else if (dist < r2 * r2) {
            state = world.getBlockState(mut);
            if (state.is(LighterEndTags.END_STONES) || state.is(Blocks.AIR)) {
              double v = noise.eval(x * 0.1, y * 0.1, z * 0.1) + noise.eval(
                  x * 0.03,
                  y * 0.03,
                  z * 0.03
              ) * 0.5;
              if (v > 0.4) {
                brimstone.add(mut.immutable());
              } else {
                world.setBlock(mut, rock, Flags.SILENT);
              }
            }
          }
        }
      }
    }
    brimstone.forEach((blockPos) -> {
      SulphurLake.placeBrimstone(world, blockPos, random);
    });

    if (random.nextInt(4) == 0) {
      int count = Mth.nextInt(random, 5, 20);
      for (int i = 0; i < count; i++) {
        mut.set(pos)
            .move(
                Mth.floor(random.nextGaussian() * 2 + 0.5),
                0,
                Mth.floor(random.nextGaussian() * 2 + 0.5)
            );
        int dist = Mth.floor(
            3 - Math.sqrt(
                Math.pow(mut.getX() - pos.getX(), 2) + Math.pow(mut.getZ() - pos.getZ(), 2))
        ) + random.nextInt(2);
        if (dist > 0) {
          state = world.getBlockState(mut);
          while (
              !state.getFluidState().isEmpty() || state.is(LighterEndTags.AQUATIC_END_VEGETATION)
          ) {
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
              for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(random)) {
                BlockPos p = mut.relative(dir);
                if (random.nextBoolean() && world.getBlockState(p).is(Blocks.WATER)) {
                  world.setBlock(
                      p,
                      LighterEndBlocks.TUBE_WORM.defaultBlockState().setValue(TubeWorm.FACING, dir),
                      Flags.SILENT
                  );
                }
              }
              mut.setY(mut.getY() + 1);
            }
            world.setBlock(
                mut,
                LighterEndBlocks.HYDROTHERMAL_VENT.defaultBlockState()
                    .setValue(HydrothermalVent.ACTIVATED, false)
                    .setValue(HydrothermalVent.WATERLOGGED, true),
                Flags.SILENT
            );
            mut.setY(mut.getY() + 1);
            state = world.getBlockState(mut);
            while (state.is(Blocks.WATER)) {
              world.setBlock(
                  mut,
                  LighterEndBlocks.VENT_BUBBLE_COLUMN.defaultBlockState(),
                  Flags.SILENT
              );
              world.createTick(
                  mut.immutable(),
                  LighterEndBlocks.VENT_BUBBLE_COLUMN,
                  Mth.nextInt(random, 8, 32)
              );
              mut.setY(mut.getY() + 1);
              state = world.getBlockState(mut);
            }
          }
        }
      }
    }

    BlockFixer.fixBlocks(world, new BlockPos(x1, y1, z1), new BlockPos(x2, y2, z2));

    return true;
  }

  private static boolean isReplaceable(BlockState state) {
    return state.is(LighterEndTags.END_STONES)
        || state.is(LighterEndBlocks.HYDROTHERMAL_VENT)
        || state.is(LighterEndBlocks.VENT_BUBBLE_COLUMN)
        || state.is(LighterEndBlocks.SULPHUR_CRYSTAL)
        || MiscUtils.replaceableOrPlant(state)
        || state.is(LighterEndTags.AQUATIC_END_VEGETATION)
        || state.is(BlockTags.LEAVES);
  }

}
