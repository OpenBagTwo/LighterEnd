package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealSource;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class UmbrellaFern extends TallGrassBlock {

  public UmbrellaFern(Properties settings) {
    super(
        settings
            .mapColor(MapColor.COLOR_ORANGE)
            .replaceable()
            .noCollision()
            .instabreak()
            .noOcclusion()
            .sound(SoundType.GRASS)
            .pushReaction(PushReaction.POPPED)
            .offsetType(OffsetType.XZ)
            .ignitedByLava()
            .lightLevel((bs) -> 2)
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
    return getGrownBlock(state).defaultBlockState().canSurvive(world, pos) && world.isEmptyBlock(
        pos.above());
  }

  @Override
  public void performBonemeal(
      ServerLevel world,
      RandomSource random,
      BlockPos pos,
      BlockState state,
      BonemealSource source
  ) {
    DoublePlantBlock.placeAt(world, getGrownBlock(state).defaultBlockState(), pos, 2);
  }

  private static DoublePlantBlock getGrownBlock(BlockState state) {
    return (DoublePlantBlock) LighterEndBlocks.TALL_UMBRELLA_FERN;
  }

  public static class TallUmbrellaFern extends TallFlowerBlock {

    public TallUmbrellaFern(Properties settings) {
      super(
          settings
              .mapColor(MapColor.COLOR_ORANGE)
              .replaceable()
              .noCollision()
              .instabreak()
              .noOcclusion()
              .sound(SoundType.GRASS)
              .pushReaction(PushReaction.POPPED)
              .offsetType(OffsetType.NONE)
              .ignitedByLava()
              .lightLevel((bs) -> 8)
      );
    }

    @Override
    public void performBonemeal(
        ServerLevel world,
        RandomSource random,
        BlockPos pos,
        BlockState state,
        BonemealSource source
    ) {
      popResource(world, pos, new ItemStack(LighterEndBlocks.UMBRELLA_FERN));
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
      return floor.is(LighterEndTags.END_SOIL);
    }

    // TODO: rotated variants

  }


}
