package io.github.openbagtwo.lighterend.world.features;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.registries.LighterEndLootTables;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndRodBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;

public class StarterChest implements Feature {

  public StarterChest() {
  }

  public static final MapCodec<StarterChest> CODEC = MapCodec.unit(StarterChest::new);

  @Override
  public MapCodec<StarterChest> codec() {
    return CODEC;
  }

  @Override
  public boolean place(
      final WorldGenLevel structureWorldAccess,
      final ChunkGenerator chunkGenerator,
      final RandomSource random,
      final BlockPos origin
  ) {
    ChunkPos chunkPos = ChunkPos.containing(origin);
    IntArrayList intArrayList = Util.toShuffledList(
        IntStream.rangeClosed(chunkPos.getMinBlockX(), chunkPos.getMaxBlockX()), random);
    IntArrayList intArrayList2 = Util.toShuffledList(
        IntStream.rangeClosed(chunkPos.getMinBlockZ(), chunkPos.getMaxBlockZ()), random);
    BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

    for (boolean tryFloating : Arrays.asList(false, true)) {
      for (Integer integer : intArrayList) {
        for (Integer integer2 : intArrayList2) {
          mutable.set(integer, 0, integer2);
          BlockPos blockPos = structureWorldAccess.getHeightmapPos(
              Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, mutable);
          if (tryFloating) {
            blockPos = blockPos.above();
          }
          if (
              (
                  structureWorldAccess.isEmptyBlock(blockPos) || structureWorldAccess.getBlockState(
                          blockPos)
                      .getCollisionShape(structureWorldAccess, blockPos).isEmpty()
              )
                  && blockPos.getY() > 50
          ) {
            structureWorldAccess.setBlock(
                blockPos,
                Blocks.BARREL.defaultBlockState().setValue(BarrelBlock.FACING, Direction.UP),
                Block.UPDATE_CLIENTS);
            RandomizableContainer.setBlockEntityLootTable(structureWorldAccess, random, blockPos,
                LighterEndLootTables.STARTER_CHEST);
            BlockState blockState = Blocks.END_ROD.defaultBlockState();

            for (Direction direction : Direction.Plane.HORIZONTAL) {
              BlockPos rodPos = blockPos.relative(direction);
              if (blockState.canSurvive(structureWorldAccess, rodPos)) {
                structureWorldAccess.setBlock(
                    rodPos,
                    blockState.setValue(EndRodBlock.FACING, direction),
                    Block.UPDATE_CLIENTS
                );

              }
            }

            return true;
          }
        }
      }
    }
    LighterEnd.LOGGER.error("Failed to generate starter chest");
    return false;
  }
}
