package io.github.openbagtwo.lighterend.world;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.PlacedFeature;

public class LighterEndPlacedFeatures {

  public static void bootstrap(Registerable<PlacedFeature> context) {
    RegistryEntryLookup<PlacedFeature> lookup = context.getRegistryLookup(
        RegistryKeys.PLACED_FEATURE
    );
  }

  public static RegistryKey<PlacedFeature> of(String id) {
    return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(LighterEnd.MOD_ID, id));
  }

}
