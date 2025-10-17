package io.github.openbagtwo.lighterend.blocks;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class Murkweed extends PlantBlock {

  public static final MapCodec<Murkweed> CODEC = createCodec(Murkweed::new);

  public Murkweed(Settings settings) {
    super(
        settings
            .mapColor(MapColor.BLACK)
            .replaceable()
            .noCollision()
            .breakInstantly()
            .nonOpaque()
            .sounds(BlockSoundGroup.GRASS)
            .pistonBehavior(PistonBehavior.DESTROY)
            .burnable()
    );
  }

  @Override
  protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
    return floor.isIn(LighterEndTags.END_SOIL);
  }

  @Override
  protected MapCodec<? extends PlantBlock> getCodec() {
    return CODEC;
  }
}
