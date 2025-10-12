package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.blocks.Agave;
import io.github.openbagtwo.lighterend.blocks.AuroraCrystal;
import io.github.openbagtwo.lighterend.blocks.Brimstone;
import io.github.openbagtwo.lighterend.blocks.Chandelier;
import io.github.openbagtwo.lighterend.blocks.Charnia;
import io.github.openbagtwo.lighterend.blocks.CreepingMoss;
import io.github.openbagtwo.lighterend.blocks.DragonBone;
import io.github.openbagtwo.lighterend.blocks.EndLily;
import io.github.openbagtwo.lighterend.blocks.EndLotus;
import io.github.openbagtwo.lighterend.blocks.EndMoss;
import io.github.openbagtwo.lighterend.blocks.Fur;
import io.github.openbagtwo.lighterend.blocks.Furnaces;
import io.github.openbagtwo.lighterend.blocks.GlowshroomCap;
import io.github.openbagtwo.lighterend.blocks.HydrothermalVent;
import io.github.openbagtwo.lighterend.blocks.Lumecorn;
import io.github.openbagtwo.lighterend.blocks.Obelisk;
import io.github.openbagtwo.lighterend.blocks.Polypore;
import io.github.openbagtwo.lighterend.blocks.Sapling;
import io.github.openbagtwo.lighterend.blocks.Shelf;
import io.github.openbagtwo.lighterend.blocks.Signs;
import io.github.openbagtwo.lighterend.blocks.SilkMothNest;
import io.github.openbagtwo.lighterend.blocks.SulphurCrystal;
import io.github.openbagtwo.lighterend.blocks.TenaneaFlower;
import io.github.openbagtwo.lighterend.blocks.UmbrellaFern;
import io.github.openbagtwo.lighterend.blocks.UmbrellaFern.TallUmbrellaFern;
import io.github.openbagtwo.lighterend.blocks.UmbrellaMembrane;
import io.github.openbagtwo.lighterend.blocks.UmbrellaTreeCluster;
import io.github.openbagtwo.lighterend.blocks.VentBubbleColumn;
import io.github.openbagtwo.lighterend.world.features.trees.Glowshroom;
import io.github.openbagtwo.lighterend.world.features.trees.TenaneaTree;
import io.github.openbagtwo.lighterend.world.features.trees.UmbrellaTree;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.Blocks;
import net.minecraft.block.ButtonBlock;
import net.minecraft.block.CopperBlockSet;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.TintedParticleLeavesBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.WoodType;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.HangingSignItem;
import net.minecraft.item.Item;
import net.minecraft.item.SignItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class LighterEndBlocks {

  public static final Block AURORA_CRYSTAL = register("aurora_crystal", AuroraCrystal::new);
  public static final Block ENDER_BLOCK = register("ender_block", settings -> new Block(
      settings.instrument(NoteBlockInstrument.IRON_XYLOPHONE).mapColor(MapColor.BRIGHT_TEAL)
          .strength(5F, 6F).requiresTool().sounds(BlockSoundGroup.STONE)));
  public static final Material VIOLECITE = new Material("violecite", MapColor.TERRACOTTA_BLACK);
  public static final Block MISSING_TILE = register("missing_tile", settings -> new Block(
      settings.instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 9.0F)
          .mapColor(MapColor.TERRACOTTA_PURPLE)));

  public static final Material AZURE_JADESTONE = new Material("azure_jadestone",
      MapColor.LIGHT_BLUE);
  public static final Material SANDY_JADESTONE = new Material("sandy_jadestone", MapColor.YELLOW);
  public static final Material VIRID_JADESTONE = new Material("virid_jadestone", MapColor.GREEN);

  public static Block DRAGON_BONE_BLOCK = register("dragon_bone_block",
      settings -> new PillarBlock(DragonBone.applySettings(settings)));
  public static Block DRAGON_BONE_STAIRS = register("dragon_bone_stairs",
      settings -> new StairsBlock(DRAGON_BONE_BLOCK.getDefaultState(),
          DragonBone.applySettings(settings)));
  public static Block DRAGON_BONE_SLAB = register("dragon_bone_slab",
      settings -> new SlabBlock(DragonBone.applySettings(settings)));

  public static Block END_MOSS = register("end_moss", EndMoss::new);

  public static Block CREEPING_MOSS = register("creeping_moss", CreepingMoss::new);

  public static Block UMBRELLA_FERN = register("umbrella_fern", UmbrellaFern::new);

  public static Block TALL_UMBRELLA_FERN = register("umbrella_fern_tall", TallUmbrellaFern::new,
      false);

  public static Block LUMECORN_SEED = register("lumecorn_seed", Lumecorn.LumecornSeed::new);
  public static Block LUMECORN = register("lumecorn", Lumecorn::new, false);
  public static Block LUMECORN_STEM = register("lumecorn_stem", Lumecorn.LumecornStem::new, false);

  public static Material UMBRALITH = new Material("umbralith", MapColor.BLACK);

  public static Block TENANEA_FLOWER = register("tenanea_flower", TenaneaFlower::new);
  public static Block TENANEA_SAPLING = register("tenanea_sapling",
      settings -> new Sapling(TenaneaTree::new, settings.mapColor(MapColor.PINK)));
  public static Block POTTED_TENANEA_SAPLING = register(
      "potted_tenanea_sapling",
      settings -> new FlowerPotBlock(TENANEA_SAPLING, applyFlowerPotSettings(settings)),
      false
  );
  public static Wood TENANEA = new Wood("tenanea", MapColor.TERRACOTTA_YELLOW, MapColor.MAGENTA);
  public static Block TENANEA_LEAVES = register(
      "tenanea_leaves",
      settings -> new TintedParticleLeavesBlock(
          0.01F,
          applyLeafSettings(settings.mapColor(MapColor.PINK))
      )
  );
  public static Block SILK_MOTH_NEST = register("silk_moth_nest", SilkMothNest::new, false);

  public static Block UMBRELLA_TREE_CLUSTER = register("umbrella_tree_cluster",
      UmbrellaTreeCluster::new);
  public static Block UMBRELLA_TREE_CLUSTER_EMPTY = register("umbrella_tree_cluster_empty",
      UmbrellaTreeCluster.EmptyCluster::new);
  public static Block UMBRELLA_TREE_SAPLING = register("umbrella_tree_sapling",
      settings -> new Sapling(UmbrellaTree::new, settings.mapColor(MapColor.BRIGHT_TEAL)));
  public static Block POTTED_UMBRELLA_SAPLING = register(
      "potted_umbrella_tree_sapling",
      settings -> new FlowerPotBlock(UMBRELLA_TREE_SAPLING, applyFlowerPotSettings(settings)),
      false
  );
  public static Wood UMBRELLA = new Wood("umbrella", MapColor.BLUE, MapColor.GREEN);
  public static Block UMBRELLA_MEMBRANE = register("umbrella_membrane", UmbrellaMembrane::new);

  public static final Block CHARNIA_CYAN = register("charnia_cyan", Charnia::new);
  public static final Block CHARNIA_GREEN = register("charnia_green", Charnia::new);
  public static final Block CHARNIA_LIGHT_BLUE = register("charnia_light_blue", Charnia::new);
  public static final Block CHARNIA_ORANGE = register("charnia_orange", Charnia::new);
  public static final Block CHARNIA_PURPLE = register("charnia_purple", Charnia::new);
  public static final Block CHARNIA_RED = register("charnia_red", Charnia::new);

  public static final Block END_LILY = register("end_lily", EndLily::new, false);
  public static final Block END_LILY_SEED = register("end_lily_seed", EndLily.Seed::new);

  public static final Block END_LOTUS_FLOWER = register("end_lotus_flower", EndLotus::new, false);
  public static final Block END_LOTUS_STEM = register("end_lotus_stem", EndLotus.Stem::new);
  public static final Block END_LOTUS_LEAF = register("end_lotus_leaf", EndLotus.Leaf::new, false);
  public static final Block END_LOTUS_SEED = register("end_lotus_seed", EndLotus.Seed::new);

  public static final Wood LOTUS = new Wood("end_lotus", MapColor.LIGHT_BLUE, MapColor.CYAN);

  public static final Block GLOWSHROOM_SAPLING = register("mossy_glowshroom_sapling",
      settings -> new Sapling(Glowshroom::new, settings.luminance((bs) -> 7)));
  public static Block POTTED_GLOWSHROOM_SAPLING = register(
      "potted_mossy_glowshroom_sapling",
      settings -> new FlowerPotBlock(GLOWSHROOM_SAPLING, applyFlowerPotSettings(settings)),
      false
  );
  public static final Wood GLOWSHROOM = new Wood(
      "mossy_glowshroom",
      MapColor.GRAY,
      MapColor.OAK_TAN
  );
  public static final Block GLOWSHROOM_CAP = register("mossy_glowshroom_cap", GlowshroomCap::new);
  public static final Block GLOWSHROOM_HYMENOPHORE = register(
      "mossy_glowshroom_hymenophore",
      settings -> new Block(
          settings
              .mapColor(MapColor.LIGHT_BLUE)
              .strength(1.0F)
              .luminance((bs) -> 15)
              .sounds(BlockSoundGroup.WART_BLOCK)
      )
  );
  public static final Block GLOWSHROOM_FUR = register(
      "mossy_glowshroom_fur", settings -> new Fur(settings, MapColor.LIGHT_BLUE, 4, true), false
  );

  public static final Block AGAVE = register("blue_vine", Agave::new, false);
  public static final Block AGAVE_BULB = register("blue_vine_lantern", Agave.Bulb::new);
  public static final Block AGAVE_FUR = register(
      "blue_vine_fur",
      settings -> new Fur(settings, MapColor.BRIGHT_TEAL, 0, false),
      false
  );
  public static final Block AGAVE_SEED = register(
      "blue_vine_seed", settings -> new Sapling(Agave.AgaveFeature::new, settings)
  );

  public static final Block AURANT_POLYPORE = register(
      "aurant_polypore",
      settings -> new Polypore(settings, MapColor.DARK_CRIMSON, 13)
  );
  public static final Block PURPLE_POLYPORE = register(
      "purple_polypore",
      settings -> new Polypore(settings, MapColor.MAGENTA, 0)
  );

  public static final Block END_FURNACE = register("end_stone_furnace", Furnaces.EndFurnace::new);
  public static final Block END_SMOKER = register("end_stone_smoker", Furnaces.EndSmoker::new);

  public static final Block END_LEVER = register("end_stone_lever", settings -> new LeverBlock(
          settings
              .noCollision()
              .strength(1.0F)
              .sounds(BlockSoundGroup.STONE)
              .pistonBehavior(PistonBehavior.DESTROY)
      )
  );

  public static final Block OBELISK = register("obelisk", Obelisk::new);

  public static final Block GOLD_CHANDELIER = register("gold_chandelier", Chandelier::new);
  public static final Block IRON_CHANDELIER = register("iron_chandelier", Chandelier::new);
  public static final CopperBlockSet COPPER_CHANDELIERS = CopperBlockSet.create(
      "copper_chandelier",
      LighterEndBlocks::register,
      Chandelier::new,
      Chandelier.Oxidizable::new,
      oxidationLevel -> Settings.create()
          .mapColor(MapColor.IRON_GRAY)
          .luminance((bs) -> 15)
          .solid()
          .nonOpaque()
          .requiresTool()
          .pistonBehavior(PistonBehavior.DESTROY)
          .strength(2.5F)
          .sounds(BlockSoundGroup.CHAIN)
  );

  public static final Block EMERALD_ICE = register(
      "emerald_ice",
      settings -> new Block(
          settings
              .mapColor(MapColor.PALE_GREEN)
              .instrument(NoteBlockInstrument.CHIME)
              .slipperiness(0.95F)
              .strength(0.75F)
              .sounds(BlockSoundGroup.GLASS)
              .requiresTool()
              .nonOpaque()
      )
  );

  public static final Block FERROUS_ICE = register(
      "ferrous_ice",
      settings -> new Block(
          settings
              .mapColor(MapColor.DULL_PINK)
              .instrument(NoteBlockInstrument.CHIME)
              .slipperiness(0.95F)
              .strength(0.75F)
              .sounds(BlockSoundGroup.GLASS)
              .requiresTool()
              .nonOpaque()
      )
  );

  public static final Block AUROUS_ICE = register(
      "aurous_ice",
      settings -> new Block(
          settings
              .mapColor(MapColor.PALE_YELLOW)
              .instrument(NoteBlockInstrument.CHIME)
              .slipperiness(0.95F)
              .strength(0.75F)
              .sounds(BlockSoundGroup.GLASS)
              .requiresTool()
              .nonOpaque()
      )
  );

  public static final Block END_STONE_REDSTONE_ORE = register(
      "end_stone_redstone_ore",
      settings -> new RedstoneOreBlock(
          settings
              .mapColor(MapColor.PALE_YELLOW)
              .strength(4.5F, 9.0F)
              .instrument(NoteBlockInstrument.BASEDRUM)
              .requiresTool()
              .ticksRandomly()
              .luminance(state -> state.get(Properties.LIT) ? 9 : 0)
      )
  );

  public static final Block UMBRALITH_REDSTONE_ORE = register(
      "umbralith_redstone_ore",
      settings -> new RedstoneOreBlock(
          settings
              .mapColor(MapColor.BLACK)
              .strength(4.5F, 9.0F)
              .instrument(NoteBlockInstrument.BASEDRUM)
              .requiresTool()
              .ticksRandomly()
              .luminance(state -> state.get(Properties.LIT) ? 9 : 0)
      )
  );

  public static final Block END_STONE_QUARTZ_ORE = register(
      "end_stone_quartz_ore",
      settings -> new ExperienceDroppingBlock(UniformIntProvider.create(2, 5),
          settings
              .mapColor(MapColor.PALE_YELLOW)
              .strength(4.5F, 9.0F)
              .instrument(NoteBlockInstrument.BASEDRUM)
              .requiresTool()
      )
  );

  public static final Block UMBRALITH_QUARTZ_ORE = register(
      "umbralith_quartz_ore",
      settings -> new ExperienceDroppingBlock(UniformIntProvider.create(2, 5),
          settings
              .mapColor(MapColor.BLACK)
              .strength(4.5F, 9.0F)
              .instrument(NoteBlockInstrument.BASEDRUM)
              .requiresTool()
      )
  );

  public static final Block BRIMSTONE = register("brimstone", Brimstone::new);
  public static final Material SULPHUR = new Material("sulphuric_rock", MapColor.BROWN);
  public static final Block SULPHUR_CRYSTAL = register("sulphur_crystal", SulphurCrystal::new);
  public static final Block HYDROTHERMAL_VENT = register(
      "hydrothermal_vent",
      HydrothermalVent::new
  );
  public static final Block VENT_BUBBLE_COLUMN = register(
      "vent_bubble_column",
      VentBubbleColumn::new
  );

  public static Block register(String name, Function<Settings, Block> factory) {
    return register(name, factory, true);
  }

  public static Block register(String name, Function<Settings, Block> factory, boolean hasItem) {
    return register(name, factory, Settings.create(), hasItem);
  }

  private static Block register(String name, Function<Settings, Block> factory, Settings settings) {
    return register(name, factory, settings, true);
  }

  private static Block register(
      String name,
      Function<Settings, Block> factory,
      Settings settings,
      boolean hasItem
  ) {
    Identifier id = LighterEnd.of(name);
    Block block = factory.apply(settings.registryKey(RegistryKey.of(RegistryKeys.BLOCK, id)));

    if (hasItem) {
      RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, id);
      Registry.register(Registries.ITEM, itemKey,
          new BlockItem(block, new Item.Settings().registryKey(itemKey)));
    }
    RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, id);
    return Registry.register(Registries.BLOCK, blockKey, block);
  }

  public static void initialize() {
  }

  public static class Material {

    public final String baseName;
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
    // public final Block pedestal;
    public final List<Block> blocks;
    private final MapColor mapColor;


    public Material(String name, MapColor mapColor) {
      this.baseName = name;
      this.mapColor = mapColor;

      baseBlock = register(baseName, settings -> new Block(applySettings(settings)));
      baseStairs = register(baseName + "_stairs",
          settings -> new StairsBlock(baseBlock.getDefaultState(), applySettings(settings)));
      baseSlab = register(baseName + "_slab", settings -> new SlabBlock(applySettings(settings)));
      baseWall = register(baseName + "_wall", settings -> new WallBlock(applySettings(settings)));

      bricks = register(baseName + "_bricks", settings -> new Block(applySettings(settings)));
      brickStairs = register(baseName + "_brick_stairs",
          settings -> new StairsBlock(bricks.getDefaultState(), applySettings(settings)));
      brickSlab = register(baseName + "_brick_slab",
          settings -> new SlabBlock(applySettings(settings)));
      brickWall = register(baseName + "_brick_wall",
          settings -> new WallBlock(applySettings(settings)));

      polished = register(baseName + "_polished", settings -> new Block(applySettings(settings)));
      polishedStairs = register(baseName + "_polished_stairs",
          settings -> new StairsBlock(polished.getDefaultState(), applySettings(settings)));
      polishedSlab = register(baseName + "_polished_slab",
          settings -> new SlabBlock(applySettings(settings)));
      polishedWall = register(baseName + "_polished_wall",
          settings -> new WallBlock(applySettings(settings)));

      tiles = register(baseName + "_tiles", settings -> new Block(applySettings(settings)));
      tileStairs = register(baseName + "_tile_stairs",
          settings -> new StairsBlock(tiles.getDefaultState(), applySettings(settings)));
      tileSlab = register(baseName + "_tile_slab",
          settings -> new SlabBlock(applySettings(settings)));
      tileWall = register(baseName + "_tile_wall",
          settings -> new WallBlock(applySettings(settings)));

      pillar = register(baseName + "_pillar", settings -> new PillarBlock(applySettings(settings)));
      button = register(baseName + "_button",
          settings -> new ButtonBlock(BlockSetType.POLISHED_BLACKSTONE, 30,
              settings.noCollision().strength(0.5F).pistonBehavior(PistonBehavior.DESTROY)));
      pressurePlate = register(baseName + "_pressure_plate",
          settings -> new PressurePlateBlock(BlockSetType.POLISHED_BLACKSTONE,
              settings.mapColor(mapColor)
                  .solid()
                  .instrument(NoteBlockInstrument.BASEDRUM)
                  .noCollision()
                  .strength(0.5F)
                  .pistonBehavior(PistonBehavior.DESTROY)));

      blocks = Arrays.asList(baseBlock, baseStairs, baseSlab, baseWall, bricks, brickStairs,
          brickSlab, brickWall, polished, polishedStairs, polishedSlab, polishedWall, tiles,
          tileStairs, tileSlab, tileWall, pillar, button, pressurePlate);
    }

    public Settings applySettings(Settings settings) {
      return settings.instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 9.0F)
          .mapColor(this.mapColor);
    }
  }

  public static class Wood {

    public final String baseName;
    public final WoodType woodType;
    public final Block log;
    public final Block strippedLog;
    public final Block wood;
    public final Block strippedWood;
    public final Block planks;
    public final Block slab;
    public final Block stairs;
    public final Block door;
    public final Block trapdoor;
    public final Block fence;
    public final Block gate;
    public final Block button;
    public final Block pressurePlate;
    public final Block ladder;
    public final Block sign;
    public final Block wallSign;
    public final Block hangingSign;
    public final Block wallHangingSign;
    public final Block shelf;
    // public final Block stool;
    public final List<Block> blocks;
    private final MapColor woodColor;


    public Wood(String name, MapColor barkColor, MapColor woodColor) {
      this.baseName = name;
      this.woodColor = woodColor;

      // TODO: Add individual sound sets (BlockSetType)
      woodType = new WoodTypeBuilder().register(
          LighterEnd.of(baseName),
          BlockSetType.CHERRY
      );

      log = register(baseName + "_log", settings -> new PillarBlock(applyLogSettings(
          settings.mapColor(
              state -> state.get(PillarBlock.AXIS) == Direction.Axis.Y ? woodColor : barkColor))));
      strippedLog = register(baseName + "_stripped_log",
          settings -> new PillarBlock(applyLogSettings(settings.mapColor(woodColor))));
      wood = register(baseName + "_wood",
          settings -> new PillarBlock(applyLogSettings(settings.mapColor(barkColor))));
      strippedWood = register(baseName + "_stripped_wood",
          settings -> new PillarBlock(applyLogSettings(settings.mapColor(woodColor))));

      StrippableBlockRegistry.register(log, strippedLog);
      StrippableBlockRegistry.register(wood, strippedWood);

      planks = register(baseName + "_planks", settings -> new Block(applyPlankSettings(settings)));
      slab = register(baseName + "_slab", settings -> new SlabBlock(applyPlankSettings(settings)));
      stairs = register(baseName + "_stairs",
          settings -> new StairsBlock(planks.getDefaultState(), applyPlankSettings(settings)));

      door = register(baseName + "_door", settings -> new DoorBlock(BlockSetType.CHERRY,
          settings.mapColor(planks.getDefaultMapColor())
              .instrument(NoteBlockInstrument.BASS)
              .strength(3.0F)
              .nonOpaque()
              .burnable()
              .pistonBehavior(PistonBehavior.DESTROY)));
      trapdoor = register(baseName + "_trapdoor", settings -> new TrapdoorBlock(BlockSetType.CHERRY,
          settings.mapColor(planks.getDefaultMapColor())
              .instrument(NoteBlockInstrument.BASS)
              .strength(3.0F)
              .nonOpaque()
              .allowsSpawning(Blocks::never)
              .burnable()));
      fence = register(baseName + "_fence",
          settings -> new FenceBlock(settings.mapColor(planks.getDefaultMapColor())
              .instrument(NoteBlockInstrument.BASS)
              .strength(2.0F, 3.0F)
              .burnable()
              .sounds(BlockSoundGroup.CHERRY_WOOD)));
      gate = register(baseName + "_fence_gate", settings -> new FenceGateBlock(woodType,
          settings.mapColor(planks.getDefaultMapColor()).solid()
              .instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).burnable()));
      button = register(baseName + "_button", settings -> new ButtonBlock(BlockSetType.CHERRY, 30,
          settings.noCollision().strength(0.5F).pistonBehavior(PistonBehavior.DESTROY)));
      pressurePlate = register(baseName + "_pressure_plate",
          settings -> new PressurePlateBlock(BlockSetType.CHERRY,
              settings.mapColor(planks.getDefaultMapColor())
                  .solid()
                  .instrument(NoteBlockInstrument.BASS)
                  .noCollision()
                  .strength(0.5F)
                  .burnable()
                  .pistonBehavior(PistonBehavior.DESTROY)));
      ladder = register(baseName + "_ladder", settings -> new LadderBlock(
          settings.strength(0.4F).sounds(BlockSoundGroup.LADDER).nonOpaque()
              .pistonBehavior(PistonBehavior.DESTROY)));
      sign = register(baseName + "_sign",
          settings -> new Signs.LighterEndStandingSignBlock(woodType,
              settings.mapColor(planks.getDefaultMapColor())), false);
      wallSign = register(baseName + "_wall_sign",
          settings -> new Signs.LighterEndWallSignBlock(woodType,
              settings.mapColor(planks.getDefaultMapColor()).lootTable(sign.getLootTableKey())
                  .overrideTranslationKey(sign.getTranslationKey())), false);
      Registry.register(Registries.ITEM, LighterEnd.of(baseName + "_sign"),
          new SignItem(sign, wallSign, new Item.Settings().maxCount(16).registryKey(
                  RegistryKey.of(RegistryKeys.ITEM,
                      LighterEnd.of(baseName + "_sign")))
              .useBlockPrefixedTranslationKey()));
      hangingSign = register(baseName + "_hanging_sign",
          settings -> new Signs.LighterEndCeilingHangingSignBlock(woodType,
              settings.mapColor(planks.getDefaultMapColor())), false);
      wallHangingSign = register(baseName + "_wall_hanging_sign",
          settings -> new Signs.LighterEndWallHangingSignBlock(woodType,
              settings.lootTable(hangingSign.getLootTableKey())
                  .overrideTranslationKey(hangingSign.getTranslationKey())
                  .mapColor(planks.getDefaultMapColor())), false);
      Registry.register(Registries.ITEM,
          LighterEnd.of(baseName + "_hanging_sign"),
          new HangingSignItem(hangingSign, wallHangingSign,
              new Item.Settings().maxCount(16).registryKey(
                      RegistryKey.of(RegistryKeys.ITEM,
                          LighterEnd.of(baseName + "_hanging_sign")))
                  .useBlockPrefixedTranslationKey()));
      shelf = register(
          baseName + "_shelf",
          settings -> new Shelf(
              settings.mapColor(planks.getDefaultMapColor())
                  .instrument(NoteBlockInstrument.BASS)
                  .sounds(BlockSoundGroup.SHELF)
                  .burnable()
                  .strength(2.0F, 3.0F)
          )
      );

      for (Block block : Arrays.asList(log, strippedLog, wood, strippedWood)) {
        FlammableBlockRegistry.getDefaultInstance().add(block, 5, 5);
      }
      for (Block block : Arrays.asList(planks, slab, stairs, fence, gate)) {
        FlammableBlockRegistry.getDefaultInstance().add(block, 5, 20);
      }
      FlammableBlockRegistry.getDefaultInstance().add(shelf, 30, 20);

      blocks = Arrays.asList(log, strippedLog, wood, strippedWood, planks, slab, stairs, door,
          trapdoor, fence, gate, button, pressurePlate, ladder, sign, hangingSign, shelf);
    }

    public Settings applyLogSettings(Settings settings) {
      return settings
          .instrument(NoteBlockInstrument.BASS)
          .strength(2.0F)
          .sounds(BlockSoundGroup.WOOD)
          .burnable();
    }

    public Settings applyPlankSettings(Settings settings) {
      return settings
          .mapColor(this.woodColor)
          .instrument(NoteBlockInstrument.BASS)
          .strength(2.0F, 3.0F)
          .sounds(BlockSoundGroup.WOOD)
          .burnable();
    }

  }

  public static Settings applyLeafSettings(Settings settings) {
    return settings
        .strength(0.2F)
        .ticksRandomly()
        .sounds(BlockSoundGroup.GRASS)
        .nonOpaque()
        .allowsSpawning(Blocks::canSpawnOnLeaves)
        .suffocates(Blocks::never)
        .blockVision(Blocks::never)
        .burnable()
        .pistonBehavior(PistonBehavior.DESTROY)
        .solidBlock(Blocks::never);
  }

  public static Settings applyFlowerPotSettings(Settings settings) {
    return settings.breakInstantly().nonOpaque().pistonBehavior(PistonBehavior.DESTROY);
  }

}
