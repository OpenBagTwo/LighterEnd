package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.world.biomes.BlossomingForest;
import io.github.openbagtwo.lighterend.world.biomes.FoggyMushroomlands;
import io.github.openbagtwo.lighterend.world.biomes.GlowingGrasslands;
import io.github.openbagtwo.lighterend.world.biomes.Megalake;
import io.github.openbagtwo.lighterend.world.biomes.ShadowForest;
import io.github.openbagtwo.lighterend.world.biomes.Starfield;
import io.github.openbagtwo.lighterend.world.biomes.SulphurSprings;
import io.github.openbagtwo.lighterend.world.biomes.UmbraValley;
import io.github.openbagtwo.lighterend.world.biomes.UmbrellaJungle;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;

public class LighterEndBiomes {

  public static final RegistryKey<Biome> BLOSSOM_FOREST = register("blossom_forest");
  public static final RegistryKey<Biome> UMBRELLA_JUNGLE = register("umbrella_jungle");
  public static final RegistryKey<Biome> GLOWING_GRASSLAND = register("glowing_grassland");
  public static final RegistryKey<Biome> MEGALAKE = register("megalake");
  public static final RegistryKey<Biome> UMBRA_VALLEY = register("umbra_valley");
  public static final RegistryKey<Biome> FOGGY_MUSHROOMLANDS = register("mushroomlands");
  public static final RegistryKey<Biome> STARFIELD = register("starfield");
  public static final RegistryKey<Biome> SULPHUR_SPRINGS = register("sulphur_springs");
  public static final RegistryKey<Biome> SHADOW_FOREST = register("shadow_forest");

  public static void bootstrap(Registerable<Biome> context) {
    context.register(BLOSSOM_FOREST, BlossomingForest.create(context));
    context.register(UMBRELLA_JUNGLE, UmbrellaJungle.create(context));
    context.register(GLOWING_GRASSLAND, GlowingGrasslands.create(context));
    context.register(MEGALAKE, Megalake.create(context));
    context.register(UMBRA_VALLEY, UmbraValley.create(context));
    context.register(FOGGY_MUSHROOMLANDS, FoggyMushroomlands.create(context));
    context.register(STARFIELD, Starfield.create(context));
    context.register(SULPHUR_SPRINGS, SulphurSprings.create(context));
    context.register(SHADOW_FOREST, ShadowForest.create(context));
  }

  private static RegistryKey<Biome> register(String name) {
    RegistryKey<Biome> key = RegistryKey.of(RegistryKeys.BIOME, LighterEnd.of(name));
    return key;
  }

}
