package io.github.openbagtwo.lighterend.world.gen.noise;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;

public class NoiseParameters {

  public static final RegistryKey<DoublePerlinNoiseSampler.NoiseParameters> END_MOSS_SURFACE = createKey(
      "end_moss_surface");

  public static void bootstrap(Registerable<DoublePerlinNoiseSampler.NoiseParameters> context) {
    register(context, END_MOSS_SURFACE,
        new DoublePerlinNoiseSampler.NoiseParameters(-5, 1.0D, 0.5D, 1.0D));
  }

  public static RegistryKey<DoublePerlinNoiseSampler.NoiseParameters> createKey(String name) {
    RegistryKey<DoublePerlinNoiseSampler.NoiseParameters> key = RegistryKey.of(
        RegistryKeys.NOISE_PARAMETERS, LighterEnd.of(name));
    return key;
  }

  public static void register(Registerable<DoublePerlinNoiseSampler.NoiseParameters> context,
      RegistryKey<DoublePerlinNoiseSampler.NoiseParameters> key,
      DoublePerlinNoiseSampler.NoiseParameters object) {
    context.register(key, object);
  }

}
