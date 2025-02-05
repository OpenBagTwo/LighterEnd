package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class LighterEndItems {

  public static final Item AURORA_CRYSTAL_SHARD = register("aurora_crystal_shard");

  public static Item register(String name){
    return register(name, new Settings());
  }
  public static Item register(String name, Settings settings) {
    Identifier id = Identifier.of(LighterEnd.MOD_ID, name);
    RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
    return Registry.register(Registries.ITEM, key, new Item(settings.registryKey(key)));
  }

  public static void initialize() {}

}
