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
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements.Strategy;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.BlockPredicate;
import net.minecraft.advancements.criterion.EffectsChangedTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.ItemUsedOnLocationTrigger;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.MobEffectsPredicate;
import net.minecraft.advancements.criterion.PlayerInteractTrigger;
import net.minecraft.advancements.criterion.PlayerTrigger;
import net.minecraft.advancements.criterion.RecipeCraftedTrigger;
import net.minecraft.advancements.criterion.UsedTotemTrigger;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;

public class AdvancementProvider extends FabricAdvancementProvider {

  protected AdvancementProvider(
      FabricPackOutput output,
      CompletableFuture<Provider> registryLookup
  ) {
    super(output, registryLookup);
  }

  @Override
  public void generateAdvancement(
      Provider lookup,
      Consumer<AdvancementHolder> consumer
  ) {
    AdvancementHolder root = Advancement.Builder.advancement().display(
        LighterEndItems.AURORA_CRYSTAL_SHARD,
        title("root"),
        description("root"),
        LighterEnd.of("block/ender_block"),
        AdvancementType.CHALLENGE,
        false,
        false,
        false
    ).addCriterion(
        LighterEnd.MOD_ID + "_loaded",
        PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.location())
    ).save(consumer, LighterEnd.MOD_ID + "/root");

    // end_lake was created by hand
    AdvancementHolder end_lake = new AdvancementHolder(
        Identifier.parse(LighterEnd.MOD_ID + "/end_lake"),
        null);

    AdvancementHolder sulphur_springs = Advancement.Builder.advancement().parent(end_lake).display(
        LighterEndBlocks.HYDROTHERMAL_VENT.asItem(),
        title("sulphur_springs"),
        description("sulphur_springs"),
        null,
        AdvancementType.TASK,
        true,
        true,
        false
    ).addCriterion(
        "found_sulphur_spring",
        PlayerTrigger.TriggerInstance.located(
            LocationPredicate.Builder.inBiome(
                lookup.lookupOrThrow(Registries.BIOME).getOrThrow(LighterEndBiomes.SULPHUR_SPRINGS)
            )
        )
    ).save(consumer, LighterEnd.MOD_ID + "/sulphur_springs");

    AdvancementHolder craft_spectral_arrow = Advancement.Builder.advancement().parent(end_lake)
        .display(
            LighterEndItems.GLOW_BARB,
            title("craft_spectral_arrow"),
            description("craft_spectral_arrow"),
            null,
            AdvancementType.GOAL,
            true,
            true,
            false
        ).addCriterion(
            "craft_spectral_arrows",
            RecipeCraftedTrigger.TriggerInstance.craftedItem(
                ResourceKey.create(Registries.RECIPE, LighterEnd.of("combat/spectral_arrow"))
            )
        ).save(consumer, LighterEnd.MOD_ID + "/craft_spectral_arrows");

