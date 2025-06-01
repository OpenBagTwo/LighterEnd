package io.github.openbagtwo.lighterend.blocks;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.tags.LighterEndTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.MapColor;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.SeagrassBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
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

public class Charnia extends PlantBlock implements Fertilizable, FluidFillable {

  public static final MapCodec<Charnia> CODEC = createCodec(Charnia::new);
  private static final VoxelShape SHAPE = SeagrassBlock.createCuboidShape(4, 0, 4, 12, 14, 12);

  @Override
  public MapCodec<Charnia> getCodec() {
    return CODEC;
  }

  public Charnia(Settings settings) {
    super(
        settings
            .mapColor(MapColor.WATER_BLUE)
            .replaceable()
            .noCollision()
            .nonOpaque()
            .breakInstantly()
            .offset(OffsetType.XZ)
            .sounds(BlockSoundGroup.WET_GRASS)
            .pistonBehavior(PistonBehavior.DESTROY)
    );
  }

  @Override
  protected VoxelShape getOutlineShape(
      BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return SHAPE;
  }

  @Override
  protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
    return floor.isSideSolidFullSquare(world, pos, Direction.UP) && floor.isIn(
        LighterEndTags.END_SOIL_AQUATIC);
  }

  @Nullable
  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
    return fluidState.isIn(FluidTags.WATER) && fluidState.getLevel() == 8 ? super.getPlacementState(
        ctx) : null;
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
    BlockState blockState = super.getStateForNeighborUpdate(state, world, tickView, pos, direction,
        neighborPos, neighborState, random);
    if (!blockState.isAir()) {
      tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }

    return blockState;
  }

  @Override
  public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
    return world.getBlockState(pos.up()).isOf(Blocks.WATER);
  }

  @Override
  protected FluidState getFluidState(BlockState state) {
    return Fluids.WATER.getStill(false);
  }

  @Override
  public boolean canFillWithFluid(@Nullable LivingEntity filler, BlockView world, BlockPos pos,
      BlockState state, Fluid fluid) {
    return false;
  }

  @Override
  public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state,
      FluidState fluidState) {
    return false;
  }

  @Override
  public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
    return true;
  }

  @Override
  public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
    dropStack(world, pos, new ItemStack(this));
  }

}
