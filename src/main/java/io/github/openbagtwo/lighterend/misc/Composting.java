package io.github.openbagtwo.lighterend.misc;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;

public class Composting {

  public static void initialize(){
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.CREEPING_MOSS, 0.5F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.UMBRELLA_FERN, 0.5F);
  }

}
