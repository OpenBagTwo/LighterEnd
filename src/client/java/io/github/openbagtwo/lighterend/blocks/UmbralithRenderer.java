package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.render.BlockRenderLayer;

public class UmbralithRenderer {

  public static void initialize() {
    BlockRenderLayerMap.putBlocks(
        BlockRenderLayer.CUTOUT_MIPPED,
        LighterEndBlocks.UMBRALITH.baseBlock
    );
  }

}
