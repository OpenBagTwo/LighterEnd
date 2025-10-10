package io.github.openbagtwo.lighterend.blocks;

import com.google.common.collect.Maps;
import java.util.EnumMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.NotNull;

public class SulphurCrystal extends Block implements Waterloggable, FluidFillable {

  public static final int MAX_STAGE = 2;
  public static final IntProperty STAGE = IntProperty.of("stage", 0, MAX_STAGE);
  public static final EnumProperty<Direction> FACING = Properties.FACING;
  public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

  private static final EnumMap<Direction, VoxelShape> SHAPES = Maps.newEnumMap(Direction.class);

  public SulphurCrystal(Settings settings) {
    super(
        settings
            .mapColor(MapColor.YELLOW)
            .sounds(BlockSoundGroup.GLASS)
            .requiresTool()
            .noCollision()
            .pistonBehavior(PistonBehavior.DESTROY)
    );
  }

  @Override
  protected void appendProperties(Builder<Block, BlockState> builder) {
    builder.add(STAGE, WATERLOGGED, FACING);
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    WorldAccess worldAccess = ctx.getWorld();
    BlockPos blockPos = ctx.getBlockPos();

    BlockState state = this.getDefaultState().with(
        WATERLOGGED,
        worldAccess.getFluidState(blockPos).getFluid() == Fluids.WATER
    );

    Direction[] directions = ctx.getPlacementDirections();

    for (Direction direction : directions) {
      state = state.with(FACING, direction.getOpposite());
      if (state.canPlaceAt(worldAccess, blockPos)) {
        return state;
      }
    }
    return null;
  }

  @Override
  public @NotNull FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
  }

  @Override
  public @NotNull VoxelShape getOutlineShape(
      BlockState state,
      BlockView view,
      BlockPos pos,
      ShapeContext ePos
  ) {
    return SHAPES.get(state.get(FACING));
  }

  @Override
  protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    Direction direction = state.get(FACING);
    BlockPos blockPos = pos.offset(direction.getOpposite());
    return world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, direction);
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
    if (state.get(WATERLOGGED)) {
      tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }

    return direction == (state.get(FACING)).getOpposite() && !state.canPlaceAt(world, pos)
        ? Blocks.AIR.getDefaultState()
        : super.getStateForNeighborUpdate(
            state,
            world,
            tickView,
            pos,
            direction,
            neighborPos,
            neighborState,
            random
        );
  }

  static {
    SHAPES.put(Direction.UP, VoxelShapes.cuboid(0.125, 0.0, 0.125, 0.875F, 0.5, 0.875F));
    SHAPES.put(Direction.DOWN, VoxelShapes.cuboid(0.125, 0.5, 0.125, 0.875F, 1.0, 0.875F));
    SHAPES.put(Direction.NORTH, VoxelShapes.cuboid(0.125, 0.125, 0.5, 0.875F, 0.875F, 1.0));
    SHAPES.put(Direction.SOUTH, VoxelShapes.cuboid(0.125, 0.125, 0.0, 0.875F, 0.875F, 0.5));
    SHAPES.put(Direction.WEST, VoxelShapes.cuboid(0.5, 0.125, 0.125, 1.0, 0.875F, 0.875F));
    SHAPES.put(Direction.EAST, VoxelShapes.cuboid(0.0, 0.125, 0.125, 0.5, 0.875F, 0.875F));
  }

}
