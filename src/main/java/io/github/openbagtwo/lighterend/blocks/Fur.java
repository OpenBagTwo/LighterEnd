package io.github.openbagtwo.lighterend.blocks;

import com.google.common.collect.Maps;
import java.util.EnumMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class Fur extends Block implements SimpleWaterloggedBlock {

  private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
  private static final EnumMap<Direction, VoxelShape> BOUNDING_SHAPES = Maps.newEnumMap(
      Direction.class);

  static {
    BOUNDING_SHAPES.put(Direction.UP, Shapes.box(0.0, 0.0, 0.0, 1.0, 0.5, 1.0));
    BOUNDING_SHAPES.put(Direction.DOWN, Shapes.box(0.0, 0.5, 0.0, 1.0, 1.0, 1.0));
    BOUNDING_SHAPES.put(Direction.NORTH, Shapes.box(0.0, 0.0, 0.5, 1.0, 1.0, 1.0));
    BOUNDING_SHAPES.put(Direction.SOUTH, Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, 0.5));
    BOUNDING_SHAPES.put(Direction.WEST, Shapes.box(0.5, 0.0, 0.0, 1.0, 1.0, 1.0));
    BOUNDING_SHAPES.put(Direction.EAST, Shapes.box(0.0, 0.0, 0.0, 0.5, 1.0, 1.0));
  }

  public Fur(Properties settings, MapColor color, int luminance, boolean wet) {
    super(
        settings
            .mapColor(color)
            .replaceable()
            .noCollision()
            .instabreak()
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
            .lightLevel(bs -> luminance)
            .ignitedByLava()
            .sound(wet ? SoundType.WET_GRASS : SoundType.GRASS)
    );
    registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false).setValue(FACING, Direction.UP));
  }

  @Override
  public @NotNull VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos,
      CollisionContext ePos) {
    return BOUNDING_SHAPES.get(state.getValue(FACING));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(WATERLOGGED);
    builder.add(FACING);
  }

  @Override
  protected FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    BlockState blockState = super.getStateForPlacement(ctx);
    if (blockState != null) {
      FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
      blockState = blockState.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
      for (Direction direction : ctx.getNearestLookingDirections()) {
        blockState = blockState.setValue(FACING, direction.getOpposite());
        if (blockState.canSurvive(ctx.getLevel(), ctx.getClickedPos())) {
          return blockState;
        }
      }
    }
    return null;
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
    Direction direction = state.getValue(FACING);
    BlockPos blockPos = pos.relative(direction.getOpposite());
    return canSupportCenter(world, blockPos, direction) || world.getBlockState(blockPos)
        .is(BlockTags.LEAVES);
  }

  @Override
  public BlockState updateShape(
      BlockState state,
      LevelReader world,
      ScheduledTickAccess tickView,
      BlockPos pos,
      Direction direction,
      BlockPos neighborPos,
      BlockState neighborState,
      RandomSource random
  ) {
    if (!canSurvive(state, world, pos)) {
      return Blocks.AIR.defaultBlockState();
    } else {
      return state;
    }
  }

  @Override
  public BlockState rotate(BlockState state, Rotation rotation) {
    return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
  }

  @Override
  public BlockState mirror(BlockState state, Mirror mirror) {
    return state.rotate(mirror.getRotation(state.getValue(FACING)));
  }
}
