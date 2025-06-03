package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class LighterEndSounds {

  public static final SoundEvent DRAGONFLY_IDLE = register("entity.dragonfly.idle");
  public static final RegistryEntry<SoundEvent> EQUIP_SILK = registerReference(
      "item.armor.equip_silk");

  public static final RegistryEntry<SoundEvent> BLOSSOM_AMBIENT = registerReference(
      "ambient.blossom.loop");
  public static final RegistryEntry<SoundEvent> BLOSSOM_MUSIC = registerReference("music.blossom");

  public static final RegistryEntry<SoundEvent> UMBRELLA_AMBIENT = registerReference(
      "ambient.umbrella.loop");
  public static final RegistryEntry<SoundEvent> UMBRELLA_MUSIC = registerReference(
      "music.umbrella");

  public static final RegistryEntry<SoundEvent> GRASSLAND_AMBIENT = registerReference(
      "ambient.grassland.loop");
  public static final RegistryEntry<SoundEvent> GRASSLAND_MUSIC = registerReference(
      "music.grassland");

  public static final RegistryEntry<SoundEvent> LAKE_AMBIENT = registerReference(
      "ambient.lake.loop");
  public static final RegistryEntry<SoundEvent> LAKE_MUSIC = registerReference(
      "music.lake");


  public static SoundEvent register(String name) {
    Identifier id = LighterEnd.of(name);
    return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
  }

  public static RegistryEntry.Reference<SoundEvent> registerReference(String name) {
    Identifier id = LighterEnd.of(name);
    return Registry.registerReference(Registries.SOUND_EVENT, id, SoundEvent.of(id));
  }

  public static void initialize() {
  }

}
