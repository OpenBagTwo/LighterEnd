package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.tags.LighterEndTags;
import io.github.openbagtwo.lighterend.utils.Flags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

public class EndLily extends Block implements FluidFillable {

  public static final BooleanProperty IS_TOP = BooleanProperty.of("is_top");

  private static final VoxelShape SHAPE_BOTTOM = Block.createCuboidShape(
      4, 0, 4, 12, 16, 12
  );
  private static final VoxelShape SHAPE_TOP = Block.createCuboidShape(
      2, 0, 2, 14, 6, 14
  );

  public EndLily(Settings settings) {
    super(settings
        .mapColor(MapColor.WATER_BLUE)
        .replaceable()
        .noCollision()
        .nonOpaque()
        .breakInstantly()
        .offset(OffsetType.XZ)
        .sounds(BlockSoundGroup.WET_GRASS)
        .pistonBehavior(PistonBehavior.DESTROY)
        .luminance((state) -> state.get(IS_TOP) ? 13 : 0)
    );
  }

  @Override
  protected void appendProperties(Builder<Block, BlockState> builder) {
    builder.add(IS_TOP);
  }

  @Override
  public BlockState getStateForNeighborUpdate(
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
      return state.get(IS_TOP)
          ? Blocks.AIR.getDefaultState()
          : Blocks.WATER.getDefaultState();
    } else {
      return state;
    }
  }

  @Override
  protected VoxelShape getOutlineShape(
      BlockState state, BlockView world, BlockPos pos, ShapeContext context
  ) {
    Vec3d vec3d = state.getModelOffset(pos);
    VoxelShape shape = state.get(IS_TOP) ? SHAPE_TOP : SHAPE_BOTTOM;
    return shape.offset(vec3d.x, vec3d.y, vec3d.z);
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(IS_TOP) ? Fluids.EMPTY.getDefaultState() : Fluids.WATER.getStill(false);
  }

  @Override
  public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    if (state.get(IS_TOP)) {
      return world.getBlockState(pos.down()).getBlock() == this;
    } else {
      BlockState down = world.getBlockState(pos.down());
      return down.isIn(LighterEndTags.AQUATIC_END_SOIL) || down.getBlock() == this;
    }
  }

  @Override
  protected ItemStack getPickStack(
      WorldView world,
      BlockPos pos,
      BlockState state,
      boolean includeData
  ) {
    return new ItemStack(LighterEndBlocks.END_LILY_SEED);
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

  public static class Seed extends Sapling implements FluidFillable {

    public Seed(Settings settings) {
      super(EndLilyFeature::new, settings);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
      return floor.isSideSolidFullSquare(world, pos, Direction.UP) && floor.isIn(
          LighterEndTags.AQUATIC_END_SOIL);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
      FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
      return fluidState.isIn(FluidTags.WATER) && fluidState.getLevel() == 8
          ? super.getPlacementState(
          ctx) : null;
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
      return EndLily.canGrow(world, pos);
    }

  }

  public static class EndLilyFeature extends Feature<DefaultFeatureConfig> {

    public EndLilyFeature() {
      super(DefaultFeatureConfig.CODEC);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> featureConfig) {
      final BlockPos pos = featureConfig.getOrigin();
      final StructureWorldAccess world = featureConfig.getWorld();

      if (canGrow(world.toServerWorld(), pos)) {
        world.setBlockState(
            pos,
            LighterEndBlocks.END_LILY.getDefaultState().with(EndLily.IS_TOP, false),
            Flags.SILENT
        );

        BlockPos up = pos.up();
        while (world.getFluidState(up).isStill()) {
          world.setBlockState(up,
              LighterEndBlocks.END_LILY.getDefaultState().with(EndLily.IS_TOP, false),
              Flags.SILENT
          );
          up = up.up();
        }
        world.setBlockState(
            up,
            LighterEndBlocks.END_LILY.getDefaultState().with(EndLily.IS_TOP, true),
            Flags.SILENT
        );
        return true;
      }
      return false;
    }
  }

  private static boolean canGrow(World world, BlockPos pos) {
    BlockPos up = pos.up();
    while (world.getBlockState(up).getFluidState().getFluid().equals(Fluids.WATER.getStill())) {
      up = up.up();
    }
    return world.isAir(up);
  }

}
