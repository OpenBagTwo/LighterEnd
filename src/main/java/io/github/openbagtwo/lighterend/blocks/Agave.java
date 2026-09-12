package io.github.openbagtwo.lighterend.blocks;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.github.openbagtwo.lighterend.utils.Flags;
import io.github.openbagtwo.lighterend.utils.PosInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Port of BetterEnd's Blue Vine, which isn't a vine
 */
public class Agave extends Block {

  private static final VoxelShape OUTLINE_SHAPE = box(4, 0, 4, 12, 16, 12);
  public static final EnumProperty<Shape> SHAPE = EnumProperty.create("shape", Shape.class);

  public Agave(Properties settings) {
    super(
        settings
            .mapColor(MapColor.COLOR_LIGHT_BLUE)
            .offsetType(OffsetType.XZ)
            .dynamicShape()
            .noOcclusion()
            .instabreak()
            .pushReaction(PushReaction.POPPED)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F, 3.0F)
            .sound(SoundType.GRASS)
            .forceSolidOn()
            .ignitedByLava()
    );
    this.registerDefaultState(defaultBlockState().setValue(SHAPE, Shape.MIDDLE));
  }

  @Override
  protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
    builder.add(SHAPE);
  }

  @Override
  protected VoxelShape getShape(
      BlockState state, BlockGetter world, BlockPos pos, CollisionContext context
  ) {
    return OUTLINE_SHAPE.move(state.getOffset(pos));
  }

  @Override
  protected VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos,
      CollisionContext context) {
    return OUTLINE_SHAPE.move(state.getOffset(pos));
  }

  @Override
  protected boolean propagatesSkylightDown(BlockState state) {
    return true;
  }

  @Override
  protected boolean isPathfindable(BlockState state, PathComputationType type) {
    return false;
  }

  @Override
  protected boolean isCollisionShapeFullBlock(BlockState state, BlockGetter world, BlockPos pos) {
    return false;
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
    if (!canSurvive(state, world, pos)) {
      world.destroyBlock(pos, true);
    }
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
    BlockState down = world.getBlockState(pos.below());
    return down.is(LighterEndTags.AQUATIC_END_SOIL) || down.getBlock() == this;
  }

  @Override
  protected ItemStack getCloneItemStack(
      LevelReader world,
      BlockPos pos,
      BlockState state,
      boolean includeData
  ) {
    return new ItemStack(LighterEndBlocks.AGAVE_SEED);
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

  public static class Bulb extends Block {

    public static final BooleanProperty NATURAL = BooleanProperty.create("natural");

    public Bulb(Properties settings) {
      super(
          settings
              .mapColor(MapColor.WARPED_WART_BLOCK)
              .strength(1.0F)
              .sound(SoundType.FROGLIGHT)
              .lightLevel((bs) -> 15)
      );
      registerDefaultState(stateDefinition.any().setValue(NATURAL, false));
    }

    @Override
    protected void createBlockStateDefinition(
        StateDefinition.Builder<Block, BlockState> stateManager) {
      stateManager.add(NATURAL);
    }

    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
      if (
          !world.getBlockState(pos.below()).is(LighterEndBlocks.AGAVE) && state.getValue(NATURAL)
      ) {
        world.destroyBlock(pos, true);
      }
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
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
      if (!state.getValue(NATURAL)) {
        return true;
      }
      return world.getBlockState(pos.below()).is(LighterEndBlocks.AGAVE);
    }
  }

  public static class AgaveFeature implements Feature {

    public AgaveFeature() {
    }

    public static final MapCodec<AgaveFeature> CODEC = MapCodec.unit(AgaveFeature::new);

    @Override
    public MapCodec<AgaveFeature> codec() {
      return CODEC;
    }

    @Override
    public boolean place(
        final WorldGenLevel world,
        final ChunkGenerator chunkGenerator,
        final RandomSource random,
        final BlockPos pos
    ) {
      int height = Mth.nextInt(random, 2, 5);
      int h = PosInfo.upRay(world, pos, height + 2);
      if (h < height + 1) {
        return false;
      }

      for (int i = 0; i <= height; i++) {
        Shape shape = Shape.MIDDLE;
        if (i == 0) {
          shape = Shape.BOTTOM;
        } else if (i == height) {
          shape = Shape.TOP;
        }
        world.setBlock(
            pos.above(i), LighterEndBlocks.AGAVE.defaultBlockState().setValue(SHAPE, shape),
            Flags.SILENT
        );
      }
      placeBulb(world, pos.above(height + 1));
      return true;
    }

    private void placeBulb(WorldGenLevel world, BlockPos pos) {
      world.setBlock(
          pos,
          LighterEndBlocks.AGAVE_BULB.defaultBlockState().setValue(Bulb.NATURAL, true),
          Flags.SILENT
      );

      for (Direction dir : UPDATE_SHAPE_ORDER) {
        if (dir == Direction.DOWN) {
          continue;
        }
        BlockPos p = pos.relative(dir);
        if (world.isEmptyBlock(p)) {
          world.setBlock(
              p,
              LighterEndBlocks.AGAVE_FUR.defaultBlockState().setValue(Fur.FACING, dir),
              Flags.SILENT);
        }
      }
    }
  }
}
