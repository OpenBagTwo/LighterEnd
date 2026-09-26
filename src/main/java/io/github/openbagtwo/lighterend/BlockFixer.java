package io.github.openbagtwo.lighterend;

import com.google.common.collect.Sets;
import io.github.openbagtwo.lighterend.blocks.Fur;
import io.github.openbagtwo.lighterend.utils.Flags;
import io.github.openbagtwo.lighterend.utils.PosInfo;
import java.util.Set;
import java.util.stream.IntStream;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.DimensionType;

public class BlockFixer {

  private static final BlockState AIR = Blocks.AIR.getDefaultState();
  private static final BlockState WATER = Blocks.WATER.getDefaultState();

  public static void fixBlocks(WorldAccess level, BlockPos start, BlockPos end) {
    final Registry<DimensionType> registry = level.getRegistryManager()
        .getOrThrow(RegistryKeys.DIMENSION_TYPE);
    final Identifier dimKey = registry.getId(level.getDimension());
    if (dimKey != null && "world_blender".equals(dimKey.getNamespace())) {
      return;
    }
    final Set<BlockPos> doubleCheck = Sets.newConcurrentHashSet();
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

        if (state.getBlock() instanceof Fur) {
          doubleCheck.add(POS.toImmutable());
        }
        // Liquids
        else if (!state.getFluidState().isEmpty()) {
          if (!state.canPlaceAt(level, POS)) {
            setWithoutUpdate(level, POS, WATER);
            POS.setY(POS.getY() - 1);
            state = level.getBlockState(POS);
            while (!state.canPlaceAt(level, POS)) {
              state = state.getFluidState().isEmpty() ? AIR : WATER;
              setWithoutUpdate(level, POS, state);
              POS.setY(POS.getY() - 1);
              state = level.getBlockState(POS);
            }
          }
          POS.setY(y - 1);
          if (level.isAir(POS)) {
            POS.setY(y);
            while (!level.getFluidState(POS).isEmpty()) {
              setWithoutUpdate(level, POS, AIR);
              POS.setY(POS.getY() + 1);
            }
            continue;
          }
          for (Direction dir : Direction.Type.HORIZONTAL) {
            if (level.isAir(POS.offset(dir))) {
              try {
                level.createOrderedTick(POS, state.getFluidState().getFluid(), 0);
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
        } else if (!state.canPlaceAt(level, POS)) {
          // Chorus
          if (state.isOf(Blocks.CHORUS_PLANT)) {
            Set<BlockPos> ends = Sets.newHashSet();
            Set<BlockPos> add = Sets.newHashSet();
            ends.add(POS.toImmutable());

            for (int i = 0; i < 64 && !ends.isEmpty(); i++) {
              ends.forEach((pos) -> {
                setWithoutUpdate(level, pos, AIR);
                for (Direction dir : Direction.Type.HORIZONTAL) {
                  BlockPos p = pos.offset(dir);
                  BlockState st = level.getBlockState(p);
                  if ((st.isOf(Blocks.CHORUS_PLANT) || st.isOf(Blocks.CHORUS_FLOWER))
                      && !st.canPlaceAt(
                      level,
                      p
                  )) {
                    add.add(p);
                  }
                }
                BlockPos p = pos.up();
                BlockState st = level.getBlockState(p);
                if ((st.isOf(Blocks.CHORUS_PLANT) || st.isOf(Blocks.CHORUS_FLOWER))
                    && !st.canPlaceAt(
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

            int ray = PosInfo.downRayRep(level, POS.toImmutable(), 64);
            if (ray > 32) {
              setWithoutUpdate(level, POS, Blocks.END_STONE.getDefaultState());
              if (level.getRandom().nextBoolean()) {
                POS.setY(POS.getY() - 1);
                state = level.getBlockState(POS);
                setWithoutUpdate(level, POS, Blocks.END_STONE.getDefaultState());
              }
            } else {
              POS.setY(y);
              BlockState replacement = AIR;
              for (Direction dir : Direction.Type.HORIZONTAL) {
                state = level.getBlockState(POS.offset(dir));
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
      if (!level.getBlockState(pos).canPlaceAt(level, pos)) {
        setWithoutUpdate(level, pos, AIR);
      }
    });
  }

  private static BlockState getAirOrFluid(BlockState state) {
    return state.getFluidState().isEmpty() ? AIR : state.getFluidState().getBlockState();
  }

  private static void setWithoutUpdate(WorldAccess world, BlockPos pos, BlockState state) {
    synchronized (world) {
      world.setBlockState(pos, state, Flags.SILENT);
    }
  }
}
