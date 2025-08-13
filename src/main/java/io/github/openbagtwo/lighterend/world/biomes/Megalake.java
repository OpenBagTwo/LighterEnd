package io.github.openbagtwo.lighterend.world.biomes;

import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import io.github.openbagtwo.lighterend.world.LighterEndPlacedFeatures;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.MusicType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.SpawnSettings.SpawnEntry;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.EndPlacedFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;

public class Megalake {

  public static Biome create(Registerable<Biome> context) {
    RegistryEntryLookup<PlacedFeature> features = context.getRegistryLookup(
        RegistryKeys.PLACED_FEATURE
    );
    RegistryEntryLookup<ConfiguredCarver<?>> carvers = context.getRegistryLookup(
        RegistryKeys.CONFIGURED_CARVER
    );

    SpawnSettings spawns = new SpawnSettings.Builder()
        .spawn(
            SpawnGroup.AMBIENT,  //dirty hack for issues with the creature group
            1,
            new SpawnEntry(LighterEndMobs.CHORUS_CRAB.mob, 1, 4)
        )
        .build();

    GenerationSettings genSettings = new GenerationSettings.LookupBackedBuilder(features, carvers)
        .feature(Feature.SURFACE_STRUCTURES, EndPlacedFeatures.END_GATEWAY_RETURN)
        .feature(Feature.VEGETAL_DECORATION, LighterEndPlacedFeatures.END_MOSS_VEGETATION)
        .feature(Feature.VEGETAL_DECORATION, LighterEndPlacedFeatures.WATER_PLANTS)
        .feature(Feature.VEGETAL_DECORATION, LighterEndPlacedFeatures.END_LILY)
        .feature(Feature.VEGETAL_DECORATION, LighterEndPlacedFeatures.LOTUS_LEAF)
        .feature(Feature.VEGETAL_DECORATION, LighterEndPlacedFeatures.END_LOTUS)
        .build();

    return new Biome.Builder()
        .precipitation(false)
        .temperature(0.5F)
        .downfall(0.5F)
        .effects(new BiomeEffects.Builder()
            .skyColor(0x000000)
            .fogColor(0xB2D1F8)
            .waterColor(0x60A3FF)
            .waterFogColor(0x60A3FF)
            .foliageColor(0x49DBD1)
            .grassColor(0x4ad6d5)
            .loopSound(LighterEndSounds.LAKE_AMBIENT)
            .music(MusicType.createIngameMusic(LighterEndSounds.LAKE_MUSIC))
            .build()
        )
        .spawnSettings(spawns)
        .generationSettings(genSettings)
        .build();
  }

}
