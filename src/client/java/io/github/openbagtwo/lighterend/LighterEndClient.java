package io.github.openbagtwo.lighterend;

import io.github.openbagtwo.lighterend.blocks.BlockEntityRenderer;
import io.github.openbagtwo.lighterend.blocks.BlockLayerRenderer;
import net.fabricmc.api.ClientModInitializer;

public class LighterEndClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    BlockLayerRenderer.initialize();
    BlockEntityRenderer.initialize();
  }
}
