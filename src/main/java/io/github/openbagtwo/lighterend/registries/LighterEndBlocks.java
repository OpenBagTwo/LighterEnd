package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.blocks.AuroraCrystalBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class LighterEndBlocks {


  public static final Block AURORA_CRYSTAL = register(
      "aurora_crystal",
      new AuroraCrystalBlock(
          BlockBehaviour.Properties.of().setId(
              ResourceKey.create(
                  Registries.BLOCK,
                  ResourceLocation.fromNamespaceAndPath(LighterEnd.MOD_ID, "aurora_crystal")
              )
          )
      ),
      true
  );

  public static Block register(String name, Block block, boolean hasItem) {
    ResourceLocation id = ResourceLocation.fromNamespaceAndPath(LighterEnd.MOD_ID, name);
    if (hasItem){
      ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
      Registry.register(BuiltInRegistries.ITEM,
          itemKey,
        new BlockItem(block, new Properties().setId(itemKey))
      );
    }
    ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id);
    return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
  }

  public static void initialize() {}

}


