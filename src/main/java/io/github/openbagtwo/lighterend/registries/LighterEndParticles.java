package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class LighterEndParticles {

  public static final SimpleParticleType TENANEA_PETAL = register(
      "tenanea_petal",
      FabricParticleTypes.simple()
  );

  public static final SimpleParticleType AMBER_SPHERE = register(
      "amber_sphere",
      FabricParticleTypes.simple()
  );

  public static final SimpleParticleType GLOWING_SPHERE = register(
      "glowing_sphere",
      FabricParticleTypes.simple()
  );

  public static final SimpleParticleType SNOWFLAKE = register(
      "snowflake",
      FabricParticleTypes.simple()
  );

  public static final SimpleParticleType SULPHUR = register(
      "sulphur",
      FabricParticleTypes.simple()
  );

  public static SimpleParticleType register(String name, SimpleParticleType particleType) {
    return Registry.register(
        Registries.PARTICLE_TYPE,
        LighterEnd.of(name),
        particleType);
  }

  public static void initialize() {
  }

}
