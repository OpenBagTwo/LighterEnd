package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.EntityTypeTags;

public class MobTagProvider extends FabricTagProvider.EntityTypeTagProvider {

  public MobTagProvider(FabricDataOutput dataOutput,
      CompletableFuture<WrapperLookup> registriesFuture) {
    super(dataOutput, registriesFuture);
  }

  @Override
  protected void configure(WrapperLookup wrapperLookup) {
    valueLookupBuilder(EntityTypeTags.ARTHROPOD).add(LighterEndMobs.SILK_MOTH.mob);
  }
}
