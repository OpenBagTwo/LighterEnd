package io.github.openbagtwo.lighterend.blocks;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.github.openbagtwo.lighterend.utils.Flags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
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
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealSource;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class EndLily extends Block implements LiquidBlockContainer {

  public static final BooleanProperty IS_TOP = BooleanProperty.create("is_top");

  private static final VoxelShape SHAPE_BOTTOM = Block.box(
      4, 0, 4, 12, 16, 12
  );
  private static final VoxelShape SHAPE_TOP = Block.box(
      2, 0, 2, 14, 6, 14
  );

  public EndLily(Properties settings) {
    super(settings
        .mapColor(MapColor.WATER)
        .replaceable()
        .noCollision()
        .noOcclusion()
        .instabreak()
        .offsetType(OffsetType.XZ)
        .sound(SoundType.WET_GRASS)
        .pushReaction(PushReaction.POPPED)
        .lightLevel((state) -> state.getValue(IS_TOP) ? 13 : 0)
    );
  }

  @Override
  protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
    builder.add(IS_TOP);
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
      tickView.scheduleTick(pos, this, 1);
    }
    return state;
  }

  @Override
  protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
    if (!state.canSurvive(world, pos)) {
      world.destroyBlock(pos, true);
    }
  }

  @Override
  protected VoxelShape getShape(
      BlockState state, BlockGetter world, BlockPos pos, CollisionContext context
  ) {
    Vec3 vec3d = state.getOffset(pos);
    VoxelShape shape = state.getValue(IS_TOP) ? SHAPE_TOP : SHAPE_BOTTOM;
    return shape.move(vec3d.x, vec3d.y, vec3d.z);
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(IS_TOP) ? Fluids.EMPTY.defaultFluidState()
        : Fluids.WATER.getSource(false);
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
    if (state.getValue(IS_TOP)) {
      return world.getBlockState(pos.below()).getBlock() == this;
    } else {
      BlockState down = world.getBlockState(pos.below());
      return down.is(LighterEndTags.AQUATIC_END_SOIL) || down.getBlock() == this;
    }
  }

  @Override
  protected ItemStack getCloneItemStack(
      LevelReader world,
      BlockPos pos,
      BlockState state,
      boolean includeData
  ) {
    return new ItemStack(LighterEndBlocks.END_LILY_SEED);
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

  public static class Seed extends Sapling implements LiquidBlockContainer {

    public Seed(Properties settings) {
      super(EndLilyFeature::new, settings, 7);
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
        final BonemealSource source
    ) {
      return EndLily.canGrow(world, pos);
    }

  }

  public static class EndLilyFeature implements Feature {

    public EndLilyFeature() {
    }

    public static final MapCodec<EndLilyFeature> CODEC = MapCodec.unit(EndLilyFeature::new);

    @Override
    public MapCodec<EndLilyFeature> codec() {
      return CODEC;
    }

    @Override
    public boolean place(
        final WorldGenLevel world,
        final ChunkGenerator chunkGenerator,
        final RandomSource random,
        final BlockPos pos
    ) {
      if (EndLily.canGrow(world, pos)) {
        world.setBlock(
            pos,
            LighterEndBlocks.END_LILY.defaultBlockState().setValue(EndLily.IS_TOP, false),
            Flags.SILENT
        );

        BlockPos up = pos.above();
        while (world.getFluidState(up).isSource()) {
          world.setBlock(up,
              LighterEndBlocks.END_LILY.defaultBlockState().setValue(EndLily.IS_TOP, false),
              Flags.SILENT
          );
          up = up.above();
        }
        world.setBlock(
            up,
            LighterEndBlocks.END_LILY.defaultBlockState().setValue(EndLily.IS_TOP, true),
            Flags.SILENT
        );
        return true;
      }
      return false;
    }
  }

  private static boolean canGrow(LevelAccessor world, BlockPos pos) {
    if (!world.getBlockState(pos).getFluidState().getType().equals(Fluids.WATER.getSource())) {
      return false;
    }
    MutableBlockPos bpos = new MutableBlockPos();
    bpos.set(pos);
    while (world.getBlockState(bpos).getFluidState().getType().equals(Fluids.WATER.getSource())) {
      bpos.setY(bpos.getY() + 1);
    }
    return world.isEmptyBlock(bpos) && world.isEmptyBlock(bpos.above());
  }

}
