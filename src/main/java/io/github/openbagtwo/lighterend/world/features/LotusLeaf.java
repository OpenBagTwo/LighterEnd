package io.github.openbagtwo.lighterend.world.features;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.blocks.EndLotus;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.utils.Flags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;

public class LotusLeaf implements Feature {

  public LotusLeaf() {
  }

  public static final MapCodec<LotusLeaf> CODEC = MapCodec.unit(LotusLeaf::new);

  @Override
  public MapCodec<LotusLeaf> codec() {
    return CODEC;
  }

  @Override
  public boolean place(
      final WorldGenLevel world,
      final ChunkGenerator chunkGenerator,
      final RandomSource random,
      final BlockPos pos
  ) {
    if (!canGenerate(world, pos)) {
      return false;
    }

    MutableBlockPos p = new MutableBlockPos();
    BlockState leaf = LighterEndBlocks.END_LOTUS_LEAF.defaultBlockState();
    world.setBlock(pos, leaf.setValue(EndLotus.Leaf.SHAPE, EndLotus.Shape.BOTTOM), Flags.SILENT);
    for (Direction move : Direction.Plane.HORIZONTAL) {
      world.setBlock(
          p.set(pos).move(move),
          leaf.setValue(EndLotus.Leaf.HORIZONTAL_FACING, move)
              .setValue(EndLotus.Leaf.SHAPE, EndLotus.Shape.MIDDLE),
          Flags.SILENT
      );
    }
    for (int i = 0; i < 4; i++) {
      Direction d1 = Direction.Plane.HORIZONTAL.stream().toList().get(i);
      Direction d2 = Direction.Plane.HORIZONTAL.stream().toList().get((i + 1) & 3);
      world.setBlock(
          p.set(pos).move(d1).move(d2),
          leaf.setValue(EndLotus.Leaf.HORIZONTAL_FACING, d1)
              .setValue(EndLotus.Leaf.SHAPE, EndLotus.Shape.TOP),
          Flags.SILENT
      );
    }
    return true;
  }


  private boolean canGenerate(WorldGenLevel world, BlockPos pos) {
    MutableBlockPos p = new MutableBlockPos();
    p.setY(pos.getY());
    int count = 0;
    for (int x = -1; x < 2; x++) {
      p.setX(pos.getX() + x);
      for (int z = -1; z < 2; z++) {
        p.setZ(pos.getZ() + z);
        if (world.isEmptyBlock(p) && world.getBlockState(p.below()).is(Blocks.WATER)) {
          count++;
        }
      }
    }
    return count == 9;
  }

}
