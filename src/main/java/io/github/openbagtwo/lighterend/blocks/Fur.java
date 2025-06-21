package io.github.openbagtwo.lighterend.blocks;

import com.google.common.collect.Maps;
import java.util.EnumMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.NotNull;

public class Fur extends Block implements Waterloggable {

  private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
  public static final EnumProperty<Direction> FACING = Properties.FACING;
  private static final EnumMap<Direction, VoxelShape> BOUNDING_SHAPES = Maps.newEnumMap(
      Direction.class);

  static {
    BOUNDING_SHAPES.put(Direction.UP, VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 0.5, 1.0));
    BOUNDING_SHAPES.put(Direction.DOWN, VoxelShapes.cuboid(0.0, 0.5, 0.0, 1.0, 1.0, 1.0));
    BOUNDING_SHAPES.put(Direction.NORTH, VoxelShapes.cuboid(0.0, 0.0, 0.5, 1.0, 1.0, 1.0));
    BOUNDING_SHAPES.put(Direction.SOUTH, VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 1.0, 0.5));
    BOUNDING_SHAPES.put(Direction.WEST, VoxelShapes.cuboid(0.5, 0.0, 0.0, 1.0, 1.0, 1.0));
    BOUNDING_SHAPES.put(Direction.EAST, VoxelShapes.cuboid(0.0, 0.0, 0.0, 0.5, 1.0, 1.0));
  }

  public Fur(Settings settings, MapColor color, int luminance, boolean wet) {
    super(
        settings
            .mapColor(color)
            .replaceable()
            .noCollision()
            .breakInstantly()
            .burnable()
            .pistonBehavior(PistonBehavior.DESTROY)
            .luminance(bs -> luminance)
            .burnable()
            .sounds(wet ? BlockSoundGroup.WET_GRASS : BlockSoundGroup.GRASS)
    );
    setDefaultState(getDefaultState().with(WATERLOGGED, false).with(FACING, Direction.UP));
  }

  @Override
  public @NotNull VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos,
      ShapeContext ePos) {
    return BOUNDING_SHAPES.get(state.get(FACING));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    builder.add(WATERLOGGED);
    builder.add(FACING);
  }

  @Override
  protected FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    BlockState blockState = super.getPlacementState(ctx);
    if (blockState != null) {
      FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
      blockState = blockState.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
      for (Direction direction : ctx.getPlacementDirections()) {
        blockState = blockState.with(FACING, direction.getOpposite());
        if (blockState.canPlaceAt(ctx.getWorld(), ctx.getBlockPos())) {
          return blockState;
        }
      }
    }
    return null;
  }

  @Override
  public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    Direction direction = state.get(FACING);
    BlockPos blockPos = pos.offset(direction.getOpposite());
    return sideCoversSmallSquare(world, blockPos, direction) || world.getBlockState(blockPos)
        .isIn(BlockTags.LEAVES);
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
      return Blocks.AIR.getDefaultState();
    } else {
      return state;
    }
  }

  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return state.with(FACING, rotation.rotate(state.get(FACING)));
  }

  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return state.rotate(mirror.getRotation(state.get(FACING)));
  }
}
