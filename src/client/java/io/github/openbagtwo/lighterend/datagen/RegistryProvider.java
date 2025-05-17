package io.github.openbagtwo.lighterend.datagen;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class RegistryProvider extends FabricDynamicRegistryProvider {

  protected RegistryProvider(FabricDataOutput output,
      CompletableFuture<WrapperLookup> registriesFuture) {
    super(output, registriesFuture);
  }

  @Override
  protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
    entries.addAll(registries.getOrThrow(RegistryKeys.CONFIGURED_FEATURE));
    entries.addAll(registries.getOrThrow(RegistryKeys.PLACED_FEATURE));
  }

  @Override
  public String getName() {
    return "LighterEndRegistryProvider";
  }

}
