package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class LighterEndSounds {

  public static final SoundEvent DRAGONFLY_IDLE = register("entity.dragonfly.idle");
  public static final SoundEvent MOTH_NEST_ENTER = register("block.silk_moth_nest.enter");
  public static final SoundEvent MOTH_NEST_EXIT = register("block.silk_moth_nest.exit");
  public static final SoundEvent MOTH_NEST_SHEAR = register("block.silk_moth_nest.shear");
  public static final SoundEvent MOTH_NEST_WORK = register("block.silk_moth_nest.work");
  public static final SoundEvent END_FISH_FLOP = register("entity.end_fish.flop");
  public static final SoundEvent END_FISH_HURT = register("entity.end_fish.hurt");
  public static final SoundEvent END_FISH_DEATH = register("entity.end_fish.death");
  public static final SoundEvent CUBOZOA_FLOP = register("entity.cubozoa.flop");
  public static final SoundEvent CRAB_HURT = register("entity.crab.hurt");
  public static final SoundEvent CRAB_DEATH = register("entity.crab.death");
  public static final SoundEvent CRAB_STEP = register("entity.crab.step");
  public static final SoundEvent TOTEM_TELEPORT = register("item.totem_of_teleportation.teleport");
  public static final SoundEvent TP_TOTEM_TARGET_SET = register("item.totem_of_teleportation.set");
  public static final SoundEvent MATCH_STRIKE = register("item.matchstick.use");

  public static final RegistryEntry<SoundEvent> EQUIP_SILK = registerReference(
      "item.armor.equip_silk");
  public static final RegistryEntry<SoundEvent> EQUIP_FUR = registerReference(
      "item.armor.equip_fur");
  public static final RegistryEntry<SoundEvent> DRINK_UMBRELLA_JUICE = registerReference(
      "item.umbrella_juice.drink");

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

  public static final RegistryEntry<SoundEvent> UMBRA_VALLEY_AMBIENT = registerReference(
      "ambient.umbra_valley.loop");
  public static final RegistryEntry<SoundEvent> UMBRA_VALLEY_MUSIC = registerReference(
      "music.umbra_valley");

  public static final RegistryEntry<SoundEvent> MUSHROOMLANDS_AMBIENT = registerReference(
      "ambient.mushroomlands.loop");
  public static final RegistryEntry<SoundEvent> MUSHROOMLANDS_MUSIC = registerReference(
      "music.mushroomlands");

  public static final RegistryEntry<SoundEvent> SULPHUR_AMBIENT = registerReference(
      "ambient.sulphur.loop");
  public static final RegistryEntry<SoundEvent> SULPHUR_MUSIC = registerReference(
      "music.sulphur");


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
