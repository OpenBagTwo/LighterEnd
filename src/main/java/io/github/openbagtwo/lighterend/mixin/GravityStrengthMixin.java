package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class GravityStrengthMixin {

  @Shadow
  public abstract Level level();

  @Inject(method = "getGravity", at = @At("RETURN"), cancellable = true)
  public void applyEndGravity(CallbackInfoReturnable<Double> cir) {
    if (
        LighterEnd.CONFIG.disableEndGravityWhileFlying()
            && (Entity) (Object) this instanceof LivingEntity player
    ) {
      if (player.isFallFlying()) {
        return;
      }
    }
    double endGravity = LighterEnd.CONFIG.getEndGravity();
    if (
        endGravity >= 0.0
            && BuiltinDimensionTypes.END.equals(
            this.level().dimensionTypeRegistration().unwrapKey().orElse(null)
        )
    ) {
      cir.setReturnValue(cir.getReturnValue() * endGravity);
    }
  }

}
