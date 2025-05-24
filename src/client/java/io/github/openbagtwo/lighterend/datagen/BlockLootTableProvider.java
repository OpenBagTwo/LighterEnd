package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks.Material;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class BlockLootTableProvider extends FabricBlockLootTableProvider {

  protected BlockLootTableProvider(FabricDataOutput dataOutput,
      CompletableFuture<WrapperLookup> registryLookup) {
    super(dataOutput, registryLookup);
  }

  @Override
  public void generate() {
    addDrop(LighterEndBlocks.AURORA_CRYSTAL, auroraCrystalDrops());
    addDrop(LighterEndBlocks.ENDER_BLOCK);
    for (Block block : LighterEndBlocks.VIOLECITE.blocks) {
      addDrop(block);
    }
    addDrop(LighterEndBlocks.MISSING_TILE);
    for (Material jadestone : Arrays.asList(
        LighterEndBlocks.AZURE_JADESTONE,
        LighterEndBlocks.SANDY_JADESTONE,
        LighterEndBlocks.VIRID_JADESTONE
    )) {
      for (Block block : jadestone.blocks) {
        addDrop(block);
      }
    }
    addDrop(LighterEndBlocks.DRAGON_BONE_BLOCK);
    addDrop(LighterEndBlocks.DRAGON_BONE_STAIRS);
    addDrop(LighterEndBlocks.DRAGON_BONE_SLAB);
    addDropWithSilkTouch(LighterEndBlocks.END_MOSS, Blocks.END_STONE);

    addDrop(LighterEndBlocks.CREEPING_MOSS, this::dropsWithSilkTouchOrShears);
    addDrop(LighterEndBlocks.UMBRELLA_FERN, this::dropsWithSilkTouchOrShears);
    addDrop(LighterEndBlocks.TALL_UMBRELLA_FERN, LighterEndBlocks.UMBRELLA_FERN);
    addDrop(LighterEndBlocks.LUMECORN_SEED);
    addDrop(LighterEndBlocks.LUMECORN_STEM, LighterEndBlocks.LUMECORN_SEED);
    addDrop(LighterEndBlocks.LUMECORN, lumecornEarDrops());

    for (Block block : LighterEndBlocks.UMBRALITH.blocks) {
      addDrop(block);
    }

    addDrop(LighterEndBlocks.TENANEA_FLOWER, this::dropsWithSilkTouch);
    addDrop(LighterEndBlocks.TENANEA_SAPLING);
    addDrop(LighterEndBlocks.TENANEA_LEAVES,
        (leaves) -> this.leavesDrops(leaves, LighterEndBlocks.TENANEA_SAPLING,
            0.025F, 0.03125F, 0.041666668F, 0.05F));

    for (Block block : LighterEndBlocks.TENANEA.blocks) {
      addDrop(block);
    }
  }

  private LootTable.Builder auroraCrystalDrops() {
    /* Note: It is intentional (for now) that you can essentially dupe Aurora Crystals with a
             Fortune pick. It was present in BetterEnd and is (IMO) a reasonable way for Aurora
             Crystals to be renewable. */
    RegistryWrapper.Impl<Enchantment> impl = this.registries.getOrThrow(RegistryKeys.ENCHANTMENT);
    return this.dropsWithSilkTouch(
        LighterEndBlocks.AURORA_CRYSTAL,
        this.applyExplosionDecay(
            LighterEndBlocks.AURORA_CRYSTAL,
            ItemEntry.builder(LighterEndItems.AURORA_CRYSTAL_SHARD)
                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F)))
                .apply(ApplyBonusLootFunction.oreDrops(impl.getOrThrow(Enchantments.FORTUNE)))
        )
    );
  }

  private LootTable.Builder lumecornEarDrops() {
    return LootTable.builder()
        .pool(
            this.addSurvivesExplosionCondition(
                LighterEndItems.LUMECORN_EAR,
                LootPool.builder().rolls(UniformLootNumberProvider.create(1.0F, 2.0F))
                    .with(ItemEntry.builder(LighterEndItems.LUMECORN_EAR))
            )
        );
  }
}
