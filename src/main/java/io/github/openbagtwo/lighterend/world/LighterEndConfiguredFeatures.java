package io.github.openbagtwo.lighterend.world;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.blocks.Lumecorn;
import io.github.openbagtwo.lighterend.blocks.SilkMothNest.SilkMothNestFeature;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.tags.LighterEndTags;
import io.github.openbagtwo.lighterend.world.features.EndLake;
import io.github.openbagtwo.lighterend.world.features.trees.TenaneaTree;
import io.github.openbagtwo.lighterend.world.features.trees.UmbrellaTree;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.VerticalSurfaceType;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import net.minecraft.world.gen.feature.VegetationPatchFeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

public class LighterEndConfiguredFeatures {

  public static final RegistryKey<ConfiguredFeature<?, ?>> END_MOSS_PATCH_BONEMEAL
      = of("end_moss_patch_bonemeal");
  public static final RegistryKey<ConfiguredFeature<?, ?>> END_MOSS_VEGETATION
      = of("end_moss_vegetation");

  public static final Feature<DefaultFeatureConfig> END_LAKE_FEATURE = Registry.register(
      Registries.FEATURE,
      LighterEnd.of("end_lake"),
      new EndLake()
  );
  public static final RegistryKey<ConfiguredFeature<?, ?>> END_LAKE = of("end_lake");

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


  public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
    RegistryEntryLookup<ConfiguredFeature<?, ?>> lookup = context.getRegistryLookup(
        RegistryKeys.CONFIGURED_FEATURE
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
                    .add(LighterEndBlocks.LUMECORN_SEED.getDefaultState(), 1)
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
                lookup.getOrThrow(END_MOSS_VEGETATION)
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
        LUMECORN,
        LUMECORN_FEATURE
    );
    ConfiguredFeatures.register(
        context,
        TENANEA_TREE,
        TENANEA_TREE_FEATURE
    );
    ConfiguredFeatures.register(
        context,
        MOTH_NEST,
        MOTH_NEST_FEATURE
    );
    ConfiguredFeatures.register(
        context,
        UMBRELLA_TREE,
        UMBRELLA_TREE_FEATURE
    );
    ConfiguredFeatures.register(
        context,
        END_LAKE,
        END_LAKE_FEATURE
    );


  }

  public static void initialize() {
  }

  public static RegistryKey<ConfiguredFeature<?, ?>> of(String id) {
    return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, LighterEnd.of(id));
  }
}
