package io.github.openbagtwo.lighterend.mixin.client;

import io.github.openbagtwo.lighterend.blocks.AuroraCrystalRenderer;
import io.github.openbagtwo.lighterend.blocks.TenaneaFlowerRenderer;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.color.block.BlockColors;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftMixin {

  @Final
  @Shadow
  private BlockColors blockColors;

  @Inject(method = "<init>*", at = @At("TAIL"))
  private void provideColors(RunArgs args, CallbackInfo info) {
    blockColors.registerColorProvider(AuroraCrystalRenderer.getBlockColor(),
        LighterEndBlocks.AURORA_CRYSTAL);
    blockColors.registerColorProvider(TenaneaFlowerRenderer.getBlockColor(),
        LighterEndBlocks.TENANEA_FLOWER);
  }

}
