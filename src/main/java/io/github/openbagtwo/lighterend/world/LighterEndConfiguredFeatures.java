package io.github.openbagtwo.lighterend.world;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.blocks.Agave.AgaveFeature;
import io.github.openbagtwo.lighterend.blocks.EndLily.EndLilyFeature;
import io.github.openbagtwo.lighterend.blocks.EndLotus.EndLotusFeature;
import io.github.openbagtwo.lighterend.blocks.Lumecorn;
import io.github.openbagtwo.lighterend.blocks.ShadowBerry;
import io.github.openbagtwo.lighterend.blocks.SilkMothNest.SilkMothNestFeature;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.github.openbagtwo.lighterend.world.features.AuroraCrystalFormation;
import io.github.openbagtwo.lighterend.world.features.BuriedBlob;
import io.github.openbagtwo.lighterend.world.features.Geyser;
import io.github.openbagtwo.lighterend.world.features.IceStar;
import io.github.openbagtwo.lighterend.world.features.LotusLeaf;
import io.github.openbagtwo.lighterend.world.features.PurplePolypores;
import io.github.openbagtwo.lighterend.world.features.StarterChest;
import io.github.openbagtwo.lighterend.world.features.SulphurCave;
import io.github.openbagtwo.lighterend.world.features.SulphurLake;
import io.github.openbagtwo.lighterend.world.features.SurfaceVent;
import io.github.openbagtwo.lighterend.world.features.UmbralithArch;
import io.github.openbagtwo.lighterend.world.features.UnderwaterPlants;
import io.github.openbagtwo.lighterend.world.features.trees.DragonTree;
import io.github.openbagtwo.lighterend.world.features.trees.Glowshroom;
import io.github.openbagtwo.lighterend.world.features.trees.TenaneaTree;
import io.github.openbagtwo.lighterend.world.features.trees.UmbrellaTree;
import java.util.List;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;

public class LighterEndConfiguredFeatures {

  public static final ResourceKey<ConfiguredFeature<?, ?>> END_MOSS_PATCH
      = of("end_moss_patch");
  public static final ResourceKey<ConfiguredFeature<?, ?>> END_MOSS_PATCH_BONEMEAL
      = of("end_moss_patch_bonemeal");
  public static final ResourceKey<ConfiguredFeature<?, ?>> END_MOSS_VEGETATION
      = of("end_moss_vegetation");
  public static final ResourceKey<ConfiguredFeature<?, ?>> SHADOW_MOSS_PATCH
      = of("end_moss_patch_shadow");
  public static final ResourceKey<ConfiguredFeature<?, ?>> SHADOW_MOSS_PATCH_BONEMEAL
      = of("end_moss_patch_bonemeal_shadow");
  public static final ResourceKey<ConfiguredFeature<?, ?>> SHADOW_MOSS_VEGETATION
      = of("end_moss_vegetation_shadow");


  public static final Feature<NoneFeatureConfiguration> LUMECORN_FEATURE = Registry.register(
      BuiltInRegistries.FEATURE,
      LighterEnd.of("lumecorn"),
      new Lumecorn.LumecornFeature());
  public static final ResourceKey<ConfiguredFeature<?, ?>> LUMECORN = of(
      "lumecorn");

  public static final Feature<NoneFeatureConfiguration> TENANEA_TREE_FEATURE = Registry.register(
      BuiltInRegistries.FEATURE,
      LighterEnd.of("tenanea_tree"),
      new TenaneaTree());
  public static final ResourceKey<ConfiguredFeature<?, ?>> TENANEA_TREE = of(
      "tenanea_tree");

  public static final Feature<NoneFeatureConfiguration> UMBRELLA_TREE_FEATURE = Registry.register(
      BuiltInRegistries.FEATURE,
      LighterEnd.of("umbrella_tree"),
      new UmbrellaTree());
  public static final ResourceKey<ConfiguredFeature<?, ?>> UMBRELLA_TREE = of(
      "umbrella_tree");

  public static final Feature<NoneFeatureConfiguration> MOTH_NEST_FEATURE = Registry.register(
      BuiltInRegistries.FEATURE,
      LighterEnd.of("silk_moth_nest"),
      new SilkMothNestFeature()
  );
  public static final ResourceKey<ConfiguredFeature<?, ?>> MOTH_NEST = of("silk_moth_nest");

