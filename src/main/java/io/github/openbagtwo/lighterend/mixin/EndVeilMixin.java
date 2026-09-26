package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.misc.StatusEffects;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public abstract class EndVeilMixin {

  @Inject(method = "isPlayerStaring", at = @At("HEAD"), cancellable = true)
  public void checkForEndVeil(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
    if (player.hasStatusEffect(StatusEffects.END_VEIL)) {
      cir.setReturnValue(false);
      cir.cancel();
    }
  }
}
