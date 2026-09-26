package io.github.openbagtwo.lighterend.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.github.openbagtwo.lighterend.world.LighterEndConfiguredFeatures;
import io.github.openbagtwo.lighterend.world.gen.LighterEndWorldGen;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.PlayerSpawnFinder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.LevelLoadListener;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WorldData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class EndSpawnMixin {

  @Unique
  private static final float END_SPAWN_RADIUS = 10_000F;

  @Unique
  private ServerLevel theEnd = null;

  @Shadow
  public abstract ServerLevel getLevel(ResourceKey<Level> key);

  @Accessor("levels")
  abstract Map<ResourceKey<Level>, ServerLevel> getWorldz();

  @Shadow
  public abstract LevelLoadListener getLevelLoadListener();

  @Shadow
  public abstract WorldData getWorldData();

  @Accessor
  abstract LevelStorageSource.LevelStorageAccess getStorageSource();

  @Inject(
      method = "createLevels",
      at = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/world/level/storage/CommandStorage;<init>(Lnet/minecraft/world/level/storage/DimensionDataStorage;)V"
      )
  )
  public void setEndSpawn(
      CallbackInfo ci,
      @Local ServerLevelData serverWorldProperties,
      @Local Registry<LevelStem> registry,
      @Local WorldOptions generatorOptions
  ) {
    if (!LighterEnd.CONFIG.enableEndSpawn()) {
      return;
    }
    if (serverWorldProperties.isInitialized()) {
      return;
    }

    LevelStem endSettings = registry.getValue(LevelStem.END);

    if (endSettings == null) {
      return;
    }

    this.theEnd = loadTheEnd(
        new DerivedLevelData(
            this.getWorldData(),
            serverWorldProperties
        ),
        endSettings,
        BiomeManager.obfuscateSeed(generatorOptions.seed())
    );
    this.getWorldz().put(Level.END, this.theEnd);

    setEndSpawn(
        this.theEnd,
        serverWorldProperties,
        this.getLevelLoadListener()
    );
    serverWorldProperties.setInitialized(true);
  }

  @Inject(method = "createLevels", at = @At("TAIL"))
  public void reInsertOurEnd(CallbackInfo ci) {
    if (this.theEnd != null) {
      this.getWorldz().put(Level.END, this.theEnd);
      this.theEnd = null;
    }
  }


  private ServerLevel loadTheEnd(
      DerivedLevelData unmodifiableLevelProperties,
      LevelStem dimensionOptions,
      long biomeSeed
  ) {

    if (LighterEnd.CONFIG.generateBiomes()) {
      ChunkGenerator defaultChunkGen = dimensionOptions.generator();
      BiomeSource defaultBiomes = defaultChunkGen.getBiomeSource();
      if (defaultBiomes instanceof MultiNoiseBiomeSource noiseBiomeSource
          && defaultChunkGen instanceof NoiseBasedChunkGenerator noiseChunkGen) {
        BiomeSource patchedBiomes = LighterEndWorldGen.addBiomesToNoiseSource(
            ((BiomeAccessor) noiseBiomeSource).accessBiomeEntries(),
            ((MinecraftServer) (Object) this).registryAccess().lookupOrThrow(
                Registries.BIOME)
        );
        dimensionOptions = new LevelStem(
            dimensionOptions.type(),
            new NoiseBasedChunkGenerator(patchedBiomes, noiseChunkGen.generatorSettings())
        );
      }
    }

    return new ServerLevel(
        (MinecraftServer) (Object) this,
        Util.backgroundExecutor(),
        this.getStorageSource(),
        unmodifiableLevelProperties,
        Level.END,
        dimensionOptions,
        false,
        biomeSeed,
        ImmutableList.of(),
        false,
        null
    );
  }

  private static void setEndSpawn(
      ServerLevel world,
      ServerLevelData worldProperties,
      LevelLoadListener loadProgress
  ) {

    ServerChunkCache serverChunkManager = world.getChunkSource();

    ChunkPos chunkPos;
    int y;
    while (true) {
      float angle = Mth.nextFloat(world.getRandom(), 0, Mth.TWO_PI);
      chunkPos = new ChunkPos(
          new BlockPos(
              Math.round(END_SPAWN_RADIUS * Mth.cos(angle)),
              64,
              Math.round(END_SPAWN_RADIUS * Mth.sin(angle)))
      );
      loadProgress.start(LevelLoadListener.Stage.PREPARE_GLOBAL_SPAWN, 0);
      loadProgress.updateFocus(world.dimension(), chunkPos);
      y = serverChunkManager.getGenerator().getSpawnHeight(world);
      if (y < world.getMinY()) {
        BlockPos blockPos = chunkPos.getWorldPosition();
        y = world.getHeight(Heightmap.Types.WORLD_SURFACE, blockPos.getX() + 8, blockPos.getZ() + 8);
      }
      if (
          y > 50 && !world.getBiome(chunkPos.getWorldPosition().offset(8, y, 8))
              .is(LighterEndTags.INVALID_SPAWN_BIOMES)
      ) {
        break;
      }
    }

    worldProperties.setSpawn(LevelData.RespawnData.of(world.dimension(),
        chunkPos.getWorldPosition().offset(8, y, 8), 0.0F, 0.0F));
    int j = 0;
    int k = 0;
    int l = 0;
    int m = -1;

    for (int n = 0; n < Mth.square(11); n++) {
      if (j >= -5 && j <= 5 && k >= -5 && k <= 5) {
        BlockPos blockPos2 = PlayerSpawnFinder.getSpawnPosInChunk(world,
            new ChunkPos(chunkPos.x + j, chunkPos.z + k));
        if (blockPos2 != null) {
          worldProperties.setSpawn(
              LevelData.RespawnData.of(world.dimension(), blockPos2, 0.0F, 0.0F));
          break;
        }
      }

      if (j == k || j < 0 && j == -k || j > 0 && j == 1 - k) {
        int o = l;
        l = -m;
        m = o;
      }

      j += l;
      k += m;
    }

    world.registryAccess()
        .lookup(Registries.CONFIGURED_FEATURE)
        .flatMap(
            featureRegistry -> featureRegistry.get(
                LighterEndConfiguredFeatures.STARTER_CHEST))
        .ifPresent(
            feature -> feature.value()
                .place(world, serverChunkManager.getGenerator(), world.random,
                    worldProperties.getRespawnData().pos())
        );

    loadProgress.finish(LevelLoadListener.Stage.PREPARE_GLOBAL_SPAWN);

  }


}
