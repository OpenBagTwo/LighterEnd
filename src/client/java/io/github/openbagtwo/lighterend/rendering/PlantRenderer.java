package io.github.openbagtwo.lighterend.rendering;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.BlockRenderLayer;

public class PlantRenderer {

  public static void initialize() {
    BlockRenderLayerMap.INSTANCE.putBlocks(
        BlockRenderLayer.CUTOUT,
        LighterEndBlocks.CREEPING_MOSS,
        LighterEndBlocks.UMBRELLA_FERN,
        LighterEndBlocks.TALL_UMBRELLA_FERN,
        LighterEndBlocks.LUMECORN_SEED,
        LighterEndBlocks.LUMECORN,
        LighterEndBlocks.LUMECORN_STEM,
        LighterEndBlocks.TENANEA_FLOWER,
        LighterEndBlocks.TENANEA_SAPLING,
        LighterEndBlocks.UMBRELLA_TREE_SAPLING
    );
  }

}
