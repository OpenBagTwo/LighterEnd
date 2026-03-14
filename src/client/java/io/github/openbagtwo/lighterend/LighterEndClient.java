package io.github.openbagtwo.lighterend;

import io.github.openbagtwo.lighterend.blocks.BlockEntityRenderer;
import io.github.openbagtwo.lighterend.mobs.EntityModels;
import io.github.openbagtwo.lighterend.networking.GravityPayload;
import io.github.openbagtwo.lighterend.particles.Geyser;
import io.github.openbagtwo.lighterend.particles.GlowingSphere;
import io.github.openbagtwo.lighterend.particles.Snowflake;
import io.github.openbagtwo.lighterend.particles.Sulphur;
import io.github.openbagtwo.lighterend.particles.TenaneaPetal;
import io.github.openbagtwo.lighterend.registries.LighterEndParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;

public class LighterEndClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    ParticleProviderRegistry.getInstance()
        .register(LighterEndParticles.TENANEA_PETAL, TenaneaPetal.Factory::new);
    ParticleProviderRegistry.getInstance()
        .register(LighterEndParticles.AMBER_SPHERE, GlowingSphere.Factory::new);
    ParticleProviderRegistry.getInstance()
        .register(LighterEndParticles.GLOWING_SPHERE, GlowingSphere.Factory::new);
    ParticleProviderRegistry.getInstance()
        .register(LighterEndParticles.SNOWFLAKE, Snowflake.Factory::new);
    ParticleProviderRegistry.getInstance()
        .register(LighterEndParticles.SULPHUR, Sulphur.Factory::new);
    ParticleProviderRegistry.getInstance()
        .register(LighterEndParticles.GEYSER, Geyser.Factory::new);

    BlockEntityRenderer.initialize();
    EntityModels.initialize();

    FabricLoader.getInstance().getModContainer(LighterEnd.MOD_ID).ifPresent(container -> {
          ResourceManagerHelper.registerBuiltinResourcePack(
              LighterEnd.of("wing_trims"),
              container,
              Component.translatable("resourcepacks.lighterend.wing_trims.title"),
              ResourcePackActivationType.DEFAULT_ENABLED
          );
          ResourceManagerHelper.registerBuiltinResourcePack(
              LighterEnd.of("visibly_waxed"),
              container,
              Component.translatable("resourcepacks.lighterend.visibly_waxed.title"),
              ResourcePackActivationType.NORMAL
          );
        }
    );

    ClientPlayNetworking.registerGlobalReceiver(GravityPayload.ID, (packet, context) -> {
      LighterEnd.CONFIG.setServerEndGravity(packet.gravity());
      LighterEnd.CONFIG.setServerFlyFix(packet.flyFix());
      LighterEnd.LOGGER.info("Received gravity settings from server");
    });
  }
}
