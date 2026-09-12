package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.registries.LighterEndBiomes;
import io.github.openbagtwo.lighterend.registries.LighterEndTrimming;
import io.github.openbagtwo.lighterend.world.LighterEndConfiguredFeatures;
import io.github.openbagtwo.lighterend.world.LighterEndPlacedFeatures;
import io.github.openbagtwo.lighterend.world.gen.noise.NoiseParameters;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class LighterEndDataGenerator implements DataGeneratorEntrypoint {

  @Override
  public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
    Pack pack = fabricDataGenerator.createPack();
    pack.addProvider(BlockLootTableProvider::new);
    pack.addProvider(ChestLootTableProvider::new);
    pack.addProvider(ModelProvider::new);
    pack.addProvider(RecipeProvider::new);
    pack.addProvider(BlockTagProvider::new);
    pack.addProvider(ItemTagProvider::new);
    pack.addProvider(MobTagProvider::new);
    pack.addProvider(BiomeTagProvider::new);
    pack.addProvider(RegistryProvider::new);
    pack.addProvider(AdvancementProvider::new);
  }

  @Override
  public void buildRegistry(RegistrySetBuilder registryBuilder) {
    registryBuilder.add(
        Registries.FEATURE,
        LighterEndConfiguredFeatures::bootstrap
    );
    registryBuilder.add(
        Registries.PLACED_FEATURE,
        LighterEndPlacedFeatures::bootstrap
    );
    registryBuilder.add(
        Registries.BIOME,
        LighterEndBiomes::bootstrap
    );
    registryBuilder.add(Registries.NOISE, NoiseParameters::bootstrap);
    registryBuilder.add(Registries.TRIM_MATERIAL, LighterEndTrimming::bootstrap);
  }
}
