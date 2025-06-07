package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class UmbralithRenderer {

  public static void initialize() {
    BlockRenderLayerMap.INSTANCE.putBlocks(
        RenderLayer.getCutoutMipped(),
        LighterEndBlocks.UMBRALITH.baseBlock
    );
  }

}
