package io.github.openbagtwo.lighterend.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityType.class)
public abstract class BlockEntityTypeMixin {

  @Inject(method = "supports", at = @At("HEAD"), cancellable = true)
  private void allowCustomSigns(BlockState state, CallbackInfoReturnable<Boolean> info) {
    final BlockEntityType self = (BlockEntityType) (Object) this;
    if (self == BlockEntityType.SIGN || self == BlockEntityType.HANGING_SIGN) {
      info.setReturnValue(true);
    }

  }


}
