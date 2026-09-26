package io.github.openbagtwo.lighterend.world.features;

import static net.minecraft.world.level.levelgen.Heightmap.Types;

import com.google.common.collect.Sets;
import io.github.openbagtwo.lighterend.blocks.Brimstone;
import io.github.openbagtwo.lighterend.blocks.SulphurCrystal;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.github.openbagtwo.lighterend.utils.Flags;
import io.github.openbagtwo.lighterend.utils.GlobalState;
import io.github.openbagtwo.lighterend.world.gen.noise.OpenSimplexNoise;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;

public class SulphurLake extends Feature<NoneFeatureConfiguration> {

  private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(15152);

  public SulphurLake() {
    super(NoneFeatureConfiguration.CODEC);
  }

  @Override
  public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
    BlockPos blockPos = context.origin();
    final WorldGenLevel world = context.level();
    blockPos = world.getHeightmapPos(Types.WORLD_SURFACE_WG, blockPos);

    if (blockPos.getY() < 57) {
      return false;
    }

    final RandomSource random = context.random();
    final MutableBlockPos POS = GlobalState.stateForThread().POS;
    double radius = Mth.nextDouble(random, 10., 20.);
    int dist2 = Mth.floor(radius * 1.5);

    int minX = blockPos.getX() - dist2;
    int maxX = blockPos.getX() + dist2;
    int minZ = blockPos.getZ() - dist2;
    int maxZ = blockPos.getZ() + dist2;

