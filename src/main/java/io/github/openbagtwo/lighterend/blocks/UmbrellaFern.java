package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShortPlantBlock;
import net.minecraft.block.TallFlowerBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class UmbrellaFern extends ShortPlantBlock {

  public UmbrellaFern(Settings settings) {
    super(
        settings
            .mapColor(MapColor.ORANGE)
            .replaceable()
            .noCollision()
            .breakInstantly()
            .nonOpaque()
            .sounds(BlockSoundGroup.GRASS)
            .pistonBehavior(PistonBehavior.DESTROY)
            .offset(OffsetType.XZ)
            .burnable()
            .luminance((bs) -> 2)
    );
  }

  @Override
  protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
    return floor.isIn(LighterEndTags.END_SOIL);
  }

  @Override
  public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
    return getLargeVariant(state).getDefaultState().canPlaceAt(world, pos) && world.isAir(pos.up());
  }

  @Override
  public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
    TallPlantBlock.placeAt(world, getLargeVariant(state).getDefaultState(), pos, 2);
  }

  private static TallPlantBlock getLargeVariant(BlockState state) {
    return (TallPlantBlock) LighterEndBlocks.TALL_UMBRELLA_FERN;
  }

  public static class TallUmbrellaFern extends TallFlowerBlock {

    public TallUmbrellaFern(Settings settings) {
      super(
          settings
              .mapColor(MapColor.ORANGE)
              .replaceable()
              .noCollision()
              .breakInstantly()
              .nonOpaque()
              .sounds(BlockSoundGroup.GRASS)
              .pistonBehavior(PistonBehavior.DESTROY)
              .offset(OffsetType.NONE)
              .burnable()
              .luminance((bs) -> 8)
      );
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
      dropStack(world, pos, new ItemStack(LighterEndBlocks.UMBRELLA_FERN));
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
      return floor.isIn(LighterEndTags.END_SOIL);
    }

    // TODO: rotated variants

  }


}
