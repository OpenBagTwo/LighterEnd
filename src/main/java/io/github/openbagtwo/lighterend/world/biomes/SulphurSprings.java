package io.github.openbagtwo.lighterend.world.biomes;

import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import io.github.openbagtwo.lighterend.registries.LighterEndParticles;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import io.github.openbagtwo.lighterend.world.LighterEndPlacedFeatures;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.EndPlacements;
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

public class SulphurSprings {

  public static Biome create(BootstrapContext<Biome> context) {
    HolderGetter<PlacedFeature> features = context.lookup(
        Registries.PLACED_FEATURE
    );
    HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(
        Registries.CONFIGURED_CARVER
    );

    MobSpawnSettings spawns = new MobSpawnSettings.Builder()
        .addSpawn(
            MobCategory.WATER_AMBIENT,
            1,
            new SpawnerData(LighterEndMobs.END_FISH.mob, 3, 8)
        ).addSpawn(
            MobCategory.WATER_AMBIENT,
            1,
            new SpawnerData(LighterEndMobs.CUBOZOA.mob, 3, 8)
        ).addSpawn(
            MobCategory.MONSTER,
            20,
            new SpawnerData(EntityType.ENDERMAN, 1, 4)
        ).addSpawn(
            MobCategory.MONSTER,
            1,
            new SpawnerData(EntityType.ENDERMITE, 1, 1)
        )
        .build();

    var genSettingsBuilder = new BiomeGenerationSettings.Builder(features, carvers)
        .addFeature(Decoration.RAW_GENERATION, LighterEndPlacedFeatures.GEYSER)
        .addFeature(Decoration.RAW_GENERATION, LighterEndPlacedFeatures.SULPHUR_CAVE)
        .addFeature(Decoration.LAKES, LighterEndPlacedFeatures.SULPHUR_LAKE)
        .addFeature(Decoration.SURFACE_STRUCTURES, EndPlacements.END_GATEWAY_RETURN)
        .addFeature(Decoration.SURFACE_STRUCTURES, LighterEndPlacedFeatures.SURFACE_VENT)
        .addFeature(Decoration.VEGETAL_DECORATION, LighterEndPlacedFeatures.WATER_PLANTS)
        .addFeature(Decoration.VEGETAL_DECORATION, LighterEndPlacedFeatures.END_LILY);

    return new Biome.BiomeBuilder()
        .hasPrecipitation(false)
        .temperature(0.5F)
        .downfall(0.5F)
        .specialEffects(new BiomeSpecialEffects.Builder()
            .waterColor(0x195A9D)
            .foliageColorOverride(0xCFDC64)
            .grassColorOverride(0xCFDC64)
            .build()
        )
        .mobSpawnSettings(spawns)
        .generationSettings(genSettingsBuilder.build())
        .setAttribute(EnvironmentAttributes.AMBIENT_PARTICLES,
            AmbientParticle.of(LighterEndParticles.SULPHUR, 0.001F))
        .setAttribute(EnvironmentAttributes.SKY_COLOR, 0x000000)
        .setAttribute(EnvironmentAttributes.FOG_COLOR, 0xCFC23E)
        .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, 0x1E413D)
        .setAttribute(
            EnvironmentAttributes.AMBIENT_SOUNDS,
            new AmbientSounds(
                Optional.of(LighterEndSounds.SULPHUR_AMBIENT),
                Optional.empty(),
                List.of()
            )
        ).setAttribute(
            EnvironmentAttributes.BACKGROUND_MUSIC,
            new BackgroundMusic(LighterEndSounds.SULPHUR_MUSIC)
        ).build();
  }

}
