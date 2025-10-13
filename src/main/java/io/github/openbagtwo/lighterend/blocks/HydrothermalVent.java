package io.github.openbagtwo.lighterend.blocks;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.blocks.entities.Updraft;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.utils.Flags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
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

public class HydrothermalVent extends BlockWithEntity implements FluidFillable, Waterloggable {

  public static final MapCodec<HydrothermalVent> CODEC = createCodec(HydrothermalVent::new);

  public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
  public static final BooleanProperty ACTIVATED = BooleanProperty.of("active");
  private static final VoxelShape SHAPE = Block.createCuboidShape(1, 1, 1, 15, 16, 15);

  public HydrothermalVent(Settings settings) {
    super(
        settings
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sounds(BlockSoundGroup.STONE)
            .requiresTool()
            .strength(1.5F, 6.0F)
            .mapColor(MapColor.STONE_GRAY)
    );
    this.setDefaultState(getDefaultState().with(WATERLOGGED, false).with(ACTIVATED, false));
  }

  @Override
  public MapCodec<HydrothermalVent> getCodec() {
    return CODEC;
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    builder.add(WATERLOGGED, ACTIVATED);
  }

  @Override
  public VoxelShape getOutlineShape(
      BlockState state,
      BlockView view,
      BlockPos pos,
      ShapeContext ePos
  ) {
    return SHAPE;
  }

  @Override
  public boolean canFillWithFluid(
      @Nullable LivingEntity filler,
      BlockView world,
      BlockPos pos,
      BlockState state,
      Fluid fluid
  ) {
    return false;
  }


  @Override
  public boolean tryFillWithFluid(
      WorldAccess world,
      BlockPos pos,
      BlockState state,
      FluidState fluidState
  ) {
    return false;
  }

  @Override
  public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    state = world.getBlockState(pos.down());
    return state.isOf(LighterEndBlocks.BORNITE.baseBlock);
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
      return state.get(WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
    } else if (
        state.get(WATERLOGGED)
            && direction == Direction.UP
            && neighborState.isOf(Blocks.WATER)
            && world instanceof ServerWorld serverWorld
    ) {
      serverWorld.createOrderedTick(pos, this, 20);
    }
    return state;
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    WorldAccess worldAccess = ctx.getWorld();
    BlockPos blockPos = ctx.getBlockPos();
    return this.getDefaultState()
        .with(WATERLOGGED, worldAccess.getFluidState(blockPos).getFluid() == Fluids.WATER);
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
  }

  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new Updraft(pos, state);
  }

  @Override
  public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
    BlockPos up = pos.up();
    if (world.getBlockState(up).isOf(Blocks.WATER)) {
      world.setBlockState(up, LighterEndBlocks.VENT_BUBBLE_COLUMN.getDefaultState(), Flags.SILENT);
      world.createOrderedTick(up, LighterEndBlocks.VENT_BUBBLE_COLUMN, 5);
    }
  }

  @Override
  public void onPlaced(
      World world,
      BlockPos pos,
      BlockState state,
      @Nullable LivingEntity placer,
      ItemStack itemStack
  ) {
    if (
        world instanceof ServerWorld
            && state.get(WATERLOGGED) && world.getBlockState(pos.up()).isOf(Blocks.WATER)) {
      scheduledTick(state, (ServerWorld) world, pos, world.random);
    }
  }

  public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
    super.randomDisplayTick(state, world, pos, random);
    if (!state.get(ACTIVATED) && random.nextBoolean()) {
      double x = pos.getX() + random.nextDouble();
      double y = pos.getY() + 0.9 + random.nextDouble() * 0.3;
      double z = pos.getZ() + random.nextDouble();
      world.addParticleClient(ParticleTypes.LARGE_SMOKE, x, y, z, 0, 0, 0);
    }
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
      World level,
      BlockState blockState,
      BlockEntityType<T> blockEntityType
  ) {
    return Updraft::tick;
  }

}
