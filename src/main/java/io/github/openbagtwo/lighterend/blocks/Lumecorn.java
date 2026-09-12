package io.github.openbagtwo.lighterend.blocks;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.github.openbagtwo.lighterend.utils.Flags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealSource;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Lumecorn extends Block {

  public static final EnumProperty<LumecornShape> SHAPE = EnumProperty.create(
      "shape",
      LumecornShape.class
  );
  private static final VoxelShape SHAPE_BOTTOM = Block.box(
      6, 0, 6, 10, 16, 10
  );
  private static final VoxelShape SHAPE_TOP = Block.box(
      6, 0, 6, 10, 8, 10
  );

  public Lumecorn(Properties settings) {
    super(
        settings
            .mapColor(MapColor.WARPED_WART_BLOCK)
            .instrument(NoteBlockInstrument.BASS)
            .sound(SoundType.WOOD)
            .strength(0.2F)
            .ignitedByLava()
            .lightLevel(bs -> bs.getValue(SHAPE).getLight())
    );
  }

  @Override
  protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
    builder.add(SHAPE);
  }

  @Override
  protected VoxelShape getShape(
      BlockState state, BlockGetter world, BlockPos pos, CollisionContext context
  ) {
    return state.getValue(SHAPE) == LumecornShape.LIGHT_TOP ? SHAPE_TOP : SHAPE_BOTTOM;
  }

  @Override
  protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
    LumecornShape shape = state.getValue(SHAPE);
    if (shape == LumecornShape.LIGHT_TOP) {
      return (
          world.getBlockState(pos.below()).is(this)
              || world.getBlockState(pos.below()).is(LighterEndBlocks.LUMECORN_STEM)
      );
    } else {
      return (
          (
              world.getBlockState(pos.below()).is(this)
                  || world.getBlockState(pos.below()).is(LighterEndBlocks.LUMECORN_STEM)
          ) && world.getBlockState(pos.above()).is(this)
      );
    }
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
      return Blocks.AIR.defaultBlockState();
    } else {
      return state;
    }
  }

  @Override
  protected ItemStack getCloneItemStack(
      LevelReader world,
      BlockPos pos,
      BlockState state,
      boolean includeData
  ) {
    return new ItemStack(LighterEndItems.LUMECORN_EAR);
  }

  public static class LumecornStem extends Block {

    public LumecornStem(Properties settings) {
      super(
          settings
              .mapColor(MapColor.WARPED_STEM)
              .instrument(NoteBlockInstrument.BASS)
              .sound(SoundType.WOOD)
              .strength(0.5F)
              .ignitedByLava()
              .lightLevel(bs -> 0)
      );
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(SHAPE);
    }

    @Override
    protected VoxelShape getShape(
        BlockState state, BlockGetter world, BlockPos pos, CollisionContext context
    ) {
      return SHAPE_BOTTOM;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
      LumecornShape shape = state.getValue(SHAPE);
      if (shape == LumecornShape.BOTTOM_BIG || shape == LumecornShape.BOTTOM_SMALL) {
        return world.getBlockState(pos.below()).is(LighterEndTags.END_SOIL);
      } else {
        return (
            world.getBlockState(pos.below()).is(this) && (
                world.getBlockState(pos.above()).is(this)
                    || world.getBlockState(pos.above()).is(LighterEndBlocks.LUMECORN)
            )
        );
      }
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
        return Blocks.AIR.defaultBlockState();
      } else {
        return state;
      }
    }

    @Override
    protected ItemStack getCloneItemStack(
        LevelReader world,
        BlockPos pos,
        BlockState state,
        boolean includeData
    ) {
      return new ItemStack(LighterEndBlocks.LUMECORN_SEED);
    }
  }

  public static class LumecornSeed extends SaplingBlock {

    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);

    public LumecornSeed(Properties settings) {
      super(
          LUMECORN_GENERATOR,
          settings
              .mapColor(MapColor.WARPED_NYLIUM)
              .noCollision()
              .instabreak()
              .sound(SoundType.CROP)
              .pushReaction(PushReaction.POPPED)
              .ignitedByLava()
              .randomTicks()
      );
      this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    public void advanceTree(ServerLevel world, BlockPos pos, BlockState state,
        RandomSource random) {
      if (!isAllowedToGrow(world, pos)) {
        return;
      }
      if (state.getValue(AGE) < 3) {
        world.setBlock(pos, state.cycle(AGE), Block.UPDATE_INVISIBLE);
      } else {
        if (!world.getBlockState(pos.below()).is(LighterEndTags.END_SOIL)) {
          return;
        }
        new LumecornFeature().place(world, world.getChunkSource().getGenerator(), random, pos);
      }
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
      return floor.is(LighterEndTags.END_SOIL);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel world, BlockPos pos,
        RandomSource random) {
      if (random.nextInt(3) == 0) {
        this.advanceTree(world, pos, state, random);
      }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      super.createBlockStateDefinition(builder);
      builder.add(AGE);
    }

    @Override
    public boolean isValidBonemealTarget(
        final LevelReader world,
        final BlockPos pos,
        final BlockState state,
        final BonemealSource source
    ) {
      return isAllowedToGrow(world, pos);
    }

    private static boolean isAllowedToGrow(LevelReader world, BlockPos pos) {
      return !LighterEnd.CONFIG.endPlantsOnlyGrowInTheEnd()
          || world.getBiome(pos).is(BiomeTags.IS_END);
    }
  }

  public enum LumecornShape implements StringRepresentable {
    LIGHT_TOP("light_top", 15),
    LIGHT_TOP_MIDDLE("light_top_middle", 15),
    LIGHT_MIDDLE("light_middle", 15),
    LIGHT_BOTTOM("light_bottom", 15),
    MIDDLE("middle", 0),
    BOTTOM_BIG("bottom_big", 0),
    BOTTOM_SMALL("bottom_small", 0);

    private final String name;
    private final int light;

    LumecornShape(String name, int light) {
      this.name = name;
      this.light = light;
    }

    @Override
    public String getSerializedName() {
      return name;
    }

    @Override
    public String toString() {
      return name;
    }

    public int getLight() {
      return light;
    }
  }

  public static class LumecornFeature implements Feature {

    public LumecornFeature() {
    }

    public static final MapCodec<LumecornFeature> CODEC = MapCodec.unit(LumecornFeature::new);

    @Override
    public MapCodec<LumecornFeature> codec() {
      return CODEC;
    }

    @Override
    public boolean place(
        final WorldGenLevel world,
        final ChunkGenerator chunkGenerator,
        final RandomSource random,
        final BlockPos pos
    ) {
      int height = random.nextIntBetweenInclusive(4, 7);
      BlockPos.MutableBlockPos mut = new BlockPos.MutableBlockPos().set(pos);
      for (int i = 1; i < height; i++) {
        mut.move(Direction.UP);
        if (!world.isEmptyBlock(mut)) {
          return false;
        }
      }
      mut.set(pos);
      BlockState topMiddle = LighterEndBlocks.LUMECORN.defaultBlockState()
          .setValue(Lumecorn.SHAPE, LumecornShape.LIGHT_TOP_MIDDLE);
      BlockState middle = LighterEndBlocks.LUMECORN.defaultBlockState()
          .setValue(Lumecorn.SHAPE, LumecornShape.LIGHT_MIDDLE);
      BlockState bottom = LighterEndBlocks.LUMECORN.defaultBlockState()
          .setValue(Lumecorn.SHAPE, LumecornShape.LIGHT_BOTTOM);
      BlockState top = LighterEndBlocks.LUMECORN.defaultBlockState().setValue(
          Lumecorn.SHAPE, LumecornShape.LIGHT_TOP
      );
      if (height == 4) {
        world.setBlock(
            mut,
            LighterEndBlocks.LUMECORN_STEM.defaultBlockState().setValue(
                Lumecorn.SHAPE, LumecornShape.BOTTOM_SMALL
            ),
            Flags.SILENT
        );
        world.setBlock(mut.move(Direction.UP), bottom, 18);
        world.setBlock(mut.move(Direction.UP), topMiddle, 18);
        world.setBlock(mut.move(Direction.UP), top, 18);
        return true;
      }
      if (random.nextBoolean()) {
        world.setBlock(
            mut,
            LighterEndBlocks.LUMECORN_STEM.defaultBlockState().setValue(
                Lumecorn.SHAPE, LumecornShape.BOTTOM_SMALL
            ),
            18
        );
      } else {
        world.setBlock(
            mut,
            LighterEndBlocks.LUMECORN_STEM.defaultBlockState().setValue(
                Lumecorn.SHAPE, LumecornShape.BOTTOM_BIG
            ),
            18
        );
        world.setBlock(
            mut.move(Direction.UP),
            LighterEndBlocks.LUMECORN_STEM.defaultBlockState().setValue(
                Lumecorn.SHAPE, LumecornShape.MIDDLE
            ),
            18
        );
        height--;
      }
      world.setBlock(mut.move(Direction.UP), bottom, 18);

      for (int i = 4; i < height; i++) {
        world.setBlock(mut.move(Direction.UP), middle, 18);
      }
      world.setBlock(mut.move(Direction.UP), topMiddle, 18);
      world.setBlock(mut.move(Direction.UP), top, 18);
      return true;
    }
  }

  public static final TreeGrower LUMECORN_GENERATOR = new TreeGrower(
      LighterEnd.MOD_ID + ":lumecorn",
      WeightedList.of(), // we're completely overriding lumecorn generation
      WeightedList.of(),
      WeightedList.of(),
      null
  );
}
