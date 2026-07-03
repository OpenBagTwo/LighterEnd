package io.github.openbagtwo.lighterend.misc;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class LighterEndPotions {

  public final static RegistryEntry<Potion> END_VEIL = register(
      "end_veil", new Potion("end_veil", new StatusEffectInstance(StatusEffects.END_VEIL, 3600))
  );
  public final static RegistryEntry<Potion> LONG_END_VEIL = register(
      "long_end_veil",
      new Potion("end_veil", new StatusEffectInstance(StatusEffects.END_VEIL, 9600))
  );

  private static RegistryEntry<Potion> register(String name, Potion potion) {
    return Registry.registerReference(Registries.POTION, LighterEnd.of(name), potion);
  }

  public static void initialize() {

    // TODO: replace manual lists of items with item tags
    //       (gotta figure out how to get at the tag registryLookup)

    FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
      builder.registerPotionRecipe(Potions.WATER, Ingredient.ofItems(
              LighterEndBlocks.AURANT_POLYPORE, LighterEndBlocks.PURPLE_POLYPORE
          ),
          Potions.AWKWARD
      );
    });

    FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
      builder.registerPotionRecipe(Potions.AWKWARD, Ingredient.ofItems(
              LighterEndItems.AGAVE_FUR, LighterEndItems.GLOWSHROOM_FUR
          ),
          END_VEIL
      );
    });
    FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
      builder.registerPotionRecipe(END_VEIL, Items.REDSTONE, LONG_END_VEIL);
    });

    FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
      builder.registerPotionRecipe(Potions.AWKWARD, Ingredient.ofItems(
              LighterEndItems.SHADOW_BERRY_JAM
          ),
          Potions.NIGHT_VISION
      );
    });
  }

}
