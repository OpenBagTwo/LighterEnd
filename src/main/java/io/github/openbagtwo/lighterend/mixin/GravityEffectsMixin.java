package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.dimension.DimensionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class GravityEffectsMixin extends GravityStrengthMixin {

  @Shadow
  public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

  /**
   * @author OpenBagTwo
   * @reason way easier to just override this calculation than to try to modify the return value
   */
  @Overwrite
  protected final int getSafeFallDistance(float health) {
    double endGravity = LighterEnd.CONFIG.getEndGravity();
    if (endGravity < 0) {
      endGravity = 0;
    }
    return MathHelper.floor(health + 3.0F / endGravity);
  }

  @Inject(method = "computeFallDamage", at = @At("RETURN"), cancellable = true)
  public void decreaseFallDamage(CallbackInfoReturnable<Integer> cir) {
    double endGravity = LighterEnd.CONFIG.getEndGravity();
    if (endGravity >= 0.0 && DimensionTypes.THE_END.equals(
        this.getWorld().getDimensionEntry().getKey().orElse(null))) {

      // TODO: this is bad math—replace with a full @Override that recomputes the value
      cir.setReturnValue(MathHelper.floor(cir.getReturnValue() * endGravity));
    }
  }


}
