package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.config.Config;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.dimension.DimensionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class GravityEffectsMixin extends GravityStrengthMixin {

  @Shadow
  public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

  @Inject(method = "getUnsafeFallDistance", at = @At("RETURN"), cancellable = true)
  public void increaseSafeFallHeight(CallbackInfoReturnable<Double> cir) {
    double endGravity = Config.loadConfiguration().getEndGravity();
    if (endGravity >= 0.0 && DimensionTypes.THE_END.equals(
        this.getWorld().getDimensionEntry().getKey().orElse(null))) {
      cir.setReturnValue(cir.getReturnValue()
          - (1 - endGravity) * this.getAttributeValue(EntityAttributes.SAFE_FALL_DISTANCE)
          / endGravity);
    }
  }

  @Inject(method = "computeFallDamage", at = @At("RETURN"), cancellable = true)
  public void decreaseFallDamage(CallbackInfoReturnable<Integer> cir) {
    double endGravity = Config.loadConfiguration().getEndGravity();
    if (endGravity >= 0.0 && DimensionTypes.THE_END.equals(
        this.getWorld().getDimensionEntry().getKey().orElse(null))) {

      // TODO: this is bad math—replace with a full @Override that recomputes the value
      cir.setReturnValue(MathHelper.floor(cir.getReturnValue() * endGravity));
    }
  }


}
