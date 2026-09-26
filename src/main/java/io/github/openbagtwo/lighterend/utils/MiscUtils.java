package io.github.openbagtwo.lighterend.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.sound.BlockSoundGroup;

public class MiscUtils {

  public static Boolean replaceableOrPlant(BlockState state) {
    final Block block = state.getBlock();

    if (state.getPistonBehavior() == PistonBehavior.DESTROY && block.getHardness() == 0) {
      return true;
    }

    if (state.getSoundGroup() == BlockSoundGroup.GRASS
        || state.getSoundGroup() == BlockSoundGroup.WET_GRASS
        || state.getSoundGroup() == BlockSoundGroup.CROP
        || state.getSoundGroup() == BlockSoundGroup.CAVE_VINES
    ) {
      return true;
    }

    return state.isReplaceable();
  }
}
