package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class LighterEndSounds {

  public static final SoundEvent DRAGONFLY_IDLE = register("entity.dragonfly.idle");

  private static SoundEvent register(String name) {
    Identifier id = LighterEnd.of(name);
    return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
  }

  public static void initialize() {
  }

}
