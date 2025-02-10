package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import io.github.openbagtwo.lighterend.tags.LighterEndTags;
import java.util.Optional;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SaplingGenerator;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public class Lumecorn extends Block {

  public static final EnumProperty<LumecornShape> SHAPE = EnumProperty.of(
      "shape",
      LumecornShape.class
  );
  private static final VoxelShape SHAPE_BOTTOM = Block.createCuboidShape(
      6, 0, 6, 10, 16, 10
  );
  private static final VoxelShape SHAPE_TOP = Block.createCuboidShape(
      6, 0, 6, 10, 8, 10
  );

  public Lumecorn(Settings settings) {
    super(
        settings
            .mapColor(MapColor.BRIGHT_TEAL)
            .instrument(NoteBlockInstrument.BASS)
            .sounds(BlockSoundGroup.WOOD)
            .strength(0.2F)
            .burnable()
            .luminance(bs -> 15)
    );
  }

  @Override
  protected void appendProperties(Builder<Block, BlockState> builder) {
    builder.add(SHAPE);
  }

  @Override
  protected VoxelShape getOutlineShape(
      BlockState state, BlockView world, BlockPos pos, ShapeContext context
  ) {
    return state.get(SHAPE) == LumecornShape.LIGHT_TOP ? SHAPE_TOP : SHAPE_BOTTOM;
  }

  @Override
  protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    LumecornShape shape = state.get(SHAPE);
    if (shape == LumecornShape.LIGHT_TOP) {
      return (
          world.getBlockState(pos.down()).isOf(this)
              || world.getBlockState(pos.down()).isOf(LighterEndBlocks.LUMECORN_STEM)
      );
    } else {
      return (
          (
              world.getBlockState(pos.down()).isOf(this)
                  || world.getBlockState(pos.down()).isOf(LighterEndBlocks.LUMECORN_STEM)
          ) && world.getBlockState(pos.up()).isOf(this)
      );
    }
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
      return Blocks.AIR.getDefaultState();
    } else {
      return state;
    }
  }

  @Override
  protected ItemStack getPickStack(
      WorldView world,
      BlockPos pos,
      BlockState state,
      boolean includeData
  ) {
    return new ItemStack(LighterEndItems.LUMECORN_EAR);
  }

  public static class LumecornStem extends Block {

    public LumecornStem(Settings settings) {
      super(
          settings
              .mapColor(MapColor.DARK_AQUA)
              .instrument(NoteBlockInstrument.BASS)
              .sounds(BlockSoundGroup.WOOD)
              .strength(0.5F)
              .burnable()
              .luminance(bs -> 0)
      );
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
      builder.add(SHAPE);
    }

    @Override
    protected VoxelShape getOutlineShape(
        BlockState state, BlockView world, BlockPos pos, ShapeContext context
    ) {
      return SHAPE_BOTTOM;
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
      LumecornShape shape = state.get(SHAPE);
      if (shape == LumecornShape.BOTTOM_BIG || shape == LumecornShape.BOTTOM_SMALL) {
        return world.getBlockState(pos.down()).isIn(LighterEndTags.END_SOIL);
      } else {
        return (
            world.getBlockState(pos.down()).isOf(this) && (
                world.getBlockState(pos.up()).isOf(this)
                    || world.getBlockState(pos.up()).isOf(LighterEndBlocks.LUMECORN)
            )
        );
      }
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
        return Blocks.AIR.getDefaultState();
      } else {
        return state;
      }
    }

    @Override
    protected ItemStack getPickStack(
        WorldView world,
        BlockPos pos,
        BlockState state,
        boolean includeData
    ) {
      return new ItemStack(LighterEndBlocks.LUMECORN_SEED);
    }
  }

  public static class LumecornSeed extends SaplingBlock {

    public static final IntProperty AGE = IntProperty.of("age", 0, 3);

    public LumecornSeed(Settings settings) {
      super(
          LUMECORN_GENERATOR,
          settings
              .mapColor(MapColor.TEAL)
              .noCollision()
              .breakInstantly()
              .sounds(BlockSoundGroup.CROP)
              .pistonBehavior(PistonBehavior.DESTROY)
              .burnable()
              .ticksRandomly()
      );
      this.setDefaultState(this.stateManager.getDefaultState().with(AGE, Integer.valueOf(0)));
    }

    @Override
    public void generate(ServerWorld world, BlockPos pos, BlockState state, Random random) {
      if (state.get(AGE) < 3) {
        world.setBlockState(pos, state.cycle(AGE), Block.NO_REDRAW);
      } else {
        if (!world.getBlockState(pos.down()).isIn(LighterEndTags.END_SOIL)) {
          return;
        }

        int height = random.nextBetween(4, 7);
        BlockPos.Mutable mut = new BlockPos.Mutable().set(pos);
        for (int i = 1; i < height; i++) {
          mut.move(Direction.UP);
          if (!world.isAir(mut)) {
            return;
          }
        }
        mut.set(pos);
        BlockState topMiddle = LighterEndBlocks.LUMECORN.getDefaultState()
            .with(Lumecorn.SHAPE, LumecornShape.LIGHT_TOP_MIDDLE);
        BlockState middle = LighterEndBlocks.LUMECORN.getDefaultState()
            .with(Lumecorn.SHAPE, LumecornShape.LIGHT_MIDDLE);
        BlockState bottom = LighterEndBlocks.LUMECORN.getDefaultState()
            .with(Lumecorn.SHAPE, LumecornShape.LIGHT_BOTTOM);
        BlockState top = LighterEndBlocks.LUMECORN.getDefaultState().with(
            Lumecorn.SHAPE, LumecornShape.LIGHT_TOP
        );
        if (height == 4) {
          world.setBlockState(
              mut,
              LighterEndBlocks.LUMECORN_STEM.getDefaultState().with(
                  Lumecorn.SHAPE, LumecornShape.BOTTOM_SMALL
              ),
              18 // 18 = Don't trigger observers or client changes
          );
          world.setBlockState(mut.move(Direction.UP), bottom, 18);
          world.setBlockState(mut.move(Direction.UP), topMiddle, 18);
          world.setBlockState(mut.move(Direction.UP), top, 18);
          return;
        }
        if (random.nextBoolean()) {
          world.setBlockState(
              mut,
              LighterEndBlocks.LUMECORN_STEM.getDefaultState().with(
                  Lumecorn.SHAPE, LumecornShape.BOTTOM_SMALL
              ),
              18
          );
        } else {
          world.setBlockState(
              mut,
              LighterEndBlocks.LUMECORN_STEM.getDefaultState().with(
                  Lumecorn.SHAPE, LumecornShape.BOTTOM_BIG
              ),
              18
          );
          world.setBlockState(
              mut.move(Direction.UP),
              LighterEndBlocks.LUMECORN_STEM.getDefaultState().with(
                  Lumecorn.SHAPE, LumecornShape.MIDDLE
              ),
              18
          );
          height--;
        }
        world.setBlockState(mut.move(Direction.UP), bottom, 18);

        for (int i = 4; i < height; i++) {
          world.setBlockState(mut.move(Direction.UP), middle, 18);
        }
        world.setBlockState(mut.move(Direction.UP), topMiddle, 18);
        world.setBlockState(mut.move(Direction.UP), top, 18);
      }
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
      return floor.isIn(LighterEndTags.END_SOIL);
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
      if (random.nextInt(15) == 0) {
        this.generate(world, pos, state, random);
      }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      super.appendProperties(builder);
      builder.add(AGE);
    }
  }

  public enum LumecornShape implements StringIdentifiable {
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
    public String asString() {
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

  public static final SaplingGenerator LUMECORN_GENERATOR = new SaplingGenerator(
      LighterEnd.MOD_ID + ":lumecorn",
      Optional.empty(),
      Optional.empty(),  // we're completely overriding lumecorn generation
      Optional.empty()
  );
}
