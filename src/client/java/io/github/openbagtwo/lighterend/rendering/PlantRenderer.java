package io.github.openbagtwo.lighterend.rendering;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class PlantRenderer {

  public static void initialize() {
    BlockRenderLayerMap.INSTANCE.putBlock(LighterEndBlocks.CREEPING_MOSS, RenderLayer.getCutout());
  }

}
