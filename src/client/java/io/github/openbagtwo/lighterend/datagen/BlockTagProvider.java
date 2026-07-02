package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.misc.Wood.WoodSet;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks.Material;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.references.BlockItemIds;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

public class BlockTagProvider extends FabricTagsProvider.BlockTagsProvider {

  protected BlockTagProvider(
      FabricPackOutput output, CompletableFuture<Provider> future
  ) {
    super(output, future);
  }

  @Override
  protected void addTags(HolderLookup.Provider lookup) {

    builder(BlockTags.IMPERMEABLE).add(
        LighterEndBlocks.AURORA_CRYSTAL.properties().blockIdOrThrow());
    builder(BlockTags.SNIFFER_DIGGABLE_BLOCK).add(
        LighterEndBlocks.END_MOSS.properties().blockIdOrThrow());

    for (Material material : Arrays.asList(
        LighterEndBlocks.VIOLECITE,
        LighterEndBlocks.AZURE_JADESTONE,
        LighterEndBlocks.SANDY_JADESTONE,
        LighterEndBlocks.VIRID_JADESTONE,
        LighterEndBlocks.UMBRALITH,
        LighterEndBlocks.BORNITE
    )) {
      for (Block block : material.blocks) {
        builder(BlockTags.MINEABLE_WITH_PICKAXE).add(block.properties().blockIdOrThrow());
        builder(BlockTags.WALLS)
            .add(
                material.baseWall.properties().blockIdOrThrow(),
                material.brickWall.properties().blockIdOrThrow(),
                material.polishedWall.properties().blockIdOrThrow(),
                material.tileWall.properties().blockIdOrThrow()
            );
        builder(BlockTags.STONE_BUTTONS).add(material.button.properties().blockIdOrThrow());
        builder(BlockTags.STONE_PRESSURE_PLATES).add(
            material.pressurePlate.properties().blockIdOrThrow()
        );
      }
    }

    for (WoodSet wood : Arrays.asList(
        LighterEndBlocks.TENANEA,
        LighterEndBlocks.UMBRELLA,
        LighterEndBlocks.LOTUS,
        LighterEndBlocks.GLOWSHROOM,
        LighterEndBlocks.DRAGON
    )) {
      for (Block block : wood.blocks) {
        builder(BlockTags.MINEABLE_WITH_AXE).add(block.properties().blockIdOrThrow());
      }
      builder(BlockTags.MINEABLE_WITH_AXE).add(
          wood.wallSign.properties().blockIdOrThrow(),
          wood.wallHangingSign.properties().blockIdOrThrow()
      );
      builder(BlockTags.PLANKS).add(wood.planks.properties().blockIdOrThrow());
      builder(BlockTags.WOODEN_BUTTONS).add(wood.button.properties().blockIdOrThrow());
      builder(BlockTags.WOODEN_DOORS).add(wood.door.properties().blockIdOrThrow());
      builder(BlockTags.WOODEN_STAIRS).add(wood.stairs.properties().blockIdOrThrow());
      builder(BlockTags.WOODEN_SLABS).add(wood.slab.properties().blockIdOrThrow());
      builder(BlockTags.WOODEN_FENCES).add(wood.fence.properties().blockIdOrThrow());
      builder(BlockTags.FENCE_GATES).add(wood.gate.properties().blockIdOrThrow());
      builder(BlockTags.WOODEN_PRESSURE_PLATES).add(
          wood.pressurePlate.properties().blockIdOrThrow()
      );
      builder(BlockItemTags.LOGS_THAT_BURN.block()).add(
          wood.log.properties().blockIdOrThrow(),
          wood.strippedLog.properties().blockIdOrThrow(),
          wood.wood.properties().blockIdOrThrow(),
          wood.strippedWood.properties().blockIdOrThrow()
      );  // this also adds them to #minecraft:logs
      builder(BlockTags.WOODEN_TRAPDOORS).add(wood.trapdoor.properties().blockIdOrThrow());
      builder(BlockTags.STANDING_SIGNS).add(wood.sign.properties().blockIdOrThrow());
      builder(BlockTags.WALL_SIGNS).add(wood.wallSign.properties().blockIdOrThrow());
      builder(BlockTags.CEILING_HANGING_SIGNS).add(wood.hangingSign.properties().blockIdOrThrow());
      builder(BlockTags.WALL_HANGING_SIGNS).add(wood.wallHangingSign.properties().blockIdOrThrow());
      builder(BlockTags.CLIMBABLE).add(wood.ladder.properties().blockIdOrThrow());
      builder(BlockTags.WOODEN_SHELVES).add(wood.shelf.properties().blockIdOrThrow());
    }

    builder(BlockTags.MINEABLE_WITH_PICKAXE)
        .add(
            LighterEndBlocks.ENDER_BLOCK.properties().blockIdOrThrow(),
            LighterEndBlocks.MISSING_TILE.properties().blockIdOrThrow(),
            LighterEndBlocks.DRAGON_BONE_BLOCK.properties().blockIdOrThrow(),
            LighterEndBlocks.DRAGON_BONE_STAIRS.properties().blockIdOrThrow(),
            LighterEndBlocks.DRAGON_BONE_SLAB.properties().blockIdOrThrow(),
            LighterEndBlocks.END_MOSS.properties().blockIdOrThrow(),
            LighterEndBlocks.END_FURNACE.properties().blockIdOrThrow(),
            LighterEndBlocks.END_SMOKER.properties().blockIdOrThrow(),
            LighterEndBlocks.GOLD_CHANDELIER.properties().blockIdOrThrow(),
            LighterEndBlocks.IRON_CHANDELIER.properties().blockIdOrThrow(),
            LighterEndBlocks.EMERALD_ICE.properties().blockIdOrThrow(),
            LighterEndBlocks.FERROUS_ICE.properties().blockIdOrThrow(),
            LighterEndBlocks.AUROUS_ICE.properties().blockIdOrThrow(),
            LighterEndBlocks.END_STONE_QUARTZ_ORE.properties().blockIdOrThrow(),
            LighterEndBlocks.END_STONE_REDSTONE_ORE.properties().blockIdOrThrow(),
            LighterEndBlocks.UMBRALITH_QUARTZ_ORE.properties().blockIdOrThrow(),
            LighterEndBlocks.UMBRALITH_REDSTONE_ORE.properties().blockIdOrThrow(),
            LighterEndBlocks.BRIMSTONE.properties().blockIdOrThrow(),
            LighterEndBlocks.HYDROTHERMAL_VENT.properties().blockIdOrThrow()
        );
    for (Block chandelier : LighterEndBlocks.COPPER_CHANDELIERS.asList()) {
      builder(BlockTags.MINEABLE_WITH_PICKAXE).add(chandelier.properties().blockIdOrThrow());
    }

    builder(BlockTags.NEEDS_STONE_TOOL)
        .add(LighterEndBlocks.ENDER_BLOCK.properties().blockIdOrThrow())
        .add(LighterEndBlocks.HYDROTHERMAL_VENT.properties().blockIdOrThrow());

    builder(BlockTags.NEEDS_IRON_TOOL)
        .add(
            LighterEndBlocks.END_STONE_QUARTZ_ORE.properties().blockIdOrThrow(),
            LighterEndBlocks.END_STONE_REDSTONE_ORE.properties().blockIdOrThrow(),
            LighterEndBlocks.UMBRALITH_QUARTZ_ORE.properties().blockIdOrThrow(),
            LighterEndBlocks.UMBRALITH_REDSTONE_ORE.properties().blockIdOrThrow()
        );

    builder(BlockTags.ENDERMAN_HOLDABLE).add(
        LighterEndBlocks.END_MOSS.properties().blockIdOrThrow()
    );
    builder(BlockTags.ENDERMAN_HOLDABLE).addTag(LighterEndTags.FURS);

    builder(BlockTags.ANIMALS_SPAWNABLE_ON).add(
        LighterEndBlocks.END_MOSS.properties().blockIdOrThrow()
    );
    builder(BlockTags.REPLACEABLE_BY_TREES).add(
        LighterEndBlocks.END_MOSS.properties().blockIdOrThrow()
    );
    builder(BlockTags.SCULK_REPLACEABLE).add(
        LighterEndBlocks.END_MOSS.properties().blockIdOrThrow()
    );

    builder(BlockTags.FLOWERS).add(
        LighterEndBlocks.CREEPING_MOSS.properties().blockIdOrThrow(),
        LighterEndBlocks.UMBRELLA_FERN.properties().blockIdOrThrow(),
        LighterEndBlocks.TALL_UMBRELLA_FERN.properties().blockIdOrThrow(),
        LighterEndBlocks.TENANEA_FLOWER.properties().blockIdOrThrow(),
        LighterEndBlocks.END_LOTUS_FLOWER.properties().blockIdOrThrow()
    );

    builder(BlockTags.MINEABLE_WITH_AXE).add(
        LighterEndBlocks.LUMECORN_STEM.properties().blockIdOrThrow(),
        LighterEndBlocks.END_LOTUS_STEM.properties().blockIdOrThrow(),
        LighterEndBlocks.UMBRELLA_TREE_CLUSTER.properties().blockIdOrThrow(),
        LighterEndBlocks.UMBRELLA_TREE_CLUSTER_EMPTY.properties().blockIdOrThrow(),
        LighterEndBlocks.GLOWSHROOM_CAP.properties().blockIdOrThrow(),
        LighterEndBlocks.GLOWSHROOM_HYMENOPHORE.properties().blockIdOrThrow(),
        LighterEndBlocks.END_LOTUS_STEM.properties().blockIdOrThrow(),
        LighterEndBlocks.AGAVE.properties().blockIdOrThrow(),
        LighterEndBlocks.AGAVE_BULB.properties().blockIdOrThrow()
    );

    builder(BlockItemTags.SAPLINGS.block()).add(
        LighterEndBlocks.TENANEA_SAPLING.properties().blockIdOrThrow(),
        LighterEndBlocks.UMBRELLA_TREE_SAPLING.properties().blockIdOrThrow(),
        LighterEndBlocks.GLOWSHROOM_SAPLING.properties().blockIdOrThrow(),
        LighterEndBlocks.DRAGON_SAPLING.properties().blockIdOrThrow()
    );
    builder(BlockTags.LEAVES).add(
        LighterEndBlocks.TENANEA_LEAVES.properties().blockIdOrThrow(),
        LighterEndBlocks.GLOWSHROOM_FUR.properties().blockIdOrThrow(),
        LighterEndBlocks.AGAVE_FUR.properties().blockIdOrThrow(),
        LighterEndBlocks.DRAGON_LEAVES.properties().blockIdOrThrow()
    );

    builder(BlockTags.FLOWER_POTS).add(
        LighterEndBlocks.POTTED_TENANEA_SAPLING.properties().blockIdOrThrow(),
        LighterEndBlocks.POTTED_UMBRELLA_SAPLING.properties().blockIdOrThrow(),
        LighterEndBlocks.POTTED_GLOWSHROOM_SAPLING.properties().blockIdOrThrow()
    );

    builder(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).add(
        LighterEndBlocks.OBELISK.properties().blockIdOrThrow()
    );
    builder(BlockTags.DRAGON_IMMUNE).add(
        LighterEndBlocks.OBELISK.properties().blockIdOrThrow()
    );
    builder(BlockTags.FEATURES_CANNOT_REPLACE).add(
        LighterEndBlocks.OBELISK.properties().blockIdOrThrow()
    );
    builder(BlockTags.GEODE_INVALID_BLOCKS).add(
        LighterEndBlocks.OBELISK.properties().blockIdOrThrow()
    );
    builder(BlockTags.WITHER_IMMUNE).add(
        LighterEndBlocks.OBELISK.properties().blockIdOrThrow()
    );

    builder(BlockTags.ICE).add(
        LighterEndBlocks.EMERALD_ICE.properties().blockIdOrThrow(),
        LighterEndBlocks.FERROUS_ICE.properties().blockIdOrThrow(),
        LighterEndBlocks.AUROUS_ICE.properties().blockIdOrThrow()
    );

    builder(BlockTags.INFINIBURN_OVERWORLD).add(
        LighterEndBlocks.BRIMSTONE.properties().blockIdOrThrow()
    );

    builder(BlockTags.SUPPORTS_CHORUS_FLOWER).addTag(LighterEndTags.END_SOIL);
    builder(BlockTags.SUPPORTS_CHORUS_PLANT).addTag(LighterEndTags.END_SOIL);

    builder(BlockTags.SULFUR_SPIKE_REPLACEABLE).add(
        LighterEndBlocks.BRIMSTONE.properties().blockIdOrThrow(),
        LighterEndBlocks.BORNITE.baseBlock.properties().blockIdOrThrow()
    );

    builder(LighterEndTags.END_MOSS_REPLACEABLE)
        .add(
            BlockItemIds.END_STONE.block(),
            BlockItemIds.BLACKSTONE.block(),
            BlockItemIds.BASALT.block(),
            BlockItemIds.DEAD_BRAIN_CORAL_BLOCK.block(),
            BlockItemIds.DEAD_BUBBLE_CORAL_BLOCK.block(),
            BlockItemIds.DEAD_FIRE_CORAL_BLOCK.block(),
            BlockItemIds.DEAD_HORN_CORAL_BLOCK.block(),
            BlockItemIds.DEAD_TUBE_CORAL_BLOCK.block(),
            LighterEndBlocks.UMBRALITH.baseBlock.properties().blockIdOrThrow(),
            LighterEndBlocks.BRIMSTONE.properties().blockIdOrThrow()
        );
    builder(LighterEndTags.END_SOIL)
        .add(
            LighterEndBlocks.END_MOSS.properties().blockIdOrThrow(),
            LighterEndBlocks.UMBRALITH.baseBlock.properties().blockIdOrThrow(),
            LighterEndBlocks.BRIMSTONE.properties().blockIdOrThrow(),
            LighterEndBlocks.BORNITE.baseBlock.properties().blockIdOrThrow()
        );
    builder(LighterEndTags.END_STONES)
        .add(
            BlockItemIds.END_STONE.block(),
            BlockItemIds.BLACKSTONE.block(),
            BlockItemIds.BASALT.block(),
            BlockItemIds.DEAD_BRAIN_CORAL_BLOCK.block(),
            BlockItemIds.DEAD_BUBBLE_CORAL_BLOCK.block(),
            BlockItemIds.DEAD_FIRE_CORAL_BLOCK.block(),
            BlockItemIds.DEAD_HORN_CORAL_BLOCK.block(),
            BlockItemIds.DEAD_TUBE_CORAL_BLOCK.block(),
            LighterEndBlocks.VIOLECITE.baseBlock.properties().blockIdOrThrow(),
            LighterEndBlocks.AZURE_JADESTONE.baseBlock.properties().blockIdOrThrow(),
            LighterEndBlocks.SANDY_JADESTONE.baseBlock.properties().blockIdOrThrow(),
            LighterEndBlocks.VIRID_JADESTONE.baseBlock.properties().blockIdOrThrow(),
            LighterEndBlocks.UMBRALITH.baseBlock.properties().blockIdOrThrow(),
            LighterEndBlocks.BRIMSTONE.properties().blockIdOrThrow(),
            LighterEndBlocks.BORNITE.baseBlock.properties().blockIdOrThrow()
        );
    builder(LighterEndTags.AQUATIC_END_SOIL)
        .add(
            BlockItemIds.END_STONE.block(),
            BlockItemIds.BLACKSTONE.block(),
            BlockItemIds.SAND.block(),
            BlockItemIds.SANDSTONE.block(),
            BlockItemIds.RED_SAND.block(),
            BlockItemIds.RED_SANDSTONE.block(),
            BlockItemIds.GRAVEL.block(),
            BlockItemIds.DIRT.block(),
            BlockItemIds.COARSE_DIRT.block(),
            BlockItemIds.MUD.block(),
            LighterEndBlocks.UMBRALITH.baseBlock.properties().blockIdOrThrow(),
            LighterEndBlocks.END_MOSS.properties().blockIdOrThrow(),
            LighterEndBlocks.BRIMSTONE.properties().blockIdOrThrow(),
            LighterEndBlocks.BORNITE.baseBlock.properties().blockIdOrThrow()
        );
    builder(LighterEndTags.AQUATIC_END_VEGETATION)
        .add(
            LighterEndBlocks.CHARNIA_CYAN.properties().blockIdOrThrow(),
            LighterEndBlocks.CHARNIA_GREEN.properties().blockIdOrThrow(),
            LighterEndBlocks.CHARNIA_LIGHT_BLUE.properties().blockIdOrThrow(),
            LighterEndBlocks.CHARNIA_ORANGE.properties().blockIdOrThrow(),
            LighterEndBlocks.CHARNIA_PURPLE.properties().blockIdOrThrow(),
            LighterEndBlocks.CHARNIA_RED.properties().blockIdOrThrow()
        );
    builder(LighterEndTags.FURS)
        .add(
            LighterEndBlocks.AGAVE_FUR.properties().blockIdOrThrow(),
            LighterEndBlocks.GLOWSHROOM_FUR.properties().blockIdOrThrow()
        );
    builder(LighterEndTags.SLIME_SPAWNABLE).addTag(LighterEndTags.END_STONES);
    builder(LighterEndTags.SLIME_SPAWNABLE).addTag(LighterEndTags.END_SOIL);
    builder(LighterEndTags.GROWS_SULPHUR_CRYSTALS).add(
        LighterEndBlocks.BRIMSTONE.properties().blockIdOrThrow()
    );
  }
}
