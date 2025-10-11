package io.github.openbagtwo.lighterend.world.features;

import static net.minecraft.world.Heightmap.Type;

import com.google.common.collect.Sets;
import io.github.openbagtwo.lighterend.blocks.Brimstone;
import io.github.openbagtwo.lighterend.blocks.SulphurCrystal;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.github.openbagtwo.lighterend.utils.Flags;
import io.github.openbagtwo.lighterend.utils.GlobalState;
import io.github.openbagtwo.lighterend.world.gen.noise.OpenSimplexNoise;
import java.util.Set;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SulphurLake extends Feature<DefaultFeatureConfig> {

  private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(15152);

  public SulphurLake() {
    super(DefaultFeatureConfig.CODEC);
  }

  @Override
  public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
    BlockPos blockPos = context.getOrigin();
    final StructureWorldAccess world = context.getWorld();
    blockPos = world.getTopPosition(Type.WORLD_SURFACE_WG, blockPos);

    if (blockPos.getY() < 57) {
      return false;
    }

    final Random random = context.getRandom();
    final Mutable POS = GlobalState.stateForThread().POS;
    double radius = MathHelper.nextDouble(random, 10., 20.);
    int dist2 = MathHelper.floor(radius * 1.5);

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
          POS.setY(world.getTopY(Type.WORLD_SURFACE_WG, x, z) - 1);
          if (world.getBlockState(POS).isIn(LighterEndTags.END_STONES)) {
            if (isBorder(world, POS)) {
              if (random.nextInt(8) > 0) {
                brimstone.add(POS.toImmutable());
                if (random.nextBoolean()) {
                  brimstone.add(POS.down());
                  if (random.nextBoolean()) {
                    brimstone.add(POS.down(2));
                  }
                }
              } else {
                if (!isAbsoluteBorder(world, POS)) {
                  world.setBlockState(POS, Blocks.WATER.getDefaultState(), Flags.SILENT);

                  world.scheduleFluidTick(POS, Fluids.WATER, 0);
                  brimstone.add(POS.down());
                  if (random.nextBoolean()) {
                    brimstone.add(POS.down(2));
                    if (random.nextBoolean()) {
                      brimstone.add(POS.down(3));
                    }
                  }
                } else {
                  brimstone.add(POS.toImmutable());
                  if (random.nextBoolean()) {
                    brimstone.add(POS.down());
                  }
                }
              }
            } else {
              world.setBlockState(POS, Blocks.WATER.getDefaultState(), Flags.SILENT);
              brimstone.remove(POS);
              for (Direction dir : Direction.Type.HORIZONTAL) {
                BlockPos offsetted = POS.offset(dir);
                if (world.getBlockState(offsetted).isIn(LighterEndTags.END_STONES)) {
                  brimstone.add(offsetted);
                }
              }
              if (isDeepWater(world, POS)) {
                world.setBlockState(POS.move(Direction.DOWN), Blocks.WATER.getDefaultState(),
                    Flags.SILENT);
                brimstone.remove(POS);
                for (Direction dir : Direction.Type.HORIZONTAL) {
                  BlockPos offseted = POS.offset(dir);
                  if (world.getBlockState(offseted).isIn(LighterEndTags.END_STONES)) {
                    brimstone.add(offseted);
                  }
                }
              }
              brimstone.add(POS.down());
              if (random.nextBoolean()) {
                brimstone.add(POS.down(2));
                if (random.nextBoolean()) {
                  brimstone.add(POS.down(3));
                }
              }
            }
          }
        } else if (dist < r2) {
          POS.setY(world.getTopY(Type.WORLD_SURFACE_WG, x, z) - 1);
          if (world.getBlockState(POS).isIn(LighterEndTags.END_STONES)) {
            brimstone.add(POS.toImmutable());
            if (random.nextBoolean()) {
              brimstone.add(POS.down());
              if (random.nextBoolean()) {
                brimstone.add(POS.down(2));
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

  private boolean isBorder(StructureWorldAccess world, BlockPos pos) {
    int y = pos.getY() + 1;
    for (Direction dir : Direction.values()) {
      if (world.getTopY(
          Type.WORLD_SURFACE_WG,
          pos.getX() + dir.getOffsetX(),
          pos.getZ() + dir.getOffsetZ()
      ) < y) {
        return true;
      }
    }
    return false;
  }

  private boolean isAbsoluteBorder(StructureWorldAccess world, BlockPos pos) {
    int y = pos.getY() - 2;
    for (Direction dir : Direction.values()) {
      if (world.getTopY(
          Type.WORLD_SURFACE_WG,
          pos.getX() + dir.getOffsetX() * 3,
          pos.getZ() + dir.getOffsetZ() * 3
      ) < y) {
        return true;
      }
    }
    return false;
  }

  private boolean isDeepWater(StructureWorldAccess world, BlockPos pos) {
    int y = pos.getY() + 1;
    for (Direction dir : Direction.values()) {
      if (world.getTopY(
          Type.WORLD_SURFACE_WG,
          pos.getX() + dir.getOffsetX(),
          pos.getZ() + dir.getOffsetZ()
      ) < y
          || world.getTopY(
          Type.WORLD_SURFACE_WG,
          pos.getX() + dir.getOffsetX() * 2,
          pos.getZ() + dir.getOffsetZ() * 2
      ) < y || world.getTopY(
          Type.WORLD_SURFACE_WG,
          pos.getX() + dir.getOffsetX() * 3,
          pos.getZ() + dir.getOffsetZ() * 3
      ) < y) {
        return false;
      }
    }
    return true;
  }

  private void placeBrimstone(StructureWorldAccess world, BlockPos pos, Random random) {
    BlockState state = getBrimstone(world, pos);
    world.setBlockState(pos, state, Flags.SILENT);
    if (state.get(Brimstone.ACTIVATED)) {
      makeShards(world, pos, random);
    }
  }

  private BlockState getBrimstone(StructureWorldAccess world, BlockPos pos) {
    for (Direction dir : Direction.values()) {
      if (world.getBlockState(pos.offset(dir)).isOf(Blocks.WATER)) {
        return LighterEndBlocks.BRIMSTONE.getDefaultState().with(Brimstone.ACTIVATED, true);
      }
    }
    return LighterEndBlocks.BRIMSTONE.getDefaultState();
  }

  private void makeShards(StructureWorldAccess world, BlockPos pos, Random random) {
    for (Direction dir : Direction.values()) {
      BlockPos side;
      if (random.nextInt(16) == 0 && world.getBlockState((side = pos.offset(dir)))
          .isOf(Blocks.WATER)) {
        BlockState state = LighterEndBlocks.SULPHUR_CRYSTAL.getDefaultState()
            .with(SulphurCrystal.WATERLOGGED, true)
            .with(SulphurCrystal.FACING, dir)
            .with(SulphurCrystal.STAGE, random.nextInt(3));
        world.setBlockState(side, state, Flags.SILENT);
      }
    }
  }
}
