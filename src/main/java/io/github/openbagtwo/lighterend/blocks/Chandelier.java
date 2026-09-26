package io.github.openbagtwo.lighterend.blocks;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.EnumMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
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

public class Chandelier extends Block {

  public static final EnumProperty<Direction> FACING = Properties.FACING;

  private static final EnumMap<Direction, VoxelShape> BOUNDING_SHAPES = Maps.newEnumMap(
      Direction.class);

  public Chandelier(Settings settings) {
    super(
        settings
            .mapColor(MapColor.IRON_GRAY)
            .luminance((bs) -> 15)
            .solid()
            .nonOpaque()
            .requiresTool()
            .pistonBehavior(PistonBehavior.DESTROY)
            .strength(2.5F)
            .sounds(BlockSoundGroup.CHAIN)
    );
    setDefaultState(getDefaultState().with(FACING, Direction.UP));
  }

  @Override
  public VoxelShape getOutlineShape(
      BlockState state,
      BlockView view,
      BlockPos pos,
      ShapeContext ePos
  ) {
    return BOUNDING_SHAPES.get(state.get(FACING));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
    stateManager.add(FACING);
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    BlockState blockState = getDefaultState();
    WorldView worldView = ctx.getWorld();
    BlockPos blockPos = ctx.getBlockPos();
    Direction[] directions = ctx.getPlacementDirections();
    for (Direction direction : directions) {
      Direction direction2 = direction.getOpposite();
      blockState = blockState.with(FACING, direction2);
      if (blockState.canPlaceAt(worldView, blockPos)) {
        return blockState;
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
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return state.with(FACING, rotation.rotate(state.get(FACING)));
  }

  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return state.rotate(mirror.getRotation(state.get(FACING)));
  }

  @Override
  protected boolean canPathfindThrough(BlockState state, NavigationType type) {
    return false;
  }

  static {
    BOUNDING_SHAPES.put(Direction.UP, Block.createCuboidShape(5, 0, 5, 11, 13, 11));
    BOUNDING_SHAPES.put(Direction.DOWN, Block.createCuboidShape(5, 3, 5, 11, 16, 11));
    BOUNDING_SHAPES.put(Direction.NORTH, VoxelShapes.cuboid(0.0, 0.0, 0.5, 1.0, 1.0, 1.0));
    BOUNDING_SHAPES.put(Direction.SOUTH, VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 1.0, 0.5));
    BOUNDING_SHAPES.put(Direction.WEST, VoxelShapes.cuboid(0.5, 0.0, 0.0, 1.0, 1.0, 1.0));
    BOUNDING_SHAPES.put(Direction.EAST, VoxelShapes.cuboid(0.0, 0.0, 0.0, 0.5, 1.0, 1.0));
  }

  public static class Oxidizable extends Chandelier implements net.minecraft.block.Oxidizable {

    public static final MapCodec<Oxidizable> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
                net.minecraft.block.Oxidizable.OxidationLevel.CODEC.fieldOf("weathering_state")
                    .forGetter(
                        Oxidizable::getDegradationLevel), createSettingsCodec()
            )
            .apply(instance, Oxidizable::new)
    );
    private final net.minecraft.block.Oxidizable.OxidationLevel oxidationLevel;

    @Override
    public MapCodec<Oxidizable> getCodec() {
      return CODEC;
    }

    public Oxidizable(net.minecraft.block.Oxidizable.OxidationLevel oxidationLevel,
        AbstractBlock.Settings settings) {
      super(settings);
      this.oxidationLevel = oxidationLevel;
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
      this.tickDegradation(state, world, pos, random);
    }

    @Override
    protected boolean hasRandomTicks(BlockState state) {
      return net.minecraft.block.Oxidizable.getIncreasedOxidationBlock(state.getBlock())
          .isPresent();
    }

    public net.minecraft.block.Oxidizable.OxidationLevel getDegradationLevel() {
      return this.oxidationLevel;
    }
  }

}
