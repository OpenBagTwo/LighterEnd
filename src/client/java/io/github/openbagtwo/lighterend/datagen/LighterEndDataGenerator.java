package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.registries.LighterEndBiomes;
import io.github.openbagtwo.lighterend.registries.LighterEndTrimming;
import io.github.openbagtwo.lighterend.world.LighterEndConfiguredFeatures;
import io.github.openbagtwo.lighterend.world.LighterEndPlacedFeatures;
import io.github.openbagtwo.lighterend.world.gen.noise.NoiseParameters;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

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
  public void buildRegistry(RegistryBuilder registryBuilder) {
    registryBuilder.addRegistry(
        RegistryKeys.CONFIGURED_FEATURE,
        LighterEndConfiguredFeatures::bootstrap
    );
    registryBuilder.addRegistry(
        RegistryKeys.PLACED_FEATURE,
        LighterEndPlacedFeatures::bootstrap
    );
    registryBuilder.addRegistry(
        RegistryKeys.BIOME,
        LighterEndBiomes::bootstrap
    );
    registryBuilder.addRegistry(RegistryKeys.NOISE_PARAMETERS, NoiseParameters::bootstrap);
    registryBuilder.addRegistry(RegistryKeys.TRIM_MATERIAL, LighterEndTrimming::bootstrap);
  }
}
