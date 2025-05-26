package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.BlockRenderLayer;

public class UmbrellaMembraneRenderer {

  public static void initialize() {
    BlockRenderLayerMap.INSTANCE.putBlocks(
        BlockRenderLayer.TRANSLUCENT,
        LighterEndBlocks.UMBRELLA_MEMBRANE
    );
  }

}
