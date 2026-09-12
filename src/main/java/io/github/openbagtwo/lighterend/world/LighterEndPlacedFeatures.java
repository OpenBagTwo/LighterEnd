package io.github.openbagtwo.lighterend.world;

import com.mojang.datafixers.util.Pair;
import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

public class LighterEndPlacedFeatures {

  public static final ResourceKey<PlacedFeature> END_MOSS_VEGETATION = of("end_moss_vegetation");
  public static final ResourceKey<PlacedFeature> LUMECORN = of("lumecorn");
  public static final ResourceKey<PlacedFeature> TENANEA_TREE = of("tenanea_tree");
  public static final ResourceKey<PlacedFeature> MOTH_NEST = of("moth_nest");
  public static final ResourceKey<PlacedFeature> UMBRELLA_TREE = of("umbrella_tree");
  public static final ResourceKey<PlacedFeature> WATER_PLANTS = of("aquatic_end_plants");
  public static final ResourceKey<PlacedFeature> END_LILY = of("end_lily");
  public static final ResourceKey<PlacedFeature> END_LOTUS = of("end_lotus");
  public static final ResourceKey<PlacedFeature> LOTUS_LEAF = of("end_lotus_leaf");
  public static final ResourceKey<PlacedFeature> UMBRALITH_ARCH = of("umbralith_arch");
  public static final ResourceKey<PlacedFeature> UMBRALITH_ARCH_THIN = of("umbralith_arch_thin");
  public static final ResourceKey<PlacedFeature> GLOWSHROOM = of("glowshroom");
  public static final ResourceKey<PlacedFeature> AGAVE = of("agave");
  public static final List<ResourceKey<PlacedFeature>> BARRENS_ICE_STARS = List.of(
      of("barrens_ice_star_copper"),
      of("barrens_ice_star_copper_small"),
      of("barrens_ice_star_iron"),
      of("barrens_ice_star_iron_small"),
      of("barrens_ice_star_gold"),
      of("barrens_ice_star_gold_small")
  );
  public static final List<ResourceKey<PlacedFeature>> STARFIELD_ICE_STARS = List.of(
      of("starfield_ice_star_copper"),
      of("starfield_ice_star_copper_small"),
      of("starfield_ice_star_iron"),
      of("starfield_ice_star_iron_small"),
      of("starfield_ice_star_gold"),
      of("starfield_ice_star_gold_small")
  );
  public static final List<ResourceKey<PlacedFeature>> JADESTONE_BLOBS = List.of(
      of("jadestone_blob_azure"),
      of("jadestone_blob_sandy"),
      of("jadestone_blob_virid")
  );
  public static final List<ResourceKey<PlacedFeature>> JADESTONE_BLOBS_BM = List.of(
      of("jadestone_blob_azure_bm"),
      of("jadestone_blob_sandy_bm"),
      of("jadestone_blob_virid_bm")
  );

  public static final ResourceKey<PlacedFeature> AURORA_CRYSTAL = of("aurora_crystal_formation");
  public static final ResourceKey<PlacedFeature> END_STONE_REDSTONE_ORE = of(
      "end_stone_redstone_ore");
  public static final ResourceKey<PlacedFeature> END_STONE_QUARTZ_ORE = of("end_stone_quartz_ore");
  public static final ResourceKey<PlacedFeature> UMBRALITH_REDSTONE_ORE = of(
      "umbralith_redstone_ore");
  public static final ResourceKey<PlacedFeature> UMBRALITH_QUARTZ_ORE = of("umbralith_quartz_ore");

  public static final ResourceKey<PlacedFeature> SULPHUR_LAKE = of("sulphur_lake");
  public static final ResourceKey<PlacedFeature> SULPHUR_CAVE = of("sulphur_cave");
  public static final ResourceKey<PlacedFeature> SURFACE_VENT = of("surface_vent");
  public static final ResourceKey<PlacedFeature> GEYSER = of("geyser");

  public static final ResourceKey<PlacedFeature> DRAGON_TREE = of("dragon_tree");
  public static final ResourceKey<PlacedFeature> SHADOW_FOREST_VEGETATION = of(
      "shadow_forest_vegetation"
  );
  public static final ResourceKey<PlacedFeature> PURPLE_POLYPORES = of("purple_polypores");


