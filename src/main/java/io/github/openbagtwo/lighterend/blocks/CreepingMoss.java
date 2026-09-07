package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealSource;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class CreepingMoss extends VegetationBlock implements BonemealableBlock {

  public CreepingMoss(Properties settings) {
    super(
        settings
            .mapColor(MapColor.COLOR_LIGHT_BLUE)
            .replaceable()
            .noCollision()
            .instabreak()
            .noOcclusion()
            .sound(SoundType.GRASS)
            .pushReaction(PushReaction.POPPED)
            .offsetType(OffsetType.XZ)
            .ignitedByLava()
            .lightLevel((bs) -> 5)
    );
  }

  @Override
  protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
    return floor.is(LighterEndTags.END_SOIL);
  }


  @Override
  public boolean isValidBonemealTarget(
      LevelReader world,
      BlockPos pos,
      BlockState state,
      BonemealSource source
  ) {
    return true;
  }

  @Override
  public boolean isBonemealSuccess(
      Level world,
      RandomSource random,
      BlockPos pos,
      BlockState state,
      BonemealSource source
  ) {
    return true;
  }

  @Override
  public void performBonemeal(
      ServerLevel world,
      RandomSource random,
      BlockPos pos,
      BlockState state,
      BonemealSource source
  ) {
    popResource(world, pos, new ItemStack(this));
  }
}
