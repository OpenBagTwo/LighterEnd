package io.github.openbagtwo.lighterend.blocks;

import com.google.common.collect.Maps;
import java.util.EnumMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Chandelier extends Block {

  public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

  private static final EnumMap<Direction, VoxelShape> BOUNDING_SHAPES = Maps.newEnumMap(
      Direction.class);

  public Chandelier(Properties settings) {
    super(
        settings
            .mapColor(MapColor.METAL)
            .lightLevel((bs) -> 15)
            .forceSolidOn()
            .noOcclusion()
            .requiresCorrectToolForDrops()
            .pushReaction(PushReaction.POPPED)
            .strength(2.5F)
            .sound(SoundType.CHAIN)
    );
    registerDefaultState(defaultBlockState().setValue(FACING, Direction.UP));
  }

  @Override
  public VoxelShape getShape(
      BlockState state,
      BlockGetter view,
      BlockPos pos,
      CollisionContext ePos
  ) {
    return BOUNDING_SHAPES.get(state.getValue(FACING));
  }

  @Override
  protected void createBlockStateDefinition(
      StateDefinition.Builder<Block, BlockState> stateManager) {
    stateManager.add(FACING);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    BlockState blockState = defaultBlockState();
    LevelReader worldView = ctx.getLevel();
    BlockPos blockPos = ctx.getClickedPos();
    Direction[] directions = ctx.getNearestLookingDirections();
    for (Direction direction : directions) {
      Direction direction2 = direction.getOpposite();
      blockState = blockState.setValue(FACING, direction2);
      if (blockState.canSurvive(worldView, blockPos)) {
        return blockState;
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

  @Override
  protected boolean isPathfindable(BlockState state, PathComputationType type) {
    return false;
  }

  static {
    BOUNDING_SHAPES.put(Direction.UP, Block.box(5, 0, 5, 11, 13, 11));
    BOUNDING_SHAPES.put(Direction.DOWN, Block.box(5, 3, 5, 11, 16, 11));
    BOUNDING_SHAPES.put(Direction.NORTH, Shapes.box(0.0, 0.0, 0.5, 1.0, 1.0, 1.0));
    BOUNDING_SHAPES.put(Direction.SOUTH, Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, 0.5));
    BOUNDING_SHAPES.put(Direction.WEST, Shapes.box(0.5, 0.0, 0.0, 1.0, 1.0, 1.0));
    BOUNDING_SHAPES.put(Direction.EAST, Shapes.box(0.0, 0.0, 0.0, 0.5, 1.0, 1.0));
  }

  public static class Oxidizable extends Chandelier implements
      net.minecraft.world.level.block.WeatheringCopper {

    private final net.minecraft.world.level.block.WeatheringCopper.WeatherState oxidationLevel;

    public Oxidizable(net.minecraft.world.level.block.WeatheringCopper.WeatherState oxidationLevel,
        BlockBehaviour.Properties settings) {
      super(settings);
      this.oxidationLevel = oxidationLevel;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel world, BlockPos pos,
        RandomSource random) {
      this.changeOverTime(state, world, pos, random);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
      return net.minecraft.world.level.block.WeatheringCopper.getNext(state.getBlock())
          .isPresent();
    }

    public net.minecraft.world.level.block.WeatheringCopper.WeatherState getAge() {
      return this.oxidationLevel;
    }
  }

}
