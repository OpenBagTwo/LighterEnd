package io.github.openbagtwo.lighterend.world;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.blocks.Agave;
import io.github.openbagtwo.lighterend.blocks.EndLily;
import io.github.openbagtwo.lighterend.blocks.EndLotus;
import io.github.openbagtwo.lighterend.blocks.Lumecorn;
import io.github.openbagtwo.lighterend.blocks.ShadowBerry;
import io.github.openbagtwo.lighterend.blocks.SilkMothNest;
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
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.VegetationPatchFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;

public class LighterEndConfiguredFeatures {

  public static final ResourceKey<Feature> END_MOSS_PATCH = of("end_moss_patch");
  public static final ResourceKey<Feature> END_MOSS_PATCH_BONEMEAL = of("end_moss_patch_bonemeal");
  public static final ResourceKey<Feature> END_MOSS_VEGETATION = of("end_moss_vegetation");
  public static final ResourceKey<Feature> SHADOW_MOSS_PATCH = of("end_moss_patch_shadow");
  public static final ResourceKey<Feature> SHADOW_MOSS_PATCH_BONEMEAL = of(
      "end_moss_patch_bonemeal_shadow"
  );
  public static final ResourceKey<Feature> SHADOW_MOSS_VEGETATION = of(
      "end_moss_vegetation_shadow"
  );
  public static final ResourceKey<Feature> LUMECORN = of("lumecorn");
  public static final ResourceKey<Feature> TENANEA_TREE = of("tenanea_tree");
  public static final ResourceKey<Feature> MOTH_NEST = of("silk_moth_nest");
  public static final ResourceKey<Feature> UMBRELLA_TREE = of("umbrella_tree");
  public static final ResourceKey<Feature> WATER_PLANTS = of("aquatic_end_plants");
  public static final ResourceKey<Feature> END_LILY = of("end_lily");
  public static final ResourceKey<Feature> END_LOTUS = of("end_lotus");
  public static final ResourceKey<Feature> LOTUS_LEAF = of("end_lotus_leaf");
  public static final ResourceKey<Feature> UMRBALITH_ARCH = of("umbralith_arch");
  public static final ResourceKey<Feature> UMRBALITH_ARCH_THIN = of("umbralith_arch_thin");
  public static final ResourceKey<Feature> GLOWSHROOM = of("glowshroom");
  public static final ResourceKey<Feature> AGAVE = of("agave");
  public static final ResourceKey<Feature> ICE_STAR_COPPER = of("ice_star_copper");
  public static final ResourceKey<Feature> ICE_STAR_COPPER_SMALL = of("ice_star_copper_small");
  public static final ResourceKey<Feature> ICE_STAR_IRON = of("ice_star_iron");
  public static final ResourceKey<Feature> ICE_STAR_IRON_SMALL = of("ice_star_iron_small");
  public static final ResourceKey<Feature> ICE_STAR_GOLD = of("ice_star_gold");
  public static final ResourceKey<Feature> ICE_STAR_GOLD_SMALL = of("ice_star_gold_small");
  public static final List<ResourceKey<Feature>> JADESTONE_BLOBS = List.of(
      of("jadestone_blob_azure"),
      of("jadestone_blob_sandy"),
      of("jadestone_blob_virid")
  );
  public static final ResourceKey<Feature> AURORA_CRYSTAL = of(
      "aurora_crystal_formation");
  public static final ResourceKey<Feature> END_STONE_REDSTONE_ORE = of(
      "end_stone_redstone_ore");
  public static final ResourceKey<Feature> END_STONE_QUARTZ_ORE = of(
      "end_stone_quartz_ore");
  public static final ResourceKey<Feature> UMBRALITH_REDSTONE_ORE = of(
      "umbralith_redstone_ore");
  public static final ResourceKey<Feature> UMBRALITH_QUARTZ_ORE = of(
      "umbralith_quartz_ore");
  public static final ResourceKey<Feature> SULPHUR_LAKE = of("sulphur_lake");
  public static final ResourceKey<Feature> SULPHUR_CAVE = of("sulphur_cave");
  public static final ResourceKey<Feature> SURFACE_VENT = of("surface_vent");
  public static final ResourceKey<Feature> GEYSER = of("geyser");
  public static final ResourceKey<Feature> DRAGON_TREE = of("dragon_tree");
  public static final ResourceKey<Feature> PURPLE_POLYPORES = of("purple_polypores");
  public static final ResourceKey<Feature> STARTER_CHEST = of("starter_chest");

