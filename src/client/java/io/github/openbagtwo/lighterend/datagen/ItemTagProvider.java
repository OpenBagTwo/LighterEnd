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
import net.minecraft.references.ItemIds;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.ItemTags;

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
          LighterEndItems.idLookup.get(wood.log.asItem()),
          LighterEndItems.idLookup.get(wood.strippedLog.asItem()),
          LighterEndItems.idLookup.get(wood.wood.asItem()),
          LighterEndItems.idLookup.get(wood.strippedWood.asItem())
      );
      builder(LighterEndTags.STRIPPED_LOG_TAGS.get(wood.baseName)).add(
          LighterEndItems.idLookup.get(wood.strippedLog.asItem()),
          LighterEndItems.idLookup.get(wood.strippedWood.asItem())
      );
      builder(ItemTags.LOGS_THAT_BURN).addTag(
          LighterEndTags.LOG_TAGS.get(wood.baseName)
      );
      builder(ItemTags.PLANKS).add(LighterEndItems.idLookup.get(wood.planks.asItem()));
      builder(ItemTags.WOODEN_BUTTONS).add(LighterEndItems.idLookup.get(wood.button.asItem()));
      builder(ItemTags.WOODEN_DOORS).add(LighterEndItems.idLookup.get(wood.door.asItem()));
      builder(ItemTags.WOODEN_STAIRS).add(LighterEndItems.idLookup.get(wood.stairs.asItem()));
      builder(ItemTags.WOODEN_SLABS).add(LighterEndItems.idLookup.get(wood.slab.asItem()));
      builder(ItemTags.WOODEN_FENCES).add(LighterEndItems.idLookup.get(wood.fence.asItem()));
      builder(ItemTags.FENCE_GATES).add(LighterEndItems.idLookup.get(wood.gate.asItem()));
      builder(ItemTags.WOODEN_PRESSURE_PLATES).add(
          LighterEndItems.idLookup.get(wood.pressurePlate.asItem()));
      builder(ItemTags.WOODEN_TRAPDOORS).add(LighterEndItems.idLookup.get(wood.trapdoor.asItem()));
      builder(ItemTags.SIGNS).add(LighterEndItems.idLookup.get(wood.sign.asItem()));
      builder(ItemTags.HANGING_SIGNS).add(LighterEndItems.idLookup.get(wood.hangingSign.asItem()));
      builder(ItemTags.WOODEN_SHELVES).add(LighterEndItems.idLookup.get(wood.shelf.asItem()));

    }
    for (Material material : Arrays.asList(
        LighterEndBlocks.VIOLECITE,
        LighterEndBlocks.AZURE_JADESTONE,
        LighterEndBlocks.SANDY_JADESTONE,
        LighterEndBlocks.VIRID_JADESTONE,
        LighterEndBlocks.UMBRALITH,
        LighterEndBlocks.BORNITE
    )) {
      builder(BlockItemTags.BUTTONS.item()).add(
          LighterEndItems.idLookup.get(material.button.asItem())
      );
    }
    builder(ItemTags.CHICKEN_FOOD).add(
        LighterEndItems.idLookup.get(LighterEndBlocks.LUMECORN_SEED.asItem()));
    builder(ItemTags.BEE_FOOD).add(
        LighterEndItems.idLookup.get(LighterEndBlocks.TENANEA_FLOWER.asItem()));
    builder(ItemTags.LEAVES).add(
        LighterEndItems.idLookup.get(LighterEndBlocks.TENANEA_LEAVES.asItem()),
        LighterEndItems.idLookup.get(LighterEndItems.GLOWSHROOM_FUR),
        LighterEndItems.idLookup.get(LighterEndItems.AGAVE_FUR),
        LighterEndItems.idLookup.get(LighterEndBlocks.DRAGON_LEAVES.asItem())
    );

    builder(ItemTags.FISHES).add(LighterEndItems.idLookup.get(LighterEndItems.RAW_END_FISH));

    builder(ItemTags.CHEST_ARMOR).add(
        LighterEndItems.idLookup.get(LighterEndEquipment.SILK_ELYTRA)
    );  // this makes silk elytra trimmable

    builder(ItemTags.GAZE_DISGUISE_EQUIPMENT).addTag(LighterEndTags.FUR_ITEMS);

    builder(ItemTags.BREWING_FUEL).add(LighterEndItems.idLookup.get(LighterEndItems.END_POWDER));

    builder(ItemTags.MEAT).add(
        LighterEndItems.idLookup.get(LighterEndItems.CRAB_MEAT),
        LighterEndItems.idLookup.get(LighterEndItems.CRAB_CAKE)
    );

    builder(ItemTags.PIGLIN_FOOD).add(
        LighterEndItems.idLookup.get(LighterEndItems.CRAB_MEAT),
        LighterEndItems.idLookup.get(LighterEndItems.CRAB_CAKE)
    );

    builder(ItemTags.TRIM_MATERIALS).add(
        LighterEndItems.idLookup.get(LighterEndItems.AURORA_CRYSTAL_SHARD)
    );

    builder(ItemTags.CAULDRON_CAN_REMOVE_DYE).add(
        LighterEndItems.idLookup.get(LighterEndEquipment.SILK_ELYTRA)
    );

    builder(LighterEndTags.REPAIRS_SILK_ARMOR).add(
        LighterEndItems.idLookup.get(LighterEndItems.SILK)
    );

    builder(LighterEndTags.FLETCHINGS).add(
        ItemIds.FEATHER,
        LighterEndItems.idLookup.get(LighterEndBlocks.CHARNIA_CYAN.asItem()),
        LighterEndItems.idLookup.get(LighterEndBlocks.CHARNIA_GREEN.asItem()),
        LighterEndItems.idLookup.get(LighterEndBlocks.CHARNIA_LIGHT_BLUE.asItem()),
        LighterEndItems.idLookup.get(LighterEndBlocks.CHARNIA_ORANGE.asItem()),
        LighterEndItems.idLookup.get(LighterEndBlocks.CHARNIA_PURPLE.asItem()),
        LighterEndItems.idLookup.get(LighterEndBlocks.CHARNIA_RED.asItem())
    );
    builder(LighterEndTags.FLETCHINGS).addTag(LighterEndTags.FUR_ITEMS);

    builder(LighterEndTags.FUR_ITEMS).add(
        LighterEndItems.idLookup.get(LighterEndItems.GLOWSHROOM_FUR),
        LighterEndItems.idLookup.get(LighterEndItems.AGAVE_FUR)
    );

    builder(LighterEndTags.POLYPORES).add(
        LighterEndItems.idLookup.get(LighterEndBlocks.AURANT_POLYPORE.asItem()),
        LighterEndItems.idLookup.get(LighterEndBlocks.PURPLE_POLYPORE.asItem())
    );

    builder(LighterEndTags.MOOSHROOM_FOOD).add(
        LighterEndItems.idLookup.get(LighterEndItems.LUMECORN_EAR)
    );
  }


}
