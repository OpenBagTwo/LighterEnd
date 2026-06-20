package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.ShelfRenderer;
import net.minecraft.client.renderer.blockentity.StandingSignRenderer;

public class BlockEntityRenderer {

  public static void initialize() {
    BlockEntityRenderers.register(
        LighterEndBlockEntities.SIGN,
        StandingSignRenderer::new
    );
    BlockEntityRenderers.register(
        LighterEndBlockEntities.HANGING_SIGN,
        HangingSignRenderer::new
    );
    BlockEntityRenderers.register(
        LighterEndBlockEntities.SHELF,
        ShelfRenderer::new
    );
    BlockEntityRenderers.register(
        LighterEndBlockEntities.PEDESTAL,
        PedestalRenderer::new
    );
  }

}
