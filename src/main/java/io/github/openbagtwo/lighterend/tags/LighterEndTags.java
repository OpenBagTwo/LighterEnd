package io.github.openbagtwo.lighterend.tags;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class LighterEndTags {

  public static final TagKey<Block> END_MOSS_REPLACEABLE = TagKey.of(
      RegistryKeys.BLOCK,
      LighterEnd.of("end_moss_replaceable")
  );

  public static final TagKey<Block> END_SOIL = TagKey.of(
      RegistryKeys.BLOCK,
      LighterEnd.of("end_soil")
  );

  public static final TagKey<Block> END_STONES = TagKey.of(
      RegistryKeys.BLOCK,
      LighterEnd.of("end_stones")
  );
}
