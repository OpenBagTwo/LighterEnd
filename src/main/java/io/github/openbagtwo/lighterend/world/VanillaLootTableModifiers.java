package io.github.openbagtwo.lighterend.world;

import io.github.openbagtwo.lighterend.config.Config;
import io.github.openbagtwo.lighterend.registries.LighterEndMusicDiscs;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class VanillaLootTableModifiers {

  public static void patchLootTables(Config config) {
    LootTableEvents.MODIFY.register((key, tableBuilder, source, registry) -> {
          if (BuiltInLootTables.END_CITY_TREASURE.equals(key)) {
            if (config.musicDiscsAreFoundInEndCities()) {
              tableBuilder.pool(LootPool.lootPool()
                  .setRolls(UniformGenerator.between(0, 1))
                  .add(LootItem.lootTableItem(LighterEndMusicDiscs.STRANGE_AND_ALIEN))
                  .add(LootItem.lootTableItem(LighterEndMusicDiscs.GRASPING_AT_STARS))
                  .add(LootItem.lootTableItem(LighterEndMusicDiscs.ENDSEEKER))
                  .add(LootItem.lootTableItem(LighterEndMusicDiscs.EO_DRACONA))
                  .build()
              );
            }
          }
        }
    );
  }

}
