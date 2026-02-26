package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class LighterEndLootTables {

  public static final ResourceKey<LootTable> STARTER_CHEST = register(
      "chests/spawn_chest"
  );

  public static final ResourceKey<LootTable> END_MOSS_SPLOOT_LOOT = register(
      "gameplay/sniffer_digging_end_moss"
  );

  public static final ResourceKey<LootTable> END_FISHING = register(
      "gameplay/fishing"
  );

  public static final ResourceKey<LootTable> MOOSHROOM_SHEARING = register(
      "shearing/glossy_mooshroom"
  );
  public static final ResourceKey<LootTable> MOOSHROOM_AURANT_SHEARING = register(
      "shearing/mooshroom/aurant"
  );
  public static final ResourceKey<LootTable> MOOSHROOM_PURPLE_SHEARING = register(
      "shearing/mooshroom/purple"
  );

  private static ResourceKey<LootTable> register(String id) {
    return ResourceKey.create(Registries.LOOT_TABLE, LighterEnd.of(id));
  }

  public static void initialize() {
  }
}
