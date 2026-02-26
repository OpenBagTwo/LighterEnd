package io.github.openbagtwo.lighterend.world.gen;

import static net.minecraft.world.level.levelgen.SurfaceRules.ON_FLOOR;
import static net.minecraft.world.level.levelgen.SurfaceRules.VERY_DEEP_UNDER_FLOOR;
import static net.minecraft.world.level.levelgen.SurfaceRules.DEEP_UNDER_FLOOR;
import static net.minecraft.world.level.levelgen.SurfaceRules.isBiome;
import static net.minecraft.world.level.levelgen.SurfaceRules.state;
import static net.minecraft.world.level.levelgen.SurfaceRules.ifTrue;
import static net.minecraft.world.level.levelgen.SurfaceRules.noiseCondition;
import static net.minecraft.world.level.levelgen.SurfaceRules.sequence;

import com.mojang.datafixers.util.Pair;
import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.config.Config;
import io.github.openbagtwo.lighterend.registries.LighterEndBiomes;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.world.LighterEndPlacedFeatures;
import io.github.openbagtwo.lighterend.world.gen.noise.NoiseParameters;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.TheEndBiomes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.SurfaceRules.RuleSource;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class LighterEndWorldGen {

  public static BiomeSource addBiomesToNoiseSource(
      Climate.ParameterList<Holder<Biome>> defaultBiomes,
      HolderGetter<Biome> context
  ) {
    List<Pair<Climate.ParameterPoint, Holder<Biome>>> biomeParams = new ArrayList<>();
    biomeParams.addAll(
        List.of(
            Pair.of(Climate.parameters(
                Climate.Parameter.point(0),
                Climate.Parameter.point(0.3F),
                Climate.Parameter.point(0.3F),
                Climate.Parameter.point(0),
                Climate.Parameter.point(0),
                Climate.Parameter.point(-0.5F),
                0.0F
            ), context.getOrThrow(LighterEndBiomes.BLOSSOM_FOREST)),
            Pair.of(Climate.parameters(
                Climate.Parameter.span(0, 1),
                Climate.Parameter.point(0.5F),
                Climate.Parameter.point(0.5F),
                Climate.Parameter.span(0, 1),
                Climate.Parameter.span(-1, 1),
                Climate.Parameter.span(-1, 1),
                0.3F
            ), context.getOrThrow(LighterEndBiomes.UMBRELLA_JUNGLE)),
            Pair.of(Climate.parameters(
                Climate.Parameter.span(0, 1),
                Climate.Parameter.span(-0.5F, -0.2F),
                Climate.Parameter.span(-1, 0),
                Climate.Parameter.span(-1, 1),
                Climate.Parameter.span(-1.4F, 1),
                Climate.Parameter.span(-1, 1),
                0.48F
            ), context.getOrThrow(LighterEndBiomes.SHADOW_FOREST)),
            Pair.of(Climate.parameters(
                Climate.Parameter.span(-1, 1),
                Climate.Parameter.span(0, 1),
                Climate.Parameter.span(0.5F, 1),
                Climate.Parameter.span(0.8F, 1),
                Climate.Parameter.point(0),
                Climate.Parameter.span(-1, 1),
                0.0F
            ), context.getOrThrow(LighterEndBiomes.MEGALAKE)),
            Pair.of(Climate.parameters(
                Climate.Parameter.span(-1, 1),
                Climate.Parameter.span(-1, -0.5F),
                Climate.Parameter.span(-1, 1),
                Climate.Parameter.point(0),
                Climate.Parameter.point(0),
                Climate.Parameter.span(-0.2F, 0.2F),
                0.01F
            ), context.getOrThrow(LighterEndBiomes.UMBRA_VALLEY)),
            Pair.of(Climate.parameters(
                Climate.Parameter.span(-1, 1),
                Climate.Parameter.span(-0.5F, 1),
                Climate.Parameter.span(-1, 1),
                Climate.Parameter.span(0.25F, 1),
                Climate.Parameter.span(-1.4F, 1),
                Climate.Parameter.span(-1, 1),
                0.42F
            ), context.getOrThrow(LighterEndBiomes.SULPHUR_SPRINGS)),
            Pair.of(Climate.parameters(
                Climate.Parameter.span(-0.5F, 0.2F),
                Climate.Parameter.span(0.5F, 1),
                Climate.Parameter.span(0.5F, 1),
                Climate.Parameter.span(0, 0.5F),
                Climate.Parameter.point(0),
                Climate.Parameter.span(-1F, 1F),
                0.4F
            ), context.getOrThrow(LighterEndBiomes.FOGGY_MUSHROOMLANDS)),
            Pair.of(Climate.parameters(
                Climate.Parameter.span(-1, 0),
                Climate.Parameter.point(1),
                Climate.Parameter.span(0.3F, 1),
                Climate.Parameter.span(-1, -0.15F),
                Climate.Parameter.span(-1.4F, 1),
                Climate.Parameter.span(-1, 1),
                0.1F
            ), context.getOrThrow(LighterEndBiomes.STARFIELD)),
            Pair.of(Climate.parameters(
                Climate.Parameter.span(-1, 1),
                Climate.Parameter.span(-1, 1),
                Climate.Parameter.span(-1, 1),
                Climate.Parameter.span(0.5F, 1),
                Climate.Parameter.span(-1.4F, 0),
                Climate.Parameter.span(-0.5F, 0.5F),
                0.3F
            ), context.getOrThrow(LighterEndBiomes.GLOWING_GRASSLAND))
        )
    );
    biomeParams.addAll(defaultBiomes.values());

    LighterEnd.LOGGER.info(
        "Injected " + LighterEnd.MOD_NAME + "'s biomes into multinoise worldgen");

    return MultiNoiseBiomeSource.createFromList(new Climate.ParameterList<>(biomeParams));

  }

  public static void modifyWorldGen(Config config) {
    if (!config.generateBiomes()) {
      return;
    }
    TheEndBiomes.addHighlandsBiome(LighterEndBiomes.GLOWING_GRASSLAND, 2.0);
    TheEndBiomes.addMidlandsBiome(LighterEndBiomes.GLOWING_GRASSLAND,
        LighterEndBiomes.GLOWING_GRASSLAND, 2.0);
    TheEndBiomes.addSmallIslandsBiome(LighterEndBiomes.GLOWING_GRASSLAND, 1.0);

    TheEndBiomes.addHighlandsBiome(LighterEndBiomes.BLOSSOM_FOREST, 1.0);

    TheEndBiomes.addHighlandsBiome(LighterEndBiomes.SHADOW_FOREST, 1.0);

    TheEndBiomes.addHighlandsBiome(LighterEndBiomes.FOGGY_MUSHROOMLANDS, 0.5);

    TheEndBiomes.addHighlandsBiome(LighterEndBiomes.UMBRELLA_JUNGLE, 1.0);
    TheEndBiomes.addMidlandsBiome(LighterEndBiomes.UMBRELLA_JUNGLE,
        LighterEndBiomes.UMBRELLA_JUNGLE, 1.0);

    TheEndBiomes.addHighlandsBiome(LighterEndBiomes.UMBRA_VALLEY, 1.0);
    TheEndBiomes.addMidlandsBiome(LighterEndBiomes.UMBRA_VALLEY,
        LighterEndBiomes.UMBRA_VALLEY, 1.0);

    TheEndBiomes.addHighlandsBiome(LighterEndBiomes.SULPHUR_SPRINGS, 1.0);
    TheEndBiomes.addMidlandsBiome(LighterEndBiomes.SULPHUR_SPRINGS,
        LighterEndBiomes.SULPHUR_SPRINGS, 1.0);

    TheEndBiomes.addHighlandsBiome(LighterEndBiomes.MEGALAKE, 1.0);

    TheEndBiomes.addSmallIslandsBiome(LighterEndBiomes.STARFIELD, 0.1);
    for (ResourceKey<Biome> parentBiome : List.of(
        Biomes.END_HIGHLANDS,
        LighterEndBiomes.GLOWING_GRASSLAND,
        LighterEndBiomes.FOGGY_MUSHROOMLANDS,
        LighterEndBiomes.UMBRA_VALLEY,
        LighterEndBiomes.MEGALAKE,
        LighterEndBiomes.SULPHUR_SPRINGS
    )) {
      TheEndBiomes.addBarrensBiome(parentBiome, LighterEndBiomes.STARFIELD, 0.18);
      TheEndBiomes.addBarrensBiome(parentBiome, Biomes.END_BARRENS, 1);
    }

    LighterEnd.LOGGER.info("Injected " + LighterEnd.MOD_NAME + "'s biomes into Fabric worldgen");
  }

  public static void addIceStars(Config config) {
    if (!config.generateBiomes()) {
      return;
    }
    List<ResourceKey<Biome>> barrensBiomes = new ArrayList<>();
    barrensBiomes.add(Biomes.END_BARRENS);
    try {
      barrensBiomes.add(
          ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("nullscape", "void_barrens"))
      );
    } catch (NullPointerException ignored) {
    }

    for (ResourceKey<PlacedFeature> star : LighterEndPlacedFeatures.BARRENS_ICE_STARS) {
      BiomeModifications.addFeature(
          BiomeSelectors.includeByKey(barrensBiomes),
          GenerationStep.Decoration.SURFACE_STRUCTURES,
          star
      );
    }
  }

  public static void addJadestoneBlobs(Config config) {
    if (!config.generateBiomes()) {
      return;
    }
    List<ResourceKey<Biome>> biomes = new ArrayList<>();
    biomes.add(Biomes.END_HIGHLANDS);
    try {
      biomes.add(
          ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("nullscape", "crystal_peaks"))
      );
    } catch (NullPointerException ignored) {
    }

    for (ResourceKey<PlacedFeature> blob : LighterEndPlacedFeatures.JADESTONE_BLOBS_BM) {
      BiomeModifications.addFeature(
          BiomeSelectors.includeByKey(biomes),
          Decoration.UNDERGROUND_ORES,
          blob
      );
    }
  }

  public static void addOres(Config config) {
    if (!config.generateOres()) {
      return;
    }
    List<ResourceKey<Biome>> endStoneBiomes = new ArrayList<>();
    endStoneBiomes.addAll(List.of(
            LighterEndBiomes.GLOWING_GRASSLAND,
            LighterEndBiomes.BLOSSOM_FOREST,
            LighterEndBiomes.UMBRELLA_JUNGLE,
            LighterEndBiomes.MEGALAKE,
            LighterEndBiomes.FOGGY_MUSHROOMLANDS,
            LighterEndBiomes.SULPHUR_SPRINGS,
            Biomes.END_HIGHLANDS,
            Biomes.END_MIDLANDS,
            Biomes.SMALL_END_ISLANDS
        )
    );

    List<ResourceKey<Biome>> umbralithBiomes = new ArrayList<>();
    umbralithBiomes.add(LighterEndBiomes.UMBRA_VALLEY);

    try {
      endStoneBiomes.add(
          ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("nullscape", "crystal_peaks"))
      );
      endStoneBiomes.add(
          ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("nullscape", "shadowlands"))
      );
    } catch (NullPointerException ignored) {
    }

    BiomeModifications.addFeature(
        BiomeSelectors.includeByKey(endStoneBiomes),
        Decoration.UNDERGROUND_ORES,
        LighterEndPlacedFeatures.END_STONE_REDSTONE_ORE
    );
    BiomeModifications.addFeature(
        BiomeSelectors.includeByKey(endStoneBiomes),
        Decoration.UNDERGROUND_ORES,
        LighterEndPlacedFeatures.END_STONE_QUARTZ_ORE
    );
    BiomeModifications.addFeature(
        BiomeSelectors.includeByKey(umbralithBiomes),
        Decoration.UNDERGROUND_ORES,
        LighterEndPlacedFeatures.UMBRALITH_REDSTONE_ORE
    );
    BiomeModifications.addFeature(
        BiomeSelectors.includeByKey(umbralithBiomes),
        Decoration.UNDERGROUND_ORES,
        LighterEndPlacedFeatures.UMBRALITH_QUARTZ_ORE
    );
  }

  public static RuleSource updateSurfaceRules() {
    return sequence(
        ifTrue(
            ON_FLOOR,
            sequence(
                ifTrue(
                    isBiome(
                        LighterEndBiomes.BLOSSOM_FOREST,
                        LighterEndBiomes.UMBRELLA_JUNGLE,
                        LighterEndBiomes.GLOWING_GRASSLAND,
                        LighterEndBiomes.FOGGY_MUSHROOMLANDS,
                        LighterEndBiomes.SHADOW_FOREST
                    ),
                    sequence(
                        ifTrue(
                            noiseCondition(NoiseParameters.END_MOSS_SURFACE, -0.5, 0.5),
                            state(LighterEndBlocks.END_MOSS.defaultBlockState())
                        )
                    )
                )
            )
        ),
        ifTrue(
            VERY_DEEP_UNDER_FLOOR,
            sequence(
                ifTrue(
                    isBiome(
                        LighterEndBiomes.UMBRA_VALLEY
                    ),
                    sequence(
                        ifTrue(
                            noiseCondition(NoiseParameters.VIOLECITE_SURFACE, -0.05, 0.05),
                            state(LighterEndBlocks.VIOLECITE.baseBlock.defaultBlockState())
                        ),
                        state(LighterEndBlocks.UMBRALITH.baseBlock.defaultBlockState())
                    )
                )
            )
        ),
        ifTrue(
            DEEP_UNDER_FLOOR,
            sequence(
                ifTrue(
                    isBiome(
                        LighterEndBiomes.SULPHUR_SPRINGS
                    ),
                    sequence(
                        ifTrue(
                            noiseCondition(NoiseParameters.SULPHUR_SURFACE, -0.3, 0.3),
                            state(LighterEndBlocks.BORNITE.baseBlock.defaultBlockState())
                        ),
                        state(Blocks.END_STONE.defaultBlockState())
                    )
                )
            )
        )
    );
  }

}
