package io.github.openbagtwo.lighterend;

import io.github.openbagtwo.lighterend.Items.ItemGroups;
import io.github.openbagtwo.lighterend.misc.Composting;
import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import io.github.openbagtwo.lighterend.registries.LighterEndParticles;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import io.github.openbagtwo.lighterend.registries.LighterEndStructures;
import io.github.openbagtwo.lighterend.world.LighterEndConfiguredFeatures;
import io.github.openbagtwo.lighterend.world.gen.LighterEndWorldGen;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LighterEnd implements ModInitializer {

  public static final String MOD_ID = "lighterend";
  public static final String MOD_NAME = "LighterEnd";

  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  @Override
  public void onInitialize() {

    LighterEndItems.initialize();
    LighterEndParticles.initialize();
    LighterEndBlocks.initialize();
    LighterEndBlockEntities.initialize();
    LighterEndMobs.initialize();
    Composting.initialize();
    LighterEndSounds.initialize();
    ItemGroups.initialize();

    LighterEndConfiguredFeatures.initialize();
    LighterEndStructures.initialize();

    LighterEndWorldGen.modifyWorldGen();
  }
}
