package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityTypeIds;

public class MobTagProvider extends FabricTagsProvider.EntityTypeTagsProvider {

  public MobTagProvider(
      FabricPackOutput dataOutput,
      CompletableFuture<Provider> registriesFuture
  ) {
    super(dataOutput, registriesFuture);
  }

  @Override
  protected void addTags(Provider wrapperLookup) {
    builder(EntityTypeTags.ARTHROPOD).add(
        LighterEndMobs.SILK_MOTH.id,
        LighterEndMobs.DRAGONFLY.id,
        LighterEndMobs.CHORUS_CRAB.id
    );
    builder(EntityTypeTags.AQUATIC).add(
        LighterEndMobs.END_FISH.id,
        LighterEndMobs.CUBOZOA.id
    );
    builder(EntityTypeTags.AXOLOTL_HUNT_TARGETS).add(
        LighterEndMobs.END_FISH.id,
        LighterEndMobs.CUBOZOA.id
    );

    builder(EntityTypeTags.FROG_FOOD).add(
        LighterEndMobs.DRAGONFLY.id,
        LighterEndMobs.END_SLIME.id
    );

    builder(EntityTypeTags.IMMUNE_TO_OOZING).add(
        LighterEndMobs.END_SLIME.id
    );

    builder(EntityTypeTags.NON_CONTROLLING_RIDER).add(
        LighterEndMobs.END_SLIME.id
    );
    // I like the idea of these weird-looking fish scaring pufferfish
//    builder(EntityTypeTags.NOT_SCARY_FOR_PUFFERFISH).add(
//        LighterEndMobs.END_FISH.id,
//        LighterEndMobs.CUBOZOA.id
//    );
    builder(LighterEndTags.MOTH_NEST_INHABITORS).add(
        LighterEndMobs.SILK_MOTH.id
    );

    builder(LighterEndTags.IGNORES_GEYSER_BUBBLES).add(
        LighterEndMobs.END_FISH.id,
        LighterEndMobs.CUBOZOA.id
    );

    builder(LighterEndTags.IMMUNE_TO_MURKWEED).add(
        EntityTypeIds.ENDERMITE,
        EntityTypeIds.PHANTOM
    );

  }
}
