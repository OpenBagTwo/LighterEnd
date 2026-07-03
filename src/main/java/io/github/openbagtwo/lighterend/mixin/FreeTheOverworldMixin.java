package io.github.openbagtwo.lighterend.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.PlayerSpawnFinder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayer.RespawnConfig;
import net.minecraft.server.level.progress.LevelLoadListener;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EndPortalBlock;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndPortalBlock.class)
public abstract class FreeTheOverworldMixin {

  @Inject(method = "getPortalDestination", at = @At("HEAD"), cancellable = true)
  public void theStarsAreNotMyDestination(
      ServerLevel world,
      Entity entity,
      BlockPos pos,
      CallbackInfoReturnable<TeleportTransition> cir
  ) {
    if (world.dimension() == Level.END && world.getRespawnData().dimension() == Level.END) {
      if (entity instanceof ServerPlayer player) {
        RespawnConfig respawnConfig = player.getRespawnConfig();
        ServerLevel respawnDimension = respawnConfig == null ? null
            : world.getServer().getLevel(respawnConfig.respawnData().dimension());
        if (
            (respawnConfig == null)
                || (respawnDimension == null)
                || ServerPlayer.findRespawnAndUseSpawnBlock(
                respawnDimension, respawnConfig, false
            ).isEmpty()
        ) {

          ServerLevel overworld = world.getServer().overworld();
          ServerChunkCache overworldChunkManager = world.getChunkSource();

          ChunkPos idealSpawn = ChunkPos.containing(
              overworldChunkManager.randomState().sampler().findSpawnPosition()
          );
          world.getServer().getLevelLoadListener()
              .start(LevelLoadListener.Stage.PREPARE_GLOBAL_SPAWN, 0
              );
          world.getServer().getLevelLoadListener().updateFocus(world.dimension(), idealSpawn);

          int j = 0;
          int k = 0;
          int m = 0;
          int n = -1;
          for (int i = 0; i < Mth.square(11); ++i) {
            BlockPos spawnPos = PlayerSpawnFinder.getSpawnPosInChunk(
                overworld,
                new ChunkPos(idealSpawn.x() + j, idealSpawn.z() + k));
            if (j >= -5 && j <= 5 && k >= -5 && k <= 5 && (spawnPos != null)) {
              cir.setReturnValue(
                  new TeleportTransition(
                      overworld,
                      PlayerSpawnFinder.fixupSpawnHeight(overworld, spawnPos),
                      Vec3.ZERO,
                      0.0F,
                      0.0F,
                      TeleportTransition.PLAY_PORTAL_SOUND.then(
                          TeleportTransition.PLACE_PORTAL_TICKET)
                  )
              );
              break;
            }
            if (j == k || j < 0 && j == -k
                || j > 0 && j == 1 - k) {
              int o = m;
              m = -n;
              n = o;
            }
            j += m;
            k += n;
          }
          world.getServer().getLevelLoadListener()
              .finish(LevelLoadListener.Stage.PREPARE_GLOBAL_SPAWN);
        }
      }
    }
  }
}
