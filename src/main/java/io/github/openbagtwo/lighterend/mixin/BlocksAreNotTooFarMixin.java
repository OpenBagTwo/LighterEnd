package io.github.openbagtwo.lighterend.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Silences the "Detected setBlock in a far chunk" complaints
 */
@Mixin(WorldGenRegion.class)
public class BlocksAreNotTooFarMixin {

  @Final
  @Shadow
  private ChunkAccess center;

  @Inject(method = "ensureCanWrite", at = @At("HEAD"), cancellable = true)
  private void alterBlockCheck(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
    int x = blockPos.getX() >> 4;
    int z = blockPos.getZ() >> 4;
    cir.setReturnValue(
        Math.abs(x - center.getPos().x()) < 2 && Math.abs(z - center.getPos().z()) < 2);
  }
}
