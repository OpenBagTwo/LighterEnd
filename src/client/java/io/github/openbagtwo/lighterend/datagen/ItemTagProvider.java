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
import net.minecraft.tags.BlockItemTags;
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
      builder(LighterEndTags.STRIPPED_LOG_TAGS.get(wood.baseName)).add(
          wood.strippedLog.asItem(),
          wood.strippedWood.asItem()
      );
      builder(ItemTags.LOGS_THAT_BURN).addTag(
          LighterEndTags.LOG_TAGS.get(wood.baseName)
      );
      builder(ItemTags.PLANKS).add(wood.planks.asItem());
      builder(ItemTags.WOODEN_BUTTONS).add(wood.button.asItem());
      builder(ItemTags.WOODEN_DOORS).add(wood.door.asItem());
      builder(ItemTags.WOODEN_STAIRS).add(wood.stairs.asItem());
      builder(ItemTags.WOODEN_SLABS).add(wood.slab.asItem());
      builder(ItemTags.WOODEN_FENCES).add(wood.fence.asItem());
      builder(ItemTags.FENCE_GATES).add(wood.gate.asItem());
      builder(ItemTags.WOODEN_PRESSURE_PLATES).add(wood.pressurePlate.asItem());
      builder(ItemTags.WOODEN_TRAPDOORS).add(wood.trapdoor.asItem());
      builder(ItemTags.SIGNS).add(wood.sign.asItem());
      builder(ItemTags.HANGING_SIGNS).add(wood.hangingSign.asItem());
      builder(ItemTags.WOODEN_SHELVES).add(wood.shelf.asItem());

    }
    for (Material material : Arrays.asList(
        LighterEndBlocks.VIOLECITE,
        LighterEndBlocks.AZURE_JADESTONE,
        LighterEndBlocks.SANDY_JADESTONE,
        LighterEndBlocks.VIRID_JADESTONE,
        LighterEndBlocks.UMBRALITH,
        LighterEndBlocks.BORNITE
    )) {
      builder(BlockItemTags.BUTTONS).add(material.button.asItem());
    }
    builder(ItemTags.CHICKEN_FOOD).add(LighterEndBlocks.LUMECORN_SEED.asItem());
    builder(ItemTags.BEE_FOOD).add(LighterEndBlocks.TENANEA_FLOWER.asItem());
    builder(ItemTags.LEAVES).add(
        LighterEndBlocks.TENANEA_LEAVES.asItem(),
        LighterEndItems.GLOWSHROOM_FUR,
        LighterEndItems.AGAVE_FUR,
        LighterEndBlocks.DRAGON_LEAVES.asItem()
    );

    builder(ItemTags.FISHES).add(LighterEndItems.RAW_END_FISH);

    builder(ItemTags.CHEST_ARMOR).add(
        LighterEndEquipment.SILK_ELYTRA
    );  // this makes silk elytra trimmable

    builder(ItemTags.GAZE_DISGUISE_EQUIPMENT).addTag(LighterEndTags.FUR_ITEMS);

    builder(ItemTags.BREWING_FUEL).add(LighterEndItems.END_POWDER);

    builder(ItemTags.MEAT).add(
        LighterEndItems.CRAB_MEAT,
        LighterEndItems.CRAB_CAKE
    );

    builder(ItemTags.PIGLIN_FOOD).add(
        LighterEndItems.CRAB_MEAT,
        LighterEndItems.CRAB_CAKE
    );

    builder(ItemTags.TRIM_MATERIALS).add(
        LighterEndItems.AURORA_CRYSTAL_SHARD
    );

    builder(ItemTags.CAULDRON_CAN_REMOVE_DYE).add(
        LighterEndEquipment.SILK_ELYTRA
    );

    builder(LighterEndTags.REPAIRS_SILK_ARMOR).add(LighterEndItems.SILK);

    builder(LighterEndTags.FLETCHINGS).add(
        Items.FEATHER,
        LighterEndBlocks.CHARNIA_CYAN.asItem(),
        LighterEndBlocks.CHARNIA_GREEN.asItem(),
        LighterEndBlocks.CHARNIA_LIGHT_BLUE.asItem(),
        LighterEndBlocks.CHARNIA_ORANGE.asItem(),
        LighterEndBlocks.CHARNIA_PURPLE.asItem(),
        LighterEndBlocks.CHARNIA_RED.asItem()
    );
    builder(LighterEndTags.FLETCHINGS).addTag(LighterEndTags.FUR_ITEMS);

    builder(LighterEndTags.FUR_ITEMS).add(
        LighterEndItems.GLOWSHROOM_FUR,
        LighterEndItems.AGAVE_FUR
    );

    builder(LighterEndTags.POLYPORES).add(
        LighterEndBlocks.AURANT_POLYPORE.asItem(),
        LighterEndBlocks.PURPLE_POLYPORE.asItem()
    );

    builder(LighterEndTags.MOOSHROOM_FOOD).add(
        LighterEndItems.LUMECORN_EAR
    );
  }


}
