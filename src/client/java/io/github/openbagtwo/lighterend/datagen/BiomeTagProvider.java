package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.registries.LighterEndBiomes;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.biome.Biome;

public class BiomeTagProvider extends FabricTagProvider<Biome> {

  public BiomeTagProvider(FabricDataOutput output,
      CompletableFuture<RegistryWrapper.WrapperLookup> future) {
    super(output, RegistryKeys.BIOME, future);
  }


  @Override
  protected void configure(WrapperLookup lookup) {
    builder(BiomeTags.IS_END).add(LighterEndBiomes.BLOSSOM_FOREST);
    builder(BiomeTags.END_CITY_HAS_STRUCTURE).add(LighterEndBiomes.BLOSSOM_FOREST);

  }
}
