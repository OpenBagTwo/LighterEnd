package io.github.openbagtwo.lighterend.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.EnumMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

public class TubeWorm extends Block implements Fertilizable, FluidFillable {

  private static final EnumMap<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(
      Direction.NORTH, createCuboidShape(1, 1, 8, 15, 15, 16),
      Direction.SOUTH, createCuboidShape(1, 1, 0, 15, 15, 8),
      Direction.WEST, createCuboidShape(8, 1, 1, 16, 15, 15),
      Direction.EAST, createCuboidShape(0, 1, 1, 8, 15, 15)
  ));
  public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;

  public TubeWorm(Settings settings) {
    super(
        settings
            .mapColor(MapColor.BROWN)
            .replaceable()
            .noCollision()
            .nonOpaque()
            .breakInstantly()
            .sounds(BlockSoundGroup.WET_GRASS)
            .pistonBehavior(PistonBehavior.DESTROY)
    );
  }

  @Override
  protected void appendProperties(Builder<Block, BlockState> builder) {
    builder.add(FACING);
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
  public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos,
      Random randomSource) {
    if (!canPlaceAt(state, world, pos)) {
      world.breakBlock(pos, true);
    }
  }

  @Override
  public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    Direction direction = state.get(FACING);
    BlockPos blockPos = pos.offset(direction.getOpposite());
    BlockState blockState = world.getBlockState(blockPos);
    return world.getFluidState(pos).getFluid() == Fluids.WATER
        && blockState.isSolid()
        && blockState.isSideSolidFullSquare(world, pos, direction);
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
  protected BlockState getStateForNeighborUpdate(
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
    }
    return state;
  }

  @Override
  public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
    return true;
  }

  @Override
  public boolean canGrow(World level, Random random, BlockPos pos, BlockState state) {
    return true;
  }

  @Override
  public void grow(ServerWorld level, Random random, BlockPos pos, BlockState state) {
    ItemEntity item = new ItemEntity(
        level,
        pos.getX() + 0.5,
        pos.getY() + 0.5,
        pos.getZ() + 0.5,
        new ItemStack(this)
    );
    level.spawnEntity(item);
  }


  @Override
  public boolean canFillWithFluid(
      @Nullable LivingEntity filler,
      BlockView world,
      BlockPos pos,
      BlockState state,
      Fluid fluid) {
    return false;
  }

  @Override
  public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state,
      FluidState fluidState) {
    return false;
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return Fluids.WATER.getStill(false);
  }
}
