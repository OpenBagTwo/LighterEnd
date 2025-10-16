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
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.VerticalSurfaceType;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import net.minecraft.world.gen.feature.VegetationPatchFeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

public class LighterEndConfiguredFeatures {

  public static final RegistryKey<ConfiguredFeature<?, ?>> END_MOSS_PATCH
      = of("end_moss_patch");
  public static final RegistryKey<ConfiguredFeature<?, ?>> END_MOSS_PATCH_BONEMEAL
      = of("end_moss_patch_bonemeal");
  public static final RegistryKey<ConfiguredFeature<?, ?>> END_MOSS_VEGETATION
      = of("end_moss_vegetation");
  public static final RegistryKey<ConfiguredFeature<?, ?>> SHADOW_MOSS_PATCH
      = of("end_moss_patch_shadow");
  public static final RegistryKey<ConfiguredFeature<?, ?>> SHADOW_MOSS_PATCH_BONEMEAL
      = of("end_moss_patch_bonemeal_shadow");
  public static final RegistryKey<ConfiguredFeature<?, ?>> SHADOW_MOSS_VEGETATION
      = of("end_moss_vegetation_shadow");


  public static final Feature<DefaultFeatureConfig> LUMECORN_FEATURE = Registry.register(
      Registries.FEATURE,
      LighterEnd.of("lumecorn"),
      new Lumecorn.LumecornFeature());
  public static final RegistryKey<ConfiguredFeature<?, ?>> LUMECORN = of(
      "lumecorn");

  public static final Feature<DefaultFeatureConfig> TENANEA_TREE_FEATURE = Registry.register(
      Registries.FEATURE,
      LighterEnd.of("tenanea_tree"),
      new TenaneaTree());
  public static final RegistryKey<ConfiguredFeature<?, ?>> TENANEA_TREE = of(
      "tenanea_tree");

  public static final Feature<DefaultFeatureConfig> UMBRELLA_TREE_FEATURE = Registry.register(
      Registries.FEATURE,
      LighterEnd.of("umbrella_tree"),
      new UmbrellaTree());
  public static final RegistryKey<ConfiguredFeature<?, ?>> UMBRELLA_TREE = of(
      "umbrella_tree");

  public static final Feature<DefaultFeatureConfig> MOTH_NEST_FEATURE = Registry.register(
      Registries.FEATURE,
      LighterEnd.of("silk_moth_nest"),
      new SilkMothNestFeature()
  );
  public static final RegistryKey<ConfiguredFeature<?, ?>> MOTH_NEST = of("silk_moth_nest");

  public static final Feature<DefaultFeatureConfig> UNDERWATER_PLANTS = Registry.register(
      Registries.FEATURE,
      LighterEnd.of("aquatic_end_plants"),
      new UnderwaterPlants()
  );
  public static final RegistryKey<ConfiguredFeature<?, ?>> WATER_PLANTS = of("aquatic_end_plants");

  public static final Feature<DefaultFeatureConfig> END_LILY_FEATURE = Registry.register(
      Registries.FEATURE,
      LighterEnd.of("end_lily"),
      new EndLilyFeature());
  public static final RegistryKey<ConfiguredFeature<?, ?>> END_LILY = of(
      "end_lily");

  public static final Feature<DefaultFeatureConfig> END_LOTUS_FEATURE = Registry.register(
      Registries.FEATURE,
      LighterEnd.of("end_lotus"),
      new EndLotusFeature());
  public static final RegistryKey<ConfiguredFeature<?, ?>> END_LOTUS = of(
      "end_lotus");

  public static final Feature<DefaultFeatureConfig> LOTUS_LEAF_FEATURE = Registry.register(
      Registries.FEATURE,
      LighterEnd.of("end_lotus_leaf"),
      new LotusLeaf());
  public static final RegistryKey<ConfiguredFeature<?, ?>> LOTUS_LEAF = of(
      "end_lotus_leaf");

