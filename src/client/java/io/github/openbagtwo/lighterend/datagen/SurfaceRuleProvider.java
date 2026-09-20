package io.github.openbagtwo.lighterend.datagen;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import org.jspecify.annotations.NonNull;

public class SurfaceRuleProvider extends FabricDynamicRegistryProvider {

  protected SurfaceRuleProvider(FabricPackOutput output,
      CompletableFuture<Provider> registriesFuture) {
    super(output, registriesFuture);
  }

  @Override
  protected void configure(HolderLookup.@NonNull Provider registries, @NonNull Entries entries) {
    entries.addAll(registries.lookupOrThrow(Registries.MATERIAL_RULE));
  }

  @Override
  public @NonNull String getName() {
    return "LighterEnd Worldgen";
  }
}
