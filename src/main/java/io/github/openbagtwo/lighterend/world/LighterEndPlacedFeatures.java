package io.github.openbagtwo.lighterend.world;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import java.util.List;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

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
  public static final RegistryKey<PlacedFeature> UMBRALITH_ARCH = of("umbralith_arch");
  public static final RegistryKey<PlacedFeature> UMBRALITH_ARCH_THIN = of("umbralith_arch_thin");
  public static final RegistryKey<PlacedFeature> GLOWSHROOM = of("glowshroom");
  public static final RegistryKey<PlacedFeature> AGAVE = of("agave");
  public static final RegistryKey<PlacedFeature> ICE_STAR_COPPER = of("ice_star_copper");
  public static final RegistryKey<PlacedFeature> ICE_STAR_COPPER_SMALL = of(
      "ice_star_copper_small");
  public static final RegistryKey<PlacedFeature> ICE_STAR_IRON = of("ice_star_iron");
  public static final RegistryKey<PlacedFeature> ICE_STAR_IRON_SMALL = of("ice_star_iron_small");


  public static void bootstrap(Registerable<PlacedFeature> context) {

    var configuredFeatures = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

    context.register(
        END_MOSS_VEGETATION,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.END_MOSS_VEGETATION),
            List.of(
                CountPlacementModifier.of(20),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of()
            )
        )
    );
    context.register(
        LUMECORN,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.LUMECORN),
            VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                PlacedFeatures.createCountExtraModifier(3, 0.5f, 2),
                LighterEndBlocks.LUMECORN_SEED)
        )
    );
    context.register(
        TENANEA_TREE,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.TENANEA_TREE),
            VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                PlacedFeatures.createCountExtraModifier(10, 0.5f, 2),
                LighterEndBlocks.TENANEA_SAPLING)
        )
    );
    context.register(
        MOTH_NEST,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.MOTH_NEST),
            VegetationPlacedFeatures.modifiers(2)
        )
    );
    context.register(
        UMBRELLA_TREE,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.UMBRELLA_TREE),
            VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                PlacedFeatures.createCountExtraModifier(1, 0.1f, 1),
                LighterEndBlocks.UMBRELLA_TREE_SAPLING
            )
        )
    );
    context.register(
        WATER_PLANTS,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.WATER_PLANTS),
            List.of(
                SquarePlacementModifier.of(),
                PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP,
                CountPlacementModifier.of(30),
                BiomePlacementModifier.of()
            )
        )
    );
    context.register(
        END_LILY,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.END_LILY),
            List.of(
                CountPlacementModifier.of(UniformIntProvider.create(6, 18)),
                SquarePlacementModifier.of(),
                PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP,
                BiomePlacementModifier.of()
            )
        )
    );
    context.register(
        END_LOTUS,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.END_LOTUS),
            List.of(
                CountPlacementModifier.of(UniformIntProvider.create(4, 12)),
                SquarePlacementModifier.of(),
                PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP,
                BiomePlacementModifier.of()
            )
        )
    );
    context.register(
        LOTUS_LEAF,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.LOTUS_LEAF),
            List.of(
                CountPlacementModifier.of(UniformIntProvider.create(4, 12)),
                SquarePlacementModifier.of(),
                PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
                BiomePlacementModifier.of()
            )
        )
    );
    context.register(
        UMBRALITH_ARCH,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.UMRBALITH_ARCH),
            List.of(
                RarityFilterPlacementModifier.of(20),
                PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
                BiomePlacementModifier.of()
            )
        )
    );
    context.register(
        UMBRALITH_ARCH_THIN,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.UMRBALITH_ARCH_THIN),
            List.of(
                RarityFilterPlacementModifier.of(20),
                PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
                BiomePlacementModifier.of()
            )
        )
    );
    context.register(
        GLOWSHROOM,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.GLOWSHROOM),
            VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                RarityFilterPlacementModifier.of(8),
                LighterEndBlocks.GLOWSHROOM_SAPLING
            )
        )
    );
    context.register(
        AGAVE,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.AGAVE),
            VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                PlacedFeatures.createCountExtraModifier(3, 0.5f, 2),
                LighterEndBlocks.AGAVE_SEED)
        )
    );

    context.register(
        ICE_STAR_COPPER,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.ICE_STAR_COPPER),
            List.of(
                RarityFilterPlacementModifier.of(1024),
                HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(256))
            )
        )
    );
    context.register(
        ICE_STAR_COPPER_SMALL,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.ICE_STAR_COPPER_SMALL),
            List.of(
                RarityFilterPlacementModifier.of(512),
                HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(256))
            )
        )
    );
    context.register(
        ICE_STAR_IRON,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.ICE_STAR_IRON),
            List.of(
                RarityFilterPlacementModifier.of(1024),
                HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(256))
            )
        )
    );
    context.register(
        ICE_STAR_IRON_SMALL,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.ICE_STAR_IRON_SMALL),
            List.of(
                RarityFilterPlacementModifier.of(512),
                HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(256))
            )
        )
    );
  }

  public static RegistryKey<PlacedFeature> of(String id) {
    return RegistryKey.of(RegistryKeys.PLACED_FEATURE, LighterEnd.of(id));
  }

}
