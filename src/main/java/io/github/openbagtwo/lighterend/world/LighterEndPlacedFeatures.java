package io.github.openbagtwo.lighterend.world;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;

public class LighterEndPlacedFeatures {

  public static final RegistryKey<PlacedFeature> END_MOSS_VEGETATION = of("end_moss_vegetation");
  public static final RegistryKey<PlacedFeature> LUMECORN = of("lumecorn");
  public static final RegistryKey<PlacedFeature> TENANEA_TREE = of("tenanea_tree");
  public static final RegistryKey<PlacedFeature> MOTH_NEST = of("moth_nest");
  public static final RegistryKey<PlacedFeature> UMBRELLA_TREE = of("umbrella_tree");


  public static void bootstrap(Registerable<PlacedFeature> context) {
    RegistryEntryLookup<PlacedFeature> lookup = context.getRegistryLookup(
        RegistryKeys.PLACED_FEATURE
    );
    var configuredFeatures = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

    context.register(END_MOSS_VEGETATION,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.END_MOSS_VEGETATION),
            VegetationPlacedFeatures.modifiers(20)));
    context.register(LUMECORN,
        new PlacedFeature(configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.LUMECORN),
            VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                PlacedFeatures.createCountExtraModifier(3, 0.5f, 2),
                LighterEndBlocks.LUMECORN_SEED)));
    context.register(TENANEA_TREE,
        new PlacedFeature(configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.TENANEA_TREE),
            VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                PlacedFeatures.createCountExtraModifier(10, 0.5f, 2),
                LighterEndBlocks.TENANEA_SAPLING)));
    context.register(MOTH_NEST,
        new PlacedFeature(configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.MOTH_NEST),
            VegetationPlacedFeatures.modifiers(2)));
    context.register(UMBRELLA_TREE,
        new PlacedFeature(configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.UMBRELLA_TREE),
            VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                PlacedFeatures.createCountExtraModifier(1, 0.1f, 1),
                LighterEndBlocks.UMBRELLA_TREE_SAPLING)));
  }

  public static RegistryKey<PlacedFeature> of(String id) {
    return RegistryKey.of(RegistryKeys.PLACED_FEATURE, LighterEnd.of(id));
  }

}