    AdvancementHolder tether_totem = Advancement.Builder.advancement().parent(root).display(
        LighterEndBlocks.OBELISK.asItem(),
        title("use_obelisk"),
        description("use_obelisk"),
        null,
        AdvancementType.TASK,
        true,
        true,
        false
    ).addCriterion(
        "use_obelisk",
        ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
            LocationPredicate.Builder.location().setBlock(
                BlockPredicate.Builder.block().of(
                    lookup.lookupOrThrow(Registries.BLOCK),
                    LighterEndBlocks.OBELISK
                )
            ),
            ItemPredicate.Builder.item().of(
                lookup.lookupOrThrow(Registries.ITEM),
                LighterEndItems.TOTEM_OF_TELEPORTATION
            )
        )
    ).save(consumer, LighterEnd.MOD_ID + "/use_obelisk");

    AdvancementHolder use_totem = Advancement.Builder.advancement().parent(tether_totem).display(
        LighterEndItems.TOTEM_OF_TELEPORTATION,
        title("use_totem"),
        description("use_totem"),
        null,
        AdvancementType.GOAL,
        true,
        true,
        false
    ).addCriterion(
        "use_totem",
        UsedTotemTrigger.TriggerInstance.usedTotem(
            lookup.lookupOrThrow(Registries.ITEM),
            LighterEndItems.TOTEM_OF_TELEPORTATION
        )
    ).save(consumer, LighterEnd.MOD_ID + "/use_totem");

    AdvancementHolder acquire_claw = Advancement.Builder.advancement().parent(root).display(
        LighterEndItems.CRAB_CLAW,
        title("acquire_claw"),
        description("acquire_claw"),
        null,
        AdvancementType.TASK,
        true,
        true,
        false
    ).addCriterion(
        "acquire_claw", InventoryChangeTrigger.TriggerInstance.hasItems(LighterEndItems.CRAB_CLAW)
    ).save(consumer, LighterEnd.MOD_ID + "/acquire_claw");

    AdvancementHolder shear_mooshroom = Advancement.Builder.advancement().parent(acquire_claw)
        .display(
            Items.SHEARS,
            title("shear_mooshroom"),
            description("shear_mooshroom"),
            null,
            AdvancementType.TASK,
            true,
            true,
            false
        ).addCriterion(
            "shear_mooshroom",
            PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
                ItemPredicate.Builder.item()
                    .of(lookup.lookupOrThrow(Registries.ITEM), Items.SHEARS),
                Optional.of(
                    EntityPredicate.wrap(
                        EntityPredicate.Builder.entity()
                            .of(
                                lookup.lookupOrThrow(Registries.ENTITY_TYPE),
                                LighterEndMobs.MOOSHROOM.mob
                            )
                    )
                )
            )
        ).save(consumer, LighterEnd.MOD_ID + "/shear_mooshroom");

    AdvancementHolder wear_fur = Advancement.Builder.advancement().parent(acquire_claw).display(
            LighterEndBlocks.AGAVE_FUR.asItem(),
            title("wear_fur"),
            description("wear_fur"),
            null,
            AdvancementType.TASK,
            true,
            true,
            false
        ).requirements(Strategy.OR)
        .addCriterion(
            "wear_agave_fur",
            InventoryChangeTrigger.TriggerInstance.hasItems(LighterEndItems.AGAVE_FUR)
        ).addCriterion(
            "wear_glowshroom_fur",
            InventoryChangeTrigger.TriggerInstance.hasItems(LighterEndItems.GLOWSHROOM_FUR)
        ).save(consumer, LighterEnd.MOD_ID + "/wear_fur");

    AdvancementHolder drink_end_veil_potion = Advancement.Builder.advancement().parent(wear_fur)
        .display(
            Items.PLAYER_HEAD,
            title("drink_end_veil_potion"),
            description("drink_end_veil_potion"),
            null,
            AdvancementType.GOAL,
            true,
            true,
            false
        ).addCriterion(
            "drink_end_veil_potion",
            EffectsChangedTrigger.TriggerInstance.hasEffects(
                MobEffectsPredicate.Builder.effects().and(StatusEffects.END_VEIL)
            )
        ).save(consumer, LighterEnd.MOD_ID + "/drink_end_veil_potion");

    AdvancementHolder acquire_silk = Advancement.Builder.advancement().parent(acquire_claw).display(
            LighterEndItems.SILK,
            title("acquire_silk"),
            description("acquire_silk"),
            null,
            AdvancementType.TASK,
            true,
            true,
            false
        ).requirements(Strategy.OR)
        .addCriterion(
            "acquire_silk", InventoryChangeTrigger.TriggerInstance.hasItems(LighterEndItems.SILK)
        ).addCriterion(
            "acquire_silk_matrix",
            InventoryChangeTrigger.TriggerInstance.hasItems(LighterEndItems.SILK_MATRIX)
        ).save(consumer, LighterEnd.MOD_ID + "/acquire_silk");

    AdvancementHolder acquire_silk_elytra = Advancement.Builder.advancement().parent(acquire_silk)
        .display(
            LighterEndEquipment.SILK_ELYTRA,
            title("acquire_silk_elytra"),
            description("acquire_silk_elytra"),
            null,
            AdvancementType.CHALLENGE,
            true,
            true,
            false
        ).addCriterion(
            "acquire_silk_elytra",
            InventoryChangeTrigger.TriggerInstance.hasItems(LighterEndEquipment.SILK_ELYTRA)
        ).rewards(AdvancementRewards.Builder.experience(50))
        .save(consumer, LighterEnd.MOD_ID + "/acquire_silk_elytra");

    AdvancementHolder acquire_end_cream = Advancement.Builder.advancement().parent(root).display(
        LighterEndItems.END_CREAM,
        title("acquire_end_cream"),
        description("acquire_end_cream"),
        null,
        AdvancementType.TASK,
        true,
        true,
        false
    ).addCriterion(
        "acquire_end_cream",
        InventoryChangeTrigger.TriggerInstance.hasItems(LighterEndItems.END_CREAM)
    ).save(consumer, LighterEnd.MOD_ID + "/acquire_end_cream");

    Advancement.Builder allBiomesBuilder = Advancement.Builder.advancement().parent(sulphur_springs)
        .display(
            LighterEndBlocks.END_MOSS.asItem(),
            title("all_the_biomes"),
            description("all_the_biomes"),
            null,
            AdvancementType.CHALLENGE,
            false,
            false,
            false
        ).requirements(Strategy.AND);

    int xpReward = 0;
    for (ResourceKey<Biome> biome : List.of(
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
      allBiomesBuilder = allBiomesBuilder.addCriterion(
          biome.identifier().getPath(),
          PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inBiome(
                  lookup.lookupOrThrow(Registries.BIOME).getOrThrow(biome)
              )
          )
      );
      xpReward += 50;
    }

    AdvancementHolder all_the_biomes = allBiomesBuilder.rewards(
        AdvancementRewards.Builder.experience(xpReward)
    ).save(consumer, LighterEnd.MOD_ID + "/all_the_biomes");
  }

  private static MutableComponent title(String path) {
    return Component.translatable(
        String.format("advancements.%s.%s.title", LighterEnd.MOD_ID, path));
  }

  private static MutableComponent description(String path) {
    return Component.translatable(
        String.format("advancements.%s.%s.description", LighterEnd.MOD_ID, path));
  }
}
