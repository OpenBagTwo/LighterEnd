package io.github.openbagtwo.lighterend.Items;

import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;

public class LighterEndFoodComponents {

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

  public static final FoodComponent UMBRELLA_JUICE_NUTRITION = new FoodComponent.Builder()
      .nutrition(5)
      .saturationModifier(0.7F)
      .alwaysEdible()
      .build();

  public static final ConsumableComponent UMBRELLA_JUICE_EFFECT = ConsumableComponents.drink()
      .consumeSeconds(2.0F)
      .sound(LighterEndSounds.DRINK_UMBRELLA_JUICE)
      .build();

  public static final FoodComponent CRAB_MEAT = new FoodComponent.Builder().nutrition(2)
      .saturationModifier(0.3F).build();
  public static final FoodComponent CRAB_CAKE = new FoodComponent.Builder().nutrition(7)
      .saturationModifier(0.7F).build();
}
