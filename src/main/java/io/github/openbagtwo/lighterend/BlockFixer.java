package io.github.openbagtwo.lighterend;

import com.google.common.collect.Sets;
import io.github.openbagtwo.lighterend.blocks.Fur;
import io.github.openbagtwo.lighterend.utils.Flags;
import io.github.openbagtwo.lighterend.utils.PosInfo;
import java.util.Set;
import java.util.stream.IntStream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;

public class BlockFixer {

  private static final BlockState AIR = Blocks.AIR.defaultBlockState();
  private static final BlockState WATER = Blocks.WATER.defaultBlockState();

  public static void fixBlocks(LevelAccessor level, BlockPos start, BlockPos end) {
    final Registry<DimensionType> registry = level.registryAccess()
        .lookupOrThrow(Registries.DIMENSION_TYPE);
    final Identifier dimKey = registry.getKey(level.dimensionType());
    if (dimKey != null && "world_blender".equals(dimKey.getNamespace())) {
      return;
    }
    final Set<BlockPos> doubleCheck = Sets.newConcurrentHashSet();
    final int dx = end.getX() - start.getX() + 1;
    final int dz = end.getZ() - start.getZ() + 1;
    final int count = dx * dz;
    final int minY = Math.max(start.getY(), level.getMinY());
    final int maxY = Math.min(end.getY(), level.getMaxY());
    IntStream.range(0, count).forEach(index -> {
      MutableBlockPos POS = new MutableBlockPos();
      POS.setX((index % dx) + start.getX());
      POS.setZ((index / dx) + start.getZ());
      BlockState state;
      for (int y = minY; y <= maxY; y++) {
        POS.setY(y);
        state = level.getBlockState(POS);

        if (state.getBlock() instanceof Fur) {
          doubleCheck.add(POS.immutable());
        }
        // Liquids
        else if (!state.getFluidState().isEmpty()) {
          if (!state.canSurvive(level, POS)) {
            setWithoutUpdate(level, POS, WATER);
            POS.setY(POS.getY() - 1);
            state = level.getBlockState(POS);
            while (!state.canSurvive(level, POS)) {
              state = state.getFluidState().isEmpty() ? AIR : WATER;
              setWithoutUpdate(level, POS, state);
              POS.setY(POS.getY() - 1);
              state = level.getBlockState(POS);
            }
          }
          POS.setY(y - 1);
          if (level.isEmptyBlock(POS)) {
            POS.setY(y);
            while (!level.getFluidState(POS).isEmpty()) {
              setWithoutUpdate(level, POS, AIR);
              POS.setY(POS.getY() + 1);
            }
            continue;
          }
          for (Direction dir : Direction.Plane.HORIZONTAL) {
            if (level.isEmptyBlock(POS.relative(dir))) {
              try {
                level.createTick(POS, state.getFluidState().getType(), 0);
              } catch (Exception e) {
              }
              break;
            }
          }
//        } else if (state.isOf(EndBlocks.SMARAGDANT_CRYSTAL) || state.isOf(
//            EndBlocks.BUDDING_SMARAGDANT_CRYSTAL)) {
//          POS.setY(POS.getY() - 1);
//          if (level.isAir(POS)) {
//            POS.setY(POS.getY() + 1);
//            while (state.isOf(EndBlocks.SMARAGDANT_CRYSTAL) || state.isOf(
//                EndBlocks.BUDDING_SMARAGDANT_CRYSTAL)) {
//              setWithoutUpdate(level, POS, AIR);
//              POS.setY(POS.getY() + 1);
//              state = level.getBlockState(POS);
//            }
//          }
//        } else if (state.getBlock() instanceof StalactiteBlock) {
//          if (!state.canPlaceAt(level, POS)) {
//            if (level.getBlockState(POS.up()).getBlock() instanceof StalactiteBlock) {
//              while (state.getBlock() instanceof StalactiteBlock) {
//                setWithoutUpdate(level, POS, AIR);
//                POS.setY(POS.getY() + 1);
//                state = level.getBlockState(POS);
//              }
//            } else {
//              while (state.getBlock() instanceof StalactiteBlock) {
//                setWithoutUpdate(level, POS, AIR);
//                POS.setY(POS.getY() - 1);
//                state = level.getBlockState(POS);
//              }
//            }
//          }
//        } else if (state.isOf(EndBlocks.CAVE_PUMPKIN)) {
//          if (!level.getBlockState(POS.up()).isOf(EndBlocks.CAVE_PUMPKIN_SEED)) {
//            setWithoutUpdate(level, POS, AIR);
//          }
        } else if (!state.canSurvive(level, POS)) {
          // Chorus
          if (state.is(Blocks.CHORUS_PLANT)) {
            Set<BlockPos> ends = Sets.newHashSet();
            Set<BlockPos> add = Sets.newHashSet();
            ends.add(POS.immutable());

            for (int i = 0; i < 64 && !ends.isEmpty(); i++) {
              ends.forEach((pos) -> {
                setWithoutUpdate(level, pos, AIR);
                for (Direction dir : Direction.Plane.HORIZONTAL) {
                  BlockPos p = pos.relative(dir);
                  BlockState st = level.getBlockState(p);
                  if ((st.is(Blocks.CHORUS_PLANT) || st.is(Blocks.CHORUS_FLOWER))
                      && !st.canSurvive(
                      level,
                      p
                  )) {
                    add.add(p);
                  }
                }
                BlockPos p = pos.above();
                BlockState st = level.getBlockState(p);
                if ((st.is(Blocks.CHORUS_PLANT) || st.is(Blocks.CHORUS_FLOWER))
                    && !st.canSurvive(
                    level,
                    p
                )) {
                  add.add(p);
                }
              });
              ends.clear();
              ends.addAll(add);
              add.clear();
            }
          }
          // Falling blocks
          else if (state.getBlock() instanceof FallingBlock) {
            BlockState falling = state;

            POS.setY(POS.getY() - 1);
            state = level.getBlockState(POS);

            int ray = PosInfo.downRayRep(level, POS.immutable(), 64);
            if (ray > 32) {
              setWithoutUpdate(level, POS, Blocks.END_STONE.defaultBlockState());
              if (level.getRandom().nextBoolean()) {
                POS.setY(POS.getY() - 1);
                state = level.getBlockState(POS);
                setWithoutUpdate(level, POS, Blocks.END_STONE.defaultBlockState());
              }
            } else {
              POS.setY(y);
              BlockState replacement = AIR;
              for (Direction dir : Direction.Plane.HORIZONTAL) {
                state = level.getBlockState(POS.relative(dir));
                if (!state.getFluidState().isEmpty()) {
                  replacement = state;
                  break;
                }
              }
              setWithoutUpdate(level, POS, replacement);
              POS.setY(y - ray);
              setWithoutUpdate(level, POS, falling);
            }
          }
          // Blocks without support
          else {
            setWithoutUpdate(level, POS, getAirOrFluid(state));
          }
        }
      }
    });

    doubleCheck.forEach((pos) -> {
      if (!level.getBlockState(pos).canSurvive(level, pos)) {
        setWithoutUpdate(level, pos, AIR);
      }
    });
  }

  private static BlockState getAirOrFluid(BlockState state) {
    return state.getFluidState().isEmpty() ? AIR : state.getFluidState().createLegacyBlock();
  }

  private static void setWithoutUpdate(LevelAccessor world, BlockPos pos, BlockState state) {
    synchronized (world) {
      world.setBlock(pos, state, Flags.SILENT);
    }
  }
}
