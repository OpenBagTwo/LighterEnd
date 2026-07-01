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
import io.github.openbagtwo.lighterend.blocks.Murkweed;
import io.github.openbagtwo.lighterend.blocks.Needlegrass;
import io.github.openbagtwo.lighterend.blocks.Obelisk;
import io.github.openbagtwo.lighterend.blocks.Pedestal;
import io.github.openbagtwo.lighterend.blocks.Polypore;
import io.github.openbagtwo.lighterend.blocks.Sapling;
import io.github.openbagtwo.lighterend.blocks.ShadowBerry;
import io.github.openbagtwo.lighterend.blocks.ShadowGrass;
import io.github.openbagtwo.lighterend.blocks.SilkMothNest;
import io.github.openbagtwo.lighterend.blocks.SulphurCrystal;
import io.github.openbagtwo.lighterend.blocks.TenaneaFlower;
import io.github.openbagtwo.lighterend.blocks.TubeWorm;
import io.github.openbagtwo.lighterend.blocks.UmbrellaFern;
import io.github.openbagtwo.lighterend.blocks.UmbrellaFern.TallUmbrellaFern;
import io.github.openbagtwo.lighterend.blocks.UmbrellaMembrane;
import io.github.openbagtwo.lighterend.blocks.UmbrellaTreeCluster;
import io.github.openbagtwo.lighterend.blocks.VentBubbleColumn;
import io.github.openbagtwo.lighterend.misc.Wood.WoodSet;
import io.github.openbagtwo.lighterend.world.features.trees.DragonTree;
import io.github.openbagtwo.lighterend.world.features.trees.Glowshroom;
import io.github.openbagtwo.lighterend.world.features.trees.TenaneaTree;
import io.github.openbagtwo.lighterend.world.features.trees.UmbrellaTree;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TintedParticleLeavesBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.WeatheringCopperCollection;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class LighterEndBlocks {

  public static final Block AURORA_CRYSTAL = register("aurora_crystal", AuroraCrystal::new);
  public static final Block ENDER_BLOCK = register("ender_block", settings -> new Block(
      settings.instrument(NoteBlockInstrument.IRON_XYLOPHONE).mapColor(MapColor.WARPED_WART_BLOCK)
          .strength(5F, 6F).requiresCorrectToolForDrops().sound(SoundType.STONE)));
  public static final Material VIOLECITE = new Material("violecite", MapColor.TERRACOTTA_BLACK);
  public static final Block MISSING_TILE = register("missing_tile", settings -> new Block(
      settings.instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
          .strength(3.0F, 9.0F)
          .mapColor(MapColor.TERRACOTTA_PURPLE)));

  public static final Material AZURE_JADESTONE = new Material("azure_jadestone",
      MapColor.COLOR_LIGHT_BLUE);
  public static final Material SANDY_JADESTONE = new Material("sandy_jadestone",
      MapColor.COLOR_YELLOW);
  public static final Material VIRID_JADESTONE = new Material("virid_jadestone",
      MapColor.COLOR_GREEN);

  public static Block DRAGON_BONE_BLOCK = register("dragon_bone_block",
      settings -> new RotatedPillarBlock(DragonBone.applySettings(settings)));
  public static Block DRAGON_BONE_STAIRS = register("dragon_bone_stairs",
      settings -> new StairBlock(DRAGON_BONE_BLOCK.defaultBlockState(),
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

  public static Material UMBRALITH = new Material("umbralith", MapColor.COLOR_BLACK);

  public static Block TENANEA_FLOWER = register("tenanea_flower", TenaneaFlower::new);
  public static Block TENANEA_SAPLING = register("tenanea_sapling",
      settings -> new Sapling(TenaneaTree::new, settings.mapColor(MapColor.COLOR_PINK)));
  public static Block POTTED_TENANEA_SAPLING = register(
      "potted_tenanea_sapling",
      settings -> new FlowerPotBlock(TENANEA_SAPLING, applyFlowerPotSettings(settings)),
      false
  );
  public static WoodSet TENANEA = new WoodSet(
      "tenanea",
      MapColor.TERRACOTTA_YELLOW,
      MapColor.COLOR_MAGENTA
  );
  public static Block TENANEA_LEAVES = register(
      "tenanea_leaves",
      settings -> new TintedParticleLeavesBlock(
          0.01F,
          applyLeafSettings(settings.mapColor(MapColor.COLOR_PINK))
      )
  );
  public static Block SILK_MOTH_NEST = register("silk_moth_nest", SilkMothNest::new, false);

  public static Block UMBRELLA_TREE_CLUSTER = register("umbrella_tree_cluster",
      UmbrellaTreeCluster::new);
  public static Block UMBRELLA_TREE_CLUSTER_EMPTY = register("umbrella_tree_cluster_empty",
      UmbrellaTreeCluster.EmptyCluster::new);
  public static Block UMBRELLA_TREE_SAPLING = register("umbrella_tree_sapling",
      settings -> new Sapling(UmbrellaTree::new, settings.mapColor(MapColor.WARPED_WART_BLOCK)));
  public static Block POTTED_UMBRELLA_SAPLING = register(
      "potted_umbrella_tree_sapling",
      settings -> new FlowerPotBlock(UMBRELLA_TREE_SAPLING, applyFlowerPotSettings(settings)),
      false
  );
  public static WoodSet UMBRELLA = new WoodSet("umbrella", MapColor.COLOR_BLUE,
      MapColor.COLOR_GREEN);
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

  public static final WoodSet LOTUS = new WoodSet("end_lotus", MapColor.COLOR_LIGHT_BLUE,
      MapColor.COLOR_CYAN);

  public static final Block GLOWSHROOM_SAPLING = register("mossy_glowshroom_sapling",
      settings -> new Sapling(Glowshroom::new, settings.lightLevel((bs) -> 7)));
  public static Block POTTED_GLOWSHROOM_SAPLING = register(
      "potted_mossy_glowshroom_sapling",
      settings -> new FlowerPotBlock(GLOWSHROOM_SAPLING, applyFlowerPotSettings(settings)),
      false
  );
  public static final WoodSet GLOWSHROOM = new WoodSet(
      "mossy_glowshroom",
      MapColor.COLOR_GRAY,
      MapColor.WOOD);
  public static final Block GLOWSHROOM_CAP = register("mossy_glowshroom_cap", GlowshroomCap::new);
  public static final Block GLOWSHROOM_HYMENOPHORE = register(
      "mossy_glowshroom_hymenophore",
      settings -> new Block(
          settings
              .mapColor(MapColor.COLOR_LIGHT_BLUE)
              .strength(1.0F)
              .lightLevel((bs) -> 15)
              .sound(SoundType.WART_BLOCK)
      )
  );
  public static final Block GLOWSHROOM_FUR = register(
      "mossy_glowshroom_fur", settings -> new Fur(settings, MapColor.COLOR_LIGHT_BLUE, 4, true),
      false
  );

  public static final Block AGAVE = register("blue_vine", Agave::new, false);
  public static final Block AGAVE_BULB = register("blue_vine_lantern", Agave.Bulb::new);
  public static final Block AGAVE_FUR = register(
      "blue_vine_fur",
      settings -> new Fur(settings, MapColor.WARPED_WART_BLOCK, 0, false),
      false
  );
  public static final Block AGAVE_SEED = register(
      "blue_vine_seed", settings -> new Sapling(Agave.AgaveFeature::new, settings)
  );

  public static final Block AURANT_POLYPORE = register(
      "aurant_polypore",
      settings -> new Polypore(settings, MapColor.CRIMSON_HYPHAE, 13)
  );
  public static final Block PURPLE_POLYPORE = register(
      "purple_polypore",
      settings -> new Polypore(settings, MapColor.COLOR_MAGENTA, 0)
  );

  public static final Block END_FURNACE = register("end_stone_furnace", Furnaces.EndFurnace::new);
  public static final Block END_SMOKER = register("end_stone_smoker", Furnaces.EndSmoker::new);

  public static final Block END_LEVER = register("end_stone_lever", settings -> new LeverBlock(
          settings
              .noCollision()
              .strength(1.0F)
              .sound(SoundType.STONE)
              .pushReaction(PushReaction.DESTROY)
      )
  );

  public static final Block OBELISK = register("obelisk", Obelisk::new);

  public static final Block GOLD_CHANDELIER = register("gold_chandelier", Chandelier::new);
  public static final Block IRON_CHANDELIER = register("iron_chandelier", Chandelier::new);
  public static final WeatheringCopperCollection<Block> COPPER_CHANDELIERS = WeatheringCopperCollection.registerBlocks(
      WeatheringCopperCollection.prefixWithState(
          WeatheringCopperCollection.create("copper_chandelier")
      ).map(
          (name) -> (BlockItemId.create(LighterEnd.of(name), LighterEnd.of(name)))
      ),
      (id, factory, properties) -> Blocks.register(id.block(), factory, properties),
      (s, p) -> new Chandelier(p),
      Chandelier.Oxidizable::new,
      oxidationLevel -> Properties.of()
          .mapColor(MapColor.METAL)
          .lightLevel((bs) -> 15)
          .forceSolidOn()
          .noOcclusion()
          .requiresCorrectToolForDrops()
          .pushReaction(PushReaction.DESTROY)
          .strength(2.5F)
          .sound(SoundType.CHAIN)
  );

  public static final WeatheringCopperCollection<Item> COPPER_CHANDELIER_ITEMS = WeatheringCopperCollection.registerItems(
      WeatheringCopperCollection.prefixWithState(
          WeatheringCopperCollection.create("copper_chandelier")
      ).map(
          (name) -> (BlockItemId.create(LighterEnd.of(name), LighterEnd.of(name)))
      ),
      COPPER_CHANDELIERS,
      Items::registerBlock
  );

  public static final Block EMERALD_ICE = register(
      "emerald_ice",
      settings -> new Block(
          settings
              .mapColor(MapColor.GRASS)
              .instrument(NoteBlockInstrument.CHIME)
              .friction(0.95F)
              .strength(0.75F)
              .sound(SoundType.GLASS)
              .requiresCorrectToolForDrops()
              .noOcclusion()
      )
  );

  public static final Block FERROUS_ICE = register(
      "ferrous_ice",
      settings -> new Block(
          settings
              .mapColor(MapColor.CRIMSON_STEM)
              .instrument(NoteBlockInstrument.CHIME)
              .friction(0.95F)
              .strength(0.75F)
              .sound(SoundType.GLASS)
              .requiresCorrectToolForDrops()
              .noOcclusion()
      )
  );

  public static final Block AUROUS_ICE = register(
      "aurous_ice",
      settings -> new Block(
          settings
              .mapColor(MapColor.SAND)
              .instrument(NoteBlockInstrument.CHIME)
              .friction(0.95F)
              .strength(0.75F)
              .sound(SoundType.GLASS)
              .requiresCorrectToolForDrops()
              .noOcclusion()
      )
  );

  public static final Block END_STONE_REDSTONE_ORE = register(
      "end_stone_redstone_ore",
      settings -> new RedStoneOreBlock(
          settings
              .mapColor(MapColor.SAND)
              .strength(4.5F, 9.0F)
              .instrument(NoteBlockInstrument.BASEDRUM)
              .requiresCorrectToolForDrops()
              .randomTicks()
              .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 9 : 0)
      )
  );

  public static final Block UMBRALITH_REDSTONE_ORE = register(
      "umbralith_redstone_ore",
      settings -> new RedStoneOreBlock(
          settings
              .mapColor(MapColor.COLOR_BLACK)
              .strength(4.5F, 9.0F)
              .instrument(NoteBlockInstrument.BASEDRUM)
              .requiresCorrectToolForDrops()
              .randomTicks()
              .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 9 : 0)
      )
  );

  public static final Block END_STONE_QUARTZ_ORE = register(
      "end_stone_quartz_ore",
      settings -> new DropExperienceBlock(UniformInt.of(2, 5),
          settings
              .mapColor(MapColor.SAND)
              .strength(4.5F, 9.0F)
              .instrument(NoteBlockInstrument.BASEDRUM)
              .requiresCorrectToolForDrops()
      )
  );

  public static final Block UMBRALITH_QUARTZ_ORE = register(
      "umbralith_quartz_ore",
      settings -> new DropExperienceBlock(UniformInt.of(2, 5),
          settings
              .mapColor(MapColor.COLOR_BLACK)
              .strength(4.5F, 9.0F)
              .instrument(NoteBlockInstrument.BASEDRUM)
              .requiresCorrectToolForDrops()
      )
  );

  public static final Block BRIMSTONE = register("brimstone", Brimstone::new);
  public static final Material BORNITE = new Material("sulphuric_rock", MapColor.COLOR_BROWN);
  public static final Block SULPHUR_CRYSTAL = register("sulphur_crystal", SulphurCrystal::new);
  public static final Block HYDROTHERMAL_VENT = register(
      "hydrothermal_vent",
      HydrothermalVent::new
  );
  public static final Block VENT_BUBBLE_COLUMN = register(
      "vent_bubble_column",
      VentBubbleColumn::new,
      false
  );
  public static final Block TUBE_WORM = register(
      "tube_worm",
      TubeWorm::new
  );

  public static final Block SHADOW_BERRY = register(
      "shadow_berry",
      ShadowBerry::new,
      false
  );
  public static final Block SHADOW_GRASS = register(
      "shadow_plant",
      ShadowGrass::new
  );
  public static final Block NEEDLEGRASS = register(
      "needlegrass",
      Needlegrass::new
  );
  public static final Block MURKWEED = register(
      "murkweed",
      Murkweed::new
  );
  public static Block DRAGON_SAPLING = register("dragon_tree_sapling",
      settings -> new Sapling(DragonTree::new, settings.mapColor(MapColor.COLOR_MAGENTA)));
  public static Block POTTED_DRAGON_SAPLING = register(
      "potted_dragon_tree_sapling",
      settings -> new FlowerPotBlock(DRAGON_SAPLING, applyFlowerPotSettings(settings)),
      false
  );
  public static WoodSet DRAGON = new WoodSet("dragon_tree", MapColor.COLOR_BLACK,
      MapColor.COLOR_PURPLE);
  public static Block DRAGON_LEAVES = register(
      "dragon_tree_leaves",
      settings -> new TintedParticleLeavesBlock(
          0.01F,
          applyLeafSettings(settings.mapColor(MapColor.COLOR_MAGENTA))
      )
  );

  public static Block register(String name, Function<Properties, Block> factory) {
    return register(name, factory, true);
  }

  public static Block register(String name, Function<Properties, Block> factory, boolean hasItem) {
    return register(name, factory, Properties.of(), hasItem);
  }

  private static Block register(
      String name,
      Function<Properties, Block> factory,
      Properties settings,
      boolean hasItem
  ) {
    if (hasItem) {
      BlockItemId id = BlockItemId.create(LighterEnd.of(name), LighterEnd.of(name));
      Block block = factory.apply(settings.setId(id.block()));
      LighterEndItems.register(
          id.item(),
          new BlockItem(
              block,
              new Item.Properties()
                  .setId(id.item())
                  .useBlockDescriptionPrefix()
                  .requiredFeatures(block.requiredFeatures())
          )
      );
      return Registry.register(BuiltInRegistries.BLOCK, id.block(), block);
    }
    ResourceKey<Block> id = ResourceKey.create(Registries.BLOCK, LighterEnd.of(name));
    Block block = factory.apply(settings.setId(id));
    return Registry.register(BuiltInRegistries.BLOCK, id, block);
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
    public final Block pedestal;
    public final List<Block> blocks;
    private final MapColor mapColor;


    public Material(String name, MapColor mapColor) {
      this.baseName = name;
      this.mapColor = mapColor;

      baseBlock = register(baseName, settings -> new Block(applySettings(settings)));
      baseStairs = register(baseName + "_stairs",
          settings -> new StairBlock(baseBlock.defaultBlockState(), applySettings(settings)));
      baseSlab = register(baseName + "_slab", settings -> new SlabBlock(applySettings(settings)));
      baseWall = register(baseName + "_wall", settings -> new WallBlock(applySettings(settings)));

      bricks = register(baseName + "_bricks", settings -> new Block(applySettings(settings)));
      brickStairs = register(baseName + "_brick_stairs",
          settings -> new StairBlock(bricks.defaultBlockState(), applySettings(settings)));
      brickSlab = register(baseName + "_brick_slab",
          settings -> new SlabBlock(applySettings(settings)));
      brickWall = register(baseName + "_brick_wall",
          settings -> new WallBlock(applySettings(settings)));

      polished = register(baseName + "_polished", settings -> new Block(applySettings(settings)));
      polishedStairs = register(baseName + "_polished_stairs",
          settings -> new StairBlock(polished.defaultBlockState(), applySettings(settings)));
      polishedSlab = register(baseName + "_polished_slab",
          settings -> new SlabBlock(applySettings(settings)));
      polishedWall = register(baseName + "_polished_wall",
          settings -> new WallBlock(applySettings(settings)));

      tiles = register(baseName + "_tiles", settings -> new Block(applySettings(settings)));
      tileStairs = register(baseName + "_tile_stairs",
          settings -> new StairBlock(tiles.defaultBlockState(), applySettings(settings)));
      tileSlab = register(baseName + "_tile_slab",
          settings -> new SlabBlock(applySettings(settings)));
      tileWall = register(baseName + "_tile_wall",
          settings -> new WallBlock(applySettings(settings)));

      pillar = register(baseName + "_pillar",
          settings -> new RotatedPillarBlock(applySettings(settings)));
      button = register(baseName + "_button",
          settings -> new ButtonBlock(BlockSetType.POLISHED_BLACKSTONE, 30,
              settings.noCollision().strength(0.5F).pushReaction(PushReaction.DESTROY)));
      pressurePlate = register(baseName + "_pressure_plate",
          settings -> new PressurePlateBlock(BlockSetType.POLISHED_BLACKSTONE,
              settings.mapColor(mapColor)
                  .forceSolidOn()
                  .instrument(NoteBlockInstrument.BASEDRUM)
                  .noCollision()
                  .strength(0.5F)
                  .pushReaction(PushReaction.DESTROY)));
      pedestal = register(baseName + "_pedestal",
          settings -> new Pedestal(applySettings(settings))
      );

      blocks = Arrays.asList(baseBlock, baseStairs, baseSlab, baseWall, bricks, brickStairs,
          brickSlab, brickWall, polished, polishedStairs, polishedSlab, polishedWall, tiles,
          tileStairs, tileSlab, tileWall, pillar, button, pressurePlate, pedestal);
    }

    public Properties applySettings(Properties settings) {
      return settings.instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
          .strength(3.0F, 9.0F)
          .mapColor(this.mapColor);
    }
  }

  public static Properties applyLeafSettings(Properties settings) {
    return settings
        .strength(0.2F)
        .randomTicks()
        .sound(SoundType.GRASS)
        .noOcclusion()
        .isValidSpawn(Blocks::ocelotOrParrot)
        .isSuffocating(Blocks::never)
        .isViewBlocking(Blocks::never)
        .ignitedByLava()
        .pushReaction(PushReaction.DESTROY)
        .isRedstoneConductor(Blocks::never);
  }

  public static Properties applyFlowerPotSettings(Properties settings) {
    return settings.instabreak().noOcclusion().pushReaction(PushReaction.DESTROY);
  }

}
