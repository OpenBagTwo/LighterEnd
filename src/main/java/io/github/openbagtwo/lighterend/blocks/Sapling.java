package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealSource;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.material.PushReaction;

public class Sapling extends SaplingBlock {

  public final Supplier<Feature> treeConstructor;
  public final int growChance;

  public Sapling(
      Supplier<Feature> treeConstructor,
      Properties settings
  ) {
    this(treeConstructor, settings, 15);
  }

  public Sapling(
      Supplier<Feature> treeConstructor,
      Properties settings,
      int growChance
  ) {
    super(SAPLING_GENERATOR,
        settings
            .noCollision()
            .instabreak()
            .sound(SoundType.CROP)
            .pushReaction(PushReaction.POPPED)
            .ignitedByLava()
            .randomTicks()
    );
    this.treeConstructor = treeConstructor;
    this.growChance = growChance;
  }

  @Override
  public void advanceTree(ServerLevel world, BlockPos pos, BlockState state, RandomSource random) {
    if (!isAllowedToGrow(world, pos)) {
      return;
    }
    if (state.getValue(STAGE) == 0) {
      world.setBlock(pos, state.cycle(STAGE), Block.UPDATE_NONE);
    } else {
      this.treeConstructor.get().place(world, world.getChunkSource().getGenerator(), random, pos);
    }
  }

  @Override
  protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
    return floor.is(LighterEndTags.END_SOIL);
  }

  @Override
  protected void randomTick(BlockState state, ServerLevel world, BlockPos pos,
      RandomSource random) {
    if (random.nextInt(this.growChance) == 0) {
      this.advanceTree(world, pos, state, random);
    }
  }

  @Override
  public boolean isValidBonemealTarget(
      LevelReader world,
      BlockPos pos,
      BlockState state,
      BonemealSource source
  ) {
    return isAllowedToGrow(world, pos);
  }

  protected static boolean isAllowedToGrow(LevelReader world, BlockPos pos) {
    return !LighterEnd.CONFIG.endPlantsOnlyGrowInTheEnd()
        || world.getBiome(pos).is(BiomeTags.IS_END);
  }

  // this is a dummy to be provided to the superclass--we're overriding all of this
  private static final TreeGrower SAPLING_GENERATOR = new TreeGrower(
      LighterEnd.MOD_ID + ":sapling",
      WeightedList.of(),
      WeightedList.of(),
      WeightedList.of(),
      null
  );
}
