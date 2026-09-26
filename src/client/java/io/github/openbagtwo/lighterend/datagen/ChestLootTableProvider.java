package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import io.github.openbagtwo.lighterend.registries.LighterEndLootTables;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableSubProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class ChestLootTableProvider extends SimpleFabricLootTableSubProvider {


  public ChestLootTableProvider(
      FabricPackOutput output,
      CompletableFuture<Provider> registryLookup
  ) {
    super(output, registryLookup, LootContextParamSets.CHEST);
  }

  @Override
  public void generate(BiConsumer<ResourceKey<LootTable>, Builder> lootTableBiConsumer) {
    lootTableBiConsumer.accept(
        LighterEndLootTables.STARTER_CHEST,
        LootTable.lootTable()
            .withPool(
                LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(Items.COPPER_AXE))
                    .add(LootItem.lootTableItem(Items.WOODEN_AXE).setWeight(3))
            )
            .withPool(
                LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(Items.COPPER_PICKAXE))
                    .add(LootItem.lootTableItem(Items.WOODEN_PICKAXE).setWeight(3))
            )
            .withPool(
                LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(3.0F))
                    .add(LootItem.lootTableItem(LighterEndItems.SHADOW_BERRY).setWeight(5).apply(
                        SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                    .add(LootItem.lootTableItem(LighterEndItems.POPPED_LUMECORN).setWeight(3).apply(
                        SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                    .add(LootItem.lootTableItem(LighterEndItems.CRAB_CAKE).setWeight(3).apply(
                        SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
            )
            .withPool(
                LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(4.0F))
                    .add(LootItem.lootTableItem(Items.STICK).setWeight(10).apply(
                        SetItemCountFunction.setCount(
                            UniformGenerator.between(1.0F, 12.0F))))
                    // the line below is kind of a troll, since Tenanea logs don't generate naturally
                    .add(LootItem.lootTableItem(LighterEndBlocks.TENANEA.log).setWeight(3).apply(
                        SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                    .add(LootItem.lootTableItem(LighterEndBlocks.UMBRELLA.log).setWeight(3).apply(
                        SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                    .add(LootItem.lootTableItem(LighterEndBlocks.LOTUS.log).setWeight(3).apply(
                        SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                    .add(LootItem.lootTableItem(LighterEndBlocks.GLOWSHROOM.log).setWeight(3).apply(
                        SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                    .add(LootItem.lootTableItem(LighterEndBlocks.DRAGON.log).setWeight(3).apply(
                        SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))

            )
            .withPool(
                LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(LighterEndItems.CRAB_CLAW).setWeight(5).apply(
                        SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
                    .add(LootItem.lootTableItem(Items.SHEARS))
            )
    );
  }
}
