package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks.Material;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks.Wood;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.ItemTags;

public class ItemTagProvider extends FabricTagProvider.ItemTagProvider {

  public ItemTagProvider(FabricDataOutput output,
      CompletableFuture<WrapperLookup> future) {
    super(output, future);
  }

  @Override
  protected void configure(RegistryWrapper.WrapperLookup lookup) {
    for (Wood wood : Arrays.asList(LighterEndBlocks.TENANEA)) {
      getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN).add(wood.log.asItem(),
          wood.strippedLog.asItem(), wood.wood.asItem(), wood.strippedWood.asItem());
      getOrCreateTagBuilder(ItemTags.PLANKS).add(wood.planks.asItem());
      getOrCreateTagBuilder(ItemTags.WOODEN_BUTTONS).add(wood.button.asItem());
      getOrCreateTagBuilder(ItemTags.WOODEN_DOORS).add(wood.door.asItem());
      getOrCreateTagBuilder(ItemTags.WOODEN_STAIRS).add(wood.stairs.asItem());
      getOrCreateTagBuilder(ItemTags.WOODEN_SLABS).add(wood.slab.asItem());
      getOrCreateTagBuilder(ItemTags.WOODEN_FENCES).add(wood.fence.asItem());
      getOrCreateTagBuilder(ItemTags.FENCE_GATES).add(wood.gate.asItem());
      getOrCreateTagBuilder(ItemTags.WOODEN_PRESSURE_PLATES).add(wood.pressurePlate.asItem());
      getOrCreateTagBuilder(ItemTags.WOODEN_TRAPDOORS).add(wood.trapdoor.asItem());
      getOrCreateTagBuilder(ItemTags.SIGNS).add(wood.sign.asItem());
      getOrCreateTagBuilder(ItemTags.HANGING_SIGNS).add(wood.hangingSign.asItem());

    }
    for (Material material : Arrays.asList(
        LighterEndBlocks.VIOLECITE,
        LighterEndBlocks.AZURE_JADESTONE,
        LighterEndBlocks.SANDY_JADESTONE,
        LighterEndBlocks.VIRID_JADESTONE, LighterEndBlocks.UMBRALITH
    )) {
      getOrCreateTagBuilder(ItemTags.STONE_BUTTONS).add(material.button.asItem());
    }
    getOrCreateTagBuilder(ItemTags.CHICKEN_FOOD).add(LighterEndBlocks.LUMECORN_SEED.asItem());
    getOrCreateTagBuilder(ItemTags.BEE_FOOD).add(LighterEndBlocks.TENANEA_FLOWER.asItem());

  }


}
