package io.github.openbagtwo.lighterend.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.openbagtwo.lighterend.world.gen.LighterEndWorldGen;
import net.minecraft.core.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class SurfaceGenMixin {

  @Inject(at = @At("TAIL"), method = "createLevels()V")
  private void addSurfaceRules(CallbackInfo ci,
      @Local Registry<LevelStem> registry) {
    LevelStem stem = registry.getValue(LevelStem.END);

    if (stem != null && stem.generator() instanceof NoiseBasedChunkGenerator generator) {
      NoiseGeneratorSettings settings = generator.generatorSettings().value();
      ChunkGeneratorSettingsAccessor accessor = (ChunkGeneratorSettingsAccessor) (Object) settings;

      accessor.setSurfaceRule(
          SurfaceRules.sequence(LighterEndWorldGen.updateSurfaceRules(), settings.surfaceRule()));
    }
  }
}
