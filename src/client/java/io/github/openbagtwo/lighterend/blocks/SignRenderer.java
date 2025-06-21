package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks.Wood;
import java.util.Arrays;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.HangingSignBlockEntityRenderer;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;

public class SignRenderer {

  public static void initialize() {
    BlockEntityRendererFactories.register(LighterEndBlockEntities.SIGN,
        SignBlockEntityRenderer::new);
    BlockEntityRendererFactories.register(LighterEndBlockEntities.HANGING_SIGN,
        HangingSignBlockEntityRenderer::new);

    for (Wood wood : Arrays.asList(
        LighterEndBlocks.TENANEA,
        LighterEndBlocks.UMBRELLA,
        LighterEndBlocks.LOTUS,
        LighterEndBlocks.GLOWSHROOM
    )) {
      TexturedRenderLayers.SIGN_TYPE_TEXTURES.put(wood.woodType,
          TexturedRenderLayers.getSignTextureId(wood.woodType));
      TexturedRenderLayers.HANGING_SIGN_TYPE_TEXTURES.put(wood.woodType,
          TexturedRenderLayers.getHangingSignTextureId(wood.woodType));
    }
  }

}
