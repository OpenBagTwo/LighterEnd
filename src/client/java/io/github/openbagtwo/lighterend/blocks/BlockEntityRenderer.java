package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.misc.Wood.WoodSet;
import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import java.util.Arrays;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.HangingSignBlockEntityRenderer;
import net.minecraft.client.render.block.entity.ShelfBlockEntityRenderer;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;

public class BlockEntityRenderer {

  public static void initialize() {
    BlockEntityRendererFactories.register(
        LighterEndBlockEntities.SIGN,
        SignBlockEntityRenderer::new
    );
    BlockEntityRendererFactories.register(
        LighterEndBlockEntities.HANGING_SIGN,
        HangingSignBlockEntityRenderer::new
    );
    BlockEntityRendererFactories.register(
        LighterEndBlockEntities.SHELF,
        ShelfBlockEntityRenderer::new
    );
    BlockEntityRendererFactories.register(
        LighterEndBlockEntities.PEDESTAL,
        PedestalRenderer::new
    );

    for (WoodSet wood : Arrays.asList(
        LighterEndBlocks.TENANEA,
        LighterEndBlocks.UMBRELLA,
        LighterEndBlocks.LOTUS,
        LighterEndBlocks.GLOWSHROOM,
        LighterEndBlocks.DRAGON
    )) {
      TexturedRenderLayers.SIGN_TYPE_TEXTURES.put(wood.woodType,
          TexturedRenderLayers.getSignTextureId(wood.woodType));
      TexturedRenderLayers.HANGING_SIGN_TYPE_TEXTURES.put(wood.woodType,
          TexturedRenderLayers.getHangingSignTextureId(wood.woodType));
    }
  }

}
