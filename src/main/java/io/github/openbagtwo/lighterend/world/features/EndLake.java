package io.github.openbagtwo.lighterend.world.features;

import io.github.openbagtwo.lighterend.tags.LighterEndTags;
import io.github.openbagtwo.lighterend.utils.Flags;
import io.github.openbagtwo.lighterend.utils.GlobalState;
import io.github.openbagtwo.lighterend.utils.MiscUtils;
import io.github.openbagtwo.lighterend.utils.PosInfo;
import io.github.openbagtwo.lighterend.world.gen.noise.OpenSimplexNoise;
import java.util.stream.IntStream;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;


public class EndLake extends Feature<DefaultFeatureConfig> {

  private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(15152);

  private static final BlockState END_STONE = Blocks.END_STONE.getDefaultState();
  private static final BlockState AIR = Blocks.AIR.getDefaultState();
  private static final BlockState WATER = Blocks.WATER.getDefaultState();
  private static final BlockState SAND = Blocks.SAND.getDefaultState();


  public EndLake() {
    super(DefaultFeatureConfig.CODEC);
  }

  @Override
  public boolean generate(FeatureContext<DefaultFeatureConfig> featureConfig) {
    final Mutable POS = GlobalState.stateForThread().POS;
    final Random random = featureConfig.getRandom();
    BlockPos blockPos = featureConfig.getOrigin();
    final StructureWorldAccess world = featureConfig.getWorld();
    double radius = MathHelper.nextDouble(random, 10.0, 20.0);
    double depth = radius * 0.5 * MathHelper.nextDouble(random, 0.8, 1.2);
    int dist = MathHelper.floor(radius);
    int dist2 = MathHelper.floor(radius * 1.5);
    int bott = MathHelper.floor(depth);
    blockPos = getPosOnSurfaceWG(world, blockPos);

    if (blockPos.getY() < 10) {
      return false;
    }

    int waterLevel = blockPos.getY();

    BlockPos pos = getPosOnSurfaceRaycast(world, blockPos.north(dist).up(10), 20);
    if (Math.abs(blockPos.getY() - pos.getY()) > 5) {
      return false;
    }
    waterLevel = Math.min(pos.getY(), waterLevel);

    pos = getPosOnSurfaceRaycast(world, blockPos.south(dist).up(10), 20);
    if (Math.abs(blockPos.getY() - pos.getY()) > 5) {
      return false;
    }
    waterLevel = Math.min(pos.getY(), waterLevel);

    pos = getPosOnSurfaceRaycast(world, blockPos.east(dist).up(10), 20);
    if (Math.abs(blockPos.getY() - pos.getY()) > 5) {
      return false;
    }
    waterLevel = Math.min(pos.getY(), waterLevel);

    pos = getPosOnSurfaceRaycast(world, blockPos.west(dist).up(10), 20);
    if (Math.abs(blockPos.getY() - pos.getY()) > 5) {
      return false;
    }
    waterLevel = Math.min(pos.getY(), waterLevel);
    BlockState state;

    int minX = blockPos.getX() - dist2;
    int maxX = blockPos.getX() + dist2;
    int minZ = blockPos.getZ() - dist2;
    int maxZ = blockPos.getZ() + dist2;
    int maskMinX = minX - 1;
    int maskMinZ = minZ - 1;

    boolean[][] mask = new boolean[maxX - minX + 3][maxZ - minZ + 3];
    for (int x = minX; x <= maxX; x++) {
      POS.setX(x);
      int mx = x - maskMinX;
      for (int z = minZ; z <= maxZ; z++) {
        POS.setZ(z);
        int mz = z - maskMinZ;
        if (!mask[mx][mz]) {
          for (int y = waterLevel + 1; y <= waterLevel + 20; y++) {
            POS.setY(y);
            FluidState fluid = world.getFluidState(POS);
            if (!fluid.isEmpty()) {
              for (int i = -1; i < 2; i++) {
                int px = mx + i;
                for (int j = -1; j < 2; j++) {
                  int pz = mz + j;
                  mask[px][pz] = true;
                }
              }
              break;
            }
          }
        }
      }
    }

    for (int x = minX; x <= maxX; x++) {
      POS.setX(x);
      int x2 = x - blockPos.getX();
      x2 *= x2;
      int mx = x - maskMinX;
      for (int z = minZ; z <= maxZ; z++) {
        POS.setZ(z);
        int z2 = z - blockPos.getZ();
        z2 *= z2;
        int mz = z - maskMinZ;
        if (!mask[mx][mz]) {
          double size = 1;
          for (int y = blockPos.getY(); y <= blockPos.getY() + 20; y++) {
            POS.setY(y);
            double add = y - blockPos.getY();
            if (add > 5) {
              size *= 0.8;
              add = 5;
            }
            double r = (add * 1.8 + radius * (NOISE.eval(
                x * 0.2,
                y * 0.2,
                z * 0.2
            ) * 0.25 + 0.75)) - 1.0 / size;
            if (r > 0) {
              r *= r;
              if (x2 + z2 <= r) {
                state = world.getBlockState(POS);
                if (state.isIn(LighterEndTags.END_STONES)) {
                  world.setBlockState(POS, AIR, Flags.SILENT);
                }
                pos = POS.down();
                if (world.getBlockState(pos).isIn(LighterEndTags.END_STONES)) {
                  state = END_STONE;
                  if (y > waterLevel + 1) {
                    world.setBlockState(pos, state, Flags.SILENT);
                  } else if (y > waterLevel) {
                    world.setBlockState(
                        pos,
                        random.nextBoolean() ? state : SAND,
                        Flags.SILENT
                    );
                  } else {
                    world.setBlockState(pos, SAND, Flags.SILENT);
                  }
                }
              }
            } else {
              break;
            }
          }
        }
      }
    }

    double aspect = (radius / depth);

    for (int x = blockPos.getX() - dist; x <= blockPos.getX() + dist; x++) {
      POS.setX(x);
      int x2 = x - blockPos.getX();
      x2 *= x2;
      int mx = x - maskMinX;
      for (int z = blockPos.getZ() - dist; z <= blockPos.getZ() + dist; z++) {
        POS.setZ(z);
        int z2 = z - blockPos.getZ();
        z2 *= z2;
        int mz = z - maskMinZ;
        if (!mask[mx][mz]) {
          for (int y = blockPos.getY() - bott; y < blockPos.getY(); y++) {
            POS.setY(y);
            double y2 = (double) (y - blockPos.getY()) * aspect;
            y2 *= y2;
            double r = radius * (NOISE.eval(x * 0.2, y * 0.2, z * 0.2) * 0.25 + 0.75);
            double rb = r * 1.2;
            r *= r;
            rb *= rb;
            if (y2 + x2 + z2 <= r) {
              state = world.getBlockState(POS);
              if (canReplace(state)) {
                state = world.getBlockState(POS.up());
                state = canReplace(state) ? (y < waterLevel ? WATER : AIR) : state;
                world.setBlockState(POS, state, Flags.SILENT);
              }
              pos = POS.down();
              if (world.getBlockState(pos).isIn(LighterEndTags.END_STONES)) {
                world.setBlockState(pos, SAND, Flags.SILENT);
              }
              pos = POS.up();
              while (canReplace(state = world.getBlockState(pos)) && !state.isAir() && state
                  .getFluidState()
                  .isEmpty()) {
                world.setBlockState(pos, pos.getY() < waterLevel ? WATER : AIR, Flags.SILENT);
                pos = pos.up();
              }
            }
            // Make border
            else if (y < waterLevel && y2 + x2 + z2 <= rb) {
              if (world.isAir(POS.up())) {
                state = END_STONE;
                world.setBlockState(
                    POS,
                    random.nextBoolean() ? state : SAND,
                    Flags.SILENT
                );
                world.setBlockState(POS.down(), END_STONE, Flags.SILENT);
              } else {
                world.setBlockState(POS, SAND, Flags.SILENT);
                world.setBlockState(POS.down(), END_STONE, Flags.SILENT);
              }
            }
          }
        }
      }
    }

    fixBlocks(
        world,
        new BlockPos(minX - 2, waterLevel - 2, minZ - 2),
        new BlockPos(maxX + 2, blockPos.getY() + 20, maxZ + 2)
    );

    return true;
  }

