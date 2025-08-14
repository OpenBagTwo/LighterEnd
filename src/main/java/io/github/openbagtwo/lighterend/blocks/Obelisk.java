package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.utils.Flags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

public class Obelisk extends Block {

  private static final VoxelShape VOXEL_SHAPE_BOTTOM = Block.createCuboidShape(1, 0, 1, 15, 16, 15);
  private static final VoxelShape VOXEL_SHAPE_MIDDLE_TOP = Block.createCuboidShape(
      2, 0, 2, 14, 16, 14
  );

  public static final EnumProperty<Shape> SHAPE = EnumProperty.of("shape", Shape.class);

  public Obelisk(Settings settings) {
    super(
        settings
            .mapColor(MapColor.PALE_YELLOW)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .strength(-1.0F, 3600000.0F)
            .dropsNothing()
            .luminance((state) -> state.get(SHAPE) == Shape.BOTTOM ? 0 : 15)
    );
  }

  @Override
  protected VoxelShape getOutlineShape(
      BlockState state, BlockView world, BlockPos pos, ShapeContext context
  ) {
    return (state.get(SHAPE) == Shape.BOTTOM) ? VOXEL_SHAPE_BOTTOM : VOXEL_SHAPE_MIDDLE_TOP;
  }

  @Override
  protected void appendProperties(Builder<Block, BlockState> builder) {
    builder.add(SHAPE);
  }

  @Override
  public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    for (int i = 0; i < 3; i++) {
      if (!world.getBlockState(pos.up(i)).isReplaceable()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void onPlaced(
      World world,
      BlockPos pos,
      BlockState state,
      @Nullable LivingEntity placer,
      ItemStack itemStack
  ) {
    state = this.getDefaultState();
    world.setBlockState(pos, state.with(SHAPE, Shape.BOTTOM), Flags.SILENT);
    world.setBlockState(pos.up(), state.with(SHAPE, Shape.MIDDLE), Flags.SILENT);
    world.setBlockState(pos.up(2), state.with(SHAPE, Shape.TOP), Flags.SILENT);
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
    Shape shape = state.get(SHAPE);
    if (shape == Shape.BOTTOM) {
      if (world.getBlockState(pos.up()).isOf(this)) {
        return state;
      } else {
        return Blocks.AIR.getDefaultState();
      }
    } else if (shape == Shape.MIDDLE) {
      if (world.getBlockState(pos.up()).isOf(this) && world.getBlockState(pos.down()).isOf(this)) {
        return state;
      } else {
        return Blocks.AIR.getDefaultState();
      }
    } else {
      if (world.getBlockState(pos.down()).isOf(this)) {
        return state;
      } else {
        return Blocks.AIR.getDefaultState();
      }
    }
  }

  @Override
  public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
    if (player.isCreative()) {
      Shape shape = state.get(SHAPE);
      if (shape == Shape.MIDDLE) {
        world.setBlockState(pos.down(), Blocks.AIR.getDefaultState(), Flags.SILENT);
      } else if (shape == Shape.TOP) {
        world.setBlockState(pos.down(2), Blocks.AIR.getDefaultState(), Flags.SILENT);
      }
    }
    return super.onBreak(world, pos, state, player);
  }

  @Override
  protected ActionResult onUseWithItem(
      ItemStack stack,
      BlockState state,
      World world,
      BlockPos pos,
      PlayerEntity player,
      Hand hand,
      BlockHitResult hit
  ) {
    if (player instanceof ServerPlayerEntity serverPlayer) {
      serverPlayer.sendMessage(Text.of("Set spawn"));
    }
    return ActionResult.SUCCESS;
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

}
