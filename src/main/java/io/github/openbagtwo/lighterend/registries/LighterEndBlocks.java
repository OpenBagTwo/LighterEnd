package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.blocks.AuroraCrystalBlock;
import java.util.function.Function;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class LighterEndBlocks {

  public static final Block AURORA_CRYSTAL = register("aurora_crystal", AuroraCrystalBlock::new, true);
  public static final Block ENDER_BLOCK = register(
    "ender_block",
    settings ->  new Block(
                   settings
                     .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                     .mapColor(MapColor.BRIGHT_TEAL)
                     .strength(5F, 6F)
                     .requiresTool()
                     .sounds(BlockSoundGroup.STONE)
                 ),
    true
  );



  public static Block register(String name, Function<AbstractBlock.Settings, Block> factory, boolean hasItem) {
    Identifier id = Identifier.of(LighterEnd.MOD_ID, name);
    Block block = factory.apply(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, id)));

    if (hasItem){
      RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, id);
      Registry.register(Registries.ITEM,
          itemKey,
        new BlockItem(block, new Settings().registryKey(itemKey))
      );
    }
    RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, id);
    return Registry.register(Registries.BLOCK, blockKey, block);
  }

  public static void initialize() {}

}


