package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class GravityEffectsMixin extends GravityStrengthMixin {

  @Shadow
  public abstract double getAttributeValue(Holder<Attribute> attribute);

  @Inject(method = "calculateFallPower", at = @At("RETURN"), cancellable = true)
  public void increaseSafeFallHeight(CallbackInfoReturnable<Double> cir) {
    double endGravity = LighterEnd.CONFIG.getEndGravity();
    if (endGravity >= 0.0 && BuiltinDimensionTypes.END.equals(
        this.level().dimensionTypeRegistration().unwrapKey().orElse(null))) {
      cir.setReturnValue(cir.getReturnValue()
          - (1 - endGravity) * this.getAttributeValue(Attributes.SAFE_FALL_DISTANCE)
          / endGravity);
    }
  }

  @Inject(method = "calculateFallDamage", at = @At("RETURN"), cancellable = true)
  public void decreaseFallDamage(CallbackInfoReturnable<Integer> cir) {
    double endGravity = LighterEnd.CONFIG.getEndGravity();
    if (endGravity >= 0.0 && BuiltinDimensionTypes.END.equals(
        this.level().dimensionTypeRegistration().unwrapKey().orElse(null))) {

      // TODO: this is bad math—replace with a full @Override that recomputes the value
      cir.setReturnValue(Mth.floor(cir.getReturnValue() * endGravity));
    }
  }


}
