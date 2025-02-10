package io.github.openbagtwo.lighterend;

import io.github.openbagtwo.lighterend.rendering.PlantRenderer;
import net.fabricmc.api.ClientModInitializer;

public class LighterEndClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    PlantRenderer.initialize();

  }
}
