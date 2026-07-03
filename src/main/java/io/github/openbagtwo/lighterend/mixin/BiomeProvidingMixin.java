package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.world.gen.LighterEndWorldGen;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(MinecraftServer.class)
public abstract class BiomeProvidingMixin {


  @ModifyArgs(method = "createLevels", at = @At(value = "INVOKE", target = "net/minecraft/server/level/ServerLevel.<init> (Lnet/minecraft/server/MinecraftServer;Ljava/util/concurrent/Executor;Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;Lnet/minecraft/world/level/storage/ServerLevelData;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/level/dimension/LevelStem;ZJLjava/util/List;ZLnet/minecraft/world/RandomSequences;)V"))
  private void addModdedBiomes(Args args) {
    if (LighterEnd.CONFIG.generateBiomes()) {
      MinecraftServer server = args.get(0);
      LevelStem dimensionOptions = args.get(5);
      if (BuiltinDimensionTypes.END.equals(
          dimensionOptions.type().unwrapKey().orElse(null))) {
        ChunkGenerator defaultChunkGen = dimensionOptions.generator();
        BiomeSource defaultBiomes = defaultChunkGen.getBiomeSource();

        if (defaultBiomes instanceof MultiNoiseBiomeSource noiseBiomeSource
            && defaultChunkGen instanceof NoiseBasedChunkGenerator noiseChunkGen) {
          BiomeSource patchedBiomes = LighterEndWorldGen.addBiomesToNoiseSource(
              ((BiomeAccessor) noiseBiomeSource).accessBiomeEntries(),
              server.registryAccess().lookupOrThrow(
                  Registries.BIOME)
          );
          args.set(5, new LevelStem(
              dimensionOptions.type(),
              new NoiseBasedChunkGenerator(patchedBiomes, noiseChunkGen.generatorSettings())
          ));
        }
      }
    }
  }


}
