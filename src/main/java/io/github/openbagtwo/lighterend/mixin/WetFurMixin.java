package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class WetFurMixin {

  @Inject(method = "baseTick", at = @At("TAIL"))
  public void checkForWetFur(CallbackInfo ci) {
    LivingEntity thisEntity = (LivingEntity) (Object) this;
    ItemStack maybeFur = thisEntity.getEquippedStack(EquipmentSlot.HEAD);
    if (
        thisEntity.isSubmergedIn(FluidTags.WATER)
            && maybeFur.isIn(LighterEndTags.FUR_ITEMS)
    ) {
      thisEntity.dropItem(maybeFur, true, true);
      thisEntity.equipStack(EquipmentSlot.HEAD, ItemStack.EMPTY);
      thisEntity.playSound(LighterEndSounds.WET_FUR);
    }
  }

}
