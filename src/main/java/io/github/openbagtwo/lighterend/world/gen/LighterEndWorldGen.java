package io.github.openbagtwo.lighterend.world.gen;

import static net.minecraft.world.gen.surfacebuilder.MaterialRules.STONE_DEPTH_FLOOR;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.biome;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.block;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.condition;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.noiseThreshold;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.sequence;

import com.mojang.datafixers.util.Pair;
import io.github.openbagtwo.lighterend.config.Config;
import io.github.openbagtwo.lighterend.registries.LighterEndBiomes;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.world.gen.noise.NoiseParameters;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.biome.v1.TheEndBiomes;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.gen.surfacebuilder.MaterialRules.MaterialRule;

public class LighterEndWorldGen {

  public static BiomeSource addBiomesToNoiseSource(
      MultiNoiseUtil.Entries<RegistryEntry<Biome>> defaultBiomes,
      RegistryEntryLookup<Biome> context
  ) {
    List<Pair<MultiNoiseUtil.NoiseHypercube, RegistryEntry<Biome>>> biomeParams = new ArrayList<>();
    biomeParams.addAll(
        List.of(
            Pair.of(MultiNoiseUtil.createNoiseHypercube(
                0,
                0.3F,
                0.3F,
                0,
                0,
                -0.5F,
                0.0F
            ), context.getOrThrow(LighterEndBiomes.BLOSSOM_FOREST)),
            Pair.of(MultiNoiseUtil.createNoiseHypercube(
                MultiNoiseUtil.ParameterRange.of(0, 1),
                MultiNoiseUtil.ParameterRange.of(0.5F),
                MultiNoiseUtil.ParameterRange.of(0.5F),
                MultiNoiseUtil.ParameterRange.of(0, 1),
                MultiNoiseUtil.ParameterRange.of(-1, 1),
                MultiNoiseUtil.ParameterRange.of(-1, 1),
                0.3F
            ), context.getOrThrow(LighterEndBiomes.UMBRELLA_JUNGLE)),
            Pair.of(MultiNoiseUtil.createNoiseHypercube(
                MultiNoiseUtil.ParameterRange.of(-1, 1),
                MultiNoiseUtil.ParameterRange.of(0, 1),
                MultiNoiseUtil.ParameterRange.of(0.5F, 1),
                MultiNoiseUtil.ParameterRange.of(0.8F, 1),
                MultiNoiseUtil.ParameterRange.of(0),
                MultiNoiseUtil.ParameterRange.of(-1, 1),
                0.0F
            ), context.getOrThrow(LighterEndBiomes.MEGALAKE)),
            Pair.of(MultiNoiseUtil.createNoiseHypercube(
                MultiNoiseUtil.ParameterRange.of(-1, 1),
                MultiNoiseUtil.ParameterRange.of(-1, 1),
                MultiNoiseUtil.ParameterRange.of(-1, 1),
                MultiNoiseUtil.ParameterRange.of(0.5F, 1),
                MultiNoiseUtil.ParameterRange.of(-1.4F, 0),
                MultiNoiseUtil.ParameterRange.of(-0.5F, 0.5F),
                0.3F
            ), context.getOrThrow(LighterEndBiomes.GLOWING_GRASSLAND))
        )
    );
    biomeParams.addAll(defaultBiomes.getEntries());

    return MultiNoiseBiomeSource.create(new MultiNoiseUtil.Entries<>(biomeParams));

  }

  public static void modifyWorldGen(Config config) {
    if (config.generateBiomes()) {
      TheEndBiomes.addHighlandsBiome(LighterEndBiomes.GLOWING_GRASSLAND, 2.0);
      TheEndBiomes.addMidlandsBiome(LighterEndBiomes.GLOWING_GRASSLAND,
          LighterEndBiomes.GLOWING_GRASSLAND, 2.0);
      TheEndBiomes.addBarrensBiome(LighterEndBiomes.GLOWING_GRASSLAND,
          LighterEndBiomes.GLOWING_GRASSLAND, 2.0);
      TheEndBiomes.addSmallIslandsBiome(LighterEndBiomes.GLOWING_GRASSLAND, 1.0);

      TheEndBiomes.addHighlandsBiome(LighterEndBiomes.BLOSSOM_FOREST, 1.0);

      TheEndBiomes.addHighlandsBiome(LighterEndBiomes.UMBRELLA_JUNGLE, 1.0);
      TheEndBiomes.addMidlandsBiome(LighterEndBiomes.UMBRELLA_JUNGLE,
          LighterEndBiomes.UMBRELLA_JUNGLE, 1.0);

      TheEndBiomes.addHighlandsBiome(LighterEndBiomes.MEGALAKE, 1.0);
    }
  }

  public static MaterialRule updateSurfaceRules() {
    return sequence(
        condition(
            STONE_DEPTH_FLOOR,
            sequence(
                condition(
                    biome(
                        LighterEndBiomes.BLOSSOM_FOREST,
                        LighterEndBiomes.UMBRELLA_JUNGLE,
                        LighterEndBiomes.GLOWING_GRASSLAND
                    ),
                    sequence(
                        condition(
                            noiseThreshold(NoiseParameters.END_MOSS_SURFACE, -0.5, 0.5),
                            block(LighterEndBlocks.END_MOSS.getDefaultState())
                        )
                    )
                )
            )
        )
    );
  }

}
