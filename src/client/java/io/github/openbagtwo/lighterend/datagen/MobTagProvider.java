package io.github.openbagtwo.lighterend.datagen;

import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.EntityTypeTags;

public class MobTagProvider extends FabricTagProvider.EntityTypeTagProvider {

  public MobTagProvider(
      FabricDataOutput dataOutput,
      CompletableFuture<WrapperLookup> registriesFuture
  ) {
    super(dataOutput, registriesFuture);
  }

  @Override
  protected void configure(WrapperLookup wrapperLookup) {
    valueLookupBuilder(EntityTypeTags.ARTHROPOD).add(
        LighterEndMobs.SILK_MOTH.mob,
        LighterEndMobs.DRAGONFLY.mob,
        LighterEndMobs.CHORUS_CRAB.mob
    );
    valueLookupBuilder(EntityTypeTags.AQUATIC).add(
        LighterEndMobs.END_FISH.mob,
        LighterEndMobs.CUBOZOA.mob
    );
    valueLookupBuilder(EntityTypeTags.AXOLOTL_HUNT_TARGETS).add(
        LighterEndMobs.END_FISH.mob,
        LighterEndMobs.CUBOZOA.mob
    );

    valueLookupBuilder(EntityTypeTags.FROG_FOOD).add(
        LighterEndMobs.DRAGONFLY.mob,
        LighterEndMobs.END_SLIME.mob
    );

    valueLookupBuilder(EntityTypeTags.IMMUNE_TO_OOZING).add(
        LighterEndMobs.END_SLIME.mob
    );

    valueLookupBuilder(EntityTypeTags.NON_CONTROLLING_RIDER).add(
        LighterEndMobs.END_SLIME.mob
    );
    // I like the idea of these weird-looking fish scaring pufferfish
//    valueLookupBuilder(EntityTypeTags.NOT_SCARY_FOR_PUFFERFISH).add(
//        LighterEndMobs.END_FISH.mob,
//        LighterEndMobs.CUBOZOA.mob
//    );
    valueLookupBuilder(LighterEndTags.MOTH_NEST_INHABITORS).add(
        LighterEndMobs.SILK_MOTH.mob
    );

  }
}
