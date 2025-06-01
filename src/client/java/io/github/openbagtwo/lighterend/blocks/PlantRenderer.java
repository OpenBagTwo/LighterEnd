package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.render.BlockRenderLayer;

public class PlantRenderer {

  public static void initialize() {
    BlockRenderLayerMap.putBlocks(
        BlockRenderLayer.CUTOUT,
        LighterEndBlocks.CREEPING_MOSS,
        LighterEndBlocks.UMBRELLA_FERN,
        LighterEndBlocks.TALL_UMBRELLA_FERN,
        LighterEndBlocks.LUMECORN_SEED,
        LighterEndBlocks.LUMECORN,
        LighterEndBlocks.LUMECORN_STEM,
        LighterEndBlocks.TENANEA_FLOWER,
        LighterEndBlocks.TENANEA_SAPLING,
        LighterEndBlocks.UMBRELLA_TREE_SAPLING,
        LighterEndBlocks.CHARNIA_CYAN,
        LighterEndBlocks.CHARNIA_GREEN,
        LighterEndBlocks.CHARNIA_LIGHT_BLUE,
        LighterEndBlocks.CHARNIA_ORANGE,
        LighterEndBlocks.CHARNIA_PURPLE,
        LighterEndBlocks.CHARNIA_RED
    );
  }

}
