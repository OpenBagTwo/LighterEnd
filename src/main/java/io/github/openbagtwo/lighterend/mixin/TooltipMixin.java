package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.registries.LighterEndData;
import java.util.function.Consumer;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TooltipProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class TooltipMixin {

  @Shadow
  public abstract <T extends TooltipProvider> void addToTooltip(
      DataComponentType<T> componentType,
      Item.TooltipContext context,
      TooltipDisplay displayComponent,
      Consumer<Component> textConsumer,
      TooltipFlag type
  );

  @Inject(method = "addDetailsToTooltip", at = @At("HEAD"))
  public void appendLighterEndTooltips(
      Item.TooltipContext context,
      TooltipDisplay displayComponent,
      @Nullable Player player,
      TooltipFlag type,
      Consumer<Component> textConsumer,
      CallbackInfo ci
  ) {
    this.addToTooltip(
        LighterEndData.MOTHS,
        context,
        displayComponent,
        textConsumer,
        type
    );
    this.addToTooltip(
        LighterEndData.SILK_LEVEL,
        context,
        displayComponent,
        textConsumer,
        type
    );
    this.addToTooltip(
        LighterEndData.TOTEM_TARGET,
        context,
        displayComponent,
        textConsumer,
        type
    );

  }

}
