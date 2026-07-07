package io.github.openbagtwo.lighterend.misc;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import net.fabricmc.fabric.api.registry.CompostableRegistry;

public class Composting {

  public static void initialize() {
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.CREEPING_MOSS, 0.5F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.UMBRELLA_FERN, 0.5F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.LUMECORN_SEED, 0.3F);
    CompostableRegistry.INSTANCE.add(LighterEndItems.LUMECORN_EAR, 0.65F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.TENANEA_FLOWER, 0.65F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.TENANEA_SAPLING, 0.3F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.TENANEA_LEAVES, 0.3F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.UMBRELLA_TREE_CLUSTER, 0.85F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.UMBRELLA_TREE_CLUSTER_EMPTY, 0.65F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.CHARNIA_CYAN, 0.65F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.CHARNIA_GREEN, 0.65F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.CHARNIA_LIGHT_BLUE, 0.65F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.CHARNIA_ORANGE, 0.65F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.CHARNIA_PURPLE, 0.65F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.CHARNIA_RED, 0.65F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.END_LILY_SEED, 0.3F);
    CompostableRegistry.INSTANCE.add(LighterEndItems.END_LILY_LEAF, 0.65F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.GLOWSHROOM_FUR, 0.65F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.GLOWSHROOM_CAP, 0.65F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.GLOWSHROOM_HYMENOPHORE, 0.65F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.GLOWSHROOM_SAPLING, 0.3F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.AGAVE_FUR, 0.65F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.AGAVE_BULB, 0.65F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.AGAVE_SEED, 0.3F);
    CompostableRegistry.INSTANCE.add(LighterEndItems.SHADOW_BERRY_SEEDS, 0.3F);
    CompostableRegistry.INSTANCE.add(LighterEndItems.SHADOW_BERRY, 0.65F);
    CompostableRegistry.INSTANCE.add(LighterEndItems.SHADOW_BERRY_COOKED, 0.85F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.SHADOW_GRASS, 0.3F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.NEEDLEGRASS, 0.3F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.MURKWEED, 0.5F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.DRAGON_LEAVES, 0.3F);
    CompostableRegistry.INSTANCE.add(LighterEndBlocks.DRAGON_SAPLING, 0.3F);
  }

}
