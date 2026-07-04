package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.registries.LighterEndBiomes;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class BiomeTagProvider extends FabricTagProvider<Biome> {

  public BiomeTagProvider(
      FabricDataOutput output,
      CompletableFuture<RegistryWrapper.WrapperLookup> future
  ) {
    super(output, RegistryKeys.BIOME, future);
  }


  @Override
  protected void configure(WrapperLookup lookup) {
    builder(BiomeTags.IS_END).add(
        LighterEndBiomes.BLOSSOM_FOREST,
        LighterEndBiomes.UMBRELLA_JUNGLE,
        LighterEndBiomes.GLOWING_GRASSLAND,
        LighterEndBiomes.MEGALAKE,
        LighterEndBiomes.UMBRA_VALLEY,
        LighterEndBiomes.FOGGY_MUSHROOMLANDS,
        LighterEndBiomes.STARFIELD,
        LighterEndBiomes.SULPHUR_SPRINGS,
        LighterEndBiomes.SHADOW_FOREST
    );
    builder(BiomeTags.END_CITY_HAS_STRUCTURE).add(
        LighterEndBiomes.BLOSSOM_FOREST,
        LighterEndBiomes.UMBRELLA_JUNGLE,
        LighterEndBiomes.GLOWING_GRASSLAND,
        LighterEndBiomes.UMBRA_VALLEY,
        LighterEndBiomes.FOGGY_MUSHROOMLANDS,
        LighterEndBiomes.SULPHUR_SPRINGS
    );

    builder(LighterEndTags.VANILLA_END_BIOMES).add(
        BiomeKeys.END_BARRENS,
        BiomeKeys.SMALL_END_ISLANDS,
        BiomeKeys.END_MIDLANDS,
        BiomeKeys.END_HIGHLANDS
    );

    builder(LighterEndTags.HAS_END_LAKES).addTag(
        LighterEndTags.VANILLA_END_BIOMES
    );
    builder(LighterEndTags.HAS_END_LAKES).add(
        LighterEndBiomes.UMBRELLA_JUNGLE,
        LighterEndBiomes.GLOWING_GRASSLAND,
        LighterEndBiomes.FOGGY_MUSHROOMLANDS,
        LighterEndBiomes.SHADOW_FOREST
    );

    builder(LighterEndTags.HAS_OBELISKS).add(
        BiomeKeys.END_HIGHLANDS,
        LighterEndBiomes.GLOWING_GRASSLAND,
        LighterEndBiomes.UMBRELLA_JUNGLE,
        LighterEndBiomes.BLOSSOM_FOREST,
        LighterEndBiomes.UMBRA_VALLEY,
        LighterEndBiomes.FOGGY_MUSHROOMLANDS,
        LighterEndBiomes.SULPHUR_SPRINGS,
        LighterEndBiomes.SHADOW_FOREST
    );
  }
}
