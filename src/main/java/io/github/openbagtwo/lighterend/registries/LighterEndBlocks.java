package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.blocks.AuroraCrystal;
import io.github.openbagtwo.lighterend.blocks.CreepingMoss;
import io.github.openbagtwo.lighterend.blocks.DragonBone;
import io.github.openbagtwo.lighterend.blocks.EndMoss;
import io.github.openbagtwo.lighterend.blocks.Lumecorn;
import io.github.openbagtwo.lighterend.blocks.Sapling;
import io.github.openbagtwo.lighterend.blocks.Signs;
import io.github.openbagtwo.lighterend.blocks.SilkMothNest;
import io.github.openbagtwo.lighterend.blocks.TenaneaFlower;
import io.github.openbagtwo.lighterend.blocks.UmbrellaFern;
import io.github.openbagtwo.lighterend.blocks.UmbrellaFern.TallUmbrellaFern;
import io.github.openbagtwo.lighterend.blocks.UmbrellaMembrane;
import io.github.openbagtwo.lighterend.blocks.UmbrellaTreeCluster;
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
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.PressurePlateBlock;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

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
  public static Wood TENANEA = new Wood("tenanea", MapColor.TERRACOTTA_YELLOW, MapColor.MAGENTA);
  public static Block TENANEA_LEAVES = register(
      "tenanea_leaves",
      settings -> new TintedParticleLeavesBlock(
          0.01F,
          applyLeafSettings(settings.mapColor(MapColor.PINK))
      )
  );
  public static Block SILK_MOTH_NEST = register("silk_moth_nest", SilkMothNest::new);

  public static Block UMBRELLA_TREE_CLUSTER = register("umbrella_tree_cluster",
      UmbrellaTreeCluster::new);
  public static Block UMBRELLA_TREE_CLUSTER_EMPTY = register("umbrella_tree_cluster_empty",
      UmbrellaTreeCluster.EmptyCluster::new);
  public static Block UMBRELLA_TREE_SAPLING = register("umbrella_tree_sapling",
      settings -> new Sapling(UmbrellaTree::new, settings.mapColor(MapColor.BRIGHT_TEAL)));
  public static Wood UMBRELLA = new Wood("umbrella", MapColor.BLUE, MapColor.GREEN);
  public static Block UMBRELLA_MEMBRANE = register("umbrella_membrane", UmbrellaMembrane::new);


  public static Block register(String name, Function<Settings, Block> factory) {
    return register(name, factory, true);
  }

  public static Block register(String name, Function<Settings, Block> factory, boolean hasItem) {
    Identifier id = LighterEnd.of(name);
    Block block = factory.apply(
        Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, id)));

    if (hasItem) {
      RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, id);
      Registry.register(Registries.ITEM, itemKey,
          new BlockItem(block, new Item.Settings().registryKey(itemKey)));
    }
    RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, id);
    return Registry.register(Registries.BLOCK, blockKey, block);
  }

  public static void initialize() {
    FlammableBlockRegistry.getDefaultInstance().add(CREEPING_MOSS, 60, 100);
    FlammableBlockRegistry.getDefaultInstance().add(UMBRELLA_FERN, 60, 100);
    FlammableBlockRegistry.getDefaultInstance().add(TALL_UMBRELLA_FERN, 60, 100);
    FlammableBlockRegistry.getDefaultInstance().add(LUMECORN, 60, 100);
    FlammableBlockRegistry.getDefaultInstance().add(LUMECORN_STEM, 60, 100);
    FlammableBlockRegistry.getDefaultInstance().add(TENANEA_FLOWER, 15, 100);
    FlammableBlockRegistry.getDefaultInstance().add(TENANEA_LEAVES, 30, 60);
    FlammableBlockRegistry.getDefaultInstance().add(SILK_MOTH_NEST, 30, 20);
    FlammableBlockRegistry.getDefaultInstance().add(UMBRELLA_TREE_CLUSTER, 60, 100);
    FlammableBlockRegistry.getDefaultInstance().add(UMBRELLA_TREE_CLUSTER_EMPTY, 30, 20);
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

      for (Block block : Arrays.asList(log, strippedLog, wood, strippedWood)) {
        FlammableBlockRegistry.getDefaultInstance().add(block, 5, 5);
      }
      for (Block block : Arrays.asList(planks, slab, stairs, fence, gate)) {
        FlammableBlockRegistry.getDefaultInstance().add(block, 5, 20);
      }

      blocks = Arrays.asList(log, strippedLog, wood, strippedWood, planks, slab, stairs, door,
          trapdoor, fence, gate, button, pressurePlate, sign, hangingSign);
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

}
