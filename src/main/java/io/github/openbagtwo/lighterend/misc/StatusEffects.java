package io.github.openbagtwo.lighterend.misc;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class StatusEffects {

  public final static RegistryEntry<StatusEffect> END_VEIL = registerEffect("end_veil",
      new EndVeilEffect());


  public static class EndVeilEffect extends StatusEffect {

    public EndVeilEffect() {
      super(StatusEffectCategory.BENEFICIAL, 0x0D554A);
    }
  }

  public static <E extends StatusEffect> RegistryEntry<StatusEffect> registerEffect(String name,
      E effect) {
    return Registry.registerReference(Registries.STATUS_EFFECT, LighterEnd.of(name), effect);
  }

  public static void initialize() {
  }

}
