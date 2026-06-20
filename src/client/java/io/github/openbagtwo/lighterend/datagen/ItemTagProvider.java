package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.misc.Wood.WoodSet;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks.Material;
import io.github.openbagtwo.lighterend.registries.LighterEndEquipment;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

public class ItemTagProvider extends FabricTagsProvider.ItemTagsProvider {

  public ItemTagProvider(
      FabricPackOutput output,
      CompletableFuture<Provider> future
  ) {
    super(output, future);
  }

  @Override
  protected void addTags(HolderLookup.Provider lookup) {
    for (WoodSet wood : Arrays.asList(
        LighterEndBlocks.TENANEA,
        LighterEndBlocks.UMBRELLA,
        LighterEndBlocks.LOTUS,
        LighterEndBlocks.GLOWSHROOM,
        LighterEndBlocks.DRAGON
    )) {
      builder(LighterEndTags.LOG_TAGS.get(wood.baseName)).add(
          wood.log.asItem(),
          wood.strippedLog.asItem(),
          wood.wood.asItem(),
          wood.strippedWood.asItem()
      );
      valueLookupBuilder(LighterEndTags.STRIPPED_LOG_TAGS.get(wood.baseName)).add(
          wood.strippedLog.asItem(),
          wood.strippedWood.asItem()
      );
      valueLookupBuilder(ItemTags.LOGS_THAT_BURN).addTag(
          LighterEndTags.LOG_TAGS.get(wood.baseName)
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
      valueLookupBuilder(ItemTags.WOODEN_SHELVES).add(wood.shelf.asItem());

    }
    for (Material material : Arrays.asList(
        LighterEndBlocks.VIOLECITE,
        LighterEndBlocks.AZURE_JADESTONE,
        LighterEndBlocks.SANDY_JADESTONE,
        LighterEndBlocks.VIRID_JADESTONE,
        LighterEndBlocks.UMBRALITH,
        LighterEndBlocks.BORNITE
    )) {
      valueLookupBuilder(ItemTags.).add(material.button.asItem());
    }
    valueLookupBuilder(ItemTags.CHICKEN_FOOD).add(LighterEndBlocks.LUMECORN_SEED.asItem());
    valueLookupBuilder(ItemTags.BEE_FOOD).add(LighterEndBlocks.TENANEA_FLOWER.asItem());
    valueLookupBuilder(ItemTags.LEAVES).add(
        LighterEndBlocks.TENANEA_LEAVES.asItem(),
        LighterEndItems.GLOWSHROOM_FUR,
        LighterEndItems.AGAVE_FUR,
        LighterEndBlocks.DRAGON_LEAVES.asItem()
    );

    valueLookupBuilder(ItemTags.FISHES).add(LighterEndItems.RAW_END_FISH);

    valueLookupBuilder(ItemTags.CHEST_ARMOR).add(
        LighterEndEquipment.SILK_ELYTRA
    );  // this makes silk elytra trimmable

    valueLookupBuilder(ItemTags.GAZE_DISGUISE_EQUIPMENT).addTag(LighterEndTags.FUR_ITEMS);

    valueLookupBuilder(ItemTags.BREWING_FUEL).add(LighterEndItems.END_POWDER);

    valueLookupBuilder(ItemTags.MEAT).add(
        LighterEndItems.CRAB_MEAT,
        LighterEndItems.CRAB_CAKE
    );

    valueLookupBuilder(ItemTags.PIGLIN_FOOD).add(
        LighterEndItems.CRAB_MEAT,
        LighterEndItems.CRAB_CAKE
    );

    valueLookupBuilder(ItemTags.TRIM_MATERIALS).add(
        LighterEndItems.AURORA_CRYSTAL_SHARD
    );

    valueLookupBuilder(ItemTags.CAULDRON_CAN_REMOVE_DYE).add(
        LighterEndEquipment.SILK_ELYTRA
    );

    valueLookupBuilder(LighterEndTags.REPAIRS_SILK_ARMOR).add(LighterEndItems.SILK);

    valueLookupBuilder(LighterEndTags.FLETCHINGS).add(
        Items.FEATHER,
        LighterEndBlocks.CHARNIA_CYAN.asItem(),
        LighterEndBlocks.CHARNIA_GREEN.asItem(),
        LighterEndBlocks.CHARNIA_LIGHT_BLUE.asItem(),
        LighterEndBlocks.CHARNIA_ORANGE.asItem(),
        LighterEndBlocks.CHARNIA_PURPLE.asItem(),
        LighterEndBlocks.CHARNIA_RED.asItem()
    );
    valueLookupBuilder(LighterEndTags.FLETCHINGS).addTag(LighterEndTags.FUR_ITEMS);

    valueLookupBuilder(LighterEndTags.FUR_ITEMS).add(
        LighterEndItems.GLOWSHROOM_FUR,
        LighterEndItems.AGAVE_FUR
    );

    valueLookupBuilder(LighterEndTags.POLYPORES).add(
        LighterEndBlocks.AURANT_POLYPORE.asItem(),
        LighterEndBlocks.PURPLE_POLYPORE.asItem()
    );

    valueLookupBuilder(LighterEndTags.MOOSHROOM_FOOD).add(
        LighterEndItems.LUMECORN_EAR
    );
  }


}
