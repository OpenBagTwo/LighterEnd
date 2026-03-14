package io.github.openbagtwo.lighterend.misc;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;

public class Oxidizing {

  public static void initialize() {
    OxidizableBlocksRegistry.registerWeatheringCopperBlocks(LighterEndBlocks.COPPER_CHANDELIERS);
  }

}
