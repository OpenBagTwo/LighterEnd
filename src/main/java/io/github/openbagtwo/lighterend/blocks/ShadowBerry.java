package io.github.openbagtwo.lighterend.blocks;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShadowBerry extends CropBlock {

  public static final MapCodec<ShadowBerry> CODEC = simpleCodec(ShadowBerry::new);
  private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 8, 15);
  public static final int MAX_AGE = 3;
  public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);

  public ShadowBerry(Properties settings) {
    super(
        settings
            .mapColor(MapColor.TERRACOTTA_BLACK)
            .noCollision()
            .randomTicks()
            .instabreak()
            .sound(SoundType.CROP)
            .pushReaction(PushReaction.DESTROY)
    );
  }

  @Override
  public MapCodec<ShadowBerry> codec() {
    return CODEC;
  }

  @Override
  protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
    builder.add(AGE);
  }

  @Override
  public VoxelShape getShape(
      BlockState state,
      BlockGetter world,
      BlockPos pos,
      CollisionContext context
  ) {
    return SHAPE;
  }

  @Override
  protected IntegerProperty getAgeProperty() {
    return AGE;
  }

  @Override
  public int getMaxAge() {
    return MAX_AGE;
  }

  @Override
  public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
    if (random.nextInt(3) != 0) {
      super.randomTick(state, world, pos, random);
    }
  }

  @Override
  protected ItemLike getBaseSeedId() {
    return LighterEndBlocks.SHADOW_BERRY_SEEDS;
  }

  @Override
  protected int getBonemealAgeIncrease(Level world) {
    return 1;
  }

  @Override
  protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
    return world.getBlockState(pos.below()).is(LighterEndTags.END_SOIL);
  }
}
