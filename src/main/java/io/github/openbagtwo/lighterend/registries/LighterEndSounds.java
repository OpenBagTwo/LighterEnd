package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class LighterEndSounds {

  public static final SoundEvent MOTH_NEST_ENTER = register("block.silk_moth_nest.enter");
  public static final SoundEvent MOTH_NEST_EXIT = register("block.silk_moth_nest.exit");
  public static final SoundEvent MOTH_NEST_SHEAR = register("block.silk_moth_nest.shear");
  public static final SoundEvent MOTH_NEST_WORK = register("block.silk_moth_nest.work");
  public static final SoundEvent DRAGONFLY_IDLE = register("entity.dragonfly.idle");
  public static final SoundEvent DRAGONFLY_HURT = register("entity.dragonfly.hurt");
  public static final SoundEvent DRAGONFLY_DEATH = register("entity.dragonfly.death");
  public static final SoundEvent SILK_MOTH_IDLE = null;  // moths are silent
  public static final SoundEvent SILK_MOTH_HURT = register("entity.silk_moth.hurt");
  public static final SoundEvent SILK_MOTH_DEATH = register("entity.silk_moth.death");
  public static final SoundEvent END_FISH_FLOP = register("entity.end_fish.flop");
  public static final SoundEvent END_FISH_IDLE = register("entity.end_fish.idle");
  public static final SoundEvent END_FISH_HURT = register("entity.end_fish.hurt");
  public static final SoundEvent END_FISH_DEATH = register("entity.end_fish.death");
  public static final SoundEvent CUBOZOA_FLOP = register("entity.cubozoa.flop");
  public static final SoundEvent CUBOZOA_IDLE = register("entity.cubozoa.idle");
  public static final SoundEvent CUBOZOA_HURT = register("entity.cubozoa.hurt");
  public static final SoundEvent CUBOZOA_DEATH = register("entity.cubozoa.death");
  public static final SoundEvent CRAB_IDLE = register("entity.crab.idle");
  public static final SoundEvent CRAB_HURT = register("entity.crab.hurt");
  public static final SoundEvent CRAB_DEATH = register("entity.crab.death");
  public static final SoundEvent CRAB_STEP = register("entity.crab.step");
  public static final SoundEvent TOTEM_TELEPORT = register("item.totem_of_teleportation.teleport");
  public static final SoundEvent TP_TOTEM_TARGET_SET = register("item.totem_of_teleportation.set");
  public static final SoundEvent MATCH_STRIKE = register("item.matchstick.use");
  public static final SoundEvent WET_FUR = register("item.armor.wet_fur");

  public static final Holder<SoundEvent> EQUIP_SILK = registerReference(
      "item.armor.equip_silk");
  public static final Holder<SoundEvent> EQUIP_FUR = registerReference(
      "item.armor.equip_fur");
  public static final Holder<SoundEvent> DRINK_UMBRELLA_JUICE = registerReference(
      "item.umbrella_juice.drink");
  public static final Holder<SoundEvent> EAT_SHADOW_BERRY_JAM = registerReference(
      "item.shadow_berry_jam.drink");

  public static final Holder<SoundEvent> BLOSSOM_AMBIENT = registerReference(
      "ambient.blossom.loop");
  public static final Holder<SoundEvent> BLOSSOM_MUSIC = registerReference("music.blossom");

  public static final Holder<SoundEvent> UMBRELLA_AMBIENT = registerReference(
      "ambient.umbrella.loop");
  public static final Holder<SoundEvent> UMBRELLA_MUSIC = registerReference(
      "music.umbrella");

  public static final Holder<SoundEvent> GRASSLAND_AMBIENT = registerReference(
      "ambient.grassland.loop");
  public static final Holder<SoundEvent> GRASSLAND_MUSIC = registerReference(
      "music.grassland");

  public static final Holder<SoundEvent> LAKE_AMBIENT = registerReference(
      "ambient.lake.loop");
  public static final Holder<SoundEvent> LAKE_MUSIC = registerReference(
      "music.lake");

  public static final Holder<SoundEvent> UMBRA_VALLEY_AMBIENT = registerReference(
      "ambient.umbra_valley.loop");
  public static final Holder<SoundEvent> UMBRA_VALLEY_MUSIC = registerReference(
      "music.umbra_valley");

  public static final Holder<SoundEvent> MUSHROOMLANDS_AMBIENT = registerReference(
      "ambient.mushroomlands.loop");
  public static final Holder<SoundEvent> MUSHROOMLANDS_MUSIC = registerReference(
      "music.mushroomlands");

  public static final Holder<SoundEvent> SULPHUR_AMBIENT = registerReference(
      "ambient.sulphur.loop");
  public static final Holder<SoundEvent> SULPHUR_MUSIC = registerReference(
      "music.sulphur");

  public static final Holder<SoundEvent> SHADOW_AMBIENT = registerReference(
      "ambient.shadow.loop");
  public static final Holder<SoundEvent> SHADOW_MUSIC = registerReference(
      "music.shadow");


  public static SoundEvent register(String name) {
    Identifier id = LighterEnd.of(name);
    return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
  }

  public static Holder.Reference<SoundEvent> registerReference(String name) {
    Identifier id = LighterEnd.of(name);
    return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
  }

  public static void initialize() {
  }

}
