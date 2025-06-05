package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks.Material;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks.Wood;
import io.github.openbagtwo.lighterend.tags.LighterEndTags;
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

  protected BlockTagProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> future) {
    super(output, future);
  }

  @Override
  protected void configure(RegistryWrapper.WrapperLookup lookup) {
    for (Material material : Arrays.asList(
        LighterEndBlocks.VIOLECITE,
        LighterEndBlocks.AZURE_JADESTONE,
        LighterEndBlocks.SANDY_JADESTONE,
        LighterEndBlocks.VIRID_JADESTONE,
        LighterEndBlocks.UMBRALITH
    )) {
      for (Block block : material.blocks) {
        valueLookupBuilder(BlockTags.PICKAXE_MINEABLE).add(block);
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

    for (Wood wood : Arrays.asList(
        LighterEndBlocks.TENANEA,
        LighterEndBlocks.UMBRELLA,
        LighterEndBlocks.LOTUS
    )) {
      for (Block block : wood.blocks) {
        valueLookupBuilder(BlockTags.AXE_MINEABLE).add(block);
      }
      valueLookupBuilder(BlockTags.AXE_MINEABLE).add(wood.wallSign, wood.wallHangingSign);
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
      );
      valueLookupBuilder(BlockTags.WOODEN_TRAPDOORS).add(wood.trapdoor);
      valueLookupBuilder(BlockTags.STANDING_SIGNS).add(wood.sign);
      valueLookupBuilder(BlockTags.WALL_SIGNS).add(wood.wallSign);
      valueLookupBuilder(BlockTags.CEILING_HANGING_SIGNS).add(wood.hangingSign);
      valueLookupBuilder(BlockTags.WALL_HANGING_SIGNS).add(wood.wallHangingSign);
      valueLookupBuilder(BlockTags.CLIMBABLE).add(wood.ladder);
    }

    valueLookupBuilder(BlockTags.PICKAXE_MINEABLE)
        .add(LighterEndBlocks.ENDER_BLOCK)
        .add(LighterEndBlocks.MISSING_TILE)
        .add(
            LighterEndBlocks.DRAGON_BONE_BLOCK,
            LighterEndBlocks.DRAGON_BONE_STAIRS,
            LighterEndBlocks.DRAGON_BONE_SLAB,
            LighterEndBlocks.END_MOSS
        );

    valueLookupBuilder(BlockTags.NEEDS_IRON_TOOL)
        .add(LighterEndBlocks.ENDER_BLOCK);

    for (Material jadestone : Arrays.asList(
        LighterEndBlocks.AZURE_JADESTONE,
        LighterEndBlocks.SANDY_JADESTONE,
        LighterEndBlocks.VIRID_JADESTONE
    )) {
      for (Block block : jadestone.blocks) {
        valueLookupBuilder(BlockTags.NEEDS_IRON_TOOL).add(block);
      }

    }

    valueLookupBuilder(BlockTags.ENDERMAN_HOLDABLE).add(LighterEndBlocks.END_MOSS);
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

    valueLookupBuilder(BlockTags.AXE_MINEABLE).add(
        LighterEndBlocks.LUMECORN_STEM,
        LighterEndBlocks.END_LOTUS_STEM
    );

    valueLookupBuilder(BlockTags.SAPLINGS).add(
        LighterEndBlocks.TENANEA_SAPLING,
        LighterEndBlocks.UMBRELLA_TREE_SAPLING
    );
    valueLookupBuilder(BlockTags.LEAVES).add(LighterEndBlocks.TENANEA_LEAVES);

    valueLookupBuilder(BlockTags.AXE_MINEABLE).add(
        LighterEndBlocks.UMBRELLA_TREE_CLUSTER,
        LighterEndBlocks.UMBRELLA_TREE_CLUSTER_EMPTY
    );

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
            LighterEndBlocks.UMBRALITH.baseBlock
        );
    valueLookupBuilder(LighterEndTags.END_SOIL)
        .add(
            LighterEndBlocks.END_MOSS,
            LighterEndBlocks.UMBRALITH.baseBlock
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
            LighterEndBlocks.UMBRALITH.baseBlock
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
            LighterEndBlocks.END_MOSS  // though pretty sure this won't survive underwater
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


  }

}
