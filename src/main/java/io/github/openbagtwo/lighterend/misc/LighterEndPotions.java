package io.github.openbagtwo.lighterend.misc;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import net.fabricmc.fabric.api.registry.FabricPotionBrewingBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;

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

    // TODO: replace manual lists of items with item tags
    //       (gotta figure out how to get at the tag registryLookup)

    FabricPotionBrewingBuilder.BUILD.register(builder -> {
      builder.registerPotionRecipe(Potions.WATER, Ingredient.of(
              LighterEndBlocks.AURANT_POLYPORE, LighterEndBlocks.PURPLE_POLYPORE
          ),
          Potions.AWKWARD
      );
    });

    FabricPotionBrewingBuilder.BUILD.register(builder -> {
      builder.registerPotionRecipe(Potions.AWKWARD, Ingredient.of(
              LighterEndItems.AGAVE_FUR, LighterEndItems.GLOWSHROOM_FUR
          ),
          END_VEIL
      );
    });
    FabricPotionBrewingBuilder.BUILD.register(builder -> {
      builder.addMix(END_VEIL, Items.REDSTONE, LONG_END_VEIL);
    });

    FabricPotionBrewingBuilder.BUILD.register(builder -> {
      builder.registerPotionRecipe(Potions.AWKWARD, Ingredient.of(
              LighterEndItems.SHADOW_BERRY_JAM
          ),
          Potions.NIGHT_VISION
      );
    });
  }

}
