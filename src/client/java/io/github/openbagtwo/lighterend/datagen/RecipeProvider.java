package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class RecipeProvider extends FabricRecipeProvider {
  protected RecipeProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
    super(output, registriesFuture);
  }

  @Override
  protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
    return new RecipeGenerator(registryLookup, exporter) {
      @Override
      public void generate() {
        RegistryWrapper.Impl<Item> itemLookup = registries.getOrThrow(RegistryKeys.ITEM);

        createShaped(RecipeCategory.MISC, LighterEndBlocks.AURORA_CRYSTAL, 1)
          .pattern("ss")
          .pattern("ss")
          .input('s', LighterEndItems.AURORA_CRYSTAL_SHARD)
          .criterion(
              hasItem(LighterEndItems.AURORA_CRYSTAL_SHARD),
              conditionsFromItem(LighterEndBlocks.AURORA_CRYSTAL)
          )
          .offerTo(exporter);

        createShaped(RecipeCategory.DECORATIONS, LighterEndBlocks.ENDER_BLOCK, 1)
            .pattern("oo")
            .pattern("oo")
            .input('o', Items.ENDER_PEARL)
            .criterion(
                hasItem(Items.ENDER_PEARL),
                conditionsFromItem(LighterEndBlocks.ENDER_BLOCK)
            )
            .offerTo(exporter);

        createShapeless(RecipeCategory.TOOLS, Items.ENDER_PEARL, 4)
            .input(LighterEndBlocks.ENDER_BLOCK)
            .criterion(
                hasItem(LighterEndBlocks.ENDER_BLOCK),
                conditionsFromItem(Items.ENDER_PEARL)
            )
            .offerTo(exporter);
      }
    };
  }

  @Override
  public String getName() {
    return "LighterEndRecipeProvider";
  }
}
