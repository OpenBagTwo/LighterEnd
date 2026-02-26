package io.github.openbagtwo.lighterend.mixin;

import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NoiseGeneratorSettings.class)
public interface ChunkGeneratorSettingsAccessor {

  @Mutable
  @Accessor
  void setSurfaceRule(SurfaceRules.RuleSource surfaceRule);

}
