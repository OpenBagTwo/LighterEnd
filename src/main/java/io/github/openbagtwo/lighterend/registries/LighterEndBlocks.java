package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.blocks.AuroraCrystalBlock;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.ButtonBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class LighterEndBlocks {

  public static final Block AURORA_CRYSTAL = register("aurora_crystal", AuroraCrystalBlock::new);
  public static final Block ENDER_BLOCK = register(
      "ender_block",
      settings -> new Block(
          settings
              .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
              .mapColor(MapColor.BRIGHT_TEAL)
              .strength(5F, 6F)
              .requiresTool()
              .sounds(BlockSoundGroup.STONE)
      )
  );
  public static final Material VIOLECITE = new Material("violecite", MapColor.TERRACOTTA_BLACK);
  public static final Block MISSING_TILE = register(
      "missing_tile",
      settings -> new Block(
          settings
              .instrument(NoteBlockInstrument.BASEDRUM)
              .requiresTool()
              .strength(3.0F, 9.0F)
              .mapColor(MapColor.TERRACOTTA_PURPLE)
      )
  );

  public static final Material AZURE_JADESTONE = new Material(
      "azure_jadestone", MapColor.LIGHT_BLUE
  );
  public static final Material SANDY_JADESTONE = new Material(
      "sandy_jadestone", MapColor.YELLOW
  );
  public static final Material VIRID_JADESTONE = new Material(
      "virid_jadestone", MapColor.GREEN
  );

  public static class Material {

    public final String baseName;
    private final MapColor mapColor;

    public final Block baseBlock;
    public final Block baseStairs;
    public final Block baseSlab;
    public final Block baseWall;

    public final Block bricks;
    public final Block brickStairs;
    public final Block brickSlab;
    public final Block brickWall;

    public final Block polished;
    public final Block polishedStairs;
    public final Block polishedSlab;
    public final Block polishedWall;

    public final Block tiles;
    public final Block tileStairs;
    public final Block tileSlab;
    public final Block tileWall;

    public final Block pillar;
    public final Block button;
    public final Block pressurePlate;

    public final List<Block> blocks;
    // public final Block pedestal;

    public Settings applySettings(Settings settings){
      return settings
          .instrument(NoteBlockInstrument.BASEDRUM)
          .requiresTool()
          .strength(3.0F, 9.0F)
          .mapColor(this.mapColor);
    }

    public Material(String name, MapColor mapColor){
      this.baseName = name;
      this.mapColor = mapColor;

      baseBlock = register(baseName, settings -> new Block(applySettings(settings)));
      baseStairs = register(baseName + "_stairs", settings -> new StairsBlock(baseBlock.getDefaultState(), applySettings(settings)));
      baseSlab = register(baseName + "_slab", settings -> new SlabBlock(applySettings(settings)));
      baseWall = register(baseName + "_wall", settings -> new WallBlock(applySettings(settings)));

      bricks = register(baseName + "_bricks", settings -> new Block(applySettings(settings)));
      brickStairs = register(baseName + "_brick_stairs", settings -> new StairsBlock(bricks.getDefaultState(), applySettings(settings)));
      brickSlab = register(baseName + "_brick_slab", settings -> new SlabBlock(applySettings(settings)));
      brickWall = register(baseName + "_brick_wall", settings -> new WallBlock(applySettings(settings)));

      polished = register(baseName + "_polished", settings -> new Block(applySettings(settings)));
      polishedStairs = register(baseName + "_polished_stairs", settings -> new StairsBlock(polished.getDefaultState(), applySettings(settings)));
      polishedSlab = register(baseName + "_polished_slab", settings -> new SlabBlock(applySettings(settings)));
      polishedWall = register(baseName + "_polished_wall", settings -> new WallBlock(applySettings(settings)));

      tiles = register(baseName + "_tiles", settings -> new Block(applySettings(settings)));
      tileStairs = register(baseName + "_tile_stairs", settings -> new StairsBlock(tiles.getDefaultState(), applySettings(settings)));
      tileSlab = register(baseName + "_tile_slab", settings -> new SlabBlock(applySettings(settings)));
      tileWall = register(baseName + "_tile_wall", settings -> new WallBlock(applySettings(settings)));

      pillar = register(baseName + "_pillar", settings -> new PillarBlock(applySettings(settings)));
      button = register(
          baseName + "_button", settings -> new ButtonBlock(
              BlockSetType.POLISHED_BLACKSTONE, 30, applySettings(settings)
          )
      );
      pressurePlate = register(
          baseName + "_pressure_plate", settings -> new PressurePlateBlock(
              BlockSetType.POLISHED_BLACKSTONE, applySettings(settings)
          )
      );

      blocks = Arrays.asList(
          baseBlock,
          baseStairs,
          baseSlab,
          baseWall,
          bricks,
          brickStairs,
          brickSlab,
          brickWall,
          polished,
          polishedStairs,
          polishedSlab,
          polishedWall,
          tiles,
          tileStairs,
          tileSlab,
          tileWall,
          pillar,
          button,
          pressurePlate
      );


    }
  }

  public static Block register(String name, Function<Settings, Block> factory) {
    return register(name, factory, true);
  }

  public static Block register(String name, Function<Settings, Block> factory, boolean hasItem) {
    Identifier id = Identifier.of(LighterEnd.MOD_ID, name);
    Block block = factory.apply(Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, id)));

    if (hasItem){
      RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, id);
      Registry.register(Registries.ITEM,
          itemKey,
        new BlockItem(block, new Item.Settings().registryKey(itemKey))
      );
    }
    RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, id);
    return Registry.register(Registries.BLOCK, blockKey, block);
  }

  public static void initialize() {}

}


