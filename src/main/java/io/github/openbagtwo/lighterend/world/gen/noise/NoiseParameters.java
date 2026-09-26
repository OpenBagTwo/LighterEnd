package io.github.openbagtwo.lighterend.world.gen.noise;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class NoiseParameters {

  public static final ResourceKey<NormalNoise.NoiseParameters> END_MOSS_SURFACE = createKey(
      "end_moss_surface");

  public static final ResourceKey<NormalNoise.NoiseParameters> VIOLECITE_SURFACE = createKey(
      "violecite_surface");

  public static final ResourceKey<NormalNoise.NoiseParameters> SULPHUR_SURFACE = createKey(
      "sulphur_surface");

  public static void bootstrap(BootstrapContext<NormalNoise.NoiseParameters> context) {
    register(context, END_MOSS_SURFACE,
        new NormalNoise.NoiseParameters(-5, 1.0D, 0.5D, 1.0D));

    register(context, VIOLECITE_SURFACE,
        new NormalNoise.NoiseParameters(-5, 1.0D, 0.5D, 1.0D));

    register(context, SULPHUR_SURFACE,
        new NormalNoise.NoiseParameters(-5, 1.0D, 0.5D, 1.0D));
  }

  public static ResourceKey<NormalNoise.NoiseParameters> createKey(String name) {
    ResourceKey<NormalNoise.NoiseParameters> key = ResourceKey.create(
        Registries.NOISE, LighterEnd.of(name));
    return key;
  }

  public static void register(BootstrapContext<NormalNoise.NoiseParameters> context,
      ResourceKey<NormalNoise.NoiseParameters> key,
      NormalNoise.NoiseParameters object) {
    context.register(key, object);
  }

}
