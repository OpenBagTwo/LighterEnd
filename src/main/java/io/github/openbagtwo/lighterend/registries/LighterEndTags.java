package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.misc.Wood.WoodSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class LighterEndTags {

  public static final TagKey<Block> END_STONES = TagKey.create(
      Registries.BLOCK,
      LighterEnd.of("end_stones")
  );

  public static final TagKey<Block> END_MOSS_REPLACEABLE = TagKey.create(
      Registries.BLOCK,
      LighterEnd.of("end_moss_replaceable")
  );

  public static final TagKey<Block> END_SOIL = TagKey.create(
      Registries.BLOCK,
      LighterEnd.of("end_soil")
  );

  public static final TagKey<Block> AQUATIC_END_SOIL = TagKey.create(
      Registries.BLOCK,
      LighterEnd.of("end_soil_aquatic")
  );

  public static final TagKey<Block> AQUATIC_END_VEGETATION = TagKey.create(
      Registries.BLOCK,
      LighterEnd.of("end_vegetation_aquatic")
  );

  public static final TagKey<Block> FURS = TagKey.create(
      Registries.BLOCK,
      LighterEnd.of("furs")
  );

  public static final TagKey<Block> SLIME_SPAWNABLE = TagKey.create(
      Registries.BLOCK,
      LighterEnd.of("slime_spawnable")
  );

  public static final TagKey<Block> GROWS_SULPHUR_CRYSTALS = TagKey.create(
      Registries.BLOCK,
      LighterEnd.of("grows_sulphur_crystals")
  );

  public static final Map<String, TagKey<Item>> LOG_TAGS = new HashMap<>();
  public static final Map<String, TagKey<Item>> STRIPPED_LOG_TAGS = new HashMap<>();

  public static final TagKey<Item> REPAIRS_SILK_ARMOR = TagKey.create(
      Registries.ITEM,
      LighterEnd.of("repairs_silk_armor")
  );

  public static final TagKey<Item> FLETCHINGS = TagKey.create(
      Registries.ITEM,
      LighterEnd.of("fletchings")
  );

  public static final TagKey<Item> FUR_ITEMS = TagKey.create(
      Registries.ITEM,
      LighterEnd.of("furs")
  );

  public static final TagKey<Item> POLYPORES = TagKey.create(
      Registries.ITEM,
      LighterEnd.of("polypores")
  );

  public static final TagKey<Item> MOOSHROOM_FOOD = TagKey.create(
      Registries.ITEM,
      LighterEnd.of("glossy_mooshroom_food")
  );

  public static final TagKey<EntityType<?>> MOTH_NEST_INHABITORS = TagKey.create(
      Registries.ENTITY_TYPE,
      LighterEnd.of("lives_in_moth_nests")
  );

  public static final TagKey<EntityType<?>> IGNORES_GEYSER_BUBBLES = TagKey.create(
      Registries.ENTITY_TYPE,
      LighterEnd.of("ignores_geyser_bubbles")
  );

  public static final TagKey<EntityType<?>> IMMUNE_TO_NEEDLEGRASS = TagKey.create(
      Registries.ENTITY_TYPE,
      LighterEnd.of("immune_to_needlegrass")
  );

  public static final TagKey<EntityType<?>> IMMUNE_TO_MURKWEED = TagKey.create(
      Registries.ENTITY_TYPE,
      LighterEnd.of("immune_to_murkweed")
  );

  public static final TagKey<Biome> VANILLA_END_BIOMES = TagKey.create(
      Registries.BIOME,
      LighterEnd.of("end_biomes_vanilla")
  );

  public static final TagKey<Biome> HAS_END_LAKES = TagKey.create(
      Registries.BIOME,
      LighterEnd.of("has_structure/end_lake")
  );

  public static final TagKey<Biome> HAS_OBELISKS = TagKey.create(
      Registries.BIOME,
      LighterEnd.of("has_structure/obelisk_chamber")
  );

  public static final TagKey<Biome> PURPLE_MOOSHROOM_BIOMES = TagKey.create(
      Registries.BIOME,
      LighterEnd.of("has_purple_mooshrooms")
  );

  public static final TagKey<Biome> INVALID_SPAWN_BIOMES = TagKey.create(
      Registries.BIOME,
      LighterEnd.of("invalid_end_spawn_biomes")
  );

  public static void initialize() {

    for (WoodSet wood : Arrays.asList(
        LighterEndBlocks.TENANEA,
        LighterEndBlocks.UMBRELLA,
        LighterEndBlocks.LOTUS,
        LighterEndBlocks.GLOWSHROOM,
        LighterEndBlocks.DRAGON
    )) {
      LOG_TAGS.put(
          wood.baseName,
          TagKey.create(Registries.ITEM, LighterEnd.of(wood.baseName + "_logs"))
      );
      STRIPPED_LOG_TAGS.put(
          wood.baseName,
          TagKey.create(Registries.ITEM, LighterEnd.of("stripped_" + wood.baseName + "_logs"))
      );
    }
  }
}
