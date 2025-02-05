package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class BlockLootTableProvider extends FabricBlockLootTableProvider {
  protected BlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<WrapperLookup> registryLookup){
    super(dataOutput, registryLookup);
  }

  @Override
  public void generate() {
    addDropWithSilkTouch(LighterEndBlocks.AURORA_CRYSTAL);
  }

}
