package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.misc.Wood.WoodSet;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks.Material;
import io.github.openbagtwo.lighterend.registries.LighterEndEquipment;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.ItemTags;

public class ItemTagProvider extends FabricTagProvider.ItemTagProvider {

  public ItemTagProvider(
      FabricDataOutput output,
      CompletableFuture<WrapperLookup> future
  ) {
    super(output, future);
  }

  @Override
  protected void configure(RegistryWrapper.WrapperLookup lookup) {
    for (WoodSet wood : Arrays.asList(
        LighterEndBlocks.TENANEA,
        LighterEndBlocks.UMBRELLA,
        LighterEndBlocks.LOTUS,
        LighterEndBlocks.GLOWSHROOM,
        LighterEndBlocks.DRAGON
    )) {
      getOrCreateTagBuilder(LighterEndTags.LOG_TAGS.get(wood.baseName)).add(
          wood.log.asItem(),
          wood.strippedLog.asItem(),
          wood.wood.asItem(),
          wood.strippedWood.asItem()
      );
      getOrCreateTagBuilder(LighterEndTags.STRIPPED_LOG_TAGS.get(wood.baseName)).add(
          wood.strippedLog.asItem(),
          wood.strippedWood.asItem()
      );
      getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN).addTag(
          LighterEndTags.LOG_TAGS.get(wood.baseName)
      );
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
        LighterEndBlocks.VIRID_JADESTONE,
        LighterEndBlocks.UMBRALITH,
        LighterEndBlocks.BORNITE
    )) {
      getOrCreateTagBuilder(ItemTags.STONE_BUTTONS).add(material.button.asItem());
    }
    getOrCreateTagBuilder(ItemTags.CHICKEN_FOOD).add(LighterEndBlocks.LUMECORN_SEED.asItem());
    getOrCreateTagBuilder(ItemTags.BEE_FOOD).add(LighterEndBlocks.TENANEA_FLOWER.asItem());
    getOrCreateTagBuilder(ItemTags.LEAVES).add(
        LighterEndBlocks.TENANEA_LEAVES.asItem(),
        LighterEndItems.GLOWSHROOM_FUR,
        LighterEndItems.AGAVE_FUR,
        LighterEndBlocks.DRAGON_LEAVES.asItem()
    );

    getOrCreateTagBuilder(ItemTags.FISHES).add(LighterEndItems.RAW_END_FISH);

    getOrCreateTagBuilder(ItemTags.DYEABLE).add(
        LighterEndEquipment.SILK_ELYTRA
    );

    getOrCreateTagBuilder(ItemTags.CHEST_ARMOR).add(
        LighterEndEquipment.SILK_ELYTRA
    );  // this makes silk elytra trimmable

    getOrCreateTagBuilder(ItemTags.GAZE_DISGUISE_EQUIPMENT).addTag(LighterEndTags.FUR_ITEMS);

    getOrCreateTagBuilder(ItemTags.BREWING_FUEL).add(LighterEndItems.END_POWDER);

    getOrCreateTagBuilder(ItemTags.MEAT).add(
        LighterEndItems.CRAB_MEAT,
        LighterEndItems.CRAB_CAKE
    );

    getOrCreateTagBuilder(ItemTags.PIGLIN_FOOD).add(
        LighterEndItems.CRAB_MEAT,
        LighterEndItems.CRAB_CAKE
    );

    getOrCreateTagBuilder(LighterEndTags.REPAIRS_SILK_ARMOR).add(LighterEndItems.SILK);

    getOrCreateTagBuilder(LighterEndTags.FLETCHINGS).add(
        Items.FEATHER,
        LighterEndBlocks.CHARNIA_CYAN.asItem(),
        LighterEndBlocks.CHARNIA_GREEN.asItem(),
        LighterEndBlocks.CHARNIA_LIGHT_BLUE.asItem(),
        LighterEndBlocks.CHARNIA_ORANGE.asItem(),
        LighterEndBlocks.CHARNIA_PURPLE.asItem(),
        LighterEndBlocks.CHARNIA_RED.asItem()
    );
    getOrCreateTagBuilder(LighterEndTags.FLETCHINGS).addTag(LighterEndTags.FUR_ITEMS);

    getOrCreateTagBuilder(LighterEndTags.FUR_ITEMS).add(
        LighterEndItems.GLOWSHROOM_FUR,
        LighterEndItems.AGAVE_FUR
    );

    getOrCreateTagBuilder(LighterEndTags.POLYPORES).add(
        LighterEndBlocks.AURANT_POLYPORE.asItem(),
        LighterEndBlocks.PURPLE_POLYPORE.asItem()
    );

    getOrCreateTagBuilder(LighterEndTags.MOOSHROOM_FOOD).add(
        LighterEndItems.LUMECORN_EAR
    );
  }


}
