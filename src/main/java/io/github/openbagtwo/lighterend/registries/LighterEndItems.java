package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

public class LighterEndItems {

  public static final Item AURORA_CRYSTAL_SHARD = register("aurora_crystal_shard");

  public static Item register(String name){
    return register(name, new Properties());
  }
  public static Item register(String name, Properties settings) {
    ResourceLocation id = ResourceLocation.fromNamespaceAndPath(LighterEnd.MOD_ID, name);
    ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
    return Registry.register(BuiltInRegistries.ITEM, key, new Item(settings.setId(key)));
  }

  public static void initialize() {}

}
