package io.github.openbagtwo.lighterend.world.gen.noise;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class NoiseParameters {

  public static final ResourceKey<NormalNoise> END_MOSS_SURFACE = createKey(
      "end_moss_surface");

  public static final ResourceKey<NormalNoise> VIOLECITE_SURFACE = createKey(
      "violecite_surface");

  public static final ResourceKey<NormalNoise> SULPHUR_SURFACE = createKey(
      "sulphur_surface");

  public static void bootstrap(final BootstrapContext<NormalNoise> context) {
    register(
        context,
        END_MOSS_SURFACE,
        NormalNoise.createParity(-5, 1.0D, 0.5D, 1.0D)
    );

    register(
        context,
        VIOLECITE_SURFACE,
        NormalNoise.createParity(-5, 1.0D, 0.5D, 1.0D)
    );

    register(
        context,
        SULPHUR_SURFACE,
        NormalNoise.createParity(-5, 1.0D, 0.5D, 1.0D)
    );
  }

  public static ResourceKey<NormalNoise> createKey(String name) {
    ResourceKey<NormalNoise> key = ResourceKey.create(
        Registries.NOISE, LighterEnd.of(name));
    return key;
  }

  public static void register(
      final BootstrapContext<NormalNoise> context,
      ResourceKey<NormalNoise> key,
      NormalNoise object) {
    context.register(key, object);
  }

}
