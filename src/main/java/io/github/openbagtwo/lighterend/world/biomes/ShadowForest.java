package io.github.openbagtwo.lighterend.world.biomes;

import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import io.github.openbagtwo.lighterend.world.LighterEndPlacedFeatures;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.particles.ParticleTypes;
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

public class ShadowForest {

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
            20,
            new SpawnerData(EntityType.PHANTOM, 1, 1)
        ).addSpawn(
            MobCategory.MONSTER,
            80,
            new SpawnerData(EntityType.ENDERMAN, 1, 4)
        ).build();

    var genSettingsBuilder = new BiomeGenerationSettings.Builder(features, carvers)
        .addFeature(Decoration.SURFACE_STRUCTURES, EndPlacements.END_GATEWAY_RETURN)
        .addFeature(Decoration.SURFACE_STRUCTURES, LighterEndPlacedFeatures.DRAGON_TREE)
        .addFeature(Decoration.VEGETAL_DECORATION, LighterEndPlacedFeatures.PURPLE_POLYPORES)
        .addFeature(Decoration.VEGETAL_DECORATION, LighterEndPlacedFeatures.SHADOW_FOREST_VEGETATION);

    for (ResourceKey<PlacedFeature> blob : LighterEndPlacedFeatures.JADESTONE_BLOBS) {
      genSettingsBuilder = genSettingsBuilder.addFeature(Decoration.UNDERGROUND_ORES, blob);
    }

    return new Biome.BiomeBuilder()
        .hasPrecipitation(false)
        .temperature(0.5F)
        .downfall(0.5F)
        .specialEffects(new BiomeSpecialEffects.Builder()
            .waterColor(0x2A2D50)
            .foliageColorOverride(0x2D2D2D)
            .grassColorOverride(0x2D2D2D)
            .build()
        )
        .mobSpawnSettings(spawns)
        .generationSettings(genSettingsBuilder.build())
        .setAttribute(EnvironmentAttributes.AMBIENT_PARTICLES,
            AmbientParticle.of(ParticleTypes.MYCELIUM, 0.01F))
        .setAttribute(EnvironmentAttributes.SKY_COLOR, 0x000000)
        .setAttribute(EnvironmentAttributes.FOG_COLOR, 0x000000)
        .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, 0x2A2D50)
        .setAttribute(
            EnvironmentAttributes.AMBIENT_SOUNDS,
            new AmbientSounds(
                Optional.of(LighterEndSounds.SHADOW_AMBIENT),
                Optional.empty(),
                List.of()
            )
        ).setAttribute(
            EnvironmentAttributes.BACKGROUND_MUSIC,
            new BackgroundMusic(LighterEndSounds.SHADOW_MUSIC)
        ).build();
  }
}