  public static final Feature<NoneFeatureConfiguration> UNDERWATER_PLANTS = Registry.register(
      BuiltInRegistries.FEATURE,
      LighterEnd.of("aquatic_end_plants"),
      new UnderwaterPlants()
  );
  public static final ResourceKey<ConfiguredFeature<?, ?>> WATER_PLANTS = of("aquatic_end_plants");

  public static final Feature<NoneFeatureConfiguration> END_LILY_FEATURE = Registry.register(
      BuiltInRegistries.FEATURE,
      LighterEnd.of("end_lily"),
      new EndLilyFeature());
  public static final ResourceKey<ConfiguredFeature<?, ?>> END_LILY = of(
      "end_lily");

  public static final Feature<NoneFeatureConfiguration> END_LOTUS_FEATURE = Registry.register(
      BuiltInRegistries.FEATURE,
      LighterEnd.of("end_lotus"),
      new EndLotusFeature());
  public static final ResourceKey<ConfiguredFeature<?, ?>> END_LOTUS = of(
      "end_lotus");

  public static final Feature<NoneFeatureConfiguration> LOTUS_LEAF_FEATURE = Registry.register(
      BuiltInRegistries.FEATURE,
      LighterEnd.of("end_lotus_leaf"),
      new LotusLeaf());
  public static final ResourceKey<ConfiguredFeature<?, ?>> LOTUS_LEAF = of(
      "end_lotus_leaf");

  public static final Feature<NoneFeatureConfiguration> ARCH_FEATURE = Registry.register(
      BuiltInRegistries.FEATURE,
      LighterEnd.of("umbralith_arch"),
      new UmbralithArch());
  public static final ResourceKey<ConfiguredFeature<?, ?>> UMRBALITH_ARCH = of(
      "umbralith_arch");

  public static final Feature<NoneFeatureConfiguration> THIN_ARCH_FEATURE = Registry.register(
      BuiltInRegistries.FEATURE,
      LighterEnd.of("umbralith_arch_thin"),
      new UmbralithArch.Thin());
  public static final ResourceKey<ConfiguredFeature<?, ?>> UMRBALITH_ARCH_THIN = of(
      "umbralith_arch_thin");

  public static final Feature<NoneFeatureConfiguration> GLOWSHROOM_FEATURE = Registry.register(
      BuiltInRegistries.FEATURE,
      LighterEnd.of("glowshroom"),
      new Glowshroom());
  public static final ResourceKey<ConfiguredFeature<?, ?>> GLOWSHROOM = of("glowshroom");

  public static final Feature<NoneFeatureConfiguration> AGAVE_FEATURE = Registry.register(
      BuiltInRegistries.FEATURE,
      LighterEnd.of("agave"),
      new AgaveFeature()
  );
  public static final ResourceKey<ConfiguredFeature<?, ?>> AGAVE = of("agave");

  public static final Feature<IceStar.Config> ICE_STAR_FEATURE = Registry.register(
      BuiltInRegistries.FEATURE,
      LighterEnd.of("ice_star"),
      new IceStar()
  );
  public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_STAR_COPPER = of(
      "ice_star_copper");
  public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_STAR_COPPER_SMALL = of(
      "ice_star_copper_small");
  public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_STAR_IRON = of(
      "ice_star_iron");
  public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_STAR_IRON_SMALL = of(
      "ice_star_iron_small");
  public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_STAR_GOLD = of(
      "ice_star_gold");
  public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_STAR_GOLD_SMALL = of(
      "ice_star_gold_small");

  public static final Feature<BuriedBlob.Config> BURIED_BLOB = Registry.register(
      BuiltInRegistries.FEATURE,
      LighterEnd.of("buried_blob"),
      new BuriedBlob()
  );

  public static final List<ResourceKey<ConfiguredFeature<?, ?>>> JADESTONE_BLOBS = List.of(
      of("jadestone_blob_azure"),
      of("jadestone_blob_sandy"),
      of("jadestone_blob_virid")
  );

  public static final Feature<NoneFeatureConfiguration> AURORA_CRYSTAL_FEATURE = Registry.register(
      BuiltInRegistries.FEATURE,
      LighterEnd.of("aurora_crystal_formation"),
      new AuroraCrystalFormation()
  );

  public static final ResourceKey<ConfiguredFeature<?, ?>> AURORA_CRYSTAL = of(
      "aurora_crystal_formation");

