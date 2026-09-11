package io.github.openbagtwo.lighterend.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.openbagtwo.lighterend.world.gen.LighterEndWorldGen;
import net.minecraft.core.Holder;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RegistryLayer;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.material.MaterialRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class SurfaceGenMixin {

  @Shadow
  public abstract LayeredRegistryAccess<RegistryLayer> registries();

  @Inject(at = @At("TAIL"), method = "createLevels()V")
  private void addSurfaceRules(
      CallbackInfo ci,
      @Local Registry<LevelStem> registry
  ) {
    LevelStem stem = registry.getValue(LevelStem.END);

    if (stem != null && stem.generator() instanceof NoiseBasedChunkGenerator generator) {
      NoiseGeneratorSettings settings = generator.generatorSettings().value();
      ChunkGeneratorSettingsAccessor accessor = (ChunkGeneratorSettingsAccessor) (Object) settings;

      accessor.setMaterialRule(Holder.direct(
              MaterialRules.sequence(
                  LighterEndWorldGen.updateSurfaceRules(
                      registries().compositeAccess().lookupOrThrow(Registries.BIOME),
                      registries().compositeAccess().lookupOrThrow(Registries.MATERIAL_CONDITION)
                  ),
                  settings.materialRule().value()
              )
          )
      );
    }
  }
}
