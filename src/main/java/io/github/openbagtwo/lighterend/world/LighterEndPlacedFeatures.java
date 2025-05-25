package io.github.openbagtwo.lighterend.world;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;

public class LighterEndPlacedFeatures {

  public static final RegistryKey<PlacedFeature> LUMECORN = of("lumecorn");
  public static final RegistryKey<PlacedFeature> TENANEA_TREE = of("tenanea_tree");
  public static final RegistryKey<PlacedFeature> UMBRELLA_TREE = of("umbrella_tree");


  public static void bootstrap(Registerable<PlacedFeature> context) {
    RegistryEntryLookup<PlacedFeature> lookup = context.getRegistryLookup(
        RegistryKeys.PLACED_FEATURE
    );
    var configuredFeatures = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
    context.register(LUMECORN,
        new PlacedFeature(configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.LUMECORN),
            VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                PlacedFeatures.createCountExtraModifier(2, 0.1f, 2),
                LighterEndBlocks.LUMECORN_SEED)));
    context.register(TENANEA_TREE,
        new PlacedFeature(configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.TENANEA_TREE),
            VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                PlacedFeatures.createCountExtraModifier(2, 0.1f, 2),
                LighterEndBlocks.TENANEA_SAPLING)));
    context.register(UMBRELLA_TREE,
        new PlacedFeature(configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.UMBRELLA_TREE),
            VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                PlacedFeatures.createCountExtraModifier(2, 0.1f, 2),
                LighterEndBlocks.UMBRELLA_TREE_SAPLING)));

  }

  public static RegistryKey<PlacedFeature> of(String id) {
    return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(LighterEnd.MOD_ID, id));
  }

}
