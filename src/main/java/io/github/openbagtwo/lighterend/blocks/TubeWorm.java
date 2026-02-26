package io.github.openbagtwo.lighterend.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.EnumMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class TubeWorm extends Block implements BonemealableBlock, LiquidBlockContainer {

  private static final EnumMap<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(
      Direction.NORTH, box(1, 1, 8, 15, 15, 16),
      Direction.SOUTH, box(1, 1, 0, 15, 15, 8),
      Direction.WEST, box(8, 1, 1, 16, 15, 15),
      Direction.EAST, box(0, 1, 1, 8, 15, 15)
  ));
  public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

  public TubeWorm(Properties settings) {
    super(
        settings
            .mapColor(MapColor.COLOR_BROWN)
            .replaceable()
            .noCollision()
            .noOcclusion()
            .instabreak()
            .sound(SoundType.WET_GRASS)
            .pushReaction(PushReaction.DESTROY)
    );
  }

  @Override
  protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
    builder.add(FACING);
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
  public void tick(BlockState state, ServerLevel world, BlockPos pos,
      RandomSource randomSource) {
    if (!canSurvive(state, world, pos)) {
      world.destroyBlock(pos, true);
    }
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
    Direction direction = state.getValue(FACING);
    BlockPos blockPos = pos.relative(direction.getOpposite());
    BlockState blockState = world.getBlockState(blockPos);
    return world.getFluidState(pos).getType() == Fluids.WATER
        && blockState.isSolid()
        && blockState.isFaceSturdy(world, pos, direction);
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
    }
    return state;
  }

  @Override
  public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
    return true;
  }

  @Override
  public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
    return true;
  }

  @Override
  public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
    ItemEntity item = new ItemEntity(
        level,
        pos.getX() + 0.5,
        pos.getY() + 0.5,
        pos.getZ() + 0.5,
        new ItemStack(this)
    );
    level.addFreshEntity(item);
  }


  @Override
  public boolean canPlaceLiquid(
      @Nullable LivingEntity filler,
      BlockGetter world,
      BlockPos pos,
      BlockState state,
      Fluid fluid) {
    return false;
  }

  @Override
  public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state,
      FluidState fluidState) {
    return false;
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return Fluids.WATER.getSource(false);
  }
}
