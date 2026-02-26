package io.github.openbagtwo.lighterend.mixin;


import io.github.openbagtwo.lighterend.items.ArmoredElytra;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class, priority = 199)
public abstract class GlideDecayMixin {

  @Shadow
  public abstract ItemStack getItemBySlot(EquipmentSlot equipmentSlot);

  @Inject(method = "updateFallFlyingMovement(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;", at = @At("RETURN"), cancellable = true)
  public void decayGlideVelocity(CallbackInfoReturnable<Vec3> cir) {
    float decayFactor = 1.0F;

    if (getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ArmoredElytra elytra) {
      decayFactor = elytra.glideDecay;
    }

    cir.setReturnValue(
        cir.getReturnValue().multiply(decayFactor, 1.0D, decayFactor)
    );
  }
}
