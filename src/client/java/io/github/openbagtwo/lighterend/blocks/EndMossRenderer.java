package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;

public class EndMossRenderer {

  public static BlockColor getBlockColor() {
    return (state, world, pos, tintIndex) -> {
      if (world != null && pos != null) {
        Holder<Biome> biome = world.getBiomeFabric(pos);
        if (biome.is(BiomeTags.IS_END)) {
          if (biome.is(LighterEndTags.VANILLA_END_BIOMES)) {
            return 0x4ad6d5;
          }
          return BiomeColors.getAverageGrassColor(world, pos);
        }
        return 0xFFFFFF;
      }
      return 0x4ad6d5;
    };
  }
}
