package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class WetFurMixin {

  @Inject(method = "baseTick", at = @At("TAIL"))
  public void checkForWetFur(CallbackInfo ci) {
    LivingEntity thisEntity = (LivingEntity) (Object) this;
    ItemStack maybeFur = thisEntity.getItemBySlot(EquipmentSlot.HEAD);
    if (
        thisEntity.isEyeInFluid(FluidTags.WATER)
            && maybeFur.is(LighterEndTags.FUR_ITEMS)
    ) {
      thisEntity.drop(maybeFur, true, true);
      thisEntity.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
      thisEntity.makeSound(LighterEndSounds.WET_FUR);
    }
  }

}
