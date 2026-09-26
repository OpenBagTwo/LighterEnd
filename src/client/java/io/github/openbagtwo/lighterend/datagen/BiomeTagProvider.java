package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.registries.LighterEndBiomes;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

public class BiomeTagProvider extends FabricTagProvider<Biome> {

  public BiomeTagProvider(
      FabricDataOutput output,
      CompletableFuture<HolderLookup.Provider> future
  ) {
    super(output, Registries.BIOME, future);
  }


  @Override
  protected void addTags(Provider lookup) {
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
    builder(BiomeTags.HAS_END_CITY).add(
        LighterEndBiomes.BLOSSOM_FOREST,
        LighterEndBiomes.UMBRELLA_JUNGLE,
        LighterEndBiomes.GLOWING_GRASSLAND,
        LighterEndBiomes.UMBRA_VALLEY,
        LighterEndBiomes.FOGGY_MUSHROOMLANDS,
        LighterEndBiomes.SULPHUR_SPRINGS
    );

    builder(LighterEndTags.VANILLA_END_BIOMES).add(
        Biomes.END_BARRENS,
        Biomes.SMALL_END_ISLANDS,
        Biomes.END_MIDLANDS,
        Biomes.END_HIGHLANDS
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
        Biomes.END_HIGHLANDS,
        LighterEndBiomes.GLOWING_GRASSLAND,
        LighterEndBiomes.UMBRELLA_JUNGLE,
        LighterEndBiomes.BLOSSOM_FOREST,
        LighterEndBiomes.UMBRA_VALLEY,
        LighterEndBiomes.FOGGY_MUSHROOMLANDS,
        LighterEndBiomes.SULPHUR_SPRINGS,
        LighterEndBiomes.SHADOW_FOREST
    );

    builder(LighterEndTags.INVALID_SPAWN_BIOMES).add(
        Biomes.THE_VOID,
        Biomes.END_BARRENS,
        Biomes.SMALL_END_ISLANDS,
        LighterEndBiomes.STARFIELD
    );
  }
}
