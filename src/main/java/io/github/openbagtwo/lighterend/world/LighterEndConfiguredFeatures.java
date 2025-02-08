package io.github.openbagtwo.lighterend.world;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.blocks.Lumecorn;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.tags.LighterEndTags;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.VerticalSurfaceType;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import net.minecraft.world.gen.feature.VegetationPatchFeatureConfig;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

public class LighterEndConfiguredFeatures {

  public static final RegistryKey<ConfiguredFeature<?, ?>> END_MOSS_PATCH_BONEMEAL
      = LighterEndConfiguredFeatures.of("end_moss_patch_bonemeal");
  public static final RegistryKey<ConfiguredFeature<?, ?>> END_MOSS_VEGETATION
      = LighterEndConfiguredFeatures.of("end_moss_vegetation");

  public static final RegistryKey<ConfiguredFeature<?, ?>> LUMECORN
      = LighterEndConfiguredFeatures.of("lumecorn");

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
                DataPool.<BlockState>builder()
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
                lookup.getOrThrow(END_MOSS_VEGETATION),
                new PlacementModifier[0]
            ),
            VerticalSurfaceType.FLOOR,
            ConstantIntProvider.create(1),
            0.0F,
            2,
            0.1F,
            UniformIntProvider.create(0, 1),
            0.25F)
    );
    ConfiguredFeatures.register(context, LUMECORN, new Lumecorn.LumecornFeature());

  }

  public static RegistryKey<ConfiguredFeature<?, ?>> of(String id) {
    return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(LighterEnd.MOD_ID, id));
  }
}
