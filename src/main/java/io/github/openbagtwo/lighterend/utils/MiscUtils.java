package io.github.openbagtwo.lighterend.utils;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

public class MiscUtils {

  public static Boolean replaceableOrPlant(BlockState state) {
    final Block block = state.getBlock();

    if (state.getPistonPushReaction() == PushReaction.DESTROY && block.defaultDestroyTime() == 0) {
      return true;
    }

    if (state.getSoundType() == SoundType.GRASS
        || state.getSoundType() == SoundType.WET_GRASS
        || state.getSoundType() == SoundType.CROP
        || state.getSoundType() == SoundType.CAVE_VINES
    ) {
      return true;
    }

    return state.canBeReplaced();
  }
}
