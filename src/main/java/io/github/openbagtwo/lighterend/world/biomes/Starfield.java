package io.github.openbagtwo.lighterend.world.biomes;

import io.github.openbagtwo.lighterend.registries.LighterEndParticles;
import io.github.openbagtwo.lighterend.world.LighterEndPlacedFeatures;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.BiomeParticleConfig;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;

public class Starfield {

  public static Biome create(Registerable<Biome> context) {
    RegistryEntryLookup<PlacedFeature> features = context.getRegistryLookup(
        RegistryKeys.PLACED_FEATURE
    );
    RegistryEntryLookup<ConfiguredCarver<?>> carvers = context.getRegistryLookup(
        RegistryKeys.CONFIGURED_CARVER
    );

    SpawnSettings spawns = new SpawnSettings.Builder().build();

    var genSettingsBuilder = new GenerationSettings.LookupBackedBuilder(features, carvers);
    for (RegistryKey<PlacedFeature> star : LighterEndPlacedFeatures.STARFIELD_ICE_STARS) {
      genSettingsBuilder.feature(Feature.SURFACE_STRUCTURES, star);
    }

    return new Biome.Builder()
        .precipitation(false)
        .temperature(-1.0F)
        .downfall(1.0F)
        .effects(
            new BiomeEffects.Builder()
                .particleConfig(new BiomeParticleConfig(LighterEndParticles.SNOWFLAKE, 0.002F))
                .skyColor(0x000000)
                .fogColor(0xe0f5fe)
                .waterColor(0x45c2be)
                .waterFogColor(0x45c2be)
                .foliageColor(0xc1f4f4)
                .grassColor(0xe6f6fc)
                .build()
        )
        .spawnSettings(spawns)
        .generationSettings(genSettingsBuilder.build())
        .build();
  }
}
