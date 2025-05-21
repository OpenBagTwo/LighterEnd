package io.github.openbagtwo.lighterend.blocks;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.MapColor;
import net.minecraft.block.MultifaceBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public class TenaneaFlowerBlock extends Block implements Fertilizable {

  public static final MapCodec<TenaneaFlowerBlock> CODEC = createCodec(TenaneaFlowerBlock::new);
  private static final VoxelShape SHAPE = Block.createCuboidShape(2, 0, 2, 14, 16, 14);
  public static final BooleanProperty TIP = Properties.TIP;
  public static final Vec3i[] COLORS;

  public TenaneaFlowerBlock(AbstractBlock.Settings settings) {
    super(
        settings
            .mapColor(MapColor.MAGENTA)
            .replaceable()
            .noCollision()
            .breakInstantly()
            .nonOpaque()
            .sounds(BlockSoundGroup.GRASS)
            .pistonBehavior(PistonBehavior.DESTROY)
            .offset(OffsetType.NONE)
            .burnable()
            .requiresTool()
            .ticksRandomly()
            .luminance((bs) -> 15)
    );
    this.setDefaultState(this.stateManager.getDefaultState().with(TIP, true));
  }

  @Override
  protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos,
      ShapeContext context) {
    return SHAPE;
  }

  @Override
  protected boolean isTransparent(BlockState state) {
    return true;
  }

  @Override
  protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    BlockPos blockPos = pos.offset(Direction.UP);
    BlockState blockState = world.getBlockState(blockPos);
    // TODO once Tenanea Leaves are a thing:
    // return blockState.isOf(LighterEndBlocks.TENANEA_LEAVES) || || blockState.isOf(
    //        LighterEndBlocks.TENANEA_FLOWER);
    return MultifaceBlock.canGrowOn(world, Direction.UP, blockPos, blockState) || blockState.isOf(
        LighterEndBlocks.TENANEA_FLOWER);
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
    if (!this.canPlaceAt(state, world, pos)) {
      tickView.scheduleBlockTick(pos, this, 1);
    }

    return state.with(TIP, !world.getBlockState(pos.down()).isOf(this));
  }

  @Override
  protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
    if (!this.canPlaceAt(state, world, pos)) {
      world.breakBlock(pos, true);
    }
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    builder.add(TIP);
  }

  @Override
  public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
    return world.getBlockState(this.getTipPos(world, pos).down()).isAir();
  }

  public BlockPos getTipPos(BlockView world, BlockPos pos) {
    BlockPos.Mutable mutable = pos.mutableCopy();

    BlockState blockState;
    do {
      mutable.move(Direction.DOWN);
      blockState = world.getBlockState(mutable);
    } while (blockState.isOf(this));

    return mutable.offset(Direction.UP).toImmutable();
  }

  @Override
  public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
    return true;
  }

  @Override
  public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
    BlockPos blockPos = this.getTipPos(world, pos).down();
    if (world.getBlockState(blockPos).isAir()) {
      world.setBlockState(blockPos, state.with(TIP, true));
    }
  }


  @Override
  public MapCodec<TenaneaFlowerBlock> getCodec() {
    return CODEC;
  }


  static {
    COLORS = new Vec3i[]{
        new Vec3i(250, 111, 222),
        new Vec3i(167, 89, 255),
        new Vec3i(120, 207, 239),
        new Vec3i(255, 87, 182)
    };
  }


}