  public static final ResourceKey<ConfiguredFeature<?, ?>> END_STONE_REDSTONE_ORE = of(
      "end_stone_redstone_ore");
  public static final ResourceKey<ConfiguredFeature<?, ?>> END_STONE_QUARTZ_ORE = of(
      "end_stone_quartz_ore");
  public static final ResourceKey<ConfiguredFeature<?, ?>> UMBRALITH_REDSTONE_ORE = of(
      "umbralith_redstone_ore");
  public static final ResourceKey<ConfiguredFeature<?, ?>> UMBRALITH_QUARTZ_ORE = of(
      "umbralith_quartz_ore");

  public static final Feature<NoneFeatureConfiguration> SULPHUR_LAKE_FEATURE = Registry.register(
      BuiltInRegistries.FEATURE,
      LighterEnd.of("sulphur_lake"),
      new SulphurLake()
  );
  public static final ResourceKey<ConfiguredFeature<?, ?>> SULPHUR_LAKE = of("sulphur_lake");

  public static final Feature<NoneFeatureConfiguration> SULPHUR_CAVE_FEATURE = Registry.register(
      BuiltInRegistries.FEATURE,
      LighterEnd.of("sulphur_cave"),
      new SulphurCave()
  );
  public static final ResourceKey<ConfiguredFeature<?, ?>> SULPHUR_CAVE = of("sulphur_cave");

  public static final Feature<NoneFeatureConfiguration> SURFACE_VENT_FEATURE = Registry.register(
      BuiltInRegistries.FEATURE,
      LighterEnd.of("surface_vent"),
      new SurfaceVent()
  );
  public static final ResourceKey<ConfiguredFeature<?, ?>> SURFACE_VENT = of("surface_vent");

  public static final Feature<NoneFeatureConfiguration> GEYSER_FEATURE = Registry.register(
      BuiltInRegistries.FEATURE,
      LighterEnd.of("geyser"),
      new Geyser()
  );
  public static final ResourceKey<ConfiguredFeature<?, ?>> GEYSER = of("geyser");

  public static final Feature<NoneFeatureConfiguration> DRAGON_TREE_FEATURE = Registry.register(
      BuiltInRegistries.FEATURE,
      LighterEnd.of("dragon_tree"),
      new DragonTree());
  public static final ResourceKey<ConfiguredFeature<?, ?>> DRAGON_TREE = of(
      "dragon_tree");

  public static final Feature<PurplePolypores.Config> PURPLE_POLYPORES_FEATURE = Registry.register(
      BuiltInRegistries.FEATURE,
      LighterEnd.of("purple_polypores"),
      new PurplePolypores()
  );
  public static final ResourceKey<ConfiguredFeature<?, ?>> PURPLE_POLYPORES = of(
      "purple_polypores");

  public static final Feature<NoneFeatureConfiguration> STARTER_CHEST_FEATURE = Registry.register(
      BuiltInRegistries.FEATURE,
      LighterEnd.of("starter_chest"),
      new StarterChest()
  );
  public static final ResourceKey<ConfiguredFeature<?, ?>> STARTER_CHEST = of("starter_chest");


  public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
    HolderGetter<ConfiguredFeature<?, ?>> lookup = context.lookup(
        Registries.CONFIGURED_FEATURE
    );

    FeatureUtils.register(
        context,
        END_MOSS_PATCH,
        Feature.SIMPLE_BLOCK,
        new SimpleBlockConfiguration(
            new WeightedStateProvider(
                WeightedList.<BlockState>builder()
                    .add(LighterEndBlocks.CREEPING_MOSS.defaultBlockState(), 10)
                    .add(LighterEndBlocks.UMBRELLA_FERN.defaultBlockState(), 10)
                    .add(LighterEndBlocks.LUMECORN_SEED.defaultBlockState(), 1)
            )
        )
    );
    FeatureUtils.register(
        context,
        END_MOSS_VEGETATION,
        Feature.SIMPLE_BLOCK,
        new SimpleBlockConfiguration(
            new WeightedStateProvider(
                WeightedList.<BlockState>builder()
                    .add(LighterEndBlocks.CREEPING_MOSS.defaultBlockState(), 10)
                    .add(LighterEndBlocks.UMBRELLA_FERN.defaultBlockState(), 10)
            )
        )
    );
    FeatureUtils.register(
        context,
        END_MOSS_PATCH_BONEMEAL,
        Feature.VEGETATION_PATCH,
        new VegetationPatchConfiguration(
            LighterEndTags.END_MOSS_REPLACEABLE,
            BlockStateProvider.simple(LighterEndBlocks.END_MOSS),
            PlacementUtils.inlinePlaced(
                lookup.getOrThrow(END_MOSS_PATCH)
            ),
            CaveSurface.FLOOR,
            ConstantInt.of(1),
            0.0F,
            2,
            0.1F,
            UniformInt.of(0, 1),
            0.25F)
    );

