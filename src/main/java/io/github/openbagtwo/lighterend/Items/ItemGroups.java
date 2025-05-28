package io.github.openbagtwo.lighterend.Items;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public final class ItemGroups {

  public static final ItemGroup LighterEndItemGroup = register("lighterend_items",
      FabricItemGroup.builder()
          .icon(() -> new ItemStack(
              LighterEndItems.AURORA_CRYSTAL_SHARD))
          .displayName(Text.translatable("itemGroup.lighterend.lighterend_items"))
          /* Auto-registreation of all modded items cribbed from:
             https://github.com/DaRealTurtyWurty/1.21-Tutorial-Mod

             MIT License

             Copyright (c) 2024 TurtyWurty
           */
          .entries((displayContext, entries) -> Registries.ITEM.getIds()
              .stream()
              .filter(key -> key.getNamespace().equals(LighterEnd.MOD_ID) && !key.getPath()
                  .endsWith("wall_sign") && !key.getPath().endsWith("wall_hanging_sign"))
              .map(Registries.ITEM::get)
              .forEach(entries::add))
          .build());

  public static <T extends ItemGroup> T register(String name, T itemGroup) {
    return Registry.register(Registries.ITEM_GROUP, LighterEnd.of(name),
        itemGroup);
  }

  public static void initialize() {
  }

}
