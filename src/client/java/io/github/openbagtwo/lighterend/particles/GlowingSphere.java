package io.github.openbagtwo.lighterend.particles;

import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;

public class GlowingSphere extends AnimatedParticle {

  private int ticks;
  private double preVX;
  private double preVY;
  private double preVZ;
  private double nextVX;
  private double nextVY;
  private double nextVZ;

  protected GlowingSphere(
      ClientWorld world,
      double x,
      double y,
      double z,
      SpriteProvider sprites,
      double r,
      double g,
      double b
  ) {
    super(world, x, y, z, sprites, 0);
    setSprite(sprites.getSprite(random));
    this.maxAge = MathHelper.nextInt(random, 150, 300);
    this.scale = MathHelper.nextFloat(random, 0.05F, 0.15F);
    this.setTargetColor(15916745);
    this.setSpriteForAge(sprites);

    preVX = random.nextGaussian() * 0.02;
    preVY = random.nextGaussian() * 0.02;
    preVZ = random.nextGaussian() * 0.02;

    nextVX = random.nextGaussian() * 0.02;
    nextVY = random.nextGaussian() * 0.02;
    nextVZ = random.nextGaussian() * 0.02;
  }

  @Override
  public void tick() {
    ticks++;
    if (ticks > 30) {
      preVX = nextVX;
      preVY = nextVY;
      preVZ = nextVZ;
      nextVX = random.nextGaussian() * 0.02;
      nextVY = random.nextGaussian() * 0.02;
      nextVZ = random.nextGaussian() * 0.02;
      ticks = 0;
    }
    double delta = (double) ticks / 30.0;

    this.velocityX = MathHelper.lerp(delta, preVX, nextVX);
    this.velocityY = MathHelper.lerp(delta, preVY, nextVY);
    this.velocityZ = MathHelper.lerp(delta, preVZ, nextVZ);

    super.tick();
  }

  public static class Factory implements ParticleFactory<SimpleParticleType> {

    private final SpriteProvider sprites;

    public Factory(SpriteProvider sprites) {
      this.sprites = sprites;
    }

    @Override
    public Particle createParticle(
        SimpleParticleType type,
        ClientWorld world,
        double x,
        double y,
        double z,
        double vX,
        double vY,
        double vZ
    ) {
      return new GlowingSphere(world, x, y, z, sprites, 1, 1, 1);
    }
  }
}