    Set<BlockPos> brimstone = Sets.newHashSet();
    for (int x = minX; x <= maxX; x++) {
      POS.setX(x);
      int x2 = x - blockPos.getX();
      x2 *= x2;
      for (int z = minZ; z <= maxZ; z++) {
        POS.setZ(z);
        int z2 = z - blockPos.getZ();
        z2 *= z2;
        double r = radius * (NOISE.eval(x * 0.2, z * 0.2) * 0.25 + 0.75);
        double r2 = r * 1.5;
        r *= r;
        r2 *= r2;
        int dist = x2 + z2;
        if (dist <= r) {
          POS.setY(world.getHeight(Types.WORLD_SURFACE_WG, x, z) - 1);
          if (world.getBlockState(POS).is(LighterEndTags.END_STONES)) {
            if (isBorder(world, POS)) {
              if (random.nextInt(8) > 0) {
                brimstone.add(POS.immutable());
                if (random.nextBoolean()) {
                  brimstone.add(POS.below());
                  if (random.nextBoolean()) {
                    brimstone.add(POS.below(2));
                  }
                }
              } else {
                if (!isAbsoluteBorder(world, POS)) {
                  world.setBlock(POS, Blocks.WATER.defaultBlockState(), Flags.SILENT);

                  world.scheduleTick(POS, Fluids.WATER, 0);
                  brimstone.add(POS.below());
                  if (random.nextBoolean()) {
                    brimstone.add(POS.below(2));
                    if (random.nextBoolean()) {
                      brimstone.add(POS.below(3));
                    }
                  }
                } else {
                  brimstone.add(POS.immutable());
                  if (random.nextBoolean()) {
                    brimstone.add(POS.below());
                  }
                }
              }
            } else {
              world.setBlock(POS, Blocks.WATER.defaultBlockState(), Flags.SILENT);
              brimstone.remove(POS);
              for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos offsetted = POS.relative(dir);
                if (world.getBlockState(offsetted).is(LighterEndTags.END_STONES)) {
                  brimstone.add(offsetted);
                }
              }
              if (isDeepWater(world, POS)) {
                world.setBlock(POS.move(Direction.DOWN), Blocks.WATER.defaultBlockState(),
                    Flags.SILENT);
                brimstone.remove(POS);
                for (Direction dir : Direction.Plane.HORIZONTAL) {
                  BlockPos offseted = POS.relative(dir);
                  if (world.getBlockState(offseted).is(LighterEndTags.END_STONES)) {
                    brimstone.add(offseted);
                  }
                }
              }
              brimstone.add(POS.below());
              if (random.nextBoolean()) {
                brimstone.add(POS.below(2));
                if (random.nextBoolean()) {
                  brimstone.add(POS.below(3));
                }
              }
            }
          }
        } else if (dist < r2) {
          POS.setY(world.getHeight(Types.WORLD_SURFACE_WG, x, z) - 1);
          if (world.getBlockState(POS).is(LighterEndTags.END_STONES)) {
            brimstone.add(POS.immutable());
            if (random.nextBoolean()) {
              brimstone.add(POS.below());
              if (random.nextBoolean()) {
                brimstone.add(POS.below(2));
              }
            }
          }
        }
      }
    }

    brimstone.forEach((bpos) -> {
      placeBrimstone(world, bpos, random);
    });

    return true;
  }

  protected static boolean isBorder(WorldGenLevel world, BlockPos pos) {
    int y = pos.getY() + 1;
    for (Direction dir : Direction.values()) {
      if (world.getHeight(
          Types.WORLD_SURFACE_WG,
          pos.getX() + dir.getStepX(),
          pos.getZ() + dir.getStepZ()
      ) < y) {
        return true;
      }
    }
    return false;
  }

  protected static boolean isAbsoluteBorder(WorldGenLevel world, BlockPos pos) {
    int y = pos.getY() - 2;
    for (Direction dir : Direction.values()) {
      if (world.getHeight(
          Types.WORLD_SURFACE_WG,
          pos.getX() + dir.getStepX() * 3,
          pos.getZ() + dir.getStepZ() * 3
      ) < y) {
        return true;
      }
    }
    return false;
  }

  protected static boolean isDeepWater(WorldGenLevel world, BlockPos pos) {
    int y = pos.getY() + 1;
    for (Direction dir : Direction.values()) {
      if (world.getHeight(
          Types.WORLD_SURFACE_WG,
          pos.getX() + dir.getStepX(),
          pos.getZ() + dir.getStepZ()
      ) < y
          || world.getHeight(
          Types.WORLD_SURFACE_WG,
          pos.getX() + dir.getStepX() * 2,
          pos.getZ() + dir.getStepZ() * 2
      ) < y || world.getHeight(
          Types.WORLD_SURFACE_WG,
          pos.getX() + dir.getStepX() * 3,
          pos.getZ() + dir.getStepZ() * 3
      ) < y) {
        return false;
      }
    }
    return true;
  }

  protected static void placeBrimstone(WorldGenLevel world, BlockPos pos, RandomSource random) {
    BlockState state = getBrimstone(world, pos);
    world.setBlock(pos, state, Flags.SILENT);
    if (state.getValue(Brimstone.ACTIVATED)) {
      makeShards(world, pos, random);
    }
  }

  protected static BlockState getBrimstone(WorldGenLevel world, BlockPos pos) {
    for (Direction dir : Direction.values()) {
      if (world.getBlockState(pos.relative(dir)).is(Blocks.WATER)) {
        return LighterEndBlocks.BRIMSTONE.defaultBlockState().setValue(Brimstone.ACTIVATED, true);
      }
    }
    return LighterEndBlocks.BRIMSTONE.defaultBlockState();
  }

  protected static void makeShards(WorldGenLevel world, BlockPos pos, RandomSource random) {
    for (Direction dir : Direction.values()) {
      BlockPos side;
      if (random.nextInt(16) == 0 && world.getBlockState((side = pos.relative(dir)))
          .is(Blocks.WATER)) {
        BlockState state = LighterEndBlocks.SULPHUR_CRYSTAL.defaultBlockState()
            .setValue(SulphurCrystal.WATERLOGGED, true)
            .setValue(SulphurCrystal.FACING, dir)
            .setValue(SulphurCrystal.STAGE, random.nextInt(3));
        world.setBlock(side, state, Flags.SILENT);
      }
    }
  }
}
