package io.github.openbagtwo.lighterend.misc;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class StatusEffects {

  public final static Holder<MobEffect> END_VEIL = registerEffect("end_veil",
      new EndVeilEffect());


  public static class EndVeilEffect extends MobEffect {

    public EndVeilEffect() {
      super(MobEffectCategory.BENEFICIAL, 0x0D554A);
    }
  }

  public static <E extends MobEffect> Holder<MobEffect> registerEffect(String name,
      E effect) {
    return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, LighterEnd.of(name), effect);
  }

  public static void initialize() {
  }

}
