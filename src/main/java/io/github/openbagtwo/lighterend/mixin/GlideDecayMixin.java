package io.github.openbagtwo.lighterend.mixin;


import io.github.openbagtwo.lighterend.Items.ArmoredElytra;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class, priority = 199)
public abstract class GlideDecayMixin {

  @Shadow
  public abstract ItemStack getEquippedStack(EquipmentSlot equipmentSlot);

  @Inject(method = "calcGlidingVelocity(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;", at = @At("RETURN"), cancellable = true)
  public void decayGlideVelocity(CallbackInfoReturnable<Vec3d> cir) {
    float decayFactor = 1.0F;

    if (getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ArmoredElytra elytra) {
      decayFactor = elytra.glideDecay;
    }

    cir.setReturnValue(
        cir.getReturnValue().multiply(decayFactor, 1.0D, decayFactor)
    );
  }
}
