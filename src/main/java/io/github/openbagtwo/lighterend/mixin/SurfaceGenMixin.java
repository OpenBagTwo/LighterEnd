package io.github.openbagtwo.lighterend.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.openbagtwo.lighterend.world.gen.LighterEndWorldGen;
import net.minecraft.registry.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class SurfaceGenMixin {

  @Inject(at = @At("TAIL"), method = "createWorlds()V")
  private void addSurfaceRules(CallbackInfo ci,
      @Local Registry<DimensionOptions> registry) {
    DimensionOptions stem = registry.get(DimensionOptions.END);

    if (stem != null && stem.chunkGenerator() instanceof NoiseChunkGenerator generator) {
      ChunkGeneratorSettings settings = generator.getSettings().value();
      ChunkGeneratorSettingsAccessor accessor = (ChunkGeneratorSettingsAccessor) (Object) settings;

      accessor.setSurfaceRule(
          MaterialRules.sequence(LighterEndWorldGen.updateSurfaceRules(), settings.surfaceRule()));
    }
  }
}
