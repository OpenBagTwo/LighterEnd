package io.github.openbagtwo.lighterend.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.github.openbagtwo.lighterend.world.LighterEndConfiguredFeatures;
import io.github.openbagtwo.lighterend.world.gen.LighterEndWorldGen;
import java.util.Map;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.SpawnLocating;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.chunk.ChunkLoadProgress;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.UnmodifiableLevelProperties;
import net.minecraft.world.level.storage.LevelStorage;
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
  private ServerWorld theEnd = null;

  @Shadow
  public abstract ServerWorld getWorld(RegistryKey<World> key);

  @Accessor("worlds")
  abstract Map<RegistryKey<World>, ServerWorld> getWorldz();

  @Shadow
  public abstract ChunkLoadProgress getChunkLoadProgress();

  @Shadow
  public abstract SaveProperties getSaveProperties();

  @Accessor
  abstract LevelStorage.Session getSession();

  @Inject(
      method = "createWorlds",
      at = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/command/DataCommandStorage;<init>(Lnet/minecraft/world/PersistentStateManager;)V"
      )
  )
  public void setEndSpawn(
      CallbackInfo ci,
      @Local ServerWorldProperties serverWorldProperties,
      @Local Registry<DimensionOptions> registry,
      @Local GeneratorOptions generatorOptions
  ) {
    if (!LighterEnd.CONFIG.enableEndSpawn()) {
      return;
    }
    if (serverWorldProperties.isInitialized()) {
      return;
    }

    DimensionOptions endSettings = registry.get(DimensionOptions.END);

    if (endSettings == null) {
      return;
    }

    this.theEnd = loadTheEnd(
        new UnmodifiableLevelProperties(
            this.getSaveProperties(),
            serverWorldProperties
        ),
        endSettings,
        BiomeAccess.hashSeed(generatorOptions.getSeed())
    );
    this.getWorldz().put(World.END, this.theEnd);

    setEndSpawn(
        this.theEnd,
        serverWorldProperties,
        this.getChunkLoadProgress()
    );
    serverWorldProperties.setInitialized(true);
  }

  @Inject(method = "createWorlds", at = @At("TAIL"))
  public void reInsertOurEnd(CallbackInfo ci) {
    if (this.theEnd != null) {
      this.getWorldz().put(World.END, this.theEnd);
      this.theEnd = null;
    }
  }


  private ServerWorld loadTheEnd(
      UnmodifiableLevelProperties unmodifiableLevelProperties,
      DimensionOptions dimensionOptions,
      long biomeSeed
  ) {

    if (LighterEnd.CONFIG.generateBiomes()) {
      ChunkGenerator defaultChunkGen = dimensionOptions.chunkGenerator();
      BiomeSource defaultBiomes = defaultChunkGen.getBiomeSource();
      if (defaultBiomes instanceof MultiNoiseBiomeSource noiseBiomeSource
          && defaultChunkGen instanceof NoiseChunkGenerator noiseChunkGen) {
        BiomeSource patchedBiomes = LighterEndWorldGen.addBiomesToNoiseSource(
            ((BiomeAccessor) noiseBiomeSource).accessBiomeEntries(),
            ((MinecraftServer) (Object) this).getRegistryManager().getOrThrow(
                RegistryKeys.BIOME)
        );
        dimensionOptions = new DimensionOptions(
            dimensionOptions.dimensionTypeEntry(),
            new NoiseChunkGenerator(patchedBiomes, noiseChunkGen.getSettings())
        );
      }
    }

    return new ServerWorld(
        (MinecraftServer) (Object) this,
        Util.getMainWorkerExecutor(),
        this.getSession(),
        unmodifiableLevelProperties,
        World.END,
        dimensionOptions,
        false,
        biomeSeed,
        ImmutableList.of(),
        false,
        null
    );
  }

  private static void setEndSpawn(
      ServerWorld world,
      ServerWorldProperties worldProperties,
      ChunkLoadProgress loadProgress
  ) {

    ServerChunkManager serverChunkManager = world.getChunkManager();

    ChunkPos chunkPos;
    int y;
    while (true) {
      float angle = MathHelper.nextFloat(world.getRandom(), 0, MathHelper.TAU);
      chunkPos = new ChunkPos(
          new BlockPos(
              Math.round(END_SPAWN_RADIUS * MathHelper.cos(angle)),
              64,
              Math.round(END_SPAWN_RADIUS * MathHelper.sin(angle)))
      );
      loadProgress.init(ChunkLoadProgress.Stage.PREPARE_GLOBAL_SPAWN, 0);
      loadProgress.initSpawnPos(world.getRegistryKey(), chunkPos);
      y = serverChunkManager.getChunkGenerator().getSpawnHeight(world);
      if (y < world.getBottomY()) {
        BlockPos blockPos = chunkPos.getStartPos();
        y = world.getTopY(Heightmap.Type.WORLD_SURFACE, blockPos.getX() + 8, blockPos.getZ() + 8);
      }
      if (
          y > 50 && !world.getBiome(chunkPos.getStartPos().add(8, y, 8))
              .isIn(LighterEndTags.INVALID_SPAWN_BIOMES)
      ) {
        break;
      }
    }

    worldProperties.setSpawnPoint(WorldProperties.SpawnPoint.create(world.getRegistryKey(),
        chunkPos.getStartPos().add(8, y, 8), 0.0F, 0.0F));
    int j = 0;
    int k = 0;
    int l = 0;
    int m = -1;

    for (int n = 0; n < MathHelper.square(11); n++) {
      if (j >= -5 && j <= 5 && k >= -5 && k <= 5) {
        BlockPos blockPos2 = SpawnLocating.findServerSpawnPoint(world,
            new ChunkPos(chunkPos.x + j, chunkPos.z + k));
        if (blockPos2 != null) {
          worldProperties.setSpawnPoint(
              WorldProperties.SpawnPoint.create(world.getRegistryKey(), blockPos2, 0.0F, 0.0F));
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

    world.getRegistryManager()
        .getOptional(RegistryKeys.CONFIGURED_FEATURE)
        .flatMap(
            featureRegistry -> featureRegistry.getOptional(
                LighterEndConfiguredFeatures.STARTER_CHEST))
        .ifPresent(
            feature -> feature.value()
                .generate(world, serverChunkManager.getChunkGenerator(), world.random,
                    worldProperties.getSpawnPoint().getPos())
        );

    loadProgress.finish(ChunkLoadProgress.Stage.PREPARE_GLOBAL_SPAWN);

  }


}
