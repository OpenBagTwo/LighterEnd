package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks.Material;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks.Wood;
import io.github.openbagtwo.lighterend.registries.LighterEndEquipment;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
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
    for (Wood wood : Arrays.asList(
        LighterEndBlocks.TENANEA,
        LighterEndBlocks.UMBRELLA,
        LighterEndBlocks.LOTUS,
        LighterEndBlocks.GLOWSHROOM
    )) {
      valueLookupBuilder(ItemTags.LOGS_THAT_BURN).add(
          wood.log.asItem(),
          wood.strippedLog.asItem(),
          wood.wood.asItem(),
          wood.strippedWood.asItem()
      );
      valueLookupBuilder(ItemTags.PLANKS).add(wood.planks.asItem());
      valueLookupBuilder(ItemTags.WOODEN_BUTTONS).add(wood.button.asItem());
      valueLookupBuilder(ItemTags.WOODEN_DOORS).add(wood.door.asItem());
      valueLookupBuilder(ItemTags.WOODEN_STAIRS).add(wood.stairs.asItem());
      valueLookupBuilder(ItemTags.WOODEN_SLABS).add(wood.slab.asItem());
      valueLookupBuilder(ItemTags.WOODEN_FENCES).add(wood.fence.asItem());
      valueLookupBuilder(ItemTags.FENCE_GATES).add(wood.gate.asItem());
      valueLookupBuilder(ItemTags.WOODEN_PRESSURE_PLATES).add(wood.pressurePlate.asItem());
      valueLookupBuilder(ItemTags.WOODEN_TRAPDOORS).add(wood.trapdoor.asItem());
      valueLookupBuilder(ItemTags.SIGNS).add(wood.sign.asItem());
      valueLookupBuilder(ItemTags.HANGING_SIGNS).add(wood.hangingSign.asItem());

    }
    for (Material material : Arrays.asList(
        LighterEndBlocks.VIOLECITE,
        LighterEndBlocks.AZURE_JADESTONE,
        LighterEndBlocks.SANDY_JADESTONE,
        LighterEndBlocks.VIRID_JADESTONE,
        LighterEndBlocks.UMBRALITH
    )) {
      valueLookupBuilder(ItemTags.STONE_BUTTONS).add(material.button.asItem());
    }
    valueLookupBuilder(ItemTags.CHICKEN_FOOD).add(LighterEndBlocks.LUMECORN_SEED.asItem());
    valueLookupBuilder(ItemTags.BEE_FOOD).add(LighterEndBlocks.TENANEA_FLOWER.asItem());
    valueLookupBuilder(ItemTags.LEAVES).add(
        LighterEndBlocks.TENANEA_LEAVES.asItem(),
        LighterEndItems.GLOWSHROOM_FUR
    );

    valueLookupBuilder(ItemTags.FISHES).add(LighterEndItems.RAW_END_FISH);

    valueLookupBuilder(ItemTags.DYEABLE).add(
        LighterEndEquipment.SILK_ELYTRA
    );

    valueLookupBuilder(ItemTags.CHEST_ARMOR).add(
        LighterEndEquipment.SILK_ELYTRA
    );  // this makes silk elytra trimmable

    valueLookupBuilder(LighterEndTags.REPAIRS_SILK_ARMOR).add(LighterEndItems.SILK);
  }


}
