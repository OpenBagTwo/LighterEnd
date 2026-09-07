package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.blocks.entities.Updraft;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.utils.Flags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class HydrothermalVent extends BaseEntityBlock implements LiquidBlockContainer,
    SimpleWaterloggedBlock {

  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  public static final BooleanProperty ACTIVATED = BooleanProperty.create("active");
  private static final VoxelShape SHAPE = Block.box(1, 1, 1, 15, 16, 15);

  public HydrothermalVent(Properties settings) {
    super(
        settings
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(SoundType.STONE)
            .requiresCorrectToolForDrops()
            .strength(1.5F, 6.0F)
            .pushReaction(PushReaction.POPPED)
            .mapColor(MapColor.STONE)
    );
    this.registerDefaultState(
        defaultBlockState().setValue(WATERLOGGED, false).setValue(ACTIVATED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(WATERLOGGED, ACTIVATED);
  }

  @Override
  public VoxelShape getShape(
      BlockState state,
      BlockGetter view,
      BlockPos pos,
      CollisionContext ePos
  ) {
    return SHAPE;
  }

  @Override
  public boolean canPlaceLiquid(
      @Nullable LivingEntity filler,
      BlockGetter world,
      BlockPos pos,
      BlockState state,
      Fluid fluid
  ) {
    return false;
  }


  @Override
  public boolean placeLiquid(
      LevelAccessor world,
      BlockPos pos,
      BlockState state,
      FluidState fluidState
  ) {
    return false;
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
    state = world.getBlockState(pos.below());
    return state.is(LighterEndBlocks.BORNITE.baseBlock);
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
      return state.getValue(WATERLOGGED) ? Blocks.WATER.defaultBlockState()
          : Blocks.AIR.defaultBlockState();
    } else if (
        state.getValue(WATERLOGGED)
            && direction == Direction.UP
            && neighborState.is(Blocks.WATER)
            && world instanceof ServerLevel serverWorld
    ) {
      serverWorld.createTick(pos, this, 20);
    }
    return state;
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    LevelAccessor worldAccess = ctx.getLevel();
    BlockPos blockPos = ctx.getClickedPos();
    return this.defaultBlockState()
        .setValue(WATERLOGGED, worldAccess.getFluidState(blockPos).getType() == Fluids.WATER);
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new Updraft(pos, state);
  }

  @Override
  public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
    BlockPos up = pos.above();
    if (world.getBlockState(up).is(Blocks.WATER)) {
      world.setBlock(up, LighterEndBlocks.VENT_BUBBLE_COLUMN.defaultBlockState(), Flags.SILENT);
      world.createTick(up, LighterEndBlocks.VENT_BUBBLE_COLUMN, 5);
    }
  }

  @Override
  public void setPlacedBy(
      Level world,
      BlockPos pos,
      BlockState state,
      @Nullable LivingEntity placer,
      ItemStack itemStack
  ) {
    if (
        world instanceof ServerLevel
            && state.getValue(WATERLOGGED) && world.getBlockState(pos.above()).is(Blocks.WATER)) {
      tick(state, (ServerLevel) world, pos, world.getRandom());
    }
  }

  public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
    super.animateTick(state, world, pos, random);
    if (!state.getValue(ACTIVATED) && random.nextBoolean()) {
      double x = pos.getX() + random.nextDouble();
      double y = pos.getY() + 0.9 + random.nextDouble() * 0.3;
      double z = pos.getZ() + random.nextDouble();
      world.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, 0, 0, 0);
    }
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
      Level level,
      BlockState blockState,
      BlockEntityType<T> blockEntityType
  ) {
    return Updraft::tick;
  }

}
