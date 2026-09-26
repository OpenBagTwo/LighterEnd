package io.github.openbagtwo.lighterend.mixin;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MultiNoiseBiomeSource.class)
public interface BiomeAccessor {

  @Invoker("getBiomeEntries")
  MultiNoiseUtil.Entries<RegistryEntry<Biome>> accessBiomeEntries();
}
