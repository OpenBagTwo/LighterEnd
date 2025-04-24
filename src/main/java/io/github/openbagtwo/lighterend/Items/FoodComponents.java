package io.github.openbagtwo.lighterend.Items;

import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;

public class FoodComponents {

  public static final FoodComponent POPPED_LUMECORN_NUTRITION = new FoodComponent.Builder()
      .nutrition(2)
      .saturationModifier(0.6F)
      .alwaysEdible()
      .build();

  public static final ConsumableComponent POPPED_LUMECORN_EFFECT = ConsumableComponents.food()
      .consumeSeconds(0.8F)
      .consumeEffect(
          new ApplyEffectsConsumeEffect(
              new StatusEffectInstance(StatusEffects.GLOWING, 40, 0)
          )
      ).build();

}
