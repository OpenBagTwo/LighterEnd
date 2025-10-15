package io.github.openbagtwo.lighterend.blocks;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class ShadowBerry extends CropBlock {

  public static final MapCodec<ShadowBerry> CODEC = createCodec(ShadowBerry::new);
  private static final VoxelShape SHAPE = Block.createCuboidShape(1, 0, 1, 15, 8, 15);
  public static final int MAX_AGE = 3;
  public static final IntProperty AGE = IntProperty.of("age", 0, MAX_AGE);

  public ShadowBerry(Settings settings) {
    super(
        settings
            .mapColor(MapColor.TERRACOTTA_BLACK)
            .noCollision()
            .ticksRandomly()
            .breakInstantly()
            .sounds(BlockSoundGroup.CROP)
            .pistonBehavior(PistonBehavior.DESTROY)
    );
  }

  @Override
  public MapCodec<ShadowBerry> getCodec() {
    return CODEC;
  }

  @Override
  protected void appendProperties(Builder<Block, BlockState> builder) {
    builder.add(AGE);
  }

  @Override
  public VoxelShape getOutlineShape(
      BlockState state,
      BlockView world,
      BlockPos pos,
      ShapeContext context
  ) {
    return SHAPE;
  }

  @Override
  protected IntProperty getAgeProperty() {
    return AGE;
  }

  @Override
  public int getMaxAge() {
    return MAX_AGE;
  }

  @Override
  public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
    if (random.nextInt(3) != 0) {
      super.randomTick(state, world, pos, random);
    }
  }

  @Override
  protected ItemConvertible getSeedsItem() {
    return LighterEndItems.SHADOW_BERRY_SEEDS;
  }

  @Override
  protected int getGrowthAmount(World world) {
    return 1;
  }

  @Override
  protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    return world.getBlockState(pos.down()).isIn(LighterEndTags.END_SOIL);
  }
}
