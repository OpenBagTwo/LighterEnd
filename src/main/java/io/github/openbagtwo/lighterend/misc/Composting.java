package io.github.openbagtwo.lighterend.misc;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;

public class Composting {

  public static void initialize(){
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.CREEPING_MOSS, 0.5F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.UMBRELLA_FERN, 0.5F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.LUMECORN_SEED, 0.3F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndItems.LUMECORN_EAR, 0.65F);
  }

}
