package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.misc.StatusEffects;
import io.github.openbagtwo.lighterend.registries.LighterEndBiomes;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndEquipment;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRequirements.CriterionMerger;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.EffectsChangedCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.ItemCriterion;
import net.minecraft.advancement.criterion.PlayerInteractedWithEntityCriterion;
import net.minecraft.advancement.criterion.RecipeCraftedCriterion;
import net.minecraft.advancement.criterion.TickCriterion;
import net.minecraft.advancement.criterion.UsedTotemCriterion;
import net.minecraft.item.Items;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.predicate.entity.EntityEffectPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class AdvancementProvider extends FabricAdvancementProvider {

  protected AdvancementProvider(
      FabricDataOutput output,
      CompletableFuture<WrapperLookup> registryLookup
  ) {
    super(output, registryLookup);
  }

  @Override
  public void generateAdvancement(
      WrapperLookup lookup,
      Consumer<AdvancementEntry> consumer
  ) {
    AdvancementEntry root = Advancement.Builder.create().display(
        LighterEndBlocks.AURORA_CRYSTAL.asItem(),
        title("root"),
        description("root"),
        LighterEnd.of("block/ender_block"),
        AdvancementFrame.CHALLENGE,
        false,
        false,
        false
    ).criterion(
        LighterEnd.MOD_ID + "_loaded",
        TickCriterion.Conditions.createLocation(LocationPredicate.Builder.create())
    ).build(consumer, LighterEnd.MOD_ID + "/root");

    // end_lake was created by hand
    AdvancementEntry end_lake = new AdvancementEntry(Identifier.of(LighterEnd.MOD_ID + "/end_lake"),
        null);

    AdvancementEntry craft_spectral_arrow = Advancement.Builder.create().parent(end_lake).display(
        LighterEndItems.GLOW_BARB,
        title("craft_spectral_arrow"),
        description("craft_spectral_arrow"),
        null,
        AdvancementFrame.GOAL,
        true,
        true,
        false
    ).criterion(
        "craft_spectral_arrows",
        RecipeCraftedCriterion.Conditions.create(
            RegistryKey.of(RegistryKeys.RECIPE, LighterEnd.of("combat/spectral_arrow"))
        )
    ).build(consumer, LighterEnd.MOD_ID + "/craft_spectral_arrows");

    AdvancementEntry tether_totem = Advancement.Builder.create().parent(root).display(
        LighterEndBlocks.OBELISK.asItem(),
        title("use_obelisk"),
        description("use_obelisk"),
        null,
        AdvancementFrame.TASK,
        true,
        true,
        false
    ).criterion(
        "use_obelisk",
        ItemCriterion.Conditions.createItemUsedOnBlock(
            LocationPredicate.Builder.create().block(
                BlockPredicate.Builder.create().blocks(
                    lookup.getOrThrow(RegistryKeys.BLOCK),
                    LighterEndBlocks.OBELISK
                )
            ),
            ItemPredicate.Builder.create().items(
                lookup.getOrThrow(RegistryKeys.ITEM),
                LighterEndItems.TOTEM_OF_TELEPORTATION
            )
        )
    ).build(consumer, LighterEnd.MOD_ID + "/use_obelisk");

    AdvancementEntry use_totem = Advancement.Builder.create().parent(tether_totem).display(
        LighterEndItems.TOTEM_OF_TELEPORTATION,
        title("use_totem"),
        description("use_totem"),
        null,
        AdvancementFrame.GOAL,
        true,
        true,
        false
    ).criterion(
        "use_totem",
        UsedTotemCriterion.Conditions.create(
            lookup.getOrThrow(RegistryKeys.ITEM),
            LighterEndItems.TOTEM_OF_TELEPORTATION
        )
    ).build(consumer, LighterEnd.MOD_ID + "/use_totem");

    AdvancementEntry acquire_claw = Advancement.Builder.create().parent(root).display(
        LighterEndItems.CRAB_CLAW,
        title("acquire_claw"),
        description("acquire_claw"),
        null,
        AdvancementFrame.TASK,
        true,
        true,
        false
    ).criterion(
        "acquire_claw", InventoryChangedCriterion.Conditions.items(LighterEndItems.CRAB_CLAW)
    ).build(consumer, LighterEnd.MOD_ID + "/acquire_claw");

    AdvancementEntry shear_mooshroom = Advancement.Builder.create().parent(acquire_claw).display(
        Items.SHEARS,
        title("shear_mooshroom"),
        description("shear_mooshroom"),
        null,
        AdvancementFrame.TASK,
        true,
        true,
        false
    ).criterion(
        "shear_mooshroom",
        PlayerInteractedWithEntityCriterion.Conditions.create(
            ItemPredicate.Builder.create()
                .items(lookup.getOrThrow(RegistryKeys.ITEM), Items.SHEARS),
            Optional.of(
                EntityPredicate.contextPredicateFromEntityPredicate(
                    EntityPredicate.Builder.create()
                        .type(
                            lookup.getOrThrow(RegistryKeys.ENTITY_TYPE),
                            LighterEndMobs.MOOSHROOM.mob
                        )
                )
            )
        )
    ).build(consumer, LighterEnd.MOD_ID + "/shear_mooshroom");

    AdvancementEntry wear_fur = Advancement.Builder.create().parent(acquire_claw).display(
            LighterEndBlocks.AGAVE_FUR.asItem(),
            title("wear_fur"),
            description("wear_fur"),
            null,
            AdvancementFrame.TASK,
            true,
            true,
            false
        ).criteriaMerger(CriterionMerger.OR)
        .criterion(
            "wear_agave_fur", InventoryChangedCriterion.Conditions.items(LighterEndItems.AGAVE_FUR)
        ).criterion(
            "wear_glowshroom_fur",
            InventoryChangedCriterion.Conditions.items(LighterEndItems.GLOWSHROOM_FUR)
        ).build(consumer, LighterEnd.MOD_ID + "/wear_fur");

    AdvancementEntry drink_end_veil_potion = Advancement.Builder.create().parent(wear_fur).display(
        Items.PLAYER_HEAD,
        title("drink_end_veil_potion"),
        description("drink_end_veil_potion"),
        null,
        AdvancementFrame.GOAL,
        true,
        true,
        false
    ).criterion(
        "drink_end_veil_potion",
        EffectsChangedCriterion.Conditions.create(
            EntityEffectPredicate.Builder.create().addEffect(StatusEffects.END_VEIL)
        )
    ).build(consumer, LighterEnd.MOD_ID + "/drink_end_veil_potion");

    AdvancementEntry acquire_silk = Advancement.Builder.create().parent(acquire_claw).display(
            LighterEndItems.SILK,
            title("acquire_silk"),
            description("acquire_silk"),
            null,
            AdvancementFrame.TASK,
            true,
            true,
            false
        ).criteriaMerger(CriterionMerger.OR)
        .criterion(
            "acquire_silk", InventoryChangedCriterion.Conditions.items(LighterEndItems.SILK)
        ).criterion(
            "acquire_silk_matrix",
            InventoryChangedCriterion.Conditions.items(LighterEndItems.SILK_MATRIX)
        ).build(consumer, LighterEnd.MOD_ID + "/acquire_silk");

    AdvancementEntry acquire_silk_elytra = Advancement.Builder.create().parent(acquire_silk)
        .display(
            LighterEndEquipment.SILK_ELYTRA,
            title("acquire_silk_elytra"),
            description("acquire_silk_elytra"),
            null,
            AdvancementFrame.CHALLENGE,
            true,
            true,
            false
        ).criterion(
            "acquire_silk_elytra",
            InventoryChangedCriterion.Conditions.items(LighterEndEquipment.SILK_ELYTRA)
        ).rewards(AdvancementRewards.Builder.experience(50))
        .build(consumer, LighterEnd.MOD_ID + "/acquire_silk_elytra");

    AdvancementEntry acquire_end_cream = Advancement.Builder.create().parent(root).display(
        LighterEndItems.END_CREAM,
        title("acquire_end_cream"),
        description("acquire_end_cream"),
        null,
        AdvancementFrame.TASK,
        true,
        true,
        false
    ).criterion(
        "acquire_end_cream", InventoryChangedCriterion.Conditions.items(LighterEndItems.END_CREAM)
    ).build(consumer, LighterEnd.MOD_ID + "/acquire_end_cream");

    Advancement.Builder allBiomesBuilder = Advancement.Builder.create().parent(end_lake).display(
            LighterEndBlocks.END_MOSS.asItem(),
            title("all_the_biomes"),
            description("all_the_biomes"),
            null,
            AdvancementFrame.CHALLENGE,
            false,
            false,
            false
        )
        .criteriaMerger(CriterionMerger.AND);

    int xpReward = 0;
    for (RegistryKey<Biome> biome : List.of(
        LighterEndBiomes.BLOSSOM_FOREST,
        LighterEndBiomes.UMBRELLA_JUNGLE,
        LighterEndBiomes.GLOWING_GRASSLAND,
        LighterEndBiomes.MEGALAKE,
        LighterEndBiomes.UMBRA_VALLEY,
        LighterEndBiomes.FOGGY_MUSHROOMLANDS,
        // starfield intentionally omitted (because it's just a variant on end barrens (read: void)
        LighterEndBiomes.SULPHUR_SPRINGS,
        LighterEndBiomes.SHADOW_FOREST
    )) {
      allBiomesBuilder = allBiomesBuilder.criterion(
          biome.getValue().getPath(),
          TickCriterion.Conditions.createLocation(LocationPredicate.Builder.createBiome(
                  lookup.getOrThrow(RegistryKeys.BIOME).getOrThrow(biome)
              )
          )
      );
      xpReward += 50;
    }

    AdvancementEntry all_the_biomes = allBiomesBuilder.rewards(
        AdvancementRewards.Builder.experience(xpReward)
    ).build(consumer, LighterEnd.MOD_ID + "/all_the_biomes");
  }

  private static MutableText title(String path) {
    return Text.translatable(String.format("advancements.%s.%s.title", LighterEnd.MOD_ID, path));
  }

  private static MutableText description(String path) {
    return Text.translatable(
        String.format("advancements.%s.%s.description", LighterEnd.MOD_ID, path));
  }
}