    FeatureUtils.register(
        context,
        SHADOW_MOSS_PATCH,
        Feature.SIMPLE_BLOCK,
        new SimpleBlockConfiguration(
            new WeightedStateProvider(
                WeightedList.<BlockState>builder()
                    .add(LighterEndBlocks.SHADOW_GRASS.defaultBlockState(), 40)
                    .add(LighterEndBlocks.NEEDLEGRASS.defaultBlockState(), 20)
                    .add(
                        LighterEndBlocks.SHADOW_BERRY.defaultBlockState().setValue(ShadowBerry.AGE, 0),
                        20)
                    .add(LighterEndBlocks.MURKWEED.defaultBlockState(), 20)
            )
        )
    );
    FeatureUtils.register(
        context,
        SHADOW_MOSS_VEGETATION,
        Feature.SIMPLE_BLOCK,
        new SimpleBlockConfiguration(
            new WeightedStateProvider(
                WeightedList.<BlockState>builder()
                    .add(LighterEndBlocks.SHADOW_GRASS.defaultBlockState(), 40)
                    .add(LighterEndBlocks.NEEDLEGRASS.defaultBlockState(), 40)
                    .add(
                        LighterEndBlocks.SHADOW_BERRY.defaultBlockState()
                            .setValue(ShadowBerry.AGE, ShadowBerry.MAX_AGE),
                        10
                    ).add(LighterEndBlocks.MURKWEED.defaultBlockState(), 10)
            )
        )
    );
    FeatureUtils.register(
        context,
        SHADOW_MOSS_PATCH_BONEMEAL,
        Feature.VEGETATION_PATCH,
        new VegetationPatchConfiguration(
            LighterEndTags.END_MOSS_REPLACEABLE,
            BlockStateProvider.simple(LighterEndBlocks.END_MOSS),
            PlacementUtils.inlinePlaced(
                lookup.getOrThrow(SHADOW_MOSS_PATCH)
            ),
            CaveSurface.FLOOR,
            ConstantInt.of(1),
            0.0F,
            2,
            0.1F,
            UniformInt.of(0, 1),
            0.25F)
    );

    FeatureUtils.register(context, LUMECORN, LUMECORN_FEATURE);
    FeatureUtils.register(context, TENANEA_TREE, TENANEA_TREE_FEATURE);
    FeatureUtils.register(context, MOTH_NEST, MOTH_NEST_FEATURE);
    FeatureUtils.register(context, UMBRELLA_TREE, UMBRELLA_TREE_FEATURE);
    FeatureUtils.register(context, WATER_PLANTS, UNDERWATER_PLANTS);
    FeatureUtils.register(context, END_LILY, END_LILY_FEATURE);
    FeatureUtils.register(context, END_LOTUS, END_LOTUS_FEATURE);
    FeatureUtils.register(context, LOTUS_LEAF, LOTUS_LEAF_FEATURE);
    FeatureUtils.register(context, UMRBALITH_ARCH, ARCH_FEATURE);
    FeatureUtils.register(context, UMRBALITH_ARCH_THIN, THIN_ARCH_FEATURE);
    FeatureUtils.register(context, GLOWSHROOM, GLOWSHROOM_FEATURE);
    FeatureUtils.register(context, AGAVE, AGAVE_FEATURE);

