package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class LighterEndLootTables {

  public static final RegistryKey<LootTable> STARTER_CHEST = register(
      "chests/spawn_chest"
  );

  public static final RegistryKey<LootTable> END_MOSS_SPLOOT_LOOT = register(
      "gameplay/sniffer_digging_end_moss"
  );

  public static final RegistryKey<LootTable> END_FISHING = register(
      "gameplay/fishing"
  );

  public static final RegistryKey<LootTable> MOOSHROOM_SHEARING = register(
      "shearing/glossy_mooshroom"
  );
  public static final RegistryKey<LootTable> MOOSHROOM_AURANT_SHEARING = register(
      "shearing/mooshroom/aurant"
  );
  public static final RegistryKey<LootTable> MOOSHROOM_PURPLE_SHEARING = register(
      "shearing/mooshroom/purple"
  );

  private static RegistryKey<LootTable> register(String id) {
    return RegistryKey.of(RegistryKeys.LOOT_TABLE, LighterEnd.of(id));
  }

  public static void initialize() {
  }
}
