package io.github.openbagtwo.lighterend.items;

import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public class LighterEndFoodComponents {

  public static final FoodProperties POPPED_LUMECORN_NUTRITION = new FoodProperties.Builder()
      .nutrition(2)
      .saturationModifier(0.6F)
      .alwaysEdible()
      .build();
  public static final Consumable POPPED_LUMECORN_EFFECT = Consumables.defaultFood()
      .consumeSeconds(0.8F)
      .onConsume(
          new ApplyStatusEffectsConsumeEffect(
              new MobEffectInstance(MobEffects.GLOWING, 40, 0)
          )
      ).build();

  public static final FoodProperties UMBRELLA_JUICE_NUTRITION = new FoodProperties.Builder()
      .nutrition(5)
      .saturationModifier(0.7F)
      .alwaysEdible()
      .build();
  public static final Consumable UMBRELLA_JUICE_EFFECT = Consumables.defaultDrink()
      .consumeSeconds(2.0F)
      .sound(LighterEndSounds.DRINK_UMBRELLA_JUICE)
      .build();

  public static final FoodProperties CRAB_MEAT = new FoodProperties.Builder()
      .nutrition(2)
      .saturationModifier(0.3F)
      .build();
  public static final Consumable RAW_CRAB_MEAT_EFFECT = Consumables.defaultFood()
      .onConsume(
          new ApplyStatusEffectsConsumeEffect(
              new MobEffectInstance(MobEffects.HUNGER, 600),
              0.5F)
      ).build();
  public static final FoodProperties CRAB_CAKE = new FoodProperties.Builder().nutrition(7)
      .saturationModifier(0.7F).build();


  public static final FoodProperties SHADOW_BERRY = new FoodProperties.Builder()
      .nutrition(4)
      .saturationModifier(0.5F)
      .build();
  public static final FoodProperties SHADOW_BERRY_COOKED = new FoodProperties.Builder()
      .nutrition(6)
      .saturationModifier(0.7F)
      .build();
  public static final FoodProperties SHADOW_BERRY_JAM = new FoodProperties.Builder()
      .nutrition(6)
      .saturationModifier(0.8F)
      .alwaysEdible()
      .build();
  public static final Consumable SHADOW_BERRY_JAM_EFFECT = Consumables.defaultDrink()
      .consumeSeconds(3.0F)
      .sound(LighterEndSounds.EAT_SHADOW_BERRY_JAM)
      .onConsume(
          new ApplyStatusEffectsConsumeEffect(
              new MobEffectInstance(MobEffects.NIGHT_VISION, 400)
          )
      ).build();
}