  public static void bootstrap(BootstrapContext<Feature> context) {
    HolderGetter<Feature> lookup = context.lookup(
        Registries.FEATURE
    );
    HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);

    context.register(
        END_MOSS_PATCH,
        new SimpleBlockFeature(
            new WeightedStateProvider(
                WeightedList.<BlockState>builder()
                    .add(LighterEndBlocks.CREEPING_MOSS.defaultBlockState(), 10)
                    .add(LighterEndBlocks.UMBRELLA_FERN.defaultBlockState(), 10)
                    .add(LighterEndBlocks.LUMECORN_SEED.defaultBlockState(), 1)
            )
        )
    );
    context.register(
        END_MOSS_VEGETATION,
        new SimpleBlockFeature(
            new WeightedStateProvider(
                WeightedList.<BlockState>builder()
                    .add(LighterEndBlocks.CREEPING_MOSS.defaultBlockState(), 10)
                    .add(LighterEndBlocks.UMBRELLA_FERN.defaultBlockState(), 10)
            )
        )
    );
    context.register(
        END_MOSS_PATCH_BONEMEAL,
        new VegetationPatchFeature(
            blocks.getOrThrow(LighterEndTags.END_MOSS_REPLACEABLE),
            BlockStateProvider.holderOf(LighterEndBlocks.END_MOSS),
            PlacementUtils.inlinePlaced(lookup.getOrThrow(END_MOSS_PATCH)),
            CaveSurface.FLOOR,
            ConstantInt.of(1),
            0.0F,
            2,
            0.1F,
            UniformInt.of(0, 1),
            0.25F
        )
    );
    context.register(
        SHADOW_MOSS_PATCH,
        new SimpleBlockFeature(
            new WeightedStateProvider(
                WeightedList.<BlockState>builder()
                    .add(LighterEndBlocks.SHADOW_GRASS.defaultBlockState(), 40)
                    .add(LighterEndBlocks.NEEDLEGRASS.defaultBlockState(), 20)
                    .add(
                        LighterEndBlocks.SHADOW_BERRY_SEEDS.defaultBlockState()
                            .setValue(ShadowBerry.AGE, 0),
                        20)
                    .add(LighterEndBlocks.MURKWEED.defaultBlockState(), 20)
            )
        )
    );
    context.register(
        SHADOW_MOSS_VEGETATION,
        new SimpleBlockFeature(
            new WeightedStateProvider(
                WeightedList.<BlockState>builder()
                    .add(LighterEndBlocks.SHADOW_GRASS.defaultBlockState(), 40)
                    .add(LighterEndBlocks.NEEDLEGRASS.defaultBlockState(), 40)
                    .add(
                        LighterEndBlocks.SHADOW_BERRY_SEEDS.defaultBlockState()
                            .setValue(ShadowBerry.AGE, ShadowBerry.MAX_AGE),
                        10
                    ).add(LighterEndBlocks.MURKWEED.defaultBlockState(), 10)
            )
        )
    );
    context.register(
        SHADOW_MOSS_PATCH_BONEMEAL,
        new VegetationPatchFeature(
            blocks.getOrThrow(LighterEndTags.END_MOSS_REPLACEABLE),
            BlockStateProvider.holderOf(LighterEndBlocks.END_MOSS),
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

    context.register(LUMECORN, new Lumecorn.LumecornFeature());
    context.register(TENANEA_TREE, new TenaneaTree());
    context.register(MOTH_NEST, new SilkMothNest.SilkMothNestFeature());
    context.register(UMBRELLA_TREE, new UmbrellaTree());
    context.register(WATER_PLANTS, new UnderwaterPlants());
    context.register(END_LILY, new EndLily.EndLilyFeature());
    context.register(END_LOTUS, new EndLotus.EndLotusFeature());
    context.register(LOTUS_LEAF, new LotusLeaf());
    context.register(UMRBALITH_ARCH, new UmbralithArch());
    context.register(UMRBALITH_ARCH_THIN, new UmbralithArch.Thin());
    context.register(GLOWSHROOM, new Glowshroom());
    context.register(AGAVE, new Agave.AgaveFeature());

    context.register(ICE_STAR_COPPER, new IceStar(0, 5, 15, 10, 25));
    context.register(ICE_STAR_COPPER_SMALL, new IceStar(0, 3, 5, 7, 12));
    context.register(ICE_STAR_IRON, new IceStar(1, 5, 15, 10, 25));
    context.register(ICE_STAR_IRON_SMALL, new IceStar(1, 3, 5, 7, 12));
    context.register(ICE_STAR_GOLD, new IceStar(2, 5, 15, 10, 25));
    context.register(ICE_STAR_GOLD_SMALL, new IceStar(2, 3, 5, 7, 12));

    context.register(JADESTONE_BLOBS.get(0), new BuriedBlob(
            Blocks.END_STONE.defaultBlockState(),
            LighterEndBlocks.AZURE_JADESTONE.baseBlock.defaultBlockState(),
            UniformInt.of(3, 7),
            6
        )
    );
    context.register(JADESTONE_BLOBS.get(1), new BuriedBlob(
            Blocks.END_STONE.defaultBlockState(),
            LighterEndBlocks.SANDY_JADESTONE.baseBlock.defaultBlockState(),
            UniformInt.of(3, 7),
            6
        )
    );
    context.register(JADESTONE_BLOBS.get(2), new BuriedBlob(
            Blocks.END_STONE.defaultBlockState(),
            LighterEndBlocks.VIRID_JADESTONE.baseBlock.defaultBlockState(),
            UniformInt.of(3, 7),
            6
        )
    );

    context.register(AURORA_CRYSTAL, new AuroraCrystalFormation());

    context.register(
        END_STONE_REDSTONE_ORE,
        new OreFeature(
            new BlockMatchTest(Blocks.END_STONE),
            LighterEndBlocks.END_STONE_REDSTONE_ORE.defaultBlockState(),
            5
        )
    );
    context.register(
        END_STONE_QUARTZ_ORE,
        new OreFeature(
            new BlockMatchTest(Blocks.END_STONE),
            LighterEndBlocks.END_STONE_QUARTZ_ORE.defaultBlockState(),
            7
        )
    );
    context.register(
        UMBRALITH_REDSTONE_ORE,
        new OreFeature(
            new BlockMatchTest(LighterEndBlocks.UMBRALITH.baseBlock),
            LighterEndBlocks.END_STONE_REDSTONE_ORE.defaultBlockState(),
            5
        )
    );
    context.register(
        UMBRALITH_QUARTZ_ORE,
        new OreFeature(
            new BlockMatchTest(LighterEndBlocks.UMBRALITH.baseBlock),
            LighterEndBlocks.END_STONE_QUARTZ_ORE.defaultBlockState(),
            7
        )
    );

    context.register(SULPHUR_LAKE, new SulphurLake());
    context.register(SULPHUR_CAVE, new SulphurCave());
    context.register(SURFACE_VENT, new SurfaceVent());
    context.register(GEYSER, new Geyser());

    context.register(DRAGON_TREE, new DragonTree());
    context.register(PURPLE_POLYPORES, new PurplePolypores(3));

    context.register(STARTER_CHEST, new StarterChest());
  }

  public static void initialize() {
  }

  public static ResourceKey<Feature> of(String id) {
    return ResourceKey.create(Registries.FEATURE, LighterEnd.of(id));
  }
}
