package io.github.openbagtwo.lighterend;

import io.github.openbagtwo.lighterend.config.Config;
import io.github.openbagtwo.lighterend.items.ItemGroups;
import io.github.openbagtwo.lighterend.misc.Composting;
import io.github.openbagtwo.lighterend.misc.Fire;
import io.github.openbagtwo.lighterend.misc.LighterEndPotions;
import io.github.openbagtwo.lighterend.misc.Oxidizing;
import io.github.openbagtwo.lighterend.misc.StatusEffects;
import io.github.openbagtwo.lighterend.networking.GravityPayload;
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
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LighterEnd implements ModInitializer {

  public static final String MOD_ID = "lighterend";
  public static final String MOD_NAME = "Lighter End";

  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  public static final Config CONFIG = Config.loadConfiguration();

  public static Identifier of(String name) {
    return Identifier.of(MOD_ID, name);
  }

  @Override
  public void onInitialize() {

    LighterEndBlocks.initialize();
    LighterEndItems.initialize();
    LighterEndMusicDiscs.initialize();
    LighterEndParticles.initialize();
    LighterEndData.initialize();
    LighterEndBlockEntities.initialize();
    LighterEndMobs.initialize();
    LighterEndSounds.initialize();
    LighterEndEquipment.initialize();
    Composting.initialize();
    Fire.initalize();
    StatusEffects.initialize();
    Oxidizing.initialize();
    LighterEndPotions.initialize();
    ItemGroups.initialize();
    LighterEndTags.initialize();
    LighterEndLootTables.initialize();

    LighterEndConfiguredFeatures.initialize();
    LighterEndStructures.initialize();

    VanillaLootTableModifiers.patchLootTables(CONFIG);
    LighterEndWorldGen.modifyWorldGen(CONFIG);
    LighterEndWorldGen.addJadestoneBlobs(CONFIG);
    LighterEndWorldGen.addIceStars(CONFIG);
    LighterEndWorldGen.addOres(CONFIG);

    PayloadTypeRegistry.playS2C().register(GravityPayload.ID, GravityPayload.CODEC);
    ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
        sender.sendPacket(
            new GravityPayload(
                LighterEnd.CONFIG.getEndGravity(),
                LighterEnd.CONFIG.disableEndGravityWhileFlying()
            )
        )
    );

  }
}
