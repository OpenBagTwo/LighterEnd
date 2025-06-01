package io.github.openbagtwo.lighterend.tags;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
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

  public static final TagKey<Block> END_SOIL_AQUATIC = TagKey.of(
      RegistryKeys.BLOCK,
      LighterEnd.of("end_soil_aquatic")
  );

  public static final TagKey<Block> END_STONES = TagKey.of(
      RegistryKeys.BLOCK,
      LighterEnd.of("end_stones")
  );

  public static final TagKey<Item> REPAIRS_SILK_ARMOR = TagKey.of(
      RegistryKeys.ITEM,
      LighterEnd.of("repairs_silk_armor")
  );

  public static final TagKey<EntityType<?>> MOTH_NEST_INHABITORS = TagKey.of(
      RegistryKeys.ENTITY_TYPE,
      LighterEnd.of("lives_in_moth_nests")
  );
}
