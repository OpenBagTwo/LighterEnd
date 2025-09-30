package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.fabricmc.fabric.impl.biome.TheEndBiomeData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = TheEndBiomeData.Overrides.class)
public abstract class BiggerBiomesMixin {

  @ModifyConstant(method = "pick(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/registry/entry/RegistryEntry;Ljava/util/Map;IILnet/minecraft/world/biome/source/util/MultiNoiseUtil$MultiNoiseSampler;)Lnet/minecraft/registry/entry/RegistryEntry;", constant = @Constant(doubleValue = 64.0))
  private double resizeEnd(double constant) {
    if (LighterEnd.CONFIG.generateBiomes()) {
      return 256.0;
    }
    return 64.0;
  }
}
