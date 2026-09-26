package io.github.openbagtwo.lighterend.world.features;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.registries.LighterEndLootTables;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndRodBlock;
import net.minecraft.inventory.LootableInventory;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class StarterChest extends Feature<DefaultFeatureConfig> {

  public StarterChest() {
    super(DefaultFeatureConfig.CODEC);
  }

  @Override
  public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
    Random random = context.getRandom();
    StructureWorldAccess structureWorldAccess = context.getWorld();
    ChunkPos chunkPos = new ChunkPos(context.getOrigin());
    IntArrayList intArrayList = Util.shuffle(
        IntStream.rangeClosed(chunkPos.getStartX(), chunkPos.getEndX()), random);
    IntArrayList intArrayList2 = Util.shuffle(
        IntStream.rangeClosed(chunkPos.getStartZ(), chunkPos.getEndZ()), random);
    BlockPos.Mutable mutable = new BlockPos.Mutable();

    for (boolean tryFloating : Arrays.asList(false, true)) {
      for (Integer integer : intArrayList) {
        for (Integer integer2 : intArrayList2) {
          mutable.set(integer, 0, integer2);
          BlockPos blockPos = structureWorldAccess.getTopPosition(
              Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, mutable);
          if (tryFloating) {
            blockPos = blockPos.offset(Direction.UP);
          }
          if (
              (
                  structureWorldAccess.isAir(blockPos)
                      || structureWorldAccess.getBlockState(blockPos)
                      .getCollisionShape(structureWorldAccess, blockPos).isEmpty()
              )
                  && blockPos.getY() > 50
          ) {
            structureWorldAccess.setBlockState(
                blockPos,
                Blocks.BARREL.getDefaultState().with(BarrelBlock.FACING, Direction.UP),
                Block.NOTIFY_LISTENERS);
            LootableInventory.setLootTable(structureWorldAccess, random, blockPos,
                LighterEndLootTables.STARTER_CHEST);
            BlockState blockState = Blocks.END_ROD.getDefaultState();

            for (Direction direction : Direction.Type.HORIZONTAL) {
              BlockPos rodPos = blockPos.offset(direction);
              if (blockState.canPlaceAt(structureWorldAccess, rodPos)) {
                structureWorldAccess.setBlockState(
                    rodPos,
                    blockState.with(EndRodBlock.FACING, direction),
                    Block.NOTIFY_LISTENERS
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
