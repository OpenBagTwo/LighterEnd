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
  public static final RegistryKey<PlacedFeature> WATER_PLANTS = of("aquatic_end_plants");
  public static final RegistryKey<PlacedFeature> END_LILY = of("end_lily");
  public static final RegistryKey<PlacedFeature> END_LOTUS = of("end_lotus");
  public static final RegistryKey<PlacedFeature> LOTUS_LEAF = of("end_lotus_leaf");


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
    context.register(WATER_PLANTS,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.WATER_PLANTS),
            VegetationPlacedFeatures.modifiers(10)));
    context.register(END_LILY,
        new PlacedFeature(configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.END_LILY),
            VegetationPlacedFeatures.modifiers(10)));
    context.register(END_LOTUS,
        new PlacedFeature(configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.END_LOTUS),
            VegetationPlacedFeatures.modifiers(10)));
    context.register(LOTUS_LEAF,
        new PlacedFeature(configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.LOTUS_LEAF),
            VegetationPlacedFeatures.modifiers(10)));
  }

  public static RegistryKey<PlacedFeature> of(String id) {
    return RegistryKey.of(RegistryKeys.PLACED_FEATURE, LighterEnd.of(id));
  }

}
