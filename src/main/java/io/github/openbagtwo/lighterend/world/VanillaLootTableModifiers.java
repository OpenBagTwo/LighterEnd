package io.github.openbagtwo.lighterend.world;

import io.github.openbagtwo.lighterend.config.Config;
import io.github.openbagtwo.lighterend.registries.LighterEndMusicDiscs;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;

public class VanillaLootTableModifiers {

  public static void patchLootTables(Config config) {
    LootTableEvents.MODIFY.register((key, tableBuilder, source, registry) -> {
          if (LootTables.END_CITY_TREASURE_CHEST.equals(key)) {
            if (config.getMusicDiscsInEndCitiesSetting()) {
              tableBuilder.pool(LootPool.builder()
                  .rolls(UniformLootNumberProvider.create(0, 1))
                  .with(ItemEntry.builder(LighterEndMusicDiscs.STRANGE_AND_ALIEN))
                  .with(ItemEntry.builder(LighterEndMusicDiscs.GRASPING_AT_STARS))
                  .with(ItemEntry.builder(LighterEndMusicDiscs.ENDSEEKER))
                  .with(ItemEntry.builder(LighterEndMusicDiscs.EO_DRACONA))
                  .build()
              );
            }
          }
        }
    );
  }

}
