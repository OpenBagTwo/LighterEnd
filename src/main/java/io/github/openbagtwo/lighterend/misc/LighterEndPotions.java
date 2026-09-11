package io.github.openbagtwo.lighterend.misc;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

public class LighterEndPotions {

  public final static Holder<Potion> END_VEIL = register(
      "end_veil", new Potion("end_veil", new MobEffectInstance(StatusEffects.END_VEIL, 3600))
  );
  public final static Holder<Potion> LONG_END_VEIL = register(
      "long_end_veil",
      new Potion("end_veil", new MobEffectInstance(StatusEffects.END_VEIL, 9600))
  );

  private static Holder<Potion> register(String name, Potion potion) {
    return Registry.registerForHolder(BuiltInRegistries.POTION, LighterEnd.of(name), potion);
  }

  public static void initialize() {
  }

}
