package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.tags.LighterEndTags;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.biome.Biome;

public class EndMossRenderer {

  public static BlockColorProvider getBlockColor() {
    return (state, world, pos, tintIndex) -> {
      if (world != null && pos != null) {
        RegistryEntry<Biome> biome = world.getBiomeFabric(pos);
        if (biome.isIn(BiomeTags.IS_END)) {
          if (biome.isIn(LighterEndTags.VANILLA_END_BIOMES)) {
            return 0x4ad6d5;
          }
          return BiomeColors.getGrassColor(world, pos);
        }
      }
      return 0xFFFFFF;
    };
  }

  public static void initialize() {
    BlockRenderLayerMap.putBlocks(
        BlockRenderLayer.CUTOUT_MIPPED,
        LighterEndBlocks.END_MOSS
    );
  }

}
