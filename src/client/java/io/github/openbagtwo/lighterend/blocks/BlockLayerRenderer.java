package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.client.render.BlockRenderLayer;

public class BlockLayerRenderer {

  public static void initialize() {
    BlockRenderLayerMap.putBlocks(
        BlockRenderLayer.TRANSLUCENT,
        LighterEndBlocks.AURORA_CRYSTAL,
        LighterEndBlocks.UMBRELLA_MEMBRANE,
        LighterEndBlocks.OBELISK,
        LighterEndBlocks.FERROUS_ICE,
        LighterEndBlocks.EMERALD_ICE,
        LighterEndBlocks.AUROUS_ICE
    );

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
        LighterEndBlocks.POTTED_TENANEA_SAPLING,
        LighterEndBlocks.UMBRELLA_TREE_SAPLING,
        LighterEndBlocks.POTTED_UMBRELLA_SAPLING,
        LighterEndBlocks.CHARNIA_CYAN,
        LighterEndBlocks.CHARNIA_GREEN,
        LighterEndBlocks.CHARNIA_LIGHT_BLUE,
        LighterEndBlocks.CHARNIA_ORANGE,
        LighterEndBlocks.CHARNIA_PURPLE,
        LighterEndBlocks.CHARNIA_RED,
        LighterEndBlocks.END_LILY,
        LighterEndBlocks.END_LILY_SEED,
        LighterEndBlocks.END_LOTUS_FLOWER,
        LighterEndBlocks.END_LOTUS_STEM,
        LighterEndBlocks.END_LOTUS_LEAF,
        LighterEndBlocks.END_LOTUS_SEED,
        LighterEndBlocks.GLOWSHROOM_FUR,
        LighterEndBlocks.LOTUS.trapdoor,
        LighterEndBlocks.GLOWSHROOM.door,
        LighterEndBlocks.GLOWSHROOM.trapdoor,
        LighterEndBlocks.GLOWSHROOM_SAPLING,
        LighterEndBlocks.POTTED_GLOWSHROOM_SAPLING,
        LighterEndBlocks.AGAVE,
        LighterEndBlocks.AGAVE_FUR,
        LighterEndBlocks.AGAVE_SEED,
        LighterEndBlocks.AURANT_POLYPORE,
        LighterEndBlocks.END_FURNACE,
        LighterEndBlocks.END_SMOKER,
        LighterEndBlocks.GOLD_CHANDELIER,
        LighterEndBlocks.IRON_CHANDELIER
    );
    for (Block chandelier : LighterEndBlocks.COPPER_CHANDELIERS.getAll()) {
      BlockRenderLayerMap.putBlocks(BlockRenderLayer.CUTOUT, chandelier);
    }

    BlockRenderLayerMap.putBlocks(
        BlockRenderLayer.CUTOUT_MIPPED,
        LighterEndBlocks.END_MOSS,
        LighterEndBlocks.UMBRALITH.baseBlock,
        LighterEndBlocks.END_STONE_QUARTZ_ORE,
        LighterEndBlocks.END_STONE_REDSTONE_ORE,
        LighterEndBlocks.UMBRALITH_QUARTZ_ORE,
        LighterEndBlocks.UMBRALITH_REDSTONE_ORE
    );
  }

}
