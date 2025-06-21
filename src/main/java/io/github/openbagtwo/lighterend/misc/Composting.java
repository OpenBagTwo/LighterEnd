package io.github.openbagtwo.lighterend.misc;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;

public class Composting {

  public static void initialize() {
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.CREEPING_MOSS, 0.5F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.UMBRELLA_FERN, 0.5F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.LUMECORN_SEED, 0.3F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndItems.LUMECORN_EAR, 0.65F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.TENANEA_FLOWER, 0.65F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.TENANEA_SAPLING, 0.3F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.TENANEA_LEAVES, 0.3F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.UMBRELLA_TREE_CLUSTER, 0.85F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.UMBRELLA_TREE_CLUSTER_EMPTY, 0.65F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.CHARNIA_CYAN, 0.65F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.CHARNIA_GREEN, 0.65F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.CHARNIA_LIGHT_BLUE, 0.65F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.CHARNIA_ORANGE, 0.65F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.CHARNIA_PURPLE, 0.65F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.CHARNIA_RED, 0.65F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.END_LILY_SEED, 0.3F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndItems.END_LILY_LEAF, 0.65F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndItems.GLOWSHROOM_FUR, 0.65F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.GLOWSHROOM_CAP, 0.65F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.GLOWSHROOM_HYMENOPHORE, 0.65F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.GLOWSHROOM_SAPLING, 0.3F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndItems.AGAVE_FUR, 0.65F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.AGAVE_BULB, 0.65F);
    CompostingChanceRegistry.INSTANCE.add(LighterEndBlocks.AGAVE_SEED, 0.3F);
  }

}
