package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import java.util.Optional;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SaplingGenerator;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class Sapling extends SaplingBlock {

  public final int growChance;

  public Sapling(
      Settings settings
  ) {
    this(settings, 15);
  }

  public Sapling(
      Settings settings,
      int growChance
  ) {
    super(SAPLING_GENERATOR,
        settings
            .noCollision()
            .breakInstantly()
            .sounds(BlockSoundGroup.CROP)
            .pistonBehavior(PistonBehavior.DESTROY)
            .burnable()
            .ticksRandomly()
    );
    this.growChance = growChance;
  }

  @Override
  public void generate(ServerWorld world, BlockPos pos, BlockState state, Random random) {
  }

  @Override
  protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
    return floor.isIn(LighterEndTags.END_SOIL);
  }

  @Override
  protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
    if (random.nextInt(this.growChance) == 0) {
      this.generate(world, pos, state, random);
    }
  }

  @Override
  public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
    return isAllowedToGrow(world, pos);
  }

  protected static boolean isAllowedToGrow(WorldView world, BlockPos pos) {
    return !LighterEnd.CONFIG.endPlantsOnlyGrowInTheEnd()
        || world.getBiome(pos).isIn(BiomeTags.IS_END);
  }

  private static final SaplingGenerator SAPLING_GENERATOR = new SaplingGenerator(
      LighterEnd.MOD_ID + ":sapling",
      Optional.empty(),
      Optional.empty(),
      Optional.empty()
  );
}
