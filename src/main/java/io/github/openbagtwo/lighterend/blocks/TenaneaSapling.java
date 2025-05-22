package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.tags.LighterEndTags;
import java.util.Optional;
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