  public static final Feature<DefaultFeatureConfig> ARCH_FEATURE = Registry.register(
      Registries.FEATURE,
      LighterEnd.of("umbralith_arch"),
      new UmbralithArch());
  public static final RegistryKey<ConfiguredFeature<?, ?>> UMRBALITH_ARCH = of(
      "umbralith_arch");

  public static final Feature<DefaultFeatureConfig> THIN_ARCH_FEATURE = Registry.register(
      Registries.FEATURE,
      LighterEnd.of("umbralith_arch_thin"),
      new UmbralithArch.Thin());
  public static final RegistryKey<ConfiguredFeature<?, ?>> UMRBALITH_ARCH_THIN = of(
      "umbralith_arch_thin");

  public static final Feature<DefaultFeatureConfig> GLOWSHROOM_FEATURE = Registry.register(
      Registries.FEATURE,
      LighterEnd.of("glowshroom"),
      new Glowshroom());
  public static final RegistryKey<ConfiguredFeature<?, ?>> GLOWSHROOM = of("glowshroom");

  public static final Feature<DefaultFeatureConfig> AGAVE_FEATURE = Registry.register(
      Registries.FEATURE,
      LighterEnd.of("agave"),
      new AgaveFeature()
  );
  public static final RegistryKey<ConfiguredFeature<?, ?>> AGAVE = of("agave");

  public static final Feature<IceStar.Config> ICE_STAR_FEATURE = Registry.register(
      Registries.FEATURE,
      LighterEnd.of("ice_star"),
      new IceStar()
  );
  public static final RegistryKey<ConfiguredFeature<?, ?>> ICE_STAR_COPPER = of(
      "ice_star_copper");
  public static final RegistryKey<ConfiguredFeature<?, ?>> ICE_STAR_COPPER_SMALL = of(
      "ice_star_copper_small");
  public static final RegistryKey<ConfiguredFeature<?, ?>> ICE_STAR_IRON = of(
      "ice_star_iron");
  public static final RegistryKey<ConfiguredFeature<?, ?>> ICE_STAR_IRON_SMALL = of(
      "ice_star_iron_small");
  public static final RegistryKey<ConfiguredFeature<?, ?>> ICE_STAR_GOLD = of(
      "ice_star_gold");
  public static final RegistryKey<ConfiguredFeature<?, ?>> ICE_STAR_GOLD_SMALL = of(
      "ice_star_gold_small");

  public static final Feature<BuriedBlob.Config> BURIED_BLOB = Registry.register(
      Registries.FEATURE,
      LighterEnd.of("buried_blob"),
      new BuriedBlob()
  );

  public static final List<RegistryKey<ConfiguredFeature<?, ?>>> JADESTONE_BLOBS = List.of(
      of("jadestone_blob_azure"),
      of("jadestone_blob_sandy"),
      of("jadestone_blob_virid")
  );

  public static final Feature<DefaultFeatureConfig> AURORA_CRYSTAL_FEATURE = Registry.register(
      Registries.FEATURE,
      LighterEnd.of("aurora_crystal_formation"),
      new AuroraCrystalFormation()
  );

  public static final RegistryKey<ConfiguredFeature<?, ?>> AURORA_CRYSTAL = of(
      "aurora_crystal_formation");

  public static final RegistryKey<ConfiguredFeature<?, ?>> END_STONE_REDSTONE_ORE = of(
      "end_stone_redstone_ore");
  public static final RegistryKey<ConfiguredFeature<?, ?>> END_STONE_QUARTZ_ORE = of(
      "end_stone_quartz_ore");
  public static final RegistryKey<ConfiguredFeature<?, ?>> UMBRALITH_REDSTONE_ORE = of(
      "umbralith_redstone_ore");
  public static final RegistryKey<ConfiguredFeature<?, ?>> UMBRALITH_QUARTZ_ORE = of(
      "umbralith_quartz_ore");

