package io.github.openbagtwo.lighterend.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Silences the "Detected setBlock in a far chunk" complaints
 */
@Mixin(ChunkRegion.class)
public class BlocksAreNotTooFarMixin {

  @Final
  @Shadow
  private Chunk centerPos;

  @Inject(method = "isValidForSetBlock", at = @At("HEAD"), cancellable = true)
  private void alterBlockCheck(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
    int x = blockPos.getX() >> 4;
    int z = blockPos.getZ() >> 4;
    cir.setReturnValue(
        Math.abs(x - centerPos.getPos().x) < 2 && Math.abs(z - centerPos.getPos().z) < 2);
  }
}
