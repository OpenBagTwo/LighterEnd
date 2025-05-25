package io.github.openbagtwo.lighterend.tags;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class LighterEndTags {

  public static final TagKey<Block> END_MOSS_REPLACEABLE = TagKey.of(
      RegistryKeys.BLOCK,
      Identifier.of(LighterEnd.MOD_ID, "end_moss_replaceable")
  );

  public static final TagKey<Block> END_SOIL = TagKey.of(
      RegistryKeys.BLOCK,
      Identifier.of(LighterEnd.MOD_ID, "end_soil")
  );

  public static final TagKey<Block> END_STONES = TagKey.of(
      RegistryKeys.BLOCK,
      Identifier.of(LighterEnd.MOD_ID, "end_stones")
  );
}