  public static final Feature<DefaultFeatureConfig> SULPHUR_LAKE_FEATURE = Registry.register(
      Registries.FEATURE,
      LighterEnd.of("sulphur_lake"),
      new SulphurLake()
  );
  public static final RegistryKey<ConfiguredFeature<?, ?>> SULPHUR_LAKE = of("sulphur_lake");

  public static final Feature<DefaultFeatureConfig> SULPHUR_CAVE_FEATURE = Registry.register(
      Registries.FEATURE,
      LighterEnd.of("sulphur_cave"),
      new SulphurCave()
  );
  public static final RegistryKey<ConfiguredFeature<?, ?>> SULPHUR_CAVE = of("sulphur_cave");

  public static final Feature<DefaultFeatureConfig> SURFACE_VENT_FEATURE = Registry.register(
      Registries.FEATURE,
      LighterEnd.of("surface_vent"),
      new SurfaceVent()
  );
  public static final RegistryKey<ConfiguredFeature<?, ?>> SURFACE_VENT = of("surface_vent");

  public static final Feature<DefaultFeatureConfig> GEYSER_FEATURE = Registry.register(
      Registries.FEATURE,
      LighterEnd.of("geyser"),
      new Geyser()
  );
  public static final RegistryKey<ConfiguredFeature<?, ?>> GEYSER = of("geyser");

  public static final Feature<DefaultFeatureConfig> DRAGON_TREE_FEATURE = Registry.register(
      Registries.FEATURE,
      LighterEnd.of("dragon_tree"),
      new DragonTree());
  public static final RegistryKey<ConfiguredFeature<?, ?>> DRAGON_TREE = of(
      "dragon_tree");

  public static final Feature<PurplePolypores.Config> PURPLE_POLYPORES_FEATURE = Registry.register(
      Registries.FEATURE,
      LighterEnd.of("purple_polypores"),
      new PurplePolypores()
  );
  public static final RegistryKey<ConfiguredFeature<?, ?>> PURPLE_POLYPORES = of(
      "purple_polypores");


  public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
    RegistryEntryLookup<ConfiguredFeature<?, ?>> lookup = context.getRegistryLookup(
        RegistryKeys.CONFIGURED_FEATURE
    );

    ConfiguredFeatures.register(
        context,
        END_MOSS_PATCH,
        Feature.SIMPLE_BLOCK,
        new SimpleBlockFeatureConfig(
            new WeightedBlockStateProvider(
                Pool.<BlockState>builder()
                    .add(LighterEndBlocks.CREEPING_MOSS.getDefaultState(), 10)
                    .add(LighterEndBlocks.UMBRELLA_FERN.getDefaultState(), 10)
                    .add(LighterEndBlocks.LUMECORN_SEED.getDefaultState(), 1)
            )
        )
    );
    ConfiguredFeatures.register(
        context,
        END_MOSS_VEGETATION,
        Feature.SIMPLE_BLOCK,
        new SimpleBlockFeatureConfig(
            new WeightedBlockStateProvider(
                Pool.<BlockState>builder()
                    .add(LighterEndBlocks.CREEPING_MOSS.getDefaultState(), 10)
                    .add(LighterEndBlocks.UMBRELLA_FERN.getDefaultState(), 10)
            )
        )
    );
    ConfiguredFeatures.register(
        context,
        END_MOSS_PATCH_BONEMEAL,
        Feature.VEGETATION_PATCH,
        new VegetationPatchFeatureConfig(
            LighterEndTags.END_MOSS_REPLACEABLE,
            BlockStateProvider.of(LighterEndBlocks.END_MOSS),
            PlacedFeatures.createEntry(
                lookup.getOrThrow(END_MOSS_PATCH)
            ),
            VerticalSurfaceType.FLOOR,
            ConstantIntProvider.create(1),
            0.0F,
            2,
            0.1F,
            UniformIntProvider.create(0, 1),
            0.25F)
    );