    FeatureUtils.register(
        context,
        ICE_STAR_COPPER,
        ICE_STAR_FEATURE,
        new IceStar.Config(0, 5, 15, 10, 25)
    );
    FeatureUtils.register(
        context,
        ICE_STAR_COPPER_SMALL,
        ICE_STAR_FEATURE,
        new IceStar.Config(0, 3, 5, 7, 12)
    );
    FeatureUtils.register(
        context,
        ICE_STAR_IRON,
        ICE_STAR_FEATURE,
        new IceStar.Config(1, 5, 15, 10, 25)
    );
    FeatureUtils.register(
        context,
        ICE_STAR_IRON_SMALL,
        ICE_STAR_FEATURE,
        new IceStar.Config(1, 3, 5, 7, 12)
    );
    FeatureUtils.register(
        context,
        ICE_STAR_GOLD,
        ICE_STAR_FEATURE,
        new IceStar.Config(2, 5, 15, 10, 25)
    );
    FeatureUtils.register(
        context,
        ICE_STAR_GOLD_SMALL,
        ICE_STAR_FEATURE,
        new IceStar.Config(2, 3, 5, 7, 12)
    );

    FeatureUtils.register(
        context,
        JADESTONE_BLOBS.get(0),
        BURIED_BLOB,
        new BuriedBlob.Config(
            Blocks.END_STONE.defaultBlockState(),
            LighterEndBlocks.AZURE_JADESTONE.baseBlock.defaultBlockState(),
            UniformInt.of(3, 7),
            6
        )
    );
    FeatureUtils.register(
        context,
        JADESTONE_BLOBS.get(1),
        BURIED_BLOB,
        new BuriedBlob.Config(
            Blocks.END_STONE.defaultBlockState(),
            LighterEndBlocks.SANDY_JADESTONE.baseBlock.defaultBlockState(),
            UniformInt.of(3, 7),
            6
        )
    );
    FeatureUtils.register(
        context,
        JADESTONE_BLOBS.get(2),
        BURIED_BLOB,
        new BuriedBlob.Config(
            Blocks.END_STONE.defaultBlockState(),
            LighterEndBlocks.VIRID_JADESTONE.baseBlock.defaultBlockState(),
            UniformInt.of(3, 7),
            6
        )
    );

    FeatureUtils.register(context, AURORA_CRYSTAL, AURORA_CRYSTAL_FEATURE);

    FeatureUtils.register(
        context,
        END_STONE_REDSTONE_ORE,
        Feature.ORE,
        new OreConfiguration(
            new BlockMatchTest(Blocks.END_STONE),
            LighterEndBlocks.END_STONE_REDSTONE_ORE.defaultBlockState(),
            5
        )
    );
    FeatureUtils.register(
        context,
        END_STONE_QUARTZ_ORE,
        Feature.ORE,
        new OreConfiguration(
            new BlockMatchTest(Blocks.END_STONE),
            LighterEndBlocks.END_STONE_QUARTZ_ORE.defaultBlockState(),
            7
        )
    );

    FeatureUtils.register(
        context,
        UMBRALITH_REDSTONE_ORE,
        Feature.ORE,
        new OreConfiguration(
            new BlockMatchTest(LighterEndBlocks.UMBRALITH.baseBlock),
            LighterEndBlocks.UMBRALITH_REDSTONE_ORE.defaultBlockState(),
            5
        )
    );
    FeatureUtils.register(
        context,
        UMBRALITH_QUARTZ_ORE,
        Feature.ORE,
        new OreConfiguration(
            new BlockMatchTest(LighterEndBlocks.UMBRALITH.baseBlock),
            LighterEndBlocks.UMBRALITH_QUARTZ_ORE.defaultBlockState(),
            7
        )
    );

    FeatureUtils.register(context, SULPHUR_LAKE, SULPHUR_LAKE_FEATURE);
    FeatureUtils.register(context, SULPHUR_CAVE, SULPHUR_CAVE_FEATURE);
    FeatureUtils.register(context, SURFACE_VENT, SURFACE_VENT_FEATURE);
    FeatureUtils.register(context, GEYSER, GEYSER_FEATURE);

    FeatureUtils.register(context, DRAGON_TREE, DRAGON_TREE_FEATURE);
    FeatureUtils.register(
        context,
        PURPLE_POLYPORES,
        PURPLE_POLYPORES_FEATURE,
        new PurplePolypores.Config(3)
    );

    FeatureUtils.register(context, STARTER_CHEST, STARTER_CHEST_FEATURE);
  }

  public static void initialize() {
  }

  public static ResourceKey<ConfiguredFeature<?, ?>> of(String id) {
    return ResourceKey.create(Registries.CONFIGURED_FEATURE, LighterEnd.of(id));
  }
}
