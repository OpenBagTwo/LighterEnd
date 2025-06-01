package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.render.BlockRenderLayer;

public class UmbrellaMembraneRenderer {

  public static void initialize() {
    BlockRenderLayerMap.putBlocks(
        BlockRenderLayer.TRANSLUCENT,
        LighterEndBlocks.UMBRELLA_MEMBRANE
    );
  }

}
