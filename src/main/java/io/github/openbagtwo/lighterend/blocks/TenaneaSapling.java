package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.tags.LighterEndTags;
import io.github.openbagtwo.lighterend.world.features.trees.TenaneaTree;
import java.util.Optional;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SaplingGenerator;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class TenaneaSapling extends SaplingBlock {

  public TenaneaSapling(Settings settings) {
    super(TENANEA_GENERATOR,
        settings
            .mapColor(MapColor.PINK)
            .noCollision()
            .breakInstantly()
            .sounds(BlockSoundGroup.CROP)
            .pistonBehavior(PistonBehavior.DESTROY)
            .burnable()
            .ticksRandomly()
    );
  }

  @Override
  public void generate(ServerWorld world, BlockPos pos, BlockState state, Random random) {
    if ((Integer) state.get(STAGE) == 0) {
      world.setBlockState(pos, state.cycle(STAGE),
          Block.SKIP_REDRAW_AND_BLOCK_ENTITY_REPLACED_CALLBACK);
    } else {
      FeatureContext<DefaultFeatureConfig> context = new FeatureContext<>(null, world,
          world.getChunkManager().getChunkGenerator(), random, pos, new DefaultFeatureConfig());
      new TenaneaTree().generate(context);
    }
  }

  @Override
  protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
    return floor.isIn(LighterEndTags.END_SOIL);
  }

  @Override
  protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
    if (random.nextInt(15) == 0) {
      this.generate(world, pos, state, random);
    }
  }

  public static final SaplingGenerator TENANEA_GENERATOR = new SaplingGenerator(
      LighterEnd.MOD_ID + ":tenanea",
      Optional.empty(),
      Optional.empty(),
      Optional.empty()
  );
}
