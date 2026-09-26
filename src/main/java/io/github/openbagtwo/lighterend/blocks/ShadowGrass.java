package io.github.openbagtwo.lighterend.blocks;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class ShadowGrass extends VegetationBlock implements BonemealableBlock {

  public static final MapCodec<ShadowGrass> CODEC = simpleCodec(ShadowGrass::new);

  public ShadowGrass(Properties settings) {
    super(
        settings
            .mapColor(MapColor.COLOR_BLACK)
            .replaceable()
            .noCollision()
            .instabreak()
            .noOcclusion()
            .sound(SoundType.GRASS)
            .pushReaction(PushReaction.DESTROY)
            .offsetType(OffsetType.XZ)
            .ignitedByLava()
    );
  }

  @Override
  protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
    return floor.is(LighterEndTags.END_SOIL);
  }

  @Override
  protected MapCodec<? extends VegetationBlock> codec() {
    return CODEC;
  }

  @Override
  public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
    return true;
  }

  @Override
  public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
    return true;
  }

  @Override
  public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
    popResource(world, pos, new ItemStack(this));
  }

}
