package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealSource;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.NetherVines;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TenaneaFlower extends GrowingPlantHeadBlock {

  private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 16, 14);
  public static final BooleanProperty TIP = BlockStateProperties.TIP;
  public static final Vec3i[] COLORS;

  public TenaneaFlower(BlockBehaviour.Properties settings) {
    super(
        settings
            .mapColor(MapColor.COLOR_MAGENTA)
            .replaceable()
            .noCollision()
            .instabreak()
            .noOcclusion()
            .sound(SoundType.GRASS)
            .pushReaction(PushReaction.POPPED)
            .offsetType(OffsetType.NONE)
            .ignitedByLava()
            .randomTicks()
            .lightLevel((bs) -> 8),
        Direction.DOWN,
        SHAPE,
        false,
        0.1
    );
    this.registerDefaultState(this.stateDefinition.any().setValue(TIP, true));
  }

  @Override
  protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
    return NetherVines.getBlocksToGrowWhenBonemealed(random);
  }

  @Override
  protected boolean canGrowInto(BlockState state) {
    return NetherVines.isValidGrowthState(state);
  }

  @Override
  protected Block getBodyBlock() {
    return LighterEndBlocks.TENANEA_FLOWER;
  }

  @Override
  protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
    BlockPos blockPos = pos.relative(Direction.UP);
    BlockState blockState = world.getBlockState(blockPos);
    // TODO once Tenanea Leaves are a thing:
    // return blockState.isOf(LighterEndBlocks.TENANEA_LEAVES) || || blockState.isOf(
    //        LighterEndBlocks.TENANEA_FLOWER);
    return MultifaceBlock.canAttachTo(world, Direction.UP, blockPos, blockState) || blockState.is(
        LighterEndBlocks.TENANEA_FLOWER);
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
    super.updateShape(state, world, tickView, pos, direction, neighborPos,
        neighborState, random);

    return state.setValue(TIP, !world.getBlockState(pos.below()).is(this));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(TIP);
  }

  @Override
  public boolean isValidBonemealTarget(
      LevelReader world,
      BlockPos pos,
      BlockState state,
      BonemealSource source
  ) {
    return world.getBlockState(this.getTipPos(world, pos).below()).isAir();
  }

  public BlockPos getTipPos(BlockGetter world, BlockPos pos) {
    BlockPos.MutableBlockPos mutable = pos.mutable();

    BlockState blockState;
    do {
      mutable.move(Direction.DOWN);
      blockState = world.getBlockState(mutable);
    } while (blockState.is(this));

    return mutable.relative(Direction.UP).immutable();
  }

  @Override
  public void performBonemeal(
      ServerLevel world,
      RandomSource random,
      BlockPos pos,
      BlockState state,
      BonemealSource source
  ) {
    super.performBonemeal(world, random, pos, state, source);
    world.setBlockAndUpdate(this.getTipPos(world, pos), state.setValue(TIP, true));
  }

  @Override
  public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
    super.animateTick(state, world, pos, random);
    if (random.nextInt(32) == 0) {
      double x = (double) pos.getX() + random.nextGaussian() + 0.5;
      double z = (double) pos.getZ() + random.nextGaussian() + 0.5;
      double y = (double) pos.getY() + random.nextDouble();
      world.addParticle(LighterEndParticles.TENANEA_PETAL, x, y, z, 0, 0, 0);
    }

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
