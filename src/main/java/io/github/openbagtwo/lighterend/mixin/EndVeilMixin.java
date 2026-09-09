package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.misc.StatusEffects;
import net.minecraft.world.entity.monster.Enderman;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enderman.class)
public abstract class EndVeilMixin {

  @Inject(method = "isBeingStaredBy", at = @At("HEAD"), cancellable = true)
  public void checkForEndVeil(Player player, CallbackInfoReturnable<Boolean> cir) {
    if (player.hasEffect(StatusEffects.END_VEIL)) {
      cir.setReturnValue(false);
      cir.cancel();
    }
  }
}
