package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.world.gen.LighterEndWorldGen;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(MinecraftServer.class)
public abstract class BiomeProvidingMixin {


  @ModifyArgs(method = "createWorlds", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;<init>(Lnet/minecraft/server/MinecraftServer;Ljava/util/concurrent/Executor;Lnet/minecraft/world/level/storage/LevelStorage$Session;Lnet/minecraft/world/level/ServerWorldProperties;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/world/dimension/DimensionOptions;Lnet/minecraft/server/WorldGenerationProgressListener;ZJLjava/util/List;ZLnet/minecraft/util/math/random/RandomSequencesState;)V"))
  private void addModdedBiomes(Args args) {
    if (LighterEnd.CONFIG.generateBiomes()) {
      MinecraftServer server = args.get(0);
      DimensionOptions dimensionOptions = args.get(5);
      if (DimensionTypes.THE_END.equals(
          dimensionOptions.dimensionTypeEntry().getKey().orElse(null))) {
        ChunkGenerator defaultChunkGen = dimensionOptions.chunkGenerator();
        BiomeSource defaultBiomes = defaultChunkGen.getBiomeSource();

        if (defaultBiomes instanceof MultiNoiseBiomeSource noiseBiomeSource
            && defaultChunkGen instanceof NoiseChunkGenerator noiseChunkGen) {
          BiomeSource patchedBiomes = LighterEndWorldGen.addBiomesToNoiseSource(
              ((BiomeAccessor) noiseBiomeSource).accessBiomeEntries(),
              server.getRegistryManager().getOrThrow(
                  RegistryKeys.BIOME)
          );
          args.set(5, new DimensionOptions(dimensionOptions.dimensionTypeEntry(),
              new NoiseChunkGenerator(patchedBiomes, noiseChunkGen.getSettings())));
        }
      }
    }
  }
}
