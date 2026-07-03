package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class GravityStrengthMixin {

  @Shadow
  public abstract World getEntityWorld();

  @Inject(method = "getFinalGravity", at = @At("RETURN"), cancellable = true)
  public void applyEndGravity(CallbackInfoReturnable<Double> cir) {
    if (
        LighterEnd.CONFIG.disableEndGravityWhileFlying()
            && (Entity) (Object) this instanceof LivingEntity player
    ) {
      if (player.isGliding()) {
        return;
      }
    }
    double endGravity = LighterEnd.CONFIG.getEndGravity();
    if (
        endGravity >= 0.0
            && DimensionTypes.THE_END.equals(
            this.getEntityWorld().getDimensionEntry().getKey().orElse(null)
        )
    ) {
      cir.setReturnValue(cir.getReturnValue() * endGravity);
    }
  }

}
