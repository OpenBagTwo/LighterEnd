package io.github.openbagtwo.lighterend.world.features;

import io.github.openbagtwo.lighterend.blocks.EndLotus;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.utils.Flags;
import io.github.openbagtwo.lighterend.utils.PosInfo;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class LotusLeaf extends Feature<DefaultFeatureConfig> {

  public LotusLeaf() {
    super(DefaultFeatureConfig.CODEC);
  }

  @Override
  public boolean generate(FeatureContext<DefaultFeatureConfig> featureConfig) {

    final BlockPos pos = featureConfig.getOrigin();
    final StructureWorldAccess world = featureConfig.getWorld();

    if (!canGenerate(world, pos)) {
      return false;
    }

    Mutable p = new Mutable();
    BlockState leaf = LighterEndBlocks.END_LOTUS_LEAF.getDefaultState();
    world.setBlockState(pos, leaf.with(EndLotus.Leaf.SHAPE, EndLotus.Shape.BOTTOM), Flags.SILENT);
    for (Direction move : PosInfo.HORIZONTAL) {
      world.setBlockState(
          p.set(pos).move(move),
          leaf.with(EndLotus.Leaf.HORIZONTAL_FACING, move)
              .with(EndLotus.Leaf.SHAPE, EndLotus.Shape.MIDDLE),
          Flags.SILENT
      );
    }
    for (int i = 0; i < 4; i++) {
      Direction d1 = PosInfo.HORIZONTAL[i];
      Direction d2 = PosInfo.HORIZONTAL[(i + 1) & 3];
      world.setBlockState(
          p.set(pos).move(d1).move(d2),
          leaf.with(EndLotus.Leaf.HORIZONTAL_FACING, d1)
              .with(EndLotus.Leaf.SHAPE, EndLotus.Shape.TOP),
          Flags.SILENT
      );
    }
    return true;
  }


  private boolean canGenerate(StructureWorldAccess world, BlockPos pos) {
    Mutable p = new Mutable();
    p.setY(pos.getY());
    int count = 0;
    for (int x = -1; x < 2; x++) {
      p.setX(pos.getX() + x);
      for (int z = -1; z < 2; z++) {
        p.setZ(pos.getZ() + z);
        if (world.isAir(p) && world.getBlockState(p.down()).isOf(Blocks.WATER)) {
          count++;
        }
      }
    }
    return count == 9;
  }

}