  private boolean canReplace(BlockState state) {
    return state.isIn(LighterEndTags.END_STONES)
        || state.isOf(Blocks.SAND)
        || MiscUtils.replaceableOrPlant(state);
  }

  public static void fixBlocks(WorldAccess level, BlockPos start, BlockPos end) {

    final int dx = end.getX() - start.getX() + 1;
    final int dz = end.getZ() - start.getZ() + 1;
    final int count = dx * dz;
    final int minY = Math.max(start.getY(), level.getBottomY());
    final int maxY = Math.min(end.getY(), level.getTopYInclusive());
    IntStream.range(0, count).forEach(index -> {
      Mutable POS = new Mutable();
      POS.setX((index % dx) + start.getX());
      POS.setZ((index / dx) + start.getZ());
      BlockState state;
      for (int y = minY; y <= maxY; y++) {
        POS.setY(y);
        state = level.getBlockState(POS);

        if (!state.getFluidState().isEmpty()) {
          if (!state.canPlaceAt(level, POS)) {
            level.setBlockState(POS, WATER, Flags.SILENT);
            POS.setY(POS.getY() - 1);
            state = level.getBlockState(POS);
            while (!state.canPlaceAt(level, POS)) {
              state = state.getFluidState().isEmpty() ? AIR : WATER;
              level.setBlockState(POS, state, Flags.SILENT);
              POS.setY(POS.getY() - 1);
              state = level.getBlockState(POS);
            }
          }
          POS.setY(y - 1);
          if (level.isAir(POS)) {
            POS.setY(y);
            while (!level.getFluidState(POS).isEmpty()) {
              level.setBlockState(POS, AIR, Flags.SILENT);
              POS.setY(POS.getY() + 1);
            }
            continue;
          }
          for (Direction dir : PosInfo.HORIZONTAL) {
            if (level.isAir(POS.offset(dir))) {
              try {
                level.scheduleFluidTick(POS, state.getFluidState().getFluid(), 0);
              } catch (Exception e) {
              }
              break;
            }
          }
        } else if (state.getBlock() instanceof FallingBlock) {
          BlockState falling = state;

          POS.setY(POS.getY() - 1);

          int ray = downRayRep(level, POS.toImmutable(), 64);
          if (ray > 32) {
            level.setBlockState(POS, END_STONE, Flags.SILENT);
            if (level.getRandom().nextBoolean()) {
              POS.setY(POS.getY() - 1);
              level.setBlockState(POS, END_STONE, Flags.SILENT);
            }
          } else {
            POS.setY(y);
            BlockState replacement = AIR;
            for (Direction dir : PosInfo.HORIZONTAL) {
              state = level.getBlockState(POS.offset(dir));
              if (!state.getFluidState().isEmpty()) {
                replacement = state;
                break;
              }
            }
            level.setBlockState(POS, replacement, Flags.SILENT);
            POS.setY(y - ray);
            level.setBlockState(POS, falling, Flags.SILENT);
          }
        } else {
          level.setBlockState(
              POS,
              state.getFluidState().isEmpty() ? AIR : state.getFluidState().getBlockState(),
              Flags.SILENT
          );
        }
      }
    });
  }

  // Utils (TODO: refactor out as needed)

  private static final ThreadLocal<Mutable> TL_POS = ThreadLocal.withInitial(() -> new Mutable());

  private static BlockPos getPosOnSurfaceWG(StructureWorldAccess world, BlockPos pos) {
    return world.getTopPosition(Type.WORLD_SURFACE_WG, pos);
  }

  private static BlockPos getPosOnSurfaceRaycast(StructureWorldAccess world, BlockPos pos,
      int dist) {
    int h = PosInfo.downRay(world, pos, dist);
    return pos.down(h);
  }

  private static int downRayRep(WorldAccess world, BlockPos pos, int maxDist) {
    final Mutable POS = TL_POS.get();
    POS.set(pos);
    for (int j = 1; j < maxDist && (world.getBlockState(POS)).isReplaceable(); j++) {
      POS.setY(POS.getY() - 1);
    }
    return pos.getY() - POS.getY();
  }
}
