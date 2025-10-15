package io.github.openbagtwo.lighterend.misc;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;

public class Fire {

  public static void initalize() {
    // note that wood set flammability is handled during wood registration

    FlammableBlockRegistry.getDefaultInstance().add(LighterEndBlocks.CREEPING_MOSS, 60, 100);
    FlammableBlockRegistry.getDefaultInstance().add(LighterEndBlocks.UMBRELLA_FERN, 60, 100);
    FlammableBlockRegistry.getDefaultInstance().add(LighterEndBlocks.TALL_UMBRELLA_FERN, 60, 100);
    FlammableBlockRegistry.getDefaultInstance().add(LighterEndBlocks.LUMECORN, 60, 100);
    FlammableBlockRegistry.getDefaultInstance().add(LighterEndBlocks.LUMECORN_STEM, 60, 100);
    FlammableBlockRegistry.getDefaultInstance().add(LighterEndBlocks.TENANEA_FLOWER, 15, 100);
    FlammableBlockRegistry.getDefaultInstance().add(LighterEndBlocks.TENANEA_LEAVES, 30, 60);
    FlammableBlockRegistry.getDefaultInstance().add(LighterEndBlocks.SILK_MOTH_NEST, 30, 20);
    FlammableBlockRegistry.getDefaultInstance()
        .add(LighterEndBlocks.UMBRELLA_TREE_CLUSTER, 60, 100);
    FlammableBlockRegistry.getDefaultInstance()
        .add(LighterEndBlocks.UMBRELLA_TREE_CLUSTER_EMPTY, 30, 20);
    FlammableBlockRegistry.getDefaultInstance().add(LighterEndBlocks.END_LOTUS_FLOWER, 60, 100);
    FlammableBlockRegistry.getDefaultInstance().add(LighterEndBlocks.END_LOTUS_STEM, 60, 60);
    FlammableBlockRegistry.getDefaultInstance().add(LighterEndBlocks.END_LOTUS_LEAF, 30, 60);
    FlammableBlockRegistry.getDefaultInstance().add(LighterEndBlocks.GLOWSHROOM_FUR, 60, 100);
    FlammableBlockRegistry.getDefaultInstance().add(LighterEndBlocks.AGAVE, 60, 60);
    FlammableBlockRegistry.getDefaultInstance().add(LighterEndBlocks.AGAVE_BULB, 30, 20);
    FlammableBlockRegistry.getDefaultInstance().add(LighterEndBlocks.AGAVE_FUR, 60, 100);
    FlammableBlockRegistry.getDefaultInstance().add(LighterEndBlocks.SHADOW_GRASS, 60, 100);
    FlammableBlockRegistry.getDefaultInstance().add(LighterEndBlocks.NEEDLEGRASS, 60, 100);
    FlammableBlockRegistry.getDefaultInstance().add(LighterEndBlocks.MURKWEED, 30, 60);
  }

}
