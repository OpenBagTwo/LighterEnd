package io.github.openbagtwo.lighterend.rendering;

import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.HangingSignBlockEntityRenderer;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;

public class SignRenderer {

  public static void initialize() {
    BlockEntityRendererFactories.register(LighterEndBlockEntities.SIGN,
        SignBlockEntityRenderer::new);
    BlockEntityRendererFactories.register(LighterEndBlockEntities.HANGING_SIGN,
        HangingSignBlockEntityRenderer::new);
  }
}
