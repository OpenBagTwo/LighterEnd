package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.utils.Flags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class Obelisk extends Block {

  private static final VoxelShape VOXEL_SHAPE_BOTTOM = Block.box(1, 0, 1, 15, 16, 15);
  private static final VoxelShape VOXEL_SHAPE_MIDDLE_TOP = Block.box(
      2, 0, 2, 14, 16, 14
  );

  public static final EnumProperty<Shape> SHAPE = EnumProperty.create("shape", Shape.class);

  public Obelisk(Properties settings) {
    super(
        settings
            .mapColor(MapColor.SAND)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .strength(-1.0F, 3600000.0F)
            .noLootTable()
            .lightLevel((state) -> state.getValue(SHAPE) == Shape.BOTTOM ? 0 : 15)
    );
  }

  @Override
  protected VoxelShape getShape(
      BlockState state, BlockGetter world, BlockPos pos, CollisionContext context
  ) {
    return (state.getValue(SHAPE) == Shape.BOTTOM) ? VOXEL_SHAPE_BOTTOM : VOXEL_SHAPE_MIDDLE_TOP;
  }

  @Override
  protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
    builder.add(SHAPE);
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
    for (int i = 0; i < 3; i++) {
      if (!world.getBlockState(pos.above(i)).canBeReplaced()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void setPlacedBy(
      Level world,
      BlockPos pos,
      BlockState state,
      @Nullable LivingEntity placer,
      ItemStack itemStack
  ) {
    state = this.defaultBlockState();
    world.setBlock(pos, state.setValue(SHAPE, Shape.BOTTOM), Flags.SILENT);
    world.setBlock(pos.above(), state.setValue(SHAPE, Shape.MIDDLE), Flags.SILENT);
    world.setBlock(pos.above(2), state.setValue(SHAPE, Shape.TOP), Flags.SILENT);
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
    Shape shape = state.getValue(SHAPE);
    if (shape == Shape.BOTTOM) {
      if (world.getBlockState(pos.above()).is(this)) {
        return state;
      } else {
        return Blocks.AIR.defaultBlockState();
      }
    } else if (shape == Shape.MIDDLE) {
      if (world.getBlockState(pos.above()).is(this) && world.getBlockState(pos.below()).is(this)) {
        return state;
      } else {
        return Blocks.AIR.defaultBlockState();
      }
    } else {
      if (world.getBlockState(pos.below()).is(this)) {
        return state;
      } else {
        return Blocks.AIR.defaultBlockState();
      }
    }
  }

  @Override
  public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
    if (player.isCreative()) {
      Shape shape = state.getValue(SHAPE);
      if (shape == Shape.MIDDLE) {
        world.setBlock(pos.below(), Blocks.AIR.defaultBlockState(), Flags.SILENT);
      } else if (shape == Shape.TOP) {
        world.setBlock(pos.below(2), Blocks.AIR.defaultBlockState(), Flags.SILENT);
      }
    }
    return super.playerWillDestroy(world, pos, state, player);
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

}
