package io.github.openbagtwo.lighterend;

import io.github.openbagtwo.lighterend.blocks.AuroraCrystalRenderer;
import io.github.openbagtwo.lighterend.blocks.EndMossRenderer;
import io.github.openbagtwo.lighterend.blocks.PlantRenderer;
import io.github.openbagtwo.lighterend.blocks.SignRenderer;
import io.github.openbagtwo.lighterend.blocks.UmbrellaMembraneRenderer;
import io.github.openbagtwo.lighterend.mobs.EntityModels;
import io.github.openbagtwo.lighterend.particles.TenaneaPetal;
import io.github.openbagtwo.lighterend.registries.LighterEndParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class LighterEndClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    AuroraCrystalRenderer.initialize();
    EndMossRenderer.initialize();
    UmbrellaMembraneRenderer.initialize();
    PlantRenderer.initialize();
    ParticleFactoryRegistry.getInstance()
        .register(LighterEndParticles.TENANEA_PETAL, TenaneaPetal.Factory::new);
    SignRenderer.initialize();
    EntityModels.initialize();
  }
}