    ConfiguredFeatures.register(
        context,
        SHADOW_MOSS_PATCH,
        Feature.SIMPLE_BLOCK,
        new SimpleBlockFeatureConfig(
            new WeightedBlockStateProvider(
                Pool.<BlockState>builder()
                    .add(LighterEndBlocks.SHADOW_GRASS.getDefaultState(), 40)
                    .add(LighterEndBlocks.NEEDLEGRASS.getDefaultState(), 20)
                    .add(
                        LighterEndBlocks.SHADOW_BERRY.getDefaultState().with(ShadowBerry.AGE, 0),
                        20)
                    .add(LighterEndBlocks.MURKWEED.getDefaultState(), 20)
            )
        )
    );
    ConfiguredFeatures.register(
        context,
        SHADOW_MOSS_VEGETATION,
        Feature.SIMPLE_BLOCK,
        new SimpleBlockFeatureConfig(
            new WeightedBlockStateProvider(
                Pool.<BlockState>builder()
                    .add(LighterEndBlocks.SHADOW_GRASS.getDefaultState(), 40)
                    .add(LighterEndBlocks.NEEDLEGRASS.getDefaultState(), 40)
                    .add(
                        LighterEndBlocks.SHADOW_BERRY.getDefaultState()
                            .with(ShadowBerry.AGE, ShadowBerry.MAX_AGE),
                        10
                    ).add(LighterEndBlocks.MURKWEED.getDefaultState(), 10)
            )
        )
    );
    ConfiguredFeatures.register(
        context,
        SHADOW_MOSS_PATCH_BONEMEAL,
        Feature.VEGETATION_PATCH,
        new VegetationPatchFeatureConfig(
            LighterEndTags.END_MOSS_REPLACEABLE,
            BlockStateProvider.of(LighterEndBlocks.END_MOSS),
            PlacedFeatures.createEntry(
                lookup.getOrThrow(SHADOW_MOSS_PATCH)
            ),
            VerticalSurfaceType.FLOOR,
            ConstantIntProvider.create(1),
            0.0F,
            2,
            0.1F,
            UniformIntProvider.create(0, 1),
            0.25F)
    );

    ConfiguredFeatures.register(context, LUMECORN, LUMECORN_FEATURE);
    ConfiguredFeatures.register(context, TENANEA_TREE, TENANEA_TREE_FEATURE);
    ConfiguredFeatures.register(context, MOTH_NEST, MOTH_NEST_FEATURE);
    ConfiguredFeatures.register(context, UMBRELLA_TREE, UMBRELLA_TREE_FEATURE);
    ConfiguredFeatures.register(context, WATER_PLANTS, UNDERWATER_PLANTS);
    ConfiguredFeatures.register(context, END_LILY, END_LILY_FEATURE);
    ConfiguredFeatures.register(context, END_LOTUS, END_LOTUS_FEATURE);
    ConfiguredFeatures.register(context, LOTUS_LEAF, LOTUS_LEAF_FEATURE);
    ConfiguredFeatures.register(context, UMRBALITH_ARCH, ARCH_FEATURE);
    ConfiguredFeatures.register(context, UMRBALITH_ARCH_THIN, THIN_ARCH_FEATURE);
    ConfiguredFeatures.register(context, GLOWSHROOM, GLOWSHROOM_FEATURE);
    ConfiguredFeatures.register(context, AGAVE, AGAVE_FEATURE);

    ConfiguredFeatures.register(
        context,
        ICE_STAR_COPPER,
        ICE_STAR_FEATURE,
        new IceStar.Config(0, 5, 15, 10, 25)
    );
    ConfiguredFeatures.register(
        context,
        ICE_STAR_COPPER_SMALL,
        ICE_STAR_FEATURE,
        new IceStar.Config(0, 3, 5, 7, 12)
    );
    ConfiguredFeatures.register(
        context,
        ICE_STAR_IRON,
        ICE_STAR_FEATURE,
        new IceStar.Config(1, 5, 15, 10, 25)
    );
    ConfiguredFeatures.register(
        context,
        ICE_STAR_IRON_SMALL,
        ICE_STAR_FEATURE,
        new IceStar.Config(1, 3, 5, 7, 12)
    );
    ConfiguredFeatures.register(
        context,
        ICE_STAR_GOLD,
        ICE_STAR_FEATURE,
        new IceStar.Config(2, 5, 15, 10, 25)
    );
    ConfiguredFeatures.register(
        context,
        ICE_STAR_GOLD_SMALL,
        ICE_STAR_FEATURE,
        new IceStar.Config(2, 3, 5, 7, 12)
    );