  public static void bootstrap(BootstrapContext<PlacedFeature> context) {

    var configuredFeatures = context.lookup(Registries.FEATURE);

    context.register(
        END_MOSS_VEGETATION,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.END_MOSS_VEGETATION),
            List.of(
                CountPlacement.of(20),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
            )
        )
    );
    context.register(
        LUMECORN,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.LUMECORN),
            VegetationPlacements.treePlacement(
                PlacementUtils.countExtra(3, 0.5f, 2),
                LighterEndBlocks.LUMECORN_SEED)
        )
    );
    context.register(
        TENANEA_TREE,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.TENANEA_TREE),
            VegetationPlacements.treePlacement(
                PlacementUtils.countExtra(10, 0.5f, 2),
                LighterEndBlocks.TENANEA_SAPLING)
        )
    );
    context.register(
        MOTH_NEST,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.MOTH_NEST),
            VegetationPlacements.worldSurfaceSquaredWithCount(2)
        )
    );
    context.register(
        UMBRELLA_TREE,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.UMBRELLA_TREE),
            VegetationPlacements.treePlacement(
                PlacementUtils.countExtra(1, 0.1f, 1),
                LighterEndBlocks.UMBRELLA_TREE_SAPLING
            )
        )
    );
    context.register(
        WATER_PLANTS,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.WATER_PLANTS),
            List.of(
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                CountPlacement.of(30),
                BiomeFilter.biome()
            )
        )
    );
    context.register(
        END_LILY,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.END_LILY),
            List.of(
                CountPlacement.of(UniformInt.of(6, 18)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                BiomeFilter.biome()
            )
        )
    );
    context.register(
        END_LOTUS,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.END_LOTUS),
            List.of(
                CountPlacement.of(UniformInt.of(4, 12)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                BiomeFilter.biome()
            )
        )
    );
    context.register(
        LOTUS_LEAF,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.LOTUS_LEAF),
            List.of(
                CountPlacement.of(UniformInt.of(4, 12)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
            )
        )
    );
    context.register(
        UMBRALITH_ARCH,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.UMRBALITH_ARCH),
            List.of(
                RarityFilter.onAverageOnceEvery(20),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                InSquarePlacement.spread(),
                BiomeFilter.biome()
            )
        )
    );
    context.register(
        UMBRALITH_ARCH_THIN,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.UMRBALITH_ARCH_THIN),
            List.of(
                RarityFilter.onAverageOnceEvery(20),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                InSquarePlacement.spread(),
                BiomeFilter.biome()
            )
        )
    );
    context.register(
        GLOWSHROOM,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.GLOWSHROOM),
            VegetationPlacements.treePlacement(
                RarityFilter.onAverageOnceEvery(8),
                LighterEndBlocks.GLOWSHROOM_SAPLING
            )
        )
    );
    context.register(
        AGAVE,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.AGAVE),
            VegetationPlacements.treePlacement(
                PlacementUtils.countExtra(3, 0.5f, 2),
                LighterEndBlocks.AGAVE_SEED)
        )
    );

    for (Pair<Integer, List<ResourceKey<PlacedFeature>>> pair : List.of(
        Pair.of(32, STARFIELD_ICE_STARS),
        Pair.of(1024, BARRENS_ICE_STARS)
    )) {
      int rarity = pair.getFirst();
      List<ResourceKey<PlacedFeature>> features = pair.getSecond();

      context.register(
          features.get(0),
          new PlacedFeature(
              configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.ICE_STAR_COPPER),
              List.of(
                  RarityFilter.onAverageOnceEvery(rarity * 2),
                  HeightRangePlacement.uniform(VerticalAnchor.bottom(),
                      VerticalAnchor.absolute(256)),
                  InSquarePlacement.spread()
                  // lack of BiomePlacementModifier is intentional--the effects are dramatic
              )
          )
      );
      context.register(
          features.get(1),
          new PlacedFeature(
              configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.ICE_STAR_COPPER_SMALL),
              List.of(
                  RarityFilter.onAverageOnceEvery(rarity),
                  HeightRangePlacement.uniform(VerticalAnchor.bottom(),
                      VerticalAnchor.absolute(256)),
                  InSquarePlacement.spread()
                  // lack of BiomePlacementModifier is intentional--the effects are dramatic
              )
          )
      );
      context.register(
          features.get(2),
          new PlacedFeature(
              configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.ICE_STAR_IRON),
              List.of(
                  RarityFilter.onAverageOnceEvery(rarity * 2),
                  HeightRangePlacement.uniform(VerticalAnchor.bottom(),
                      VerticalAnchor.absolute(256)),
                  InSquarePlacement.spread()
                  // lack of BiomePlacementModifier is intentional--the effects are dramatic
              )
          )
      );
      context.register(
          features.get(3),
          new PlacedFeature(
              configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.ICE_STAR_IRON_SMALL),
              List.of(
                  RarityFilter.onAverageOnceEvery(rarity),
                  HeightRangePlacement.uniform(VerticalAnchor.bottom(),
                      VerticalAnchor.absolute(256)),
                  InSquarePlacement.spread()
                  // lack of BiomePlacementModifier is intentional--the effects are dramatic
              )
          )
      );
      context.register(
          features.get(4),
          new PlacedFeature(
              configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.ICE_STAR_GOLD),
              List.of(
                  RarityFilter.onAverageOnceEvery(rarity * 2),
                  HeightRangePlacement.uniform(VerticalAnchor.bottom(),
                      VerticalAnchor.absolute(256)),
                  InSquarePlacement.spread()
                  // lack of BiomePlacementModifier is intentional--the effects are dramatic
              )
          )
      );
      context.register(
          features.get(5),
          new PlacedFeature(
              configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.ICE_STAR_GOLD_SMALL),
              List.of(
                  RarityFilter.onAverageOnceEvery(rarity),
                  HeightRangePlacement.uniform(VerticalAnchor.bottom(),
                      VerticalAnchor.absolute(256)),
                  InSquarePlacement.spread()
                  // lack of BiomePlacementModifier is intentional--the effects are dramatic
              )
          )
      );
    }

    for (List<ResourceKey<PlacedFeature>> blobs : List.of(JADESTONE_BLOBS, JADESTONE_BLOBS_BM)) {
      for (int i = 0; i < 3; i++) {
        context.register(
            blobs.get(i),
            new PlacedFeature(
                configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.JADESTONE_BLOBS.get(i)),
                List.of(
                    CountPlacement.of(5),
                    InSquarePlacement.spread(),
                    PlacementUtils.FULL_RANGE,
                    BiomeFilter.biome()
                )
            )
        );
      }
    }
    context.register(
        AURORA_CRYSTAL,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.AURORA_CRYSTAL),
            List.of(
                RarityFilter.onAverageOnceEvery(16),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
            )
        )
    );
    context.register(
        END_STONE_REDSTONE_ORE,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.END_STONE_REDSTONE_ORE),
            List.of(
                CountPlacement.of(10),
                InSquarePlacement.spread(),
                PlacementUtils.FULL_RANGE,
                BiomeFilter.biome()
            )
        )
    );
    context.register(
        END_STONE_QUARTZ_ORE,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.END_STONE_QUARTZ_ORE),
            List.of(
                CountPlacement.of(10),
                InSquarePlacement.spread(),
                PlacementUtils.FULL_RANGE,
                BiomeFilter.biome()
            )
        )
    );
    context.register(
        UMBRALITH_REDSTONE_ORE,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.UMBRALITH_REDSTONE_ORE),
            List.of(
                CountPlacement.of(20),
                InSquarePlacement.spread(),
                PlacementUtils.FULL_RANGE,
                BiomeFilter.biome()
            )
        )
    );
    context.register(
        UMBRALITH_QUARTZ_ORE,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.UMBRALITH_QUARTZ_ORE),
            List.of(
                CountPlacement.of(20),
                InSquarePlacement.spread(),
                PlacementUtils.FULL_RANGE,
                BiomeFilter.biome()
            )
        )
    );
    context.register(
        SULPHUR_LAKE,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.SULPHUR_LAKE),
            List.of(
                RarityFilter.onAverageOnceEvery(8),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                InSquarePlacement.spread(),
                BiomeFilter.biome()
            )
        )
    );
    context.register(
        SULPHUR_CAVE,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.SULPHUR_CAVE),
            List.of(
                CountPlacement.of(2),
                PlacementUtils.FULL_RANGE,
                InSquarePlacement.spread(),
                BiomeFilter.biome()
            )
        )
    );
    context.register(
        SURFACE_VENT,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.SURFACE_VENT),
            List.of(
                RarityFilter.onAverageOnceEvery(2),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                InSquarePlacement.spread(),
                BiomeFilter.biome()
            )
        )
    );
    context.register(
        GEYSER,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.GEYSER),
            List.of(
                RarityFilter.onAverageOnceEvery(8),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                InSquarePlacement.spread(),
                BiomeFilter.biome()
            )
        )
    );
    context.register(
        SHADOW_FOREST_VEGETATION,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.SHADOW_MOSS_VEGETATION),
            List.of(
                CountPlacement.of(32),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
            )
        )
    );
    context.register(
        DRAGON_TREE,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.DRAGON_TREE),
            VegetationPlacements.treePlacement(
                PlacementUtils.countExtra(10, 0.5f, 2),
                LighterEndBlocks.DRAGON_SAPLING)
        )
    );
    context.register(
        PURPLE_POLYPORES,
        new PlacedFeature(
            configuredFeatures.getOrThrow(LighterEndConfiguredFeatures.PURPLE_POLYPORES),
            List.of(
                CountPlacement.of(5),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
            )
        )
    );


  }

  public static ResourceKey<PlacedFeature> of(String id) {
    return ResourceKey.create(Registries.PLACED_FEATURE, LighterEnd.of(id));
  }

}
