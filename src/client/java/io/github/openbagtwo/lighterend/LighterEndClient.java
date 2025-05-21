package io.github.openbagtwo.lighterend;

import io.github.openbagtwo.lighterend.particles.TenaneaPetal;
import io.github.openbagtwo.lighterend.registries.LighterEndParticles;
import io.github.openbagtwo.lighterend.rendering.AuroraCrystalRenderer;
import io.github.openbagtwo.lighterend.rendering.PlantRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class LighterEndClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    AuroraCrystalRenderer.initialize();
    PlantRenderer.initialize();
    ParticleFactoryRegistry.getInstance()
        .register(LighterEndParticles.TENANEA_PETAL, TenaneaPetal.Factory::new);

  }
}
