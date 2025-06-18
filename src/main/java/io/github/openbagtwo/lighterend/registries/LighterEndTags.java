package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;

public class LighterEndTags {

  public static final TagKey<Block> END_STONES = TagKey.of(
      RegistryKeys.BLOCK,
      LighterEnd.of("end_stones")
  );

  public static final TagKey<Block> END_MOSS_REPLACEABLE = TagKey.of(
      RegistryKeys.BLOCK,
      LighterEnd.of("end_moss_replaceable")
  );

  public static final TagKey<Block> END_SOIL = TagKey.of(
      RegistryKeys.BLOCK,
      LighterEnd.of("end_soil")
  );

  public static final TagKey<Block> AQUATIC_END_SOIL = TagKey.of(
      RegistryKeys.BLOCK,
      LighterEnd.of("end_soil_aquatic")
  );

  public static final TagKey<Block> AQUATIC_END_VEGETATION = TagKey.of(
      RegistryKeys.BLOCK,
      LighterEnd.of("end_vegetation_aquatic")
  );

  public static final TagKey<Item> REPAIRS_SILK_ARMOR = TagKey.of(
      RegistryKeys.ITEM,
      LighterEnd.of("repairs_silk_armor")
  );

  public static final TagKey<EntityType<?>> MOTH_NEST_INHABITORS = TagKey.of(
      RegistryKeys.ENTITY_TYPE,
      LighterEnd.of("lives_in_moth_nests")
  );

  public static final TagKey<Biome> VANILLA_END_BIOMES = TagKey.of(
      RegistryKeys.BIOME,
      LighterEnd.of("end_biomes_vanilla")
  );

  public static final TagKey<Biome> HAS_END_LAKES = TagKey.of(
      RegistryKeys.BIOME,
      LighterEnd.of("has_structure/end_lake")
  );

  public static void initialize() {
  }
}
