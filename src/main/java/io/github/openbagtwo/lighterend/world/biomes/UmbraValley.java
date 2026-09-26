package io.github.openbagtwo.lighterend.world.biomes;

import io.github.openbagtwo.lighterend.registries.LighterEndParticles;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import io.github.openbagtwo.lighterend.world.LighterEndPlacedFeatures;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.EndPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.attribute.AmbientParticle;
import net.minecraft.world.attribute.AmbientSounds;
import net.minecraft.world.attribute.BackgroundMusic;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class UmbraValley {

  public static Biome create(BootstrapContext<Biome> context) {
    HolderGetter<PlacedFeature> features = context.lookup(
        Registries.PLACED_FEATURE
    );
    HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(
        Registries.CONFIGURED_CARVER
    );

    MobSpawnSettings spawns = new MobSpawnSettings.Builder()
        .addSpawn(
            MobCategory.MONSTER,
            19,
            new SpawnerData(EntityType.PHANTOM, 1, 1)
        ).addSpawn(
            MobCategory.MONSTER,
            80,
            new SpawnerData(EntityType.ENDERMAN, 4, 4)
        ).addSpawn(
            MobCategory.MONSTER,
            1,
            new SpawnerData(EntityType.ENDERMITE, 1, 1)
        ).build();

    var genSettingsBuilder = new BiomeGenerationSettings.Builder(features, carvers)
        .addFeature(Decoration.SURFACE_STRUCTURES, EndPlacements.END_GATEWAY_RETURN)
        .addFeature(Decoration.SURFACE_STRUCTURES, LighterEndPlacedFeatures.AURORA_CRYSTAL)
        .addFeature(Decoration.SURFACE_STRUCTURES, LighterEndPlacedFeatures.UMBRALITH_ARCH)
        .addFeature(Decoration.SURFACE_STRUCTURES, LighterEndPlacedFeatures.UMBRALITH_ARCH_THIN);

    for (ResourceKey<PlacedFeature> blob : LighterEndPlacedFeatures.JADESTONE_BLOBS) {
      genSettingsBuilder = genSettingsBuilder.addFeature(Decoration.UNDERGROUND_ORES, blob);
    }

    return new Biome.BiomeBuilder()
        .hasPrecipitation(false)
        .temperature(0.5F)
        .downfall(0.5F)
        .specialEffects(new BiomeSpecialEffects.Builder()
            .waterColor(0x45C286)
            .foliageColorOverride(0xACBDBE)
            .grassColorOverride(0xACBDBE)
            .build()
        )
        .mobSpawnSettings(spawns)
        .generationSettings(genSettingsBuilder.build())
        .setAttribute(EnvironmentAttributes.AMBIENT_PARTICLES,
            AmbientParticle.of(LighterEndParticles.AMBER_SPHERE, 0.0001F))
        .setAttribute(EnvironmentAttributes.SKY_COLOR, 0x000000)
        .setAttribute(EnvironmentAttributes.FOG_COLOR, 0x646464)
        .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, 0x45C286)
        .setAttribute(
            EnvironmentAttributes.AMBIENT_SOUNDS,
            new AmbientSounds(
                Optional.of(LighterEndSounds.UMBRA_VALLEY_AMBIENT),
                Optional.empty(),
                List.of()
            )
        ).setAttribute(
            EnvironmentAttributes.BACKGROUND_MUSIC,
            new BackgroundMusic(LighterEndSounds.UMBRA_VALLEY_MUSIC)
        ).build();
  }

}
