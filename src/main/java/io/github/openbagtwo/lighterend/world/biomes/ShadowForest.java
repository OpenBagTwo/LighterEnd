package io.github.openbagtwo.lighterend.world.biomes;

import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import io.github.openbagtwo.lighterend.world.LighterEndPlacedFeatures;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
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

public class ShadowForest {

  public static Biome create(Registerable<Biome> context) {
    RegistryEntryLookup<PlacedFeature> features = context.getRegistryLookup(
        RegistryKeys.PLACED_FEATURE
    );
    RegistryEntryLookup<ConfiguredCarver<?>> carvers = context.getRegistryLookup(
        RegistryKeys.CONFIGURED_CARVER
    );

    SpawnSettings spawns = new SpawnSettings.Builder()
        .spawn(
            SpawnGroup.MONSTER,
            20,
            new SpawnEntry(EntityType.PHANTOM, 1, 1)
        ).spawn(
            SpawnGroup.MONSTER,
            80,
            new SpawnEntry(EntityType.ENDERMAN, 1, 4)
        ).build();

    var genSettingsBuilder = new GenerationSettings.LookupBackedBuilder(features, carvers)
        .feature(Feature.SURFACE_STRUCTURES, EndPlacedFeatures.END_GATEWAY_RETURN)
        .feature(Feature.SURFACE_STRUCTURES, LighterEndPlacedFeatures.DRAGON_TREE)
        .feature(Feature.VEGETAL_DECORATION, LighterEndPlacedFeatures.SHADOW_FOREST_VEGETATION);

    for (RegistryKey<PlacedFeature> blob : LighterEndPlacedFeatures.JADESTONE_BLOBS) {
      genSettingsBuilder = genSettingsBuilder.feature(Feature.UNDERGROUND_ORES, blob);
    }

    return new Biome.Builder()
        .precipitation(false)
        .temperature(0.5F)
        .downfall(0.5F)
        .effects(new BiomeEffects.Builder()
            .particleConfig(new BiomeParticleConfig(ParticleTypes.MYCELIUM, 0.01F))
            .skyColor(0x000000)
            .fogColor(0x000000)
            .waterColor(0x2A2D50)
            .waterFogColor(0x2A2D50)
            .foliageColor(0x2D2D2D)
            .grassColor(0x2D2D2D)
            .loopSound(LighterEndSounds.SHADOW_AMBIENT)
            .music(MusicType.createIngameMusic(LighterEndSounds.SHADOW_MUSIC))
            .build()
        )
        .spawnSettings(spawns)
        .generationSettings(genSettingsBuilder.build())
        .build();
  }
}
