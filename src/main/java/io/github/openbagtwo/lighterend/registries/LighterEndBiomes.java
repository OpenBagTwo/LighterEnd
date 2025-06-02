package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.world.biomes.BlossomingForest;
import io.github.openbagtwo.lighterend.world.biomes.UmbrellaJungle;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;

public class LighterEndBiomes {

  public static final RegistryKey<Biome> BLOSSOM_FOREST = register("blossom_forest");
  public static final RegistryKey<Biome> UMBRELLA_JUNGLE = register("umbrella_jungle");

  public static void bootstrap(Registerable<Biome> context) {
    context.register(BLOSSOM_FOREST, BlossomingForest.create(context));
    context.register(UMBRELLA_JUNGLE, UmbrellaJungle.create(context));
  }

  private static RegistryKey<Biome> register(String name) {
    RegistryKey<Biome> key = RegistryKey.of(RegistryKeys.BIOME, LighterEnd.of(name));
    return key;
  }

}
