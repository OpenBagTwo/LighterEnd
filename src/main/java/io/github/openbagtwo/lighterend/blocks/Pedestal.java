package io.github.openbagtwo.lighterend.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class Pedestal extends Block {

  private static final VoxelShape SHAPE;

  public Pedestal(Settings settings) {
    super(
        settings
            .pistonBehavior(PistonBehavior.BLOCK)
    );
  }

  @Override
  protected VoxelShape getOutlineShape(
      BlockState state,
      BlockView world,
      BlockPos pos,
      ShapeContext context
  ) {
    return SHAPE;
  }

  static {
    VoxelShape basinUp = Block.createCuboidShape(2, 3, 2, 14, 4, 14);
    VoxelShape basinDown = Block.createCuboidShape(0, 0, 0, 16, 3, 16);
    VoxelShape pedestalDefault = Block.createCuboidShape(1, 12, 1, 15, 14, 15);
    VoxelShape pillarDefault = Block.createCuboidShape(3, 0, 3, 13, 12, 13);
    VoxelShape basin = VoxelShapes.union(basinDown, basinUp);
    SHAPE = VoxelShapes.union(basin, pillarDefault, pedestalDefault);
  }

}
