package io.github.openbagtwo.lighterend.blocks;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.github.openbagtwo.lighterend.utils.Flags;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
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
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.material.WaterFluid;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class EndLotus extends Block {

  private static final VoxelShape SHAPE_OUTLINE = Block.box(2, 0, 2, 14, 14, 14);
  private static final VoxelShape SHAPE_COLLISION = Block.box(0, 0, 0, 16, 2, 16);

  public EndLotus(Properties settings) {
    super(
        settings
            .mapColor(MapColor.COLOR_PINK)
            .instrument(NoteBlockInstrument.BASS)
            .sound(SoundType.WOOD)
            .strength(0.2F)
            .ignitedByLava()
            .pushReaction(PushReaction.POPPED)
            .noOcclusion()
            .instabreak()
            .lightLevel(bs -> 15)
    );
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
  protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
    return state.is(LighterEndBlocks.END_LOTUS_STEM);
  }

  @Override
  protected VoxelShape getShape(
      BlockState state, BlockGetter world, BlockPos pos, CollisionContext context
  ) {
    return SHAPE_OUTLINE;
  }

  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockGetter view, BlockPos pos,
      CollisionContext ePos) {
    return SHAPE_COLLISION;
  }

  @Override
  protected ItemStack getCloneItemStack(
      LevelReader world,
      BlockPos pos,
      BlockState state,
      boolean includeData
  ) {
    return new ItemStack(LighterEndBlocks.END_LOTUS_SEED);
  }

  public static class Stem extends Block implements SimpleWaterloggedBlock, LiquidBlockContainer {

    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty LEAF = BooleanProperty.create("leaf");
    public static final EnumProperty<Shape> SHAPE = EnumProperty.create("shape", Shape.class);
    private static final Map<Axis, VoxelShape> SHAPES = Maps.newEnumMap(Axis.class);

    public Stem(Properties settings) {
      super(
          settings
              .mapColor(MapColor.COLOR_GREEN)
              .instrument(NoteBlockInstrument.BASS)
              .strength(2.0F, 3.0F)
              .sound(SoundType.BAMBOO_WOOD)
              .pushReaction(PushReaction.POPPED)
              .noOcclusion()
              .forceSolidOn()
              .ignitedByLava()
      );
      this.registerDefaultState(defaultBlockState()
          .setValue(WATERLOGGED, false)
          .setValue(SHAPE, Shape.MIDDLE)
          .setValue(LEAF, false)
          .setValue(FACING, Direction.UP));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(FACING, WATERLOGGED, SHAPE, LEAF);
    }

    @Override
    protected VoxelShape getShape(
        BlockState state, BlockGetter world, BlockPos pos, CollisionContext context
    ) {
      return state.getValue(LEAF) ? SHAPES.get(Axis.Y)
          : SHAPES.get(state.getValue(FACING).getAxis());
    }

    @Override
    public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false)
          : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      LevelAccessor worldAccess = ctx.getLevel();
      BlockPos blockPos = ctx.getClickedPos();
      return this.defaultBlockState()
          .setValue(WATERLOGGED, worldAccess.getFluidState(blockPos).getType() == Fluids.WATER)
          .setValue(FACING, ctx.getClickedFace());
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
      if (state.getValue(WATERLOGGED)) {
        tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
      }
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
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
      BlockState down = world.getBlockState(pos.below());
      return down.is(LighterEndTags.AQUATIC_END_SOIL) || down.getBlock() == this;
    }

    static {
      SHAPES.put(Axis.X, Block.box(0, 6, 6, 16, 10, 10));
      SHAPES.put(Axis.Y, Block.box(6, 0, 6, 10, 16, 10));
      SHAPES.put(Axis.Z, Block.box(6, 6, 0, 10, 10, 16));
    }
  }

  public static class Leaf extends Block {

    public static final EnumProperty<Direction> HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Shape> SHAPE = EnumProperty.create("shape", Shape.class);
    private static final VoxelShape VSHAPE = Block.box(0, 0, 0, 16, 1, 16);

    public Leaf(Properties settings) {
      super(
          settings
              .mapColor(MapColor.COLOR_PINK)
              .instabreak()
              .sound(SoundType.LILY_PAD)
              .noOcclusion()
              .pushReaction(PushReaction.POPPED)
      );
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
      BlockState down = world.getBlockState(pos.below());
      return !down.getFluidState().isEmpty() && down.getFluidState()
          .getType() instanceof WaterFluid;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(SHAPE, HORIZONTAL_FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos,
        CollisionContext ePos) {
      return VSHAPE;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
      return state.setValue(HORIZONTAL_FACING, rotation.rotate(state.getValue(HORIZONTAL_FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
      return state.rotate(mirror.getRotation(state.getValue(HORIZONTAL_FACING)));
    }


    @Override
    protected ItemStack getCloneItemStack(
        LevelReader world,
        BlockPos pos,
        BlockState state,
        boolean includeData
    ) {
      return new ItemStack(LighterEndBlocks.END_LOTUS_SEED);
    }
  }

  public static class Seed extends Sapling implements LiquidBlockContainer {

    public Seed(Properties settings) {
      super(EndLotusFeature::new, settings, 7);
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
        BonemealSource source
    ) {
      return EndLotus.canGrow(world, pos);
    }

  }

  public static class EndLotusFeature implements Feature {

    public EndLotusFeature() {
    }

    @Override
    public MapCodec<EndLotusFeature> codec() {
      return MapCodec.unit(EndLotusFeature::new);
    }

    @Override
    public boolean place(
        final WorldGenLevel world,
        final ChunkGenerator chunkGenerator,
        final RandomSource random,
        final BlockPos pos
    ) {
      if (EndLotus.canGrow(world, pos)) {
        BlockState startLeaf = LighterEndBlocks.END_LOTUS_STEM.defaultBlockState()
            .setValue(Stem.LEAF, true);
        BlockState roots = LighterEndBlocks.END_LOTUS_STEM.defaultBlockState()
            .setValue(Stem.SHAPE, Shape.BOTTOM)
            .setValue(Stem.WATERLOGGED, true);
        BlockState stem = LighterEndBlocks.END_LOTUS_STEM.defaultBlockState();
        BlockState flower = LighterEndBlocks.END_LOTUS_FLOWER.defaultBlockState();

        world.setBlock(pos, roots, Flags.SILENT);
        MutableBlockPos bpos = new MutableBlockPos().set(pos);
        bpos.setY(bpos.getY() + 1);
        while (world.getFluidState(bpos).isSource()) {
          world.setBlock(bpos, stem.setValue(Stem.WATERLOGGED, true), Flags.SILENT);
          bpos.setY(bpos.getY() + 1);
        }

        int height =
            random.nextBoolean() ? 0 : random.nextBoolean() ? 1 : random.nextBoolean() ? 1 : -1;
        Shape shape = (height == 0) ? Shape.TOP : Shape.MIDDLE;
        Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        BlockPos leafCenter = bpos.immutable().relative(dir);
        if (hasLeaf(world, leafCenter)) {
          generateLeaf(world, leafCenter);
          world.setBlock(
              bpos,
              startLeaf.setValue(Stem.SHAPE, shape).setValue(Stem.FACING, dir),
              Flags.SILENT
          );
        } else {
          world.setBlock(bpos, stem.setValue(Stem.SHAPE, shape), Flags.SILENT);
        }

        bpos.setY(bpos.getY() + 1);
        for (int i = 1; i <= height; i++) {
          if (!world.isEmptyBlock(bpos)) {
            bpos.setY(bpos.getY() - 1);
            world.setBlock(bpos, flower, Flags.SILENT);
            bpos.setY(bpos.getY() - 1);
            stem = world.getBlockState(bpos);
            world.setBlock(bpos, stem.setValue(Stem.SHAPE, Shape.TOP), Flags.SILENT);
            return true;
          }
          world.setBlock(bpos, stem, Flags.SILENT);
          bpos.setY(bpos.getY() + 1);
        }

        if (!world.isEmptyBlock(bpos) || height < 0) {
          bpos.setY(bpos.getY() - 1);
        }

        world.setBlock(bpos, flower, Flags.SILENT);
        bpos.setY(bpos.getY() - 1);
        stem = world.getBlockState(bpos);
        if (!stem.is(LighterEndBlocks.END_LOTUS_STEM)) {
          stem = LighterEndBlocks.END_LOTUS_STEM.defaultBlockState();
          if (!world.getBlockState(bpos.north()).getFluidState().isEmpty()) {
            stem = stem.setValue(Stem.WATERLOGGED, true);
          }
        }

        if (world.getBlockState(bpos.relative(dir)).is(LighterEndBlocks.END_LOTUS_LEAF)) {
          stem = stem.setValue(Stem.LEAF, true).setValue(Stem.FACING, dir);
        }

        world.setBlock(bpos, stem.setValue(Stem.SHAPE, Shape.TOP), Flags.SILENT);
        return true;
      }
      return false;
    }

    private void generateLeaf(WorldGenLevel world, BlockPos pos) {
      MutableBlockPos p = new MutableBlockPos();
      BlockState leaf = LighterEndBlocks.END_LOTUS_LEAF.defaultBlockState();
      world.setBlock(pos, leaf.setValue(Leaf.SHAPE, Shape.BOTTOM), Flags.SILENT);
      for (Direction move : Direction.Plane.HORIZONTAL) {
        world.setBlock(
            p.set(pos).move(move),
            leaf.setValue(Leaf.HORIZONTAL_FACING, move)
                .setValue(Leaf.SHAPE, Shape.MIDDLE),
            Flags.SILENT
        );
      }
      for (int i = 0; i < 4; i++) {
        Direction d1 = Direction.Plane.HORIZONTAL.stream().toList().get(i);
        Direction d2 = Direction.Plane.HORIZONTAL.stream().toList().get((i + 1) & 3);
        world.setBlock(
            p.set(pos).move(d1).move(d2),
            leaf.setValue(Leaf.HORIZONTAL_FACING, d1)
                .setValue(Leaf.SHAPE, Shape.TOP),
            Flags.SILENT
        );
      }
    }

    private boolean hasLeaf(WorldGenLevel world, BlockPos pos) {
      MutableBlockPos p = new MutableBlockPos();
      p.setY(pos.getY());
      int count = 0;
      for (int x = -1; x < 2; x++) {
        p.setX(pos.getX() + x);
        for (int z = -1; z < 2; z++) {
          p.setZ(pos.getZ() + z);
          if (world.isEmptyBlock(p) && !world.getFluidState(p.below()).isEmpty()) {
            count++;
          }
        }
      }
      return count == 9;
    }
  }


  public enum Shape implements StringRepresentable {
    TOP("top"),
    MIDDLE("middle"),
    BOTTOM("bottom");

    private final String name;

    Shape(String name) {
      this.name = name;
    }

    @Override
    public String getSerializedName() {
      return name;
    }

    @Override
    public String toString() {
      return name;
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
