package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.misc.Wood.WoodSet;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks.Material;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.BlockTags;

public class BlockTagProvider extends FabricTagProvider.BlockTagProvider {

  protected BlockTagProvider(
      FabricDataOutput output, CompletableFuture<WrapperLookup> future
  ) {
    super(output, future);
  }

  @Override
  protected void configure(RegistryWrapper.WrapperLookup lookup) {

    getOrCreateTagBuilder(BlockTags.IMPERMEABLE).add(LighterEndBlocks.AURORA_CRYSTAL);
    getOrCreateTagBuilder(BlockTags.SNIFFER_DIGGABLE_BLOCK).add(LighterEndBlocks.END_MOSS);

    for (Material material : Arrays.asList(
        LighterEndBlocks.VIOLECITE,
        LighterEndBlocks.AZURE_JADESTONE,
        LighterEndBlocks.SANDY_JADESTONE,
        LighterEndBlocks.VIRID_JADESTONE,
        LighterEndBlocks.UMBRALITH,
        LighterEndBlocks.BORNITE
    )) {
      for (Block block : material.blocks) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(block);
        getOrCreateTagBuilder(BlockTags.WALLS)
            .add(
                material.baseWall,
                material.brickWall,
                material.polishedWall,
                material.tileWall
            );
        getOrCreateTagBuilder(BlockTags.STONE_BUTTONS).add(material.button);
        getOrCreateTagBuilder(BlockTags.STONE_PRESSURE_PLATES).add(material.pressurePlate);
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
        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE).add(block);
      }
      getOrCreateTagBuilder(BlockTags.AXE_MINEABLE).add(wood.wallSign, wood.wallHangingSign);
      getOrCreateTagBuilder(BlockTags.PLANKS).add(wood.planks);
      getOrCreateTagBuilder(BlockTags.WOODEN_BUTTONS).add(wood.button);
      getOrCreateTagBuilder(BlockTags.WOODEN_DOORS).add(wood.door);
      getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS).add(wood.stairs);
      getOrCreateTagBuilder(BlockTags.WOODEN_SLABS).add(wood.slab);
      getOrCreateTagBuilder(BlockTags.WOODEN_FENCES).add(wood.fence);
      getOrCreateTagBuilder(BlockTags.FENCE_GATES).add(wood.gate);
      getOrCreateTagBuilder(BlockTags.WOODEN_PRESSURE_PLATES).add(wood.pressurePlate);
      getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN).add(
          wood.log,
          wood.strippedLog,
          wood.wood,
          wood.strippedWood
      );  // this also adds them to #minecraft:logs
      getOrCreateTagBuilder(BlockTags.WOODEN_TRAPDOORS).add(wood.trapdoor);
      getOrCreateTagBuilder(BlockTags.STANDING_SIGNS).add(wood.sign);
      getOrCreateTagBuilder(BlockTags.WALL_SIGNS).add(wood.wallSign);
      getOrCreateTagBuilder(BlockTags.CEILING_HANGING_SIGNS).add(wood.hangingSign);
      getOrCreateTagBuilder(BlockTags.WALL_HANGING_SIGNS).add(wood.wallHangingSign);
      getOrCreateTagBuilder(BlockTags.CLIMBABLE).add(wood.ladder);
    }

    getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
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
            LighterEndBlocks.END_STONE_QUARTZ_ORE,
            LighterEndBlocks.END_STONE_REDSTONE_ORE,
            LighterEndBlocks.UMBRALITH_QUARTZ_ORE,
            LighterEndBlocks.UMBRALITH_REDSTONE_ORE,
            LighterEndBlocks.BRIMSTONE,
            LighterEndBlocks.HYDROTHERMAL_VENT
        );

    getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
        .add(LighterEndBlocks.ENDER_BLOCK)
        .add(LighterEndBlocks.HYDROTHERMAL_VENT);

    getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
        .add(
            LighterEndBlocks.END_STONE_QUARTZ_ORE,
            LighterEndBlocks.END_STONE_REDSTONE_ORE,
            LighterEndBlocks.UMBRALITH_QUARTZ_ORE,
            LighterEndBlocks.UMBRALITH_REDSTONE_ORE
        );

    getOrCreateTagBuilder(BlockTags.ENDERMAN_HOLDABLE).add(
        LighterEndBlocks.END_MOSS
    );
    getOrCreateTagBuilder(BlockTags.ENDERMAN_HOLDABLE).addTag(LighterEndTags.FURS);

    getOrCreateTagBuilder(BlockTags.ANIMALS_SPAWNABLE_ON).add(LighterEndBlocks.END_MOSS);
    getOrCreateTagBuilder(BlockTags.REPLACEABLE_BY_TREES).add(LighterEndBlocks.END_MOSS);
    getOrCreateTagBuilder(BlockTags.SCULK_REPLACEABLE).add(LighterEndBlocks.END_MOSS);

    getOrCreateTagBuilder(BlockTags.FLOWERS).add(
        LighterEndBlocks.CREEPING_MOSS,
        LighterEndBlocks.UMBRELLA_FERN,
        LighterEndBlocks.TALL_UMBRELLA_FERN,
        LighterEndBlocks.TENANEA_FLOWER,
        LighterEndBlocks.END_LOTUS_FLOWER
    );

    getOrCreateTagBuilder(BlockTags.AXE_MINEABLE).add(
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

    getOrCreateTagBuilder(BlockTags.SAPLINGS).add(
        LighterEndBlocks.TENANEA_SAPLING,
        LighterEndBlocks.UMBRELLA_TREE_SAPLING,
        LighterEndBlocks.GLOWSHROOM_SAPLING,
        LighterEndBlocks.DRAGON_SAPLING
    );
    getOrCreateTagBuilder(BlockTags.LEAVES).add(
        LighterEndBlocks.TENANEA_LEAVES,
        LighterEndBlocks.GLOWSHROOM_FUR,
        LighterEndBlocks.AGAVE_FUR,
        LighterEndBlocks.DRAGON_LEAVES
    );

    getOrCreateTagBuilder(BlockTags.FLOWER_POTS).add(
        LighterEndBlocks.POTTED_TENANEA_SAPLING,
        LighterEndBlocks.POTTED_UMBRELLA_SAPLING,
        LighterEndBlocks.POTTED_GLOWSHROOM_SAPLING
    );

    getOrCreateTagBuilder(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).add(
        LighterEndBlocks.OBELISK
    );
    getOrCreateTagBuilder(BlockTags.DRAGON_IMMUNE).add(
        LighterEndBlocks.OBELISK
    );
    getOrCreateTagBuilder(BlockTags.FEATURES_CANNOT_REPLACE).add(
        LighterEndBlocks.OBELISK
    );
    getOrCreateTagBuilder(BlockTags.GEODE_INVALID_BLOCKS).add(
        LighterEndBlocks.OBELISK
    );
    getOrCreateTagBuilder(BlockTags.WITHER_IMMUNE).add(
        LighterEndBlocks.OBELISK
    );

    getOrCreateTagBuilder(BlockTags.ICE).add(
        LighterEndBlocks.EMERALD_ICE,
        LighterEndBlocks.FERROUS_ICE
    );

    getOrCreateTagBuilder(BlockTags.INFINIBURN_OVERWORLD).add(LighterEndBlocks.BRIMSTONE);

    getOrCreateTagBuilder(LighterEndTags.END_MOSS_REPLACEABLE)
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
    getOrCreateTagBuilder(LighterEndTags.END_SOIL)
        .add(
            LighterEndBlocks.END_MOSS,
            LighterEndBlocks.UMBRALITH.baseBlock,
            LighterEndBlocks.BRIMSTONE,
            LighterEndBlocks.BORNITE.baseBlock
        );
    getOrCreateTagBuilder(LighterEndTags.END_STONES)
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
    getOrCreateTagBuilder(LighterEndTags.AQUATIC_END_SOIL)
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
    getOrCreateTagBuilder(LighterEndTags.AQUATIC_END_VEGETATION)
        .add(
            LighterEndBlocks.CHARNIA_CYAN,
            LighterEndBlocks.CHARNIA_GREEN,
            LighterEndBlocks.CHARNIA_LIGHT_BLUE,
            LighterEndBlocks.CHARNIA_ORANGE,
            LighterEndBlocks.CHARNIA_PURPLE,
            LighterEndBlocks.CHARNIA_RED
        );
    getOrCreateTagBuilder(LighterEndTags.FURS)
        .add(
            LighterEndBlocks.AGAVE_FUR,
            LighterEndBlocks.GLOWSHROOM_FUR
        );
    getOrCreateTagBuilder(LighterEndTags.SLIME_SPAWNABLE).addTag(LighterEndTags.END_STONES);
    getOrCreateTagBuilder(LighterEndTags.SLIME_SPAWNABLE).addTag(LighterEndTags.END_SOIL);
    getOrCreateTagBuilder(LighterEndTags.GROWS_SULPHUR_CRYSTALS).add(LighterEndBlocks.BRIMSTONE);


  }

}
