package io.github.openbagtwo.lighterend.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.EnumMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public class Polypore extends Block {

  private static final EnumMap<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(
      Direction.NORTH, createCuboidShape(1, 1, 8, 15, 15, 16),
      Direction.SOUTH, createCuboidShape(1, 1, 0, 15, 15, 8),
      Direction.WEST, createCuboidShape(8, 1, 1, 16, 15, 15),
      Direction.EAST, createCuboidShape(0, 1, 1, 8, 15, 15)
  ));
  public static final EnumProperty<Direction> FACING = Properties.FACING;

  public Polypore(Settings settings, MapColor color, int luminance) {
    super(
        settings
            .offset(OffsetType.NONE)
            .mapColor(color)
            .nonOpaque()
            .breakInstantly()
            .pistonBehavior(PistonBehavior.DESTROY)
            .noCollision()
            .hardness(0.2F)
            .luminance(bs -> luminance)
            .sounds(BlockSoundGroup.WOOD)
    );
    setDefaultState(getDefaultState().with(FACING, Direction.UP));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
    stateManager.add(FACING);
  }

  @Override
  public VoxelShape getOutlineShape(
      BlockState state,
      BlockView view,
      BlockPos pos,
      ShapeContext ePos
  ) {
    return SHAPES.get(state.get(FACING));
  }

  @Override
  public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    Direction direction = state.get(FACING);
    BlockPos blockPos = pos.offset(direction.getOpposite());
    BlockState blockState = world.getBlockState(blockPos);
    return blockState.isSolid() && blockState.isSideSolidFullSquare(world, pos, direction);
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    BlockState blockState = this.getDefaultState();
    WorldView worldView = ctx.getWorld();
    BlockPos blockPos = ctx.getBlockPos();
    Direction[] directions = ctx.getPlacementDirections();
    for (Direction direction : directions) {
      if (direction.getAxis().isHorizontal()) {
        Direction direction2 = direction.getOpposite();
        blockState = blockState.with(FACING, direction2);
        if (blockState.canPlaceAt(worldView, blockPos)) {
          return blockState;
        }
      }
    }
    return null;
  }

  @Override
  public BlockState getStateForNeighborUpdate(
      BlockState state,
      WorldView world,
      ScheduledTickView tickView,
      BlockPos pos,
      Direction direction,
      BlockPos neighborPos,
      BlockState neighborState,
      Random random
  ) {
    if (!canPlaceAt(state, world, pos)) {
      return Blocks.AIR.getDefaultState();
    } else {
      return state;
    }
  }

  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return state.with(FACING, rotation.rotate(state.get(FACING)));
  }

  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return state.rotate(mirror.getRotation(state.get(FACING)));
  }
}
