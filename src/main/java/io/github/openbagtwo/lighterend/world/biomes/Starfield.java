package io.github.openbagtwo.lighterend.world.biomes;

import io.github.openbagtwo.lighterend.registries.LighterEndParticles;
import io.github.openbagtwo.lighterend.world.LighterEndPlacedFeatures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ARGB;
import net.minecraft.world.attribute.AmbientParticle;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class Starfield {

  public static Biome create(BootstrapContext<Biome> context) {
    HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
    HolderGetter<WorldCarver> carvers = context.lookup(Registries.CARVER);

    MobSpawnSettings spawns = new MobSpawnSettings.Builder().build();

    var genSettingsBuilder = new BiomeGenerationSettings.Builder(features, carvers);
    for (ResourceKey<PlacedFeature> star : LighterEndPlacedFeatures.STARFIELD_ICE_STARS) {
      genSettingsBuilder.addFeature(Decoration.SURFACE_STRUCTURES, star);
    }

    return new Biome.BiomeBuilder()
        .hasPrecipitation(false)
        .temperature(-1.0F)
        .downfall(1.0F)
        .specialEffects(new BiomeSpecialEffects.Builder()
            .waterColor(0x45c2be)
            .foliageColorOverride(0xc1f4f4)
            .grassColorOverride(0xe6f6fc)
            .build()
        )
        .mobSpawnSettings(spawns)
        .generationSettings(genSettingsBuilder.build())
        .setAttribute(EnvironmentAttributes.AMBIENT_PARTICLES,
            AmbientParticle.of(LighterEndParticles.SNOWFLAKE, 0.002F))
        .setAttribute(EnvironmentAttributes.SKY_COLOR, ARGB.vector3fFromRGB24(0x000000))
        .setAttribute(EnvironmentAttributes.FOG_COLOR, ARGB.vector3fFromRGB24(0xe0f5fe))
        .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, ARGB.vector3fFromRGB24(0x45c2be))
        .build();
  }
}
