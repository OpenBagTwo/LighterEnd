package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.world.LighterEndConfiguredFeatures;
import io.github.openbagtwo.lighterend.world.LighterEndPlacedFeatures;
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
    pack.addProvider(ModelProvider::new);
    pack.addProvider(RecipeProvider::new);
    pack.addProvider(BlockTagProvider::new);
    pack.addProvider(ItemTagProvider::new);
    pack.addProvider(RegistryProvider::new);
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
  }
}
