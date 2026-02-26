package io.github.openbagtwo.lighterend.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class GlowingSphere extends SimpleAnimatedParticle {

  private int ticks;
  private double preVX;
  private double preVY;
  private double preVZ;
  private double nextVX;
  private double nextVY;
  private double nextVZ;

  protected GlowingSphere(
      ClientLevel world,
      double x,
      double y,
      double z,
      SpriteSet sprites
  ) {
    super(world, x, y, z, sprites, 0);
    setSprite(sprites.get(random));
    this.lifetime = Mth.nextInt(random, 150, 300);
    this.quadSize = Mth.nextFloat(random, 0.05F, 0.15F);
    this.setFadeColor(15916745);
    this.setSpriteFromAge(sprites);

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

    this.xd = Mth.lerp(delta, preVX, nextVX);
    this.yd = Mth.lerp(delta, preVY, nextVY);
    this.zd = Mth.lerp(delta, preVZ, nextVZ);

    super.tick();
  }

  public static class Factory implements ParticleProvider<SimpleParticleType> {

    private final SpriteSet sprites;

    public Factory(SpriteSet sprites) {
      this.sprites = sprites;
    }

    @Override
    public Particle createParticle(
        SimpleParticleType type,
        ClientLevel world,
        double x,
        double y,
        double z,
        double vX,
        double vY,
        double vZ,
        RandomSource random
    ) {
      return new GlowingSphere(world, x, y, z, sprites);
    }
  }
}
