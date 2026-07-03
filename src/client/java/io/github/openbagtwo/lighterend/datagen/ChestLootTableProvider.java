package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import io.github.openbagtwo.lighterend.registries.LighterEndLootTables;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class ChestLootTableProvider extends SimpleFabricLootTableProvider {


  public ChestLootTableProvider(
      FabricDataOutput output,
      CompletableFuture<WrapperLookup> registryLookup
  ) {
    super(output, registryLookup, LootContextTypes.CHEST);
  }

  @Override
  public void accept(BiConsumer<RegistryKey<LootTable>, Builder> lootTableBiConsumer) {
    lootTableBiConsumer.accept(
        LighterEndLootTables.STARTER_CHEST,
        LootTable.builder()
            .pool(
                LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(Items.COPPER_AXE))
                    .with(ItemEntry.builder(Items.WOODEN_AXE).weight(3))
            )
            .pool(
                LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(Items.COPPER_PICKAXE))
                    .with(ItemEntry.builder(Items.WOODEN_PICKAXE).weight(3))
            )
            .pool(
                LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(3.0F))
                    .with(ItemEntry.builder(LighterEndItems.SHADOW_BERRY).weight(5).apply(
                        SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))))
                    .with(ItemEntry.builder(LighterEndItems.POPPED_LUMECORN).weight(3).apply(
                        SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))))
                    .with(ItemEntry.builder(LighterEndItems.CRAB_CAKE).weight(3).apply(
                        SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))))
            )
            .pool(
                LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(4.0F))
                    .with(ItemEntry.builder(Items.STICK).weight(10).apply(
                        SetCountLootFunction.builder(
                            UniformLootNumberProvider.create(1.0F, 12.0F))))
                    // the line below is kind of a troll, since Tenanea logs don't generate naturally
                    .with(ItemEntry.builder(LighterEndBlocks.TENANEA.log).weight(3).apply(
                        SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                    .with(ItemEntry.builder(LighterEndBlocks.UMBRELLA.log).weight(3).apply(
                        SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                    .with(ItemEntry.builder(LighterEndBlocks.LOTUS.log).weight(3).apply(
                        SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                    .with(ItemEntry.builder(LighterEndBlocks.GLOWSHROOM.log).weight(3).apply(
                        SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                    .with(ItemEntry.builder(LighterEndBlocks.DRAGON.log).weight(3).apply(
                        SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))

            )
            .pool(
                LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(LighterEndItems.CRAB_CLAW).weight(5).apply(
                        SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 4.0F))))
                    .with(ItemEntry.builder(Items.SHEARS))
            )
    );
  }
}
