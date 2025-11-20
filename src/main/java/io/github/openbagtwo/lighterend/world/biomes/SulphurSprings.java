package io.github.openbagtwo.lighterend.world.biomes;

import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import io.github.openbagtwo.lighterend.registries.LighterEndParticles;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import io.github.openbagtwo.lighterend.world.LighterEndPlacedFeatures;
import java.util.List;
import java.util.Optional;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
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
            .waterColor(0x195A9D)
            .foliageColor(0xCFDC64)
            .grassColor(0xCFDC64)
            .build()
        )
        .spawnSettings(spawns)
        .generationSettings(genSettingsBuilder.build())
        .setEnvironmentAttribute(EnvironmentAttributes.AMBIENT_PARTICLES_VISUAL,
            AmbientParticle.of(LighterEndParticles.SULPHUR, 0.001F))
        .setEnvironmentAttribute(EnvironmentAttributes.SKY_COLOR_VISUAL, 0x000000)
        .setEnvironmentAttribute(EnvironmentAttributes.FOG_COLOR_VISUAL, 0xCFC23E)
        .setEnvironmentAttribute(EnvironmentAttributes.WATER_FOG_COLOR_VISUAL, 0x1E413D)
        .setEnvironmentAttribute(
            EnvironmentAttributes.AMBIENT_SOUNDS_AUDIO,
            new AmbientSounds(
                Optional.of(LighterEndSounds.SULPHUR_AMBIENT),
                Optional.empty(),
                List.of()
            )
        ).setEnvironmentAttribute(
            EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO,
            new BackgroundMusic(LighterEndSounds.SULPHUR_MUSIC)
        ).build();
  }

}
