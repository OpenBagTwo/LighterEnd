package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.registries.LighterEndData;
import java.util.function.Consumer;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.TooltipDisplayComponent;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class TooltipMixin {

  @Shadow
  public abstract <T extends TooltipAppender> void appendComponentTooltip(
      ComponentType<T> componentType,
      Item.TooltipContext context,
      TooltipDisplayComponent displayComponent,
      Consumer<Text> textConsumer,
      TooltipType type
  );

  @Inject(method = "appendTooltip", at = @At("HEAD"))
  public void appendLighterEndTooltips(
      Item.TooltipContext context,
      TooltipDisplayComponent displayComponent,
      @Nullable PlayerEntity player,
      TooltipType type,
      Consumer<Text> textConsumer,
      CallbackInfo ci
  ) {
    this.appendComponentTooltip(
        LighterEndData.MOTHS,
        context,
        displayComponent,
        textConsumer,
        type
    );
    this.appendComponentTooltip(
        LighterEndData.SILK_LEVEL,
        context,
        displayComponent,
        textConsumer,
        type
    );
    this.appendComponentTooltip(
        LighterEndData.TOTEM_TARGET,
        context,
        displayComponent,
        textConsumer,
        type
    );

  }

}