    ConfiguredFeatures.register(
        context,
        JADESTONE_BLOBS.get(0),
        BURIED_BLOB,
        new BuriedBlob.Config(
            Blocks.END_STONE.getDefaultState(),
            LighterEndBlocks.AZURE_JADESTONE.baseBlock.getDefaultState(),
            UniformIntProvider.create(3, 7),
            6
        )
    );
    ConfiguredFeatures.register(
        context,
        JADESTONE_BLOBS.get(1),
        BURIED_BLOB,
        new BuriedBlob.Config(
            Blocks.END_STONE.getDefaultState(),
            LighterEndBlocks.SANDY_JADESTONE.baseBlock.getDefaultState(),
            UniformIntProvider.create(3, 7),
            6
        )
    );
    ConfiguredFeatures.register(
        context,
        JADESTONE_BLOBS.get(2),
        BURIED_BLOB,
        new BuriedBlob.Config(
            Blocks.END_STONE.getDefaultState(),
            LighterEndBlocks.VIRID_JADESTONE.baseBlock.getDefaultState(),
            UniformIntProvider.create(3, 7),
            6
        )
    );

    ConfiguredFeatures.register(context, AURORA_CRYSTAL, AURORA_CRYSTAL_FEATURE);

    ConfiguredFeatures.register(
        context,
        END_STONE_REDSTONE_ORE,
        Feature.ORE,
        new OreFeatureConfig(
            new BlockMatchRuleTest(Blocks.END_STONE),
            LighterEndBlocks.END_STONE_REDSTONE_ORE.getDefaultState(),
            5
        )
    );
    ConfiguredFeatures.register(
        context,
        END_STONE_QUARTZ_ORE,
        Feature.ORE,
        new OreFeatureConfig(
            new BlockMatchRuleTest(Blocks.END_STONE),
            LighterEndBlocks.END_STONE_QUARTZ_ORE.getDefaultState(),
            7
        )
    );

    ConfiguredFeatures.register(
        context,
        UMBRALITH_REDSTONE_ORE,
        Feature.ORE,
        new OreFeatureConfig(
            new BlockMatchRuleTest(LighterEndBlocks.UMBRALITH.baseBlock),
            LighterEndBlocks.UMBRALITH_REDSTONE_ORE.getDefaultState(),
            5
        )
    );
    ConfiguredFeatures.register(
        context,
        UMBRALITH_QUARTZ_ORE,
        Feature.ORE,
        new OreFeatureConfig(
            new BlockMatchRuleTest(LighterEndBlocks.UMBRALITH.baseBlock),
            LighterEndBlocks.UMBRALITH_QUARTZ_ORE.getDefaultState(),
            7
        )
    );

    ConfiguredFeatures.register(context, SULPHUR_LAKE, SULPHUR_LAKE_FEATURE);
    ConfiguredFeatures.register(context, SULPHUR_CAVE, SULPHUR_CAVE_FEATURE);
    ConfiguredFeatures.register(context, SURFACE_VENT, SURFACE_VENT_FEATURE);
    ConfiguredFeatures.register(context, GEYSER, GEYSER_FEATURE);

    ConfiguredFeatures.register(context, DRAGON_TREE, DRAGON_TREE_FEATURE);
    ConfiguredFeatures.register(
        context,
        PURPLE_POLYPORES,
        PURPLE_POLYPORES_FEATURE,
        new PurplePolypores.Config(3)
    );
  }

  public static void initialize() {
  }

  public static RegistryKey<ConfiguredFeature<?, ?>> of(String id) {
    return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, LighterEnd.of(id));
  }
}
