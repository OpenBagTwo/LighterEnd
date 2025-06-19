package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.blocks.EndLily;
import io.github.openbagtwo.lighterend.blocks.SilkMothNest;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndData;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.CopyComponentsLootFunction;
import net.minecraft.loot.function.CopyStateLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.entry.RegistryEntry.Reference;

public class BlockLootTableProvider extends FabricBlockLootTableProvider {

  protected BlockLootTableProvider(FabricDataOutput dataOutput,
      CompletableFuture<WrapperLookup> registryLookup) {
    super(dataOutput, registryLookup);
  }

  @Override
  public void generate() {
    for (List<Block> material : Arrays.asList(
        LighterEndBlocks.VIOLECITE.blocks,
        LighterEndBlocks.AZURE_JADESTONE.blocks,
        LighterEndBlocks.SANDY_JADESTONE.blocks,
        LighterEndBlocks.VIRID_JADESTONE.blocks,
        LighterEndBlocks.UMBRALITH.blocks,
        LighterEndBlocks.TENANEA.blocks,
        LighterEndBlocks.UMBRELLA.blocks,
        LighterEndBlocks.LOTUS.blocks
    )) {
      for (Block block : material) {
        if (block instanceof SlabBlock) {
          addDrop(block, this::slabDrops);
        } else if (block instanceof DoorBlock) {
          addDrop(block, this::doorDrops);
        } else {
          addDrop(block);
        }
      }
    }

    addDrop(LighterEndBlocks.AURORA_CRYSTAL, auroraCrystalDrops());
    addDrop(LighterEndBlocks.ENDER_BLOCK);

    addDrop(LighterEndBlocks.MISSING_TILE);

    addDrop(LighterEndBlocks.DRAGON_BONE_BLOCK);
    addDrop(LighterEndBlocks.DRAGON_BONE_STAIRS);
    addDrop(LighterEndBlocks.DRAGON_BONE_SLAB, this::slabDrops);
    addDrop(LighterEndBlocks.END_MOSS, this.drops(LighterEndBlocks.END_MOSS, Blocks.END_STONE));

    addDrop(LighterEndBlocks.CREEPING_MOSS, this::dropsWithSilkTouchOrShears);
    addDrop(LighterEndBlocks.UMBRELLA_FERN, this::dropsWithSilkTouchOrShears);
    addDrop(LighterEndBlocks.TALL_UMBRELLA_FERN, LighterEndBlocks.UMBRELLA_FERN);
    addDrop(LighterEndBlocks.LUMECORN_SEED);
    addDrop(LighterEndBlocks.LUMECORN_STEM, LighterEndBlocks.LUMECORN_SEED);
    addDrop(LighterEndBlocks.LUMECORN, lumecornEarDrops());

    addDrop(LighterEndBlocks.TENANEA_FLOWER, this::dropsWithSilkTouchOrShears);
    addDrop(LighterEndBlocks.TENANEA_SAPLING);
    addDrop(LighterEndBlocks.TENANEA_LEAVES,
        (leaves) -> this.leavesDrops(leaves, LighterEndBlocks.TENANEA_SAPLING,
            0.025F, 0.03125F, 0.041666668F, 0.05F));

    addDrop(LighterEndBlocks.SILK_MOTH_NEST, mothNestDrops());
    addDrop(LighterEndBlocks.UMBRELLA_TREE_CLUSTER);
    addDrop(LighterEndBlocks.UMBRELLA_TREE_CLUSTER_EMPTY);

    addDrop(LighterEndBlocks.UMBRELLA_MEMBRANE);

    addDrop(LighterEndBlocks.CHARNIA_CYAN, this::dropsWithSilkTouchOrShears);
    addDrop(LighterEndBlocks.CHARNIA_GREEN, this::dropsWithSilkTouchOrShears);
    addDrop(LighterEndBlocks.CHARNIA_LIGHT_BLUE, this::dropsWithSilkTouchOrShears);
    addDrop(LighterEndBlocks.CHARNIA_ORANGE, this::dropsWithSilkTouchOrShears);
    addDrop(LighterEndBlocks.CHARNIA_PURPLE, this::dropsWithSilkTouchOrShears);
    addDrop(LighterEndBlocks.CHARNIA_RED, this::dropsWithSilkTouchOrShears);

    addDrop(LighterEndBlocks.END_LILY, endLilyDrops());
    addDrop(LighterEndBlocks.END_LOTUS_FLOWER, lotusFlowerDrops());
    addDrop(LighterEndBlocks.END_LOTUS_STEM);
    addDrop(LighterEndBlocks.END_LOTUS_LEAF, LighterEndItems.END_LILY_LEAF);
    addDrop(LighterEndBlocks.END_LOTUS_SEED);
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

  private LootTable.Builder mothNestDrops() {
    return LootTable.builder()
        .pool(
            LootPool.builder()
                .conditionally(this.createSilkTouchCondition())
                .rolls(ConstantLootNumberProvider.create(1.0F))
                .with(
                    ItemEntry.builder(LighterEndItems.SILK_MOTH_NEST)
                        .apply(CopyComponentsLootFunction.builder(
                                CopyComponentsLootFunction.Source.BLOCK_ENTITY)
                            .include(LighterEndData.MOTHS
                            ))
                        .apply(CopyStateLootFunction.builder(LighterEndBlocks.SILK_MOTH_NEST)
                            .addProperty(SilkMothNest.FULLNESS))
                )
        );
  }

  public LootTable.Builder endLilyDrops() {
    Reference<Enchantment> fortune = this.registries.getOrThrow(RegistryKeys.ENCHANTMENT)
        .getOrThrow(Enchantments.FORTUNE);

    LootCondition.Builder onlyTopDrops = BlockStatePropertyLootCondition.builder(
            LighterEndBlocks.END_LILY)
        .properties(StatePredicate.Builder.create().exactMatch(EndLily.IS_TOP, true));

    return this.applyExplosionDecay(
            LighterEndBlocks.END_LILY,
            LootTable.builder()
                .pool(LootPool.builder()
                    .with(ItemEntry.builder(LighterEndItems.END_LILY_LEAF).conditionally(onlyTopDrops)
                    ).apply(
                        ApplyBonusLootFunction.binomialWithBonusCount(
                            fortune, 0.5714286F, 3))))
        .pool(
            LootPool.builder()
                .conditionally(onlyTopDrops)
                .with(ItemEntry.builder(LighterEndBlocks.END_LILY_SEED).apply(
                    ApplyBonusLootFunction.binomialWithBonusCount(
                        fortune, 0.5714286F, 3)))

        );
  }

  private LootTable.Builder lotusFlowerDrops() {
    return LootTable.builder()
        .pool(
            this.addSurvivesExplosionCondition(
                LighterEndBlocks.END_LOTUS_SEED,
                LootPool.builder().rolls(UniformLootNumberProvider.create(1.0F, 2.0F))
                    .with(ItemEntry.builder(LighterEndBlocks.END_LOTUS_SEED))
            )
        );
  }
}
