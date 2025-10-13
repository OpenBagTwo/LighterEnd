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
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SulphurCave extends Feature<DefaultFeatureConfig> {

  public SulphurCave() {
    super(DefaultFeatureConfig.CODEC);
  }

  @Override
  public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
    final Random random = context.getRandom();
    BlockPos pos = context.getOrigin();
    final StructureWorldAccess world = context.getWorld();
    int radius = MathHelper.nextInt(random, 10, 30);

    int top = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, pos.getX(), pos.getZ());
    Mutable bpos = new Mutable();
    bpos.setX(pos.getX());
    bpos.setZ(pos.getZ());
    bpos.setY(top - 1);

    BlockState state = world.getBlockState(bpos);
    while (!state.isIn(LighterEndTags.END_STONES) && bpos.getY() > 5) {
      bpos.setY(bpos.getY() - 1);
      state = world.getBlockState(bpos);
    }
    if (bpos.getY() < 10) {
      return false;
    }
    top = (int) (bpos.getY() - (radius * 1.3F + 5));

    while (state.isIn(LighterEndTags.END_STONES)
        || !state.getFluidState().isEmpty() && bpos.getY() > 5) {
      bpos.setY(bpos.getY() - 1);
      state = world.getBlockState(bpos);
    }
    int bottom = (int) (bpos.getY() + radius * 1.3F + 5);

    if (top <= bottom) {
      return false;
    }

    Mutable mut = new Mutable();
    pos = new BlockPos(pos.getX(), MathHelper.nextInt(random, bottom, top), pos.getZ());

    OpenSimplexNoise noise = new OpenSimplexNoise(MathUtils.getSeed(534, pos.getX(), pos.getZ()));

    int x1 = pos.getX() - radius - 5;
    int z1 = pos.getZ() - radius - 5;
    int x2 = pos.getX() + radius + 5;
    int z2 = pos.getZ() + radius + 5;
    int y1 = MathHelper.floor(pos.getY() - (radius + 5) / 1.6);
    int y2 = MathHelper.floor(pos.getY() + (radius + 5) / 1.6);

    double hr = radius * 0.75;
    double nr = radius * 0.25;

    Set<BlockPos> brimstone = Sets.newHashSet();
    BlockState rock = LighterEndBlocks.BORNITE.baseBlock.getDefaultState();
    int waterLevel = pos.getY()
        + MathHelper.nextInt(random, MathHelper.floor(radius * 0.8), radius);
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
              world.setBlockState(
                  mut,
                  y < waterLevel ? Blocks.WATER.getDefaultState()
                      : Blocks.CAVE_AIR.getDefaultState(),
                  Flags.SILENT
              );
            }
          } else if (dist < r2 * r2) {
            state = world.getBlockState(mut);
            if (state.isIn(LighterEndTags.END_STONES) || state.isOf(Blocks.AIR)) {
              double v = noise.eval(x * 0.1, y * 0.1, z * 0.1) + noise.eval(
                  x * 0.03,
                  y * 0.03,
                  z * 0.03
              ) * 0.5;
              if (v > 0.4) {
                brimstone.add(mut.toImmutable());
              } else {
                world.setBlockState(mut, rock, Flags.SILENT);
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
      int count = MathHelper.nextInt(random, 5, 20);
      for (int i = 0; i < count; i++) {
        mut.set(pos)
            .move(
                MathHelper.floor(random.nextGaussian() * 2 + 0.5),
                0,
                MathHelper.floor(random.nextGaussian() * 2 + 0.5)
            );
        int dist = MathHelper.floor(
            3 - Math.sqrt(
                Math.pow(mut.getX() - pos.getX(), 2) + Math.pow(mut.getZ() - pos.getZ(), 2))
        ) + random.nextInt(2);
        if (dist > 0) {
          state = world.getBlockState(mut);
          while (
              !state.getFluidState().isEmpty() || state.isIn(LighterEndTags.AQUATIC_END_VEGETATION)
          ) {
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
              for (Direction dir : Direction.Type.HORIZONTAL.getShuffled(random)) {
                BlockPos p = mut.offset(dir);
                if (random.nextBoolean() && world.getBlockState(p).isOf(Blocks.WATER)) {
                  world.setBlockState(
                      p,
                      LighterEndBlocks.TUBE_WORM.getDefaultState().with(TubeWorm.FACING, dir),
                      Flags.SILENT
                  );
                }
              }
              mut.setY(mut.getY() + 1);
            }
            world.setBlockState(
                mut,
                LighterEndBlocks.HYDROTHERMAL_VENT.getDefaultState()
                    .with(HydrothermalVent.WATERLOGGED, true),
                Flags.SILENT
            );
            mut.setY(mut.getY() + 1);
            state = world.getBlockState(mut);
            while (state.isOf(Blocks.WATER)) {
              world.setBlockState(
                  mut,
                  LighterEndBlocks.VENT_BUBBLE_COLUMN.getDefaultState(),
                  Flags.SILENT
              );
              world.createOrderedTick(
                  mut.toImmutable(),
                  LighterEndBlocks.VENT_BUBBLE_COLUMN,
                  MathHelper.nextInt(random, 8, 32)
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
    return state.isIn(LighterEndTags.END_STONES)
        || state.isOf(LighterEndBlocks.HYDROTHERMAL_VENT)
        || state.isOf(LighterEndBlocks.VENT_BUBBLE_COLUMN)
        || state.isOf(LighterEndBlocks.SULPHUR_CRYSTAL)
        || MiscUtils.replaceableOrPlant(state)
        || state.isIn(LighterEndTags.AQUATIC_END_VEGETATION)
        || state.isIn(BlockTags.LEAVES);
  }

}
