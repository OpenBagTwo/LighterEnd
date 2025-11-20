package io.github.openbagtwo.lighterend.world.biomes;

import io.github.openbagtwo.lighterend.registries.LighterEndParticles;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import io.github.openbagtwo.lighterend.world.LighterEndPlacedFeatures;
import java.util.List;
import java.util.Optional;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
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

public class UmbraValley {

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
            19,
            new SpawnEntry(EntityType.PHANTOM, 1, 1)
        ).spawn(
            SpawnGroup.MONSTER,
            80,
            new SpawnEntry(EntityType.ENDERMAN, 4, 4)
        ).spawn(
            SpawnGroup.MONSTER,
            1,
            new SpawnEntry(EntityType.ENDERMITE, 1, 1)
        ).build();

    var genSettingsBuilder = new GenerationSettings.LookupBackedBuilder(features, carvers)
        .feature(Feature.SURFACE_STRUCTURES, EndPlacedFeatures.END_GATEWAY_RETURN)
        .feature(Feature.SURFACE_STRUCTURES, LighterEndPlacedFeatures.AURORA_CRYSTAL)
        .feature(Feature.SURFACE_STRUCTURES, LighterEndPlacedFeatures.UMBRALITH_ARCH)
        .feature(Feature.SURFACE_STRUCTURES, LighterEndPlacedFeatures.UMBRALITH_ARCH_THIN);

    for (RegistryKey<PlacedFeature> blob : LighterEndPlacedFeatures.JADESTONE_BLOBS) {
      genSettingsBuilder = genSettingsBuilder.feature(Feature.UNDERGROUND_ORES, blob);
    }

    return new Biome.Builder()
        .precipitation(false)
        .temperature(0.5F)
        .downfall(0.5F)
        .effects(new BiomeEffects.Builder()
            .waterColor(0x45C286)
            .foliageColor(0xACBDBE)
            .grassColor(0xACBDBE)
            .build()
        )
        .spawnSettings(spawns)
        .generationSettings(genSettingsBuilder.build())
        .setEnvironmentAttribute(EnvironmentAttributes.AMBIENT_PARTICLES_VISUAL,
            AmbientParticle.of(LighterEndParticles.AMBER_SPHERE, 0.0001F))
        .setEnvironmentAttribute(EnvironmentAttributes.SKY_COLOR_VISUAL, 0x000000)
        .setEnvironmentAttribute(EnvironmentAttributes.FOG_COLOR_VISUAL, 0x646464)
        .setEnvironmentAttribute(EnvironmentAttributes.WATER_FOG_COLOR_VISUAL, 0x45C286)
        .setEnvironmentAttribute(
            EnvironmentAttributes.AMBIENT_SOUNDS_AUDIO,
            new AmbientSounds(
                Optional.of(LighterEndSounds.UMBRA_VALLEY_AMBIENT),
                Optional.empty(),
                List.of()
            )
        ).setEnvironmentAttribute(
            EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO,
            new BackgroundMusic(LighterEndSounds.UMBRA_VALLEY_MUSIC)
        ).build();
  }

}
