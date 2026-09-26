package io.github.openbagtwo.lighterend.blocks;

import com.google.common.collect.Maps;
import java.util.EnumMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class SulphurCrystal extends Block implements SimpleWaterloggedBlock, LiquidBlockContainer {

  public static final int MAX_STAGE = 2;
  public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, MAX_STAGE);
  public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  private static final EnumMap<Direction, VoxelShape> SHAPES = Maps.newEnumMap(Direction.class);

  public SulphurCrystal(Properties settings) {
    super(
        settings
            .mapColor(MapColor.COLOR_YELLOW)
            .sound(SoundType.GLASS)
            .pushReaction(PushReaction.DESTROY)
    );
  }

  @Override
  protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
    builder.add(STAGE, WATERLOGGED, FACING);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    LevelAccessor worldAccess = ctx.getLevel();
    BlockPos blockPos = ctx.getClickedPos();

    BlockState state = this.defaultBlockState().setValue(
        WATERLOGGED,
        worldAccess.getFluidState(blockPos).getType() == Fluids.WATER
    );

    Direction[] directions = ctx.getNearestLookingDirections();

    for (Direction direction : directions) {
      state = state.setValue(FACING, direction.getOpposite());
      if (state.canSurvive(worldAccess, blockPos)) {
        return state;
      }
    }
    return null;
  }

  @Override
  public @NotNull FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  public @NotNull VoxelShape getShape(
      BlockState state,
      BlockGetter view,
      BlockPos pos,
      CollisionContext ePos
  ) {
    return SHAPES.get(state.getValue(FACING));
  }

  @Override
  protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
    Direction direction = state.getValue(FACING);
    BlockPos blockPos = pos.relative(direction.getOpposite());
    return world.getBlockState(blockPos).isFaceSturdy(world, blockPos, direction);
  }

  @Override
  protected BlockState updateShape(
      BlockState state,
      LevelReader world,
      ScheduledTickAccess tickView,
      BlockPos pos,
      Direction direction,
      BlockPos neighborPos,
      BlockState neighborState,
      RandomSource random
  ) {
    if (state.getValue(WATERLOGGED)) {
      tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
    }

    return direction == (state.getValue(FACING)).getOpposite() && !state.canSurvive(world, pos)
        ? Blocks.AIR.defaultBlockState()
        : super.updateShape(
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
    SHAPES.put(Direction.UP, Shapes.box(0.125, 0.0, 0.125, 0.875F, 0.5, 0.875F));
    SHAPES.put(Direction.DOWN, Shapes.box(0.125, 0.5, 0.125, 0.875F, 1.0, 0.875F));
    SHAPES.put(Direction.NORTH, Shapes.box(0.125, 0.125, 0.5, 0.875F, 0.875F, 1.0));
    SHAPES.put(Direction.SOUTH, Shapes.box(0.125, 0.125, 0.0, 0.875F, 0.875F, 0.5));
    SHAPES.put(Direction.WEST, Shapes.box(0.5, 0.125, 0.125, 1.0, 0.875F, 0.875F));
    SHAPES.put(Direction.EAST, Shapes.box(0.0, 0.125, 0.125, 0.5, 0.875F, 0.875F));
  }

}
