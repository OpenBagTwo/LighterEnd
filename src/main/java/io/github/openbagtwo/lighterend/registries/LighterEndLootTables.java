package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class LighterEndLootTables {

  public static final RegistryKey<LootTable> END_MOSS_SPLOOT_LOOT = register(
      "gameplay/sniffer_digging_end_moss"
  );

  public static final RegistryKey<LootTable> END_FISHING = register(
      "gameplay/fishing"
  );

  private static RegistryKey<LootTable> register(String id) {
    return RegistryKey.of(RegistryKeys.LOOT_TABLE, LighterEnd.of(id));
  }

  public static void initialize() {
  }
}
