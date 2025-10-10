package io.github.openbagtwo.lighterend;

import io.github.openbagtwo.lighterend.blocks.BlockEntityRenderer;
import io.github.openbagtwo.lighterend.blocks.BlockLayerRenderer;
import io.github.openbagtwo.lighterend.mobs.EntityModels;
import io.github.openbagtwo.lighterend.particles.GlowingSphere;
import io.github.openbagtwo.lighterend.particles.Snowflake;
import io.github.openbagtwo.lighterend.particles.Sulphur;
import io.github.openbagtwo.lighterend.particles.TenaneaPetal;
import io.github.openbagtwo.lighterend.registries.LighterEndParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class LighterEndClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    BlockLayerRenderer.initialize();
    ParticleFactoryRegistry.getInstance()
        .register(LighterEndParticles.TENANEA_PETAL, TenaneaPetal.Factory::new);
    ParticleFactoryRegistry.getInstance()
        .register(LighterEndParticles.AMBER_SPHERE, GlowingSphere.Factory::new);
    ParticleFactoryRegistry.getInstance()
        .register(LighterEndParticles.GLOWING_SPHERE, GlowingSphere.Factory::new);
    ParticleFactoryRegistry.getInstance()
        .register(LighterEndParticles.SNOWFLAKE, Snowflake.Factory::new);
    ParticleFactoryRegistry.getInstance()
        .register(LighterEndParticles.SULPHUR, Sulphur.Factory::new);
    BlockEntityRenderer.initialize();
    EntityModels.initialize();
  }
}
