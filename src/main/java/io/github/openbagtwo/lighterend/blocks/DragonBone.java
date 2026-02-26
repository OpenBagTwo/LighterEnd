package io.github.openbagtwo.lighterend.blocks;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class DragonBone {

  public static Properties applySettings(Properties settings) {
    return settings
        .instrument(NoteBlockInstrument.XYLOPHONE)
        .requiresCorrectToolForDrops()
        .strength(3.0F)
        .sound(SoundType.BONE_BLOCK)
        .mapColor(MapColor.TERRACOTTA_BLACK);
  }

}
