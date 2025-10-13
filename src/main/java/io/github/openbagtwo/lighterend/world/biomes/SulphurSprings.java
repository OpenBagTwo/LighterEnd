package io.github.openbagtwo.lighterend.world.biomes;

import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import io.github.openbagtwo.lighterend.registries.LighterEndParticles;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import io.github.openbagtwo.lighterend.world.LighterEndPlacedFeatures;
import net.minecraft.entity.EntityType;
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

public class SulphurSprings {

  public static Biome create(Registerable<Biome> context) {
    RegistryEntryLookup<PlacedFeature> features = context.getRegistryLookup(
        RegistryKeys.PLACED_FEATURE
    );
    RegistryEntryLookup<ConfiguredCarver<?>> carvers = context.getRegistryLookup(
        RegistryKeys.CONFIGURED_CARVER
    );

    SpawnSettings spawns = new SpawnSettings.Builder()
        .spawn(
            SpawnGroup.WATER_AMBIENT,
            1,
            new SpawnEntry(LighterEndMobs.END_FISH.mob, 3, 8)
        ).spawn(
            SpawnGroup.WATER_AMBIENT,
            1,
            new SpawnEntry(LighterEndMobs.CUBOZOA.mob, 3, 8)
        ).spawn(
            SpawnGroup.CREATURE,
            1,
            new SpawnEntry(LighterEndMobs.MOOSHROOM.mob, 2, 4)
        ).spawn(
            SpawnGroup.MONSTER,
            20,
            new SpawnEntry(EntityType.ENDERMAN, 1, 4)
        ).spawn(
            SpawnGroup.MONSTER,
            1,
            new SpawnEntry(EntityType.ENDERMITE, 1, 1)
        )
        .build();

    var genSettingsBuilder = new GenerationSettings.LookupBackedBuilder(features, carvers)
        .feature(Feature.RAW_GENERATION, LighterEndPlacedFeatures.GEYSER)
        .feature(Feature.RAW_GENERATION, LighterEndPlacedFeatures.SULPHUR_CAVE)
        .feature(Feature.LAKES, LighterEndPlacedFeatures.SULPHUR_LAKE)
        .feature(Feature.SURFACE_STRUCTURES, EndPlacedFeatures.END_GATEWAY_RETURN)
        .feature(Feature.SURFACE_STRUCTURES, LighterEndPlacedFeatures.SURFACE_VENT)
        .feature(Feature.VEGETAL_DECORATION, LighterEndPlacedFeatures.WATER_PLANTS)
        .feature(Feature.VEGETAL_DECORATION, LighterEndPlacedFeatures.END_LILY);

    return new Biome.Builder()
        .precipitation(false)
        .temperature(0.5F)
        .downfall(0.5F)
        .effects(new BiomeEffects.Builder()
            .skyColor(0x000000)
            .fogColor(0xCFC23E)
            .waterColor(0x195A9D)
            .waterFogColor(0x1E413D)
            .foliageColor(0xCFDC64)
            .grassColor(0xCFDC64)
            .loopSound(LighterEndSounds.SULPHUR_AMBIENT)
            .music(MusicType.createIngameMusic(LighterEndSounds.SULPHUR_MUSIC))
            .particleConfig(new BiomeParticleConfig(LighterEndParticles.SULPHUR, 0.001F))
            .build()
        )
        .spawnSettings(spawns)
        .generationSettings(genSettingsBuilder.build())
        .build();


  }

}
