package io.github.openbagtwo.lighterend.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.EnumMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Polypore extends Block {

  private static final EnumMap<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(
      Direction.NORTH, box(1, 1, 8, 15, 15, 16),
      Direction.SOUTH, box(1, 1, 0, 15, 15, 8),
      Direction.WEST, box(8, 1, 1, 16, 15, 15),
      Direction.EAST, box(0, 1, 1, 8, 15, 15),
      Direction.UP, box(0, 0, 0, 0, 0, 0),
      Direction.DOWN, box(0, 0, 0, 0, 0, 0)
  ));
  public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

  public Polypore(Properties settings, MapColor color, int luminance) {
    super(
        settings
            .offsetType(OffsetType.NONE)
            .mapColor(color)
            .noOcclusion()
            .instabreak()
            .pushReaction(PushReaction.DESTROY)
            .noCollision()
            .destroyTime(0.2F)
            .lightLevel(bs -> luminance)
            .sound(SoundType.WOOD)
    );
    registerDefaultState(defaultBlockState().setValue(FACING, Direction.UP));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
    stateManager.add(FACING);
  }

  @Override
  public VoxelShape getShape(
      BlockState state,
      BlockGetter view,
      BlockPos pos,
      CollisionContext ePos
  ) {
    return SHAPES.get(state.getValue(FACING));
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
    Direction direction = state.getValue(FACING);
    BlockPos blockPos = pos.relative(direction.getOpposite());
    BlockState blockState = world.getBlockState(blockPos);
    return blockState.isSolid() && blockState.isFaceSturdy(world, pos, direction);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    BlockState blockState = this.defaultBlockState();
    LevelReader worldView = ctx.getLevel();
    BlockPos blockPos = ctx.getClickedPos();
    Direction[] directions = ctx.getNearestLookingDirections();
    for (Direction direction : directions) {
      if (direction.getAxis().isHorizontal()) {
        Direction direction2 = direction.getOpposite();
        blockState = blockState.setValue(FACING, direction2);
        if (blockState.canSurvive(worldView, blockPos)) {
          return blockState;
        }
      }
    }
    return null;
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
