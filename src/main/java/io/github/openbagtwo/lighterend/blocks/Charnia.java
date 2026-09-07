package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealSource;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.SeagrassBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class Charnia extends VegetationBlock implements BonemealableBlock, LiquidBlockContainer {

  private static final VoxelShape SHAPE = SeagrassBlock.box(4, 0, 4, 12, 14, 12);

  public Charnia(Properties settings) {
    super(
        settings
            .mapColor(MapColor.WATER)
            .replaceable()
            .noCollision()
            .noOcclusion()
            .instabreak()
            .offsetType(OffsetType.XZ)
            .sound(SoundType.WET_GRASS)
            .pushReaction(PushReaction.POPPED)
    );
  }

  @Override
  protected VoxelShape getShape(
      BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    return SHAPE;
  }

  @Override
  protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
    return floor.isFaceSturdy(world, pos, Direction.UP) && floor.is(
        LighterEndTags.AQUATIC_END_SOIL);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
    return fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8
        ? super.getStateForPlacement(
        ctx) : null;
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
    BlockState blockState = super.updateShape(state, world, tickView, pos, direction,
        neighborPos, neighborState, random);
    if (!blockState.isAir()) {
      tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
    }

    return blockState;
  }

  @Override
  public boolean isValidBonemealTarget(
      LevelReader world,
      BlockPos pos,
      BlockState state,
      final BonemealSource source
  ) {
    return world.getBlockState(pos.above()).is(Blocks.WATER);
  }

  @Override
  protected FluidState getFluidState(BlockState state) {
    return Fluids.WATER.getSource(false);
  }

  @Override
  public boolean canPlaceLiquid(@Nullable LivingEntity filler, BlockGetter world, BlockPos pos,
      BlockState state, Fluid fluid) {
    return false;
  }

  @Override
  public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state,
      FluidState fluidState) {
    return false;
  }

  @Override
  public boolean isBonemealSuccess(
      Level world,
      RandomSource random,
      BlockPos pos,
      BlockState state,
      BonemealSource source
  ) {
    return true;
  }

  @Override
  public void performBonemeal(
      ServerLevel world,
      RandomSource random,
      BlockPos pos,
      BlockState state,
      BonemealSource source
  ) {
    popResource(world, pos, new ItemStack(this));
  }

}
