package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup.Provider;

public class BlockLootTableProvider extends FabricBlockLootTableProvider {
  protected BlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<Provider> registryLookup){
    super(dataOutput, registryLookup);
  }

  @Override
  public void generate() {
    dropWhenSilkTouch(LighterEndBlocks.AURORA_CRYSTAL);
  }

}
