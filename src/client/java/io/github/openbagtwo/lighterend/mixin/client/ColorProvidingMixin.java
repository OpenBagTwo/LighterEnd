package io.github.openbagtwo.lighterend.mixin.client;

import io.github.openbagtwo.lighterend.blocks.AuroraCrystalRenderer;
import io.github.openbagtwo.lighterend.blocks.EndMossRenderer;
import io.github.openbagtwo.lighterend.blocks.TenaneaFlowerRenderer;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class ColorProvidingMixin {

  @Final
  @Shadow
  private BlockColors blockColors;

  @Inject(method = "<init>*", at = @At("TAIL"))
  private void provideColors(GameConfig args, CallbackInfo info) {
    blockColors.register(
        List.of(AuroraCrystalRenderer.getBlockColor()),
        LighterEndBlocks.AURORA_CRYSTAL,
        LighterEndBlocks.OBELISK
    );
    blockColors.register(
        List.of(TenaneaFlowerRenderer.getBlockColor()),
        LighterEndBlocks.TENANEA_FLOWER
    );
    blockColors.register(
        List.of(EndMossRenderer.getBlockColor()),
        LighterEndBlocks.END_MOSS
    );
  }

}
