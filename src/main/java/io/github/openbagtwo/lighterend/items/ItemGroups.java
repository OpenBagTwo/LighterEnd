package io.github.openbagtwo.lighterend.items;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class ItemGroups {

  public static final CreativeModeTab LighterEndItemGroup = register("lighterend_items",
      FabricItemGroup.builder()
          .icon(() -> new ItemStack(
              LighterEndItems.AURORA_CRYSTAL_SHARD))
          .title(Component.translatable("itemGroup.lighterend.lighterend_items"))
          /* Auto-registreation of all modded items cribbed from:
             https://github.com/DaRealTurtyWurty/1.21-Tutorial-Mod

             MIT License

             Copyright (c) 2024 TurtyWurty
           */
          .displayItems((displayContext, entries) -> BuiltInRegistries.ITEM.keySet()
              .stream()
              .sorted()
              .filter(key -> key.getNamespace().equals(LighterEnd.MOD_ID) && !key.getPath()
                  .endsWith("wall_sign") && !key.getPath().endsWith("wall_hanging_sign"))
              .map(BuiltInRegistries.ITEM::getValue)
              .forEach(entries::accept))
          .build());

  public static <T extends CreativeModeTab> T register(String name, T itemGroup) {
    return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, LighterEnd.of(name),
        itemGroup);
  }

  public static void initialize() {
  }

}
