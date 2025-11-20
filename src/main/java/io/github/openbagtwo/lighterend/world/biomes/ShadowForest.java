package io.github.openbagtwo.lighterend.world.biomes;

import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import io.github.openbagtwo.lighterend.world.LighterEndPlacedFeatures;
import java.util.List;
import java.util.Optional;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.attribute.AmbientParticle;
import net.minecraft.world.attribute.AmbientSounds;
import net.minecraft.world.attribute.BackgroundMusic;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
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
        .feature(Feature.VEGETAL_DECORATION, LighterEndPlacedFeatures.PURPLE_POLYPORES)
        .feature(Feature.VEGETAL_DECORATION, LighterEndPlacedFeatures.SHADOW_FOREST_VEGETATION);

    for (RegistryKey<PlacedFeature> blob : LighterEndPlacedFeatures.JADESTONE_BLOBS) {
      genSettingsBuilder = genSettingsBuilder.feature(Feature.UNDERGROUND_ORES, blob);
    }

    return new Biome.Builder()
        .precipitation(false)
        .temperature(0.5F)
        .downfall(0.5F)
        .effects(new BiomeEffects.Builder()
            .waterColor(0x2A2D50)
            .foliageColor(0x2D2D2D)
            .grassColor(0x2D2D2D)
            .build()
        )
        .spawnSettings(spawns)
        .generationSettings(genSettingsBuilder.build())
        .setEnvironmentAttribute(EnvironmentAttributes.AMBIENT_PARTICLES_VISUAL,
            AmbientParticle.of(ParticleTypes.MYCELIUM, 0.01F))
        .setEnvironmentAttribute(EnvironmentAttributes.SKY_COLOR_VISUAL, 0x000000)
        .setEnvironmentAttribute(EnvironmentAttributes.FOG_COLOR_VISUAL, 0x000000)
        .setEnvironmentAttribute(EnvironmentAttributes.WATER_FOG_COLOR_VISUAL, 0x2A2D50)
        .setEnvironmentAttribute(
            EnvironmentAttributes.AMBIENT_SOUNDS_AUDIO,
            new AmbientSounds(
                Optional.of(LighterEndSounds.SHADOW_AMBIENT),
                Optional.empty(),
                List.of()
            )
        ).setEnvironmentAttribute(
            EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO,
            new BackgroundMusic(LighterEndSounds.SHADOW_MUSIC)
        ).build();
  }
}
