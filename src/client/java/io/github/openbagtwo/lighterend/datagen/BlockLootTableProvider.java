package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.blocks.EndLily;
import io.github.openbagtwo.lighterend.blocks.ShadowBerry;
import io.github.openbagtwo.lighterend.blocks.SilkMothNest;
import io.github.openbagtwo.lighterend.blocks.SulphurCrystal;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndData;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class BlockLootTableProvider extends FabricBlockLootSubProvider {

  protected BlockLootTableProvider(
      FabricPackOutput dataOutput,
      CompletableFuture<Provider> registryLookup
  ) {
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
        LighterEndBlocks.BORNITE.blocks,
        LighterEndBlocks.TENANEA.blocks,
        LighterEndBlocks.UMBRELLA.blocks,
        LighterEndBlocks.LOTUS.blocks,
        LighterEndBlocks.GLOWSHROOM.blocks,
        LighterEndBlocks.DRAGON.blocks
    )) {
      for (Block block : material) {
        if (block instanceof SlabBlock) {
          add(block, this::createSlabItemTable);
        } else if (block instanceof DoorBlock) {
          add(block, this::createDoorTable);
        } else {
          dropSelf(block);
        }
      }
    }

    for (Block pot : Arrays.asList(
        LighterEndBlocks.POTTED_TENANEA_SAPLING,
        LighterEndBlocks.POTTED_UMBRELLA_SAPLING,
        LighterEndBlocks.POTTED_GLOWSHROOM_SAPLING
    )) {
      dropPottedContents(pot);
    }

    add(LighterEndBlocks.AURORA_CRYSTAL, auroraCrystalDrops());
    dropSelf(LighterEndBlocks.ENDER_BLOCK);

    dropSelf(LighterEndBlocks.MISSING_TILE);

    dropSelf(LighterEndBlocks.DRAGON_BONE_BLOCK);
    dropSelf(LighterEndBlocks.DRAGON_BONE_STAIRS);
    add(LighterEndBlocks.DRAGON_BONE_SLAB, this::createSlabItemTable);
    add(
        LighterEndBlocks.END_MOSS,
        block -> this.createSingleItemTableWithSilkTouch(
            block,
            Blocks.END_STONE
        )
    );

    add(LighterEndBlocks.CREEPING_MOSS, this::createShearsOrSilkTouchOnlyDrop);
    add(LighterEndBlocks.UMBRELLA_FERN, this::createShearsOrSilkTouchOnlyDrop);
    dropOther(LighterEndBlocks.TALL_UMBRELLA_FERN, LighterEndBlocks.UMBRELLA_FERN);
    dropSelf(LighterEndBlocks.LUMECORN_SEED);
    dropOther(LighterEndBlocks.LUMECORN_STEM, LighterEndBlocks.LUMECORN_SEED);
    add(LighterEndBlocks.LUMECORN, lumecornEarDrops());

    add(LighterEndBlocks.TENANEA_FLOWER, this::createShearsOrSilkTouchOnlyDrop);
    dropSelf(LighterEndBlocks.TENANEA_SAPLING);
    add(LighterEndBlocks.TENANEA_LEAVES,
        (leaves) -> this.createLeavesDrops(leaves, LighterEndBlocks.TENANEA_SAPLING,
            0.025F, 0.03125F, 0.041666668F, 0.05F));

    add(LighterEndBlocks.SILK_MOTH_NEST, mothNestDrops());
    dropSelf(LighterEndBlocks.UMBRELLA_TREE_CLUSTER);
    dropSelf(LighterEndBlocks.UMBRELLA_TREE_CLUSTER_EMPTY);

    dropSelf(LighterEndBlocks.UMBRELLA_MEMBRANE);

    add(LighterEndBlocks.CHARNIA_CYAN, this::createShearsOrSilkTouchOnlyDrop);
    add(LighterEndBlocks.CHARNIA_GREEN, this::createShearsOrSilkTouchOnlyDrop);
    add(LighterEndBlocks.CHARNIA_LIGHT_BLUE, this::createShearsOrSilkTouchOnlyDrop);
    add(LighterEndBlocks.CHARNIA_ORANGE, this::createShearsOrSilkTouchOnlyDrop);
    add(LighterEndBlocks.CHARNIA_PURPLE, this::createShearsOrSilkTouchOnlyDrop);
    add(LighterEndBlocks.CHARNIA_RED, this::createShearsOrSilkTouchOnlyDrop);

    add(LighterEndBlocks.END_LILY, endLilyDrops());
    add(LighterEndBlocks.END_LOTUS_FLOWER, lotusFlowerDrops());
    dropSelf(LighterEndBlocks.END_LOTUS_STEM);
    dropOther(LighterEndBlocks.END_LOTUS_LEAF, LighterEndItems.END_LILY_LEAF);
    dropSelf(LighterEndBlocks.END_LOTUS_SEED);

    add(
        LighterEndBlocks.GLOWSHROOM_FUR,
        (block -> this.createShearsOrSilkTouchOnlyDrop(LighterEndItems.GLOWSHROOM_FUR))
    );
    dropSelf(LighterEndBlocks.GLOWSHROOM_CAP);
    dropSelf(LighterEndBlocks.GLOWSHROOM_HYMENOPHORE);
    dropSelf(LighterEndBlocks.GLOWSHROOM_SAPLING);
    dropOther(LighterEndBlocks.AGAVE, LighterEndBlocks.AGAVE_SEED);
    dropSelf(LighterEndBlocks.AGAVE_BULB);
    add(
        LighterEndBlocks.AGAVE_FUR,
        (block -> this.createShearsOrSilkTouchOnlyDrop(LighterEndItems.AGAVE_FUR))
    );
    dropSelf(LighterEndBlocks.AURANT_POLYPORE);
    dropSelf(LighterEndBlocks.PURPLE_POLYPORE);
    add(LighterEndBlocks.END_FURNACE, this::createNameableBlockEntityTable);
    add(LighterEndBlocks.END_SMOKER, this::createNameableBlockEntityTable);

    dropSelf(LighterEndBlocks.END_LEVER);

    dropSelf(LighterEndBlocks.GOLD_CHANDELIER);
    dropSelf(LighterEndBlocks.IRON_CHANDELIER);
    for (Block chandelier : LighterEndBlocks.COPPER_CHANDELIERS.asList()) {
      dropSelf(chandelier);
    }

    dropSelf(LighterEndBlocks.EMERALD_ICE);
    dropSelf(LighterEndBlocks.FERROUS_ICE);
    dropSelf(LighterEndBlocks.AUROUS_ICE);

    add(LighterEndBlocks.END_STONE_REDSTONE_ORE, this::createRedstoneOreDrops);
    add(LighterEndBlocks.UMBRALITH_REDSTONE_ORE, this::createRedstoneOreDrops);
    add(LighterEndBlocks.END_STONE_QUARTZ_ORE, block -> this.createOreDrop(block, Items.QUARTZ));
    add(LighterEndBlocks.UMBRALITH_QUARTZ_ORE, block -> this.createOreDrop(block, Items.QUARTZ));

    dropSelf(LighterEndBlocks.BRIMSTONE);
    add(LighterEndBlocks.SULPHUR_CRYSTAL, sulphurCrystalDrops());
    add(LighterEndBlocks.HYDROTHERMAL_VENT, this::createSilkTouchOnlyTable);

    add(
        LighterEndBlocks.SHADOW_BERRY,
        createCropDrops(
            LighterEndBlocks.SHADOW_BERRY,
            LighterEndItems.SHADOW_BERRY,
            LighterEndItems.SHADOW_BERRY_SEEDS,
            LootItemBlockStatePropertyCondition.hasBlockStateProperties(
                LighterEndBlocks.SHADOW_BERRY
            ).setProperties(
                StatePropertiesPredicate.Builder.properties().hasProperty(
                    ShadowBerry.AGE,
                    ShadowBerry.MAX_AGE
                )
            )
        )
    );
    add(LighterEndBlocks.SHADOW_GRASS, this::createShearsOrSilkTouchOnlyDrop);
    this.add(
        LighterEndBlocks.NEEDLEGRASS,
        block -> this.createShearsDispatchTable(
            block,
            this.applyExplosionDecay(
                block, LootItem.lootTableItem(Items.STICK)
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
            )
        )
    );
    dropSelf(LighterEndBlocks.MURKWEED);

    add(LighterEndBlocks.DRAGON_LEAVES,
        (leaves) -> this.createLeavesDrops(leaves, LighterEndBlocks.DRAGON_SAPLING,
            0.025F, 0.03125F, 0.041666668F, 0.05F));
    dropSelf(LighterEndBlocks.DRAGON_SAPLING);
  }

  private LootTable.Builder auroraCrystalDrops() {
    /* Note: It is intentional (for now) that you can essentially dupe Aurora Crystals with a
             Fortune pick. It was present in BetterEnd and is (IMO) a reasonable way for Aurora
             Crystals to be renewable. */
    HolderLookup.RegistryLookup<Enchantment> impl = this.registries.lookupOrThrow(
        Registries.ENCHANTMENT);
    return this.createSilkTouchDispatchTable(
        LighterEndBlocks.AURORA_CRYSTAL,
        this.applyExplosionDecay(
            LighterEndBlocks.AURORA_CRYSTAL,
            LootItem.lootTableItem(LighterEndItems.AURORA_CRYSTAL_SHARD)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))
                .apply(ApplyBonusCount.addOreBonusCount(impl.getOrThrow(Enchantments.FORTUNE)))
        )
    );
  }

  private LootTable.Builder lumecornEarDrops() {
    return LootTable.lootTable()
        .withPool(
            this.applyExplosionCondition(
                LighterEndItems.LUMECORN_EAR,
                LootPool.lootPool().setRolls(UniformGenerator.between(1.0F, 2.0F))
                    .add(LootItem.lootTableItem(LighterEndItems.LUMECORN_EAR))
            )
        );
  }

  private LootTable.Builder mothNestDrops() {
    return LootTable.lootTable()
        .withPool(
            LootPool.lootPool()
                .when(this.hasSilkTouch())
                .setRolls(ConstantValue.exactly(1.0F))
                .add(
                    LootItem.lootTableItem(LighterEndItems.SILK_MOTH_NEST)
                        .apply(
                            CopyComponentsFunction.copyComponentsFromBlockEntity(
                                LootContextParams.BLOCK_ENTITY).include(LighterEndData.MOTHS)
                        ).apply(
                            CopyBlockState.copyState(LighterEndBlocks.SILK_MOTH_NEST)
                                .copy(SilkMothNest.FULLNESS)
                        )
                )
        );
  }

  public LootTable.Builder endLilyDrops() {
    Reference<Enchantment> fortune = this.registries.lookupOrThrow(Registries.ENCHANTMENT)
        .getOrThrow(Enchantments.FORTUNE);

    LootItemCondition.Builder topCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(
        LighterEndBlocks.END_LILY
    ).setProperties(
        StatePropertiesPredicate.Builder.properties().hasProperty(EndLily.IS_TOP, true)
    );

    return this.applyExplosionDecay(
        LighterEndBlocks.END_LILY,
        LootTable.lootTable().withPool(
            LootPool.lootPool()
                .add(LootItem.lootTableItem(
                    LighterEndItems.END_LILY_LEAF).when(topCondition)
                ).apply(
                    ApplyBonusCount.addBonusBinomialDistributionCount(
                        fortune, 0.5714286F, 3
                    )
                )
        )
    ).withPool(
        LootPool.lootPool()
            .when(topCondition)
            .add(LootItem.lootTableItem(LighterEndBlocks.END_LILY_SEED).apply(
                ApplyBonusCount.addBonusBinomialDistributionCount(
                    fortune, 0.5714286F, 3)))

    );
  }

  private LootTable.Builder lotusFlowerDrops() {
    return LootTable.lootTable()
        .withPool(
            this.applyExplosionCondition(
                LighterEndBlocks.END_LOTUS_SEED,
                LootPool.lootPool().setRolls(UniformGenerator.between(1.0F, 2.0F))
                    .add(LootItem.lootTableItem(LighterEndBlocks.END_LOTUS_SEED))
            )
        );
  }

  private LootTable.Builder sulphurCrystalDrops() {

    Reference<Enchantment> fortune = this.registries.lookupOrThrow(Registries.ENCHANTMENT)
        .getOrThrow(Enchantments.FORTUNE);

    LootItemCondition.Builder fullyGrownCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(
        LighterEndBlocks.SULPHUR_CRYSTAL
    ).setProperties(
        StatePropertiesPredicate.Builder.properties()
            .hasProperty(SulphurCrystal.STAGE, SulphurCrystal.MAX_STAGE)
    );

    return LootTable
        .lootTable()
        .withPool(
            LootPool.lootPool()
                .when(this.hasSilkTouch())
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(LighterEndBlocks.SULPHUR_CRYSTAL)
                    .apply(SetItemCountFunction
                        .setCount(UniformGenerator.between(1, 3))
                        .when(fullyGrownCondition)
                    )
                    .apply(SetItemCountFunction
                        .setCount(ConstantValue.exactly(1))
                        .when(InvertedLootItemCondition.invert(fullyGrownCondition))
                    )
                    .apply(ApplyBonusCount
                        .addOreBonusCount(fortune)
                        .when(fullyGrownCondition)
                    )
                    .apply(ApplyExplosionDecay.explosionDecay())
                )
        ).withPool(
            LootPool.lootPool()
                .when(AllOfCondition.allOf(
                    InvertedLootItemCondition.invert(this.hasSilkTouch()),
                    fullyGrownCondition))
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(LighterEndItems.CRYSTALLINE_SULPHUR)
                    .apply(SetItemCountFunction
                        .setCount(UniformGenerator.between(1, 3))
                    )
                    .apply(ApplyExplosionDecay.explosionDecay())
                )
        );
  }
}
