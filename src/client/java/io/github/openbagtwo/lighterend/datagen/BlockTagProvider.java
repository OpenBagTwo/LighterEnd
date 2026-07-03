package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.misc.Wood.WoodSet;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks.Material;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class BlockTagProvider extends FabricTagProvider.BlockTagProvider {

  protected BlockTagProvider(
      FabricDataOutput output, CompletableFuture<Provider> future
  ) {
    super(output, future);
  }

  @Override
  protected void addTags(HolderLookup.Provider lookup) {

    valueLookupBuilder(BlockTags.IMPERMEABLE).add(LighterEndBlocks.AURORA_CRYSTAL);
    valueLookupBuilder(BlockTags.SNIFFER_DIGGABLE_BLOCK).add(LighterEndBlocks.END_MOSS);

    for (Material material : Arrays.asList(
        LighterEndBlocks.VIOLECITE,
        LighterEndBlocks.AZURE_JADESTONE,
        LighterEndBlocks.SANDY_JADESTONE,
        LighterEndBlocks.VIRID_JADESTONE,
        LighterEndBlocks.UMBRALITH,
        LighterEndBlocks.BORNITE
    )) {
      for (Block block : material.blocks) {
        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(block);
        valueLookupBuilder(BlockTags.WALLS)
            .add(
                material.baseWall,
                material.brickWall,
                material.polishedWall,
                material.tileWall
            );
        valueLookupBuilder(BlockTags.STONE_BUTTONS).add(material.button);
        valueLookupBuilder(BlockTags.STONE_PRESSURE_PLATES).add(material.pressurePlate);
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
        valueLookupBuilder(BlockTags.MINEABLE_WITH_AXE).add(block);
      }
      valueLookupBuilder(BlockTags.MINEABLE_WITH_AXE).add(wood.wallSign, wood.wallHangingSign);
      valueLookupBuilder(BlockTags.PLANKS).add(wood.planks);
      valueLookupBuilder(BlockTags.WOODEN_BUTTONS).add(wood.button);
      valueLookupBuilder(BlockTags.WOODEN_DOORS).add(wood.door);
      valueLookupBuilder(BlockTags.WOODEN_STAIRS).add(wood.stairs);
      valueLookupBuilder(BlockTags.WOODEN_SLABS).add(wood.slab);
      valueLookupBuilder(BlockTags.WOODEN_FENCES).add(wood.fence);
      valueLookupBuilder(BlockTags.FENCE_GATES).add(wood.gate);
      valueLookupBuilder(BlockTags.WOODEN_PRESSURE_PLATES).add(wood.pressurePlate);
      valueLookupBuilder(BlockTags.LOGS_THAT_BURN).add(
          wood.log,
          wood.strippedLog,
          wood.wood,
          wood.strippedWood
      );  // this also adds them to #minecraft:logs
      valueLookupBuilder(BlockTags.WOODEN_TRAPDOORS).add(wood.trapdoor);
      valueLookupBuilder(BlockTags.STANDING_SIGNS).add(wood.sign);
      valueLookupBuilder(BlockTags.WALL_SIGNS).add(wood.wallSign);
      valueLookupBuilder(BlockTags.CEILING_HANGING_SIGNS).add(wood.hangingSign);
      valueLookupBuilder(BlockTags.WALL_HANGING_SIGNS).add(wood.wallHangingSign);
      valueLookupBuilder(BlockTags.CLIMBABLE).add(wood.ladder);
      valueLookupBuilder(BlockTags.WOODEN_SHELVES).add(wood.shelf);
    }

    valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
        .add(
            LighterEndBlocks.ENDER_BLOCK,
            LighterEndBlocks.MISSING_TILE,
            LighterEndBlocks.DRAGON_BONE_BLOCK,
            LighterEndBlocks.DRAGON_BONE_STAIRS,
            LighterEndBlocks.DRAGON_BONE_SLAB,
            LighterEndBlocks.END_MOSS,
            LighterEndBlocks.END_FURNACE,
            LighterEndBlocks.END_SMOKER,
            LighterEndBlocks.GOLD_CHANDELIER,
            LighterEndBlocks.IRON_CHANDELIER,
            LighterEndBlocks.EMERALD_ICE,
            LighterEndBlocks.FERROUS_ICE,
            LighterEndBlocks.AUROUS_ICE,
            LighterEndBlocks.END_STONE_QUARTZ_ORE,
            LighterEndBlocks.END_STONE_REDSTONE_ORE,
            LighterEndBlocks.UMBRALITH_QUARTZ_ORE,
            LighterEndBlocks.UMBRALITH_REDSTONE_ORE,
            LighterEndBlocks.BRIMSTONE,
            LighterEndBlocks.HYDROTHERMAL_VENT
        );
    for (Block chandelier : LighterEndBlocks.COPPER_CHANDELIERS.asList()) {
      valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(chandelier);
    }

    valueLookupBuilder(BlockTags.NEEDS_STONE_TOOL)
        .add(LighterEndBlocks.ENDER_BLOCK)
        .add(LighterEndBlocks.HYDROTHERMAL_VENT);

    valueLookupBuilder(BlockTags.NEEDS_IRON_TOOL)
        .add(
            LighterEndBlocks.END_STONE_QUARTZ_ORE,
            LighterEndBlocks.END_STONE_REDSTONE_ORE,
            LighterEndBlocks.UMBRALITH_QUARTZ_ORE,
            LighterEndBlocks.UMBRALITH_REDSTONE_ORE
        );

    valueLookupBuilder(BlockTags.ENDERMAN_HOLDABLE).add(
        LighterEndBlocks.END_MOSS
    );
    valueLookupBuilder(BlockTags.ENDERMAN_HOLDABLE).addTag(LighterEndTags.FURS);

    valueLookupBuilder(BlockTags.ANIMALS_SPAWNABLE_ON).add(LighterEndBlocks.END_MOSS);
    valueLookupBuilder(BlockTags.REPLACEABLE_BY_TREES).add(LighterEndBlocks.END_MOSS);
    valueLookupBuilder(BlockTags.SCULK_REPLACEABLE).add(LighterEndBlocks.END_MOSS);

    valueLookupBuilder(BlockTags.FLOWERS).add(
        LighterEndBlocks.CREEPING_MOSS,
        LighterEndBlocks.UMBRELLA_FERN,
        LighterEndBlocks.TALL_UMBRELLA_FERN,
        LighterEndBlocks.TENANEA_FLOWER,
        LighterEndBlocks.END_LOTUS_FLOWER
    );

    valueLookupBuilder(BlockTags.MINEABLE_WITH_AXE).add(
        LighterEndBlocks.LUMECORN_STEM,
        LighterEndBlocks.END_LOTUS_STEM,
        LighterEndBlocks.UMBRELLA_TREE_CLUSTER,
        LighterEndBlocks.UMBRELLA_TREE_CLUSTER_EMPTY,
        LighterEndBlocks.GLOWSHROOM_CAP,
        LighterEndBlocks.GLOWSHROOM_HYMENOPHORE,
        LighterEndBlocks.END_LOTUS_STEM,
        LighterEndBlocks.AGAVE,
        LighterEndBlocks.AGAVE_BULB
    );

    valueLookupBuilder(BlockTags.SAPLINGS).add(
        LighterEndBlocks.TENANEA_SAPLING,
        LighterEndBlocks.UMBRELLA_TREE_SAPLING,
        LighterEndBlocks.GLOWSHROOM_SAPLING,
        LighterEndBlocks.DRAGON_SAPLING
    );
    valueLookupBuilder(BlockTags.LEAVES).add(
        LighterEndBlocks.TENANEA_LEAVES,
        LighterEndBlocks.GLOWSHROOM_FUR,
        LighterEndBlocks.AGAVE_FUR,
        LighterEndBlocks.DRAGON_LEAVES
    );

    valueLookupBuilder(BlockTags.FLOWER_POTS).add(
        LighterEndBlocks.POTTED_TENANEA_SAPLING,
        LighterEndBlocks.POTTED_UMBRELLA_SAPLING,
        LighterEndBlocks.POTTED_GLOWSHROOM_SAPLING
    );

    valueLookupBuilder(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).add(
        LighterEndBlocks.OBELISK
    );
    valueLookupBuilder(BlockTags.DRAGON_IMMUNE).add(
        LighterEndBlocks.OBELISK
    );
    valueLookupBuilder(BlockTags.FEATURES_CANNOT_REPLACE).add(
        LighterEndBlocks.OBELISK
    );
    valueLookupBuilder(BlockTags.GEODE_INVALID_BLOCKS).add(
        LighterEndBlocks.OBELISK
    );
    valueLookupBuilder(BlockTags.WITHER_IMMUNE).add(
        LighterEndBlocks.OBELISK
    );

    valueLookupBuilder(BlockTags.ICE).add(
        LighterEndBlocks.EMERALD_ICE,
        LighterEndBlocks.FERROUS_ICE,
        LighterEndBlocks.AUROUS_ICE
    );

    valueLookupBuilder(BlockTags.INFINIBURN_OVERWORLD).add(LighterEndBlocks.BRIMSTONE);

    valueLookupBuilder(LighterEndTags.END_MOSS_REPLACEABLE)
        .add(
            Blocks.END_STONE,
            Blocks.BLACKSTONE,
            Blocks.BASALT,
            Blocks.DEAD_BRAIN_CORAL_BLOCK,
            Blocks.DEAD_BUBBLE_CORAL_BLOCK,
            Blocks.DEAD_FIRE_CORAL_BLOCK,
            Blocks.DEAD_HORN_CORAL_BLOCK,
            Blocks.DEAD_TUBE_CORAL_BLOCK,
            LighterEndBlocks.UMBRALITH.baseBlock,
            LighterEndBlocks.BRIMSTONE
        );
    valueLookupBuilder(LighterEndTags.END_SOIL)
        .add(
            LighterEndBlocks.END_MOSS,
            LighterEndBlocks.UMBRALITH.baseBlock,
            LighterEndBlocks.BRIMSTONE,
            LighterEndBlocks.BORNITE.baseBlock
        );
    valueLookupBuilder(LighterEndTags.END_STONES)
        .add(
            Blocks.END_STONE,
            Blocks.BLACKSTONE,
            Blocks.BASALT,
            Blocks.DEAD_BRAIN_CORAL_BLOCK,
            Blocks.DEAD_BUBBLE_CORAL_BLOCK,
            Blocks.DEAD_FIRE_CORAL_BLOCK,
            Blocks.DEAD_HORN_CORAL_BLOCK,
            Blocks.DEAD_TUBE_CORAL_BLOCK,
            LighterEndBlocks.VIOLECITE.baseBlock,
            LighterEndBlocks.AZURE_JADESTONE.baseBlock,
            LighterEndBlocks.SANDY_JADESTONE.baseBlock,
            LighterEndBlocks.VIRID_JADESTONE.baseBlock,
            LighterEndBlocks.UMBRALITH.baseBlock,
            LighterEndBlocks.BRIMSTONE,
            LighterEndBlocks.BORNITE.baseBlock
        );
    valueLookupBuilder(LighterEndTags.AQUATIC_END_SOIL)
        .add(
            Blocks.END_STONE,
            Blocks.BLACKSTONE,
            Blocks.SAND,
            Blocks.SANDSTONE,
            Blocks.RED_SAND,
            Blocks.RED_SANDSTONE,
            Blocks.GRAVEL,
            Blocks.DIRT,
            Blocks.COARSE_DIRT,
            Blocks.MUD,
            LighterEndBlocks.UMBRALITH.baseBlock,
            LighterEndBlocks.END_MOSS,  // though pretty sure this won't survive underwater
            LighterEndBlocks.BRIMSTONE,
            LighterEndBlocks.BORNITE.baseBlock
        );
    valueLookupBuilder(LighterEndTags.AQUATIC_END_VEGETATION)
        .add(
            LighterEndBlocks.CHARNIA_CYAN,
            LighterEndBlocks.CHARNIA_GREEN,
            LighterEndBlocks.CHARNIA_LIGHT_BLUE,
            LighterEndBlocks.CHARNIA_ORANGE,
            LighterEndBlocks.CHARNIA_PURPLE,
            LighterEndBlocks.CHARNIA_RED
        );
    valueLookupBuilder(LighterEndTags.FURS)
        .add(
            LighterEndBlocks.AGAVE_FUR,
            LighterEndBlocks.GLOWSHROOM_FUR
        );
    valueLookupBuilder(LighterEndTags.SLIME_SPAWNABLE).addTag(LighterEndTags.END_STONES);
    valueLookupBuilder(LighterEndTags.SLIME_SPAWNABLE).addTag(LighterEndTags.END_SOIL);
    valueLookupBuilder(LighterEndTags.GROWS_SULPHUR_CRYSTALS).add(LighterEndBlocks.BRIMSTONE);


  }

}
