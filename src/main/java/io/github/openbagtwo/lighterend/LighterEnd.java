package io.github.openbagtwo.lighterend;

import io.github.openbagtwo.lighterend.config.Config;
import io.github.openbagtwo.lighterend.items.ItemGroups;
import io.github.openbagtwo.lighterend.misc.Composting;
import io.github.openbagtwo.lighterend.misc.Fire;
import io.github.openbagtwo.lighterend.misc.LighterEndPotions;
import io.github.openbagtwo.lighterend.misc.StatusEffects;
import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndData;
import io.github.openbagtwo.lighterend.registries.LighterEndEquipment;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import io.github.openbagtwo.lighterend.registries.LighterEndLootTables;
import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import io.github.openbagtwo.lighterend.registries.LighterEndMusicDiscs;
import io.github.openbagtwo.lighterend.registries.LighterEndParticles;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import io.github.openbagtwo.lighterend.registries.LighterEndStructures;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.github.openbagtwo.lighterend.world.LighterEndConfiguredFeatures;
import io.github.openbagtwo.lighterend.world.VanillaLootTableModifiers;
import io.github.openbagtwo.lighterend.world.gen.LighterEndWorldGen;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LighterEnd implements ModInitializer {

  public static final String MOD_ID = "lighterend";
  public static final String MOD_NAME = "LighterEnd";

  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  public static final Config CONFIG = Config.loadConfiguration();

  public static Identifier of(String name) {
    return Identifier.of(MOD_ID, name);
  }

  @Override
  public void onInitialize() {

    LighterEndItems.initialize();
    LighterEndMusicDiscs.initialize();
    LighterEndParticles.initialize();
    LighterEndBlocks.initialize();
    LighterEndData.initialize();
    LighterEndBlockEntities.initialize();
    LighterEndMobs.initialize();
    LighterEndSounds.initialize();
    LighterEndEquipment.initialize();
    Composting.initialize();
    Fire.initalize();
    StatusEffects.initialize();
    LighterEndPotions.initialize();
    ItemGroups.initialize();
    LighterEndTags.initialize();
    LighterEndLootTables.initialize();

    LighterEndConfiguredFeatures.initialize();
    LighterEndStructures.initialize();

    VanillaLootTableModifiers.patchLootTables(CONFIG);
    LighterEndWorldGen.modifyWorldGen(CONFIG);
    LighterEndWorldGen.addIceStars(CONFIG);
    LighterEndWorldGen.addJadestoneBlobs(CONFIG);
  }
}
