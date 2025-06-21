package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.github.openbagtwo.lighterend.utils.Flags;
import io.github.openbagtwo.lighterend.utils.PosInfo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.tick.ScheduledTickView;

/**
 * Port of BetterEnd's Blue Vine, which isn't a vine
 */
public class Agave extends Block {

  private static final VoxelShape OUTLINE_SHAPE = createCuboidShape(4, 0, 4, 12, 16, 12);
  public static final EnumProperty<Shape> SHAPE = EnumProperty.of("shape", Shape.class);

  public Agave(Settings settings) {
    super(
        settings
            .mapColor(MapColor.LIGHT_BLUE)
            .offset(OffsetType.XZ)
            .dynamicBounds()
            .nonOpaque()
            .breakInstantly()
            .pistonBehavior(PistonBehavior.DESTROY)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F, 3.0F)
            .sounds(BlockSoundGroup.GRASS)
            .solid()
            .burnable()
    );
    this.setDefaultState(getDefaultState().with(SHAPE, Shape.MIDDLE));
  }

  @Override
  protected void appendProperties(Builder<Block, BlockState> builder) {
    builder.add(SHAPE);
  }

  @Override
  protected VoxelShape getOutlineShape(
      BlockState state, BlockView world, BlockPos pos, ShapeContext context
  ) {
    return OUTLINE_SHAPE.offset(state.getModelOffset(pos));
  }

  @Override
  protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos,
      ShapeContext context) {
    return OUTLINE_SHAPE.offset(state.getModelOffset(pos));
  }

  @Override
  protected boolean isTransparent(BlockState state) {
    return true;
  }

  @Override
  protected boolean canPathfindThrough(BlockState state, NavigationType type) {
    return false;
  }

  @Override
  protected boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
    return false;
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
      tickView.scheduleBlockTick(pos, this, 1);
    }
    return state;
  }

  @Override
  protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
    if (!canPlaceAt(state, world, pos)) {
      world.breakBlock(pos, true);
    }
  }

  @Override
  public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    BlockState down = world.getBlockState(pos.down());
    return down.isIn(LighterEndTags.AQUATIC_END_SOIL) || down.getBlock() == this;
  }

  @Override
  protected ItemStack getPickStack(
      WorldView world,
      BlockPos pos,
      BlockState state,
      boolean includeData
  ) {
    return new ItemStack(LighterEndBlocks.AGAVE_SEED);
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

  public static class Bulb extends Block {

    public static final BooleanProperty NATURAL = BooleanProperty.of("natural");

    public Bulb(Settings settings) {
      super(
          settings
              .mapColor(MapColor.BRIGHT_TEAL)
              .strength(1.0F)
              .sounds(BlockSoundGroup.FROGLIGHT)
              .luminance((bs) -> 15)
      );
      setDefaultState(stateManager.getDefaultState().with(NATURAL, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
      stateManager.add(NATURAL);
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
      if (
          !world.getBlockState(pos.down()).isOf(LighterEndBlocks.AGAVE) && state.get(NATURAL)
      ) {
        world.breakBlock(pos, true);
      }
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
        tickView.scheduleBlockTick(pos, this, 1);
      }
      return state;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
      if (!state.get(NATURAL)) {
        return true;
      }
      return world.getBlockState(pos.down()).isOf(LighterEndBlocks.AGAVE);
    }
  }

  public static class AgaveFeature extends Feature<DefaultFeatureConfig> {

    public AgaveFeature() {
      super(DefaultFeatureConfig.CODEC);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> featureConfig) {
      final Random random = featureConfig.getRandom();
      final BlockPos pos = featureConfig.getOrigin();
      final StructureWorldAccess world = featureConfig.getWorld();

      int height = MathHelper.nextInt(random, 2, 5);
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
        world.setBlockState(
            pos.up(i), LighterEndBlocks.AGAVE.getDefaultState().with(SHAPE, shape), Flags.SILENT
        );
      }
      placeBulb(world, pos.up(height + 1));
      return true;
    }

    private void placeBulb(StructureWorldAccess world, BlockPos pos) {
      world.setBlockState(
          pos,
          LighterEndBlocks.AGAVE_BULB.getDefaultState().with(Bulb.NATURAL, true),
          Flags.SILENT
      );

      for (Direction dir : DIRECTIONS) {
        if (dir == Direction.DOWN) {
          continue;
        }
        BlockPos p = pos.offset(dir);
        if (world.isAir(p)) {
          world.setBlockState(
              p,
              LighterEndBlocks.AGAVE_FUR.getDefaultState().with(Fur.FACING, dir),
              Flags.SILENT);
        }
      }
    }
  }
}
