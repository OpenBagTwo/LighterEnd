package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.registries.LighterEndData;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class TooltipMixin {

  @Shadow
  protected abstract <T extends TooltipAppender> void appendTooltip(
      ComponentType<T> componentType,
      Item.TooltipContext context,
      Consumer<Text> textConsumer,
      TooltipType type
  );

  @Inject(method = "getTooltip", at = @At("RETURN"))
  public void appendLighterEndTooltips(
      Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type,
      CallbackInfoReturnable<List<Text>> cir
  ) {
    List<Text> list = cir.getReturnValue();
    this.appendTooltip(
        LighterEndData.MOTHS,
        context,
        list::add,
        type
    );
    this.appendTooltip(
        LighterEndData.SILK_LEVEL,
        context,
        list::add,
        type
    );
    cir.setReturnValue(list);

  }

}
