package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

public class EndMossRenderer {

  public static BlockTintSource getBlockColor() {
    return new BlockTintSource() {
      @Override
      public int color(final BlockState state) {
        return colorInWorld(state, null, BlockPos.ZERO);
      }

      @Override
      public int colorInWorld(
          final BlockState state,
          final BlockAndTintGetter world,
          final BlockPos pos
      ) {
        if (world != null && pos != null) {
          if (!world.hasBiomes()) {
            return 0x4ad6d5;
          }
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
      }
    };
  }
}
