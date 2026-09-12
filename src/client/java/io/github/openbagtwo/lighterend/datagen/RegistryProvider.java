package io.github.openbagtwo.lighterend.datagen;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;

public class RegistryProvider extends FabricDynamicRegistryProvider {

  protected RegistryProvider(
      FabricPackOutput output,
      CompletableFuture<Provider> registriesFuture
  ) {
    super(output, registriesFuture);
  }

  @Override
  protected void configure(HolderLookup.Provider registries, Entries entries) {
    entries.addAll(registries.lookupOrThrow(Registries.NOISE));
    entries.addAll(registries.lookupOrThrow(Registries.FEATURE));
    entries.addAll(registries.lookupOrThrow(Registries.PLACED_FEATURE));
    entries.addAll(registries.lookupOrThrow(Registries.BIOME));
    entries.addAll(registries.lookupOrThrow(Registries.TRIM_MATERIAL));
  }

  @Override
  public String getName() {
    return "LighterEndRegistryProvider";
  }
}
