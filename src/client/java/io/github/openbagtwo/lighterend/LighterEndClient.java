package io.github.openbagtwo.lighterend;

import io.github.openbagtwo.lighterend.blocks.BlockLayerRenderer;
import io.github.openbagtwo.lighterend.blocks.EndMossRenderer;
import io.github.openbagtwo.lighterend.blocks.SignRenderer;
import io.github.openbagtwo.lighterend.blocks.UmbralithRenderer;
import io.github.openbagtwo.lighterend.blocks.UmbrellaMembraneRenderer;
import io.github.openbagtwo.lighterend.mobs.EntityModels;
import io.github.openbagtwo.lighterend.particles.GlowingSphere;
import io.github.openbagtwo.lighterend.particles.TenaneaPetal;
import io.github.openbagtwo.lighterend.registries.LighterEndParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class LighterEndClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    EndMossRenderer.initialize();
    UmbralithRenderer.initialize();
    UmbrellaMembraneRenderer.initialize();
    BlockLayerRenderer.initialize();
    ParticleFactoryRegistry.getInstance()
        .register(LighterEndParticles.TENANEA_PETAL, TenaneaPetal.Factory::new);
    ParticleFactoryRegistry.getInstance()
        .register(LighterEndParticles.AMBER_SPHERE, GlowingSphere.Factory::new);
    ParticleFactoryRegistry.getInstance()
        .register(LighterEndParticles.GLOWING_SPHERE, GlowingSphere.Factory::new);
    SignRenderer.initialize();
    EntityModels.initialize();
  }
}
