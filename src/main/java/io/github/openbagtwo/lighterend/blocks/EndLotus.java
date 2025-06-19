package io.github.openbagtwo.lighterend.blocks;

import com.google.common.collect.Maps;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.github.openbagtwo.lighterend.utils.Flags;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
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

public class EndLotus extends Block {

  private static final VoxelShape SHAPE_OUTLINE = Block.createCuboidShape(2, 0, 2, 14, 14, 14);
  private static final VoxelShape SHAPE_COLLISION = Block.createCuboidShape(0, 0, 0, 16, 2, 16);

  public EndLotus(Settings settings) {
    super(
        settings
            .mapColor(MapColor.PINK)
            .instrument(NoteBlockInstrument.BASS)
            .sounds(BlockSoundGroup.WOOD)
            .strength(0.2F)
            .burnable()
            .pistonBehavior(PistonBehavior.DESTROY)
            .nonOpaque()
            .breakInstantly()
            .luminance(bs -> 15)
    );
  }

  @Override
  protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    return state.isOf(LighterEndBlocks.END_LOTUS_STEM);
  }

  @Override
  protected VoxelShape getOutlineShape(
      BlockState state, BlockView world, BlockPos pos, ShapeContext context
  ) {
    return SHAPE_OUTLINE;
  }

  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos,
      ShapeContext ePos) {
    return SHAPE_COLLISION;
  }

  @Override
  protected ItemStack getPickStack(
      WorldView world,
      BlockPos pos,
      BlockState state,
      boolean includeData
  ) {
    return new ItemStack(LighterEndBlocks.END_LOTUS_SEED);
  }

  public static class Stem extends Block implements Waterloggable, FluidFillable {

    public static final EnumProperty<Direction> FACING = Properties.FACING;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final BooleanProperty LEAF = BooleanProperty.of("leaf");
    public static final EnumProperty<Shape> SHAPE = EnumProperty.of("shape", Shape.class);
    private static final Map<Axis, VoxelShape> SHAPES = Maps.newEnumMap(Axis.class);

    public Stem(Settings settings) {
      super(
          settings
              .mapColor(MapColor.GREEN)
              .instrument(NoteBlockInstrument.BASS)
              .strength(2.0F, 3.0F)
              .sounds(BlockSoundGroup.BAMBOO_WOOD)
              .pistonBehavior(PistonBehavior.DESTROY)
              .nonOpaque()
              .solid()
              .burnable()
      );
      this.setDefaultState(getDefaultState()
          .with(WATERLOGGED, false)
          .with(SHAPE, Shape.MIDDLE)
          .with(LEAF, false)
          .with(FACING, Direction.UP));
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
      builder.add(FACING, WATERLOGGED, SHAPE, LEAF);
    }

    @Override
    protected VoxelShape getOutlineShape(
        BlockState state, BlockView world, BlockPos pos, ShapeContext context
    ) {
      return state.get(LEAF) ? SHAPES.get(Axis.Y) : SHAPES.get(state.get(FACING).getAxis());
    }

    @Override
    public FluidState getFluidState(BlockState state) {
      return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
      WorldAccess worldAccess = ctx.getWorld();
      BlockPos blockPos = ctx.getBlockPos();
      return this.getDefaultState()
          .with(WATERLOGGED, worldAccess.getFluidState(blockPos).getFluid() == Fluids.WATER)
          .with(FACING, ctx.getSide());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
      return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
      return state.rotate(mirror.getRotation(state.get(FACING)));
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
      if (state.get(WATERLOGGED)) {
        tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
      }
      return state;
    }

    static {
      SHAPES.put(Axis.X, Block.createCuboidShape(0, 6, 6, 16, 10, 10));
      SHAPES.put(Axis.Y, Block.createCuboidShape(6, 0, 6, 10, 16, 10));
      SHAPES.put(Axis.Z, Block.createCuboidShape(6, 6, 0, 10, 10, 16));
    }
  }

  public static class Leaf extends Block {

    public static final EnumProperty<Direction> HORIZONTAL_FACING = Properties.HORIZONTAL_FACING;
    public static final EnumProperty<Shape> SHAPE = EnumProperty.of("shape", Shape.class);
    private static final VoxelShape VSHAPE = Block.createCuboidShape(0, 0, 0, 16, 1, 16);

    public Leaf(Settings settings) {
      super(
          settings
              .mapColor(MapColor.PINK)
              .breakInstantly()
              .sounds(BlockSoundGroup.LILY_PAD)
              .nonOpaque()
              .pistonBehavior(PistonBehavior.DESTROY)
      );
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
      BlockState down = world.getBlockState(pos.down());
      return !down.getFluidState().isEmpty() && down.getFluidState()
          .getFluid() instanceof WaterFluid;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      builder.add(SHAPE, HORIZONTAL_FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos,
        ShapeContext ePos) {
      return VSHAPE;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
      return state.with(HORIZONTAL_FACING, rotation.rotate(state.get(HORIZONTAL_FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
      return state.rotate(mirror.getRotation(state.get(HORIZONTAL_FACING)));
    }


    @Override
    protected ItemStack getPickStack(
        WorldView world,
        BlockPos pos,
        BlockState state,
        boolean includeData
    ) {
      return new ItemStack(LighterEndBlocks.END_LOTUS_SEED);
    }
  }

  public static class Seed extends Sapling implements FluidFillable {

    public Seed(Settings settings) {
      super(EndLotusFeature::new, settings);
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
      return EndLotus.canGrow(world, pos);
    }

  }

  public static class EndLotusFeature extends Feature<DefaultFeatureConfig> {

    public EndLotusFeature() {
      super(DefaultFeatureConfig.CODEC);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> featureConfig) {
      final BlockPos pos = featureConfig.getOrigin();
      final StructureWorldAccess world = featureConfig.getWorld();
      final Random random = featureConfig.getRandom();

      if (EndLotus.canGrow(world, pos)) {
        BlockState startLeaf = LighterEndBlocks.END_LOTUS_STEM.getDefaultState()
            .with(Stem.LEAF, true);
        BlockState roots = LighterEndBlocks.END_LOTUS_STEM.getDefaultState()
            .with(Stem.SHAPE, Shape.BOTTOM)
            .with(Stem.WATERLOGGED, true);
        BlockState stem = LighterEndBlocks.END_LOTUS_STEM.getDefaultState();
        BlockState flower = LighterEndBlocks.END_LOTUS_FLOWER.getDefaultState();

        world.setBlockState(pos, roots, Flags.SILENT);
        Mutable bpos = new Mutable().set(pos);
        bpos.setY(bpos.getY() + 1);
        while (world.getFluidState(bpos).isStill()) {
          world.setBlockState(bpos, stem.with(Stem.WATERLOGGED, true), Flags.SILENT);
          bpos.setY(bpos.getY() + 1);
        }

        int height =
            random.nextBoolean() ? 0 : random.nextBoolean() ? 1 : random.nextBoolean() ? 1 : -1;
        Shape shape = (height == 0) ? Shape.TOP : Shape.MIDDLE;
        Direction dir = Direction.Type.HORIZONTAL.random(random);
        BlockPos leafCenter = bpos.toImmutable().offset(dir);
        if (hasLeaf(world, leafCenter)) {
          generateLeaf(world, leafCenter);
          world.setBlockState(
              bpos,
              startLeaf.with(Stem.SHAPE, shape).with(Stem.FACING, dir),
              Flags.SILENT
          );
        } else {
          world.setBlockState(bpos, stem.with(Stem.SHAPE, shape), Flags.SILENT);
        }

        bpos.setY(bpos.getY() + 1);
        for (int i = 1; i <= height; i++) {
          if (!world.isAir(bpos)) {
            bpos.setY(bpos.getY() - 1);
            world.setBlockState(bpos, flower, Flags.SILENT);
            bpos.setY(bpos.getY() - 1);
            stem = world.getBlockState(bpos);
            world.setBlockState(bpos, stem.with(Stem.SHAPE, Shape.TOP), Flags.SILENT);
            return true;
          }
          world.setBlockState(bpos, stem, Flags.SILENT);
          bpos.setY(bpos.getY() + 1);
        }

        if (!world.isAir(bpos) || height < 0) {
          bpos.setY(bpos.getY() - 1);
        }

        world.setBlockState(bpos, flower, Flags.SILENT);
        bpos.setY(bpos.getY() - 1);
        stem = world.getBlockState(bpos);
        if (!stem.isOf(LighterEndBlocks.END_LOTUS_STEM)) {
          stem = LighterEndBlocks.END_LOTUS_STEM.getDefaultState();
          if (!world.getBlockState(bpos.north()).getFluidState().isEmpty()) {
            stem = stem.with(Stem.WATERLOGGED, true);
          }
        }

        if (world.getBlockState(bpos.offset(dir)).isOf(LighterEndBlocks.END_LOTUS_LEAF)) {
          stem = stem.with(Stem.LEAF, true).with(Stem.FACING, dir);
        }

        world.setBlockState(bpos, stem.with(Stem.SHAPE, Shape.TOP), Flags.SILENT);
        return true;
      }
      return false;
    }

    private void generateLeaf(StructureWorldAccess world, BlockPos pos) {
      Mutable p = new Mutable();
      BlockState leaf = LighterEndBlocks.END_LOTUS_LEAF.getDefaultState();
      world.setBlockState(pos, leaf.with(Leaf.SHAPE, Shape.BOTTOM), Flags.SILENT);
      for (Direction move : Direction.Type.HORIZONTAL) {
        world.setBlockState(
            p.set(pos).move(move),
            leaf.with(Leaf.HORIZONTAL_FACING, move)
                .with(Leaf.SHAPE, Shape.MIDDLE),
            Flags.SILENT
        );
      }
      for (int i = 0; i < 4; i++) {
        Direction d1 = Direction.Type.HORIZONTAL.stream().toList().get(i);
        Direction d2 = Direction.Type.HORIZONTAL.stream().toList().get((i + 1) & 3);
        world.setBlockState(
            p.set(pos).move(d1).move(d2),
            leaf.with(Leaf.HORIZONTAL_FACING, d1)
                .with(Leaf.SHAPE, Shape.TOP),
            Flags.SILENT
        );
      }
    }

    private boolean hasLeaf(StructureWorldAccess world, BlockPos pos) {
      Mutable p = new Mutable();
      p.setY(pos.getY());
      int count = 0;
      for (int x = -1; x < 2; x++) {
        p.setX(pos.getX() + x);
        for (int z = -1; z < 2; z++) {
          p.setZ(pos.getZ() + z);
          if (world.isAir(p) && !world.getFluidState(p.down()).isEmpty()) {
            count++;
          }
        }
      }
      return count == 9;
    }
  }


  public enum Shape implements StringIdentifiable {
    TOP("top"),
    MIDDLE("middle"),
    BOTTOM("bottom");

    private final String name;

    Shape(String name) {
      this.name = name;
    }

    @Override
    public String asString() {
      return name;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  private static boolean canGrow(WorldAccess world, BlockPos pos) {
    if (!world.getBlockState(pos).getFluidState().getFluid().equals(Fluids.WATER.getStill())) {
      return false;
    }
    Mutable bpos = new Mutable();
    bpos.set(pos);
    while (world.getBlockState(bpos).getFluidState().getFluid().equals(Fluids.WATER.getStill())) {
      bpos.setY(bpos.getY() + 1);
    }
    return world.isAir(bpos) && world.isAir(bpos.up());
  }

}
