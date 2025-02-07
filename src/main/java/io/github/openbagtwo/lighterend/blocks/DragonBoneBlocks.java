package io.github.openbagtwo.lighterend.blocks;

import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.sound.BlockSoundGroup;

public class DragonBoneBlocks {

  public static Settings applySettings(Settings settings){
    return settings
        .instrument(NoteBlockInstrument.XYLOPHONE)
        .requiresTool()
        .strength(3.0F)
        .sounds(BlockSoundGroup.BONE)
        .mapColor(MapColor.TERRACOTTA_BLACK);
  }

}
