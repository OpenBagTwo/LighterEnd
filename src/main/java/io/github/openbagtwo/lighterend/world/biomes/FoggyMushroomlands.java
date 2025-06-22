package io.github.openbagtwo.lighterend.world.biomes;

import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import io.github.openbagtwo.lighterend.registries.LighterEndParticles;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import io.github.openbagtwo.lighterend.world.LighterEndPlacedFeatures;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.MusicType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.BiomeParticleConfig;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.SpawnSettings.SpawnEntry;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.EndPlacedFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;

public class FoggyMushroomlands {

  public static Biome create(Registerable<Biome> context) {
    RegistryEntryLookup<PlacedFeature> features = context.getRegistryLookup(
        RegistryKeys.PLACED_FEATURE
    );
    RegistryEntryLookup<ConfiguredCarver<?>> carvers = context.getRegistryLookup(
        RegistryKeys.CONFIGURED_CARVER
    );

    SpawnSettings spawns = new SpawnSettings.Builder()
        .spawn(
            SpawnGroup.AMBIENT,
            1,
            new SpawnEntry(LighterEndMobs.DRAGONFLY.mob, 1, 1)
        )
        .spawn(
            SpawnGroup.MONSTER,
            10,
            new SpawnEntry(LighterEndMobs.END_SLIME.mob, 1, 2)
        )
        .build();

    GenerationSettings genSettings = new GenerationSettings.LookupBackedBuilder(features, carvers)
        .feature(Feature.SURFACE_STRUCTURES, EndPlacedFeatures.END_GATEWAY_RETURN)
        .feature(Feature.SURFACE_STRUCTURES, LighterEndPlacedFeatures.GLOWSHROOM)
        .feature(Feature.VEGETAL_DECORATION, LighterEndPlacedFeatures.END_MOSS_VEGETATION)
        .feature(Feature.VEGETAL_DECORATION, LighterEndPlacedFeatures.AGAVE)
        .feature(Feature.VEGETAL_DECORATION, LighterEndPlacedFeatures.WATER_PLANTS)
        .feature(Feature.VEGETAL_DECORATION, LighterEndPlacedFeatures.END_LILY)
        .build();

    return new Biome.Builder()
        .precipitation(false)
        .temperature(0.5F)
        .downfall(0.5F)
        .effects(new BiomeEffects.Builder()
            .particleConfig(new BiomeParticleConfig(LighterEndParticles.GLOWING_SPHERE, 0.001F))
            .skyColor(0x000000)
            .fogColor(0x297AAD)
            .waterColor(0x77E3FA)
            .waterFogColor(0x77E3FA)
            .foliageColor(0x49D2D1)
            .grassColor(0x31bfe1)
            .loopSound(LighterEndSounds.MUSHROOMLANDS_AMBIENT)
            .music(MusicType.createIngameMusic(LighterEndSounds.MUSHROOMLANDS_MUSIC))
            .build()
        )
        .spawnSettings(spawns)
        .generationSettings(genSettings)
        .build();
  }

}
