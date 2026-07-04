package io.github.openbagtwo.lighterend.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;

public class Snowflake extends SpriteBillboardParticle {

  private int ticks;
  private double preVX;
  private double preVY;
  private double preVZ;
  private double nextVX;
  private double nextVY;
  private double nextVZ;

  protected Snowflake(
      ClientWorld world,
      double x,
      double y,
      double z,
      double r,
      double g,
      double b,
      SpriteProvider sprites
  ) {
    super(world, x, y, z, r, g, b);
    this.setSprite(sprites);

    this.maxAge = MathHelper.nextInt(random, 150, 300);
    this.scale = MathHelper.nextFloat(random, 0.05F, 0.2F);
    this.setAlpha(0F);

    preVX = random.nextGaussian() * 0.015;
    preVY = random.nextGaussian() * 0.015;
    preVZ = random.nextGaussian() * 0.015;

    nextVX = random.nextGaussian() * 0.015;
    nextVY = random.nextGaussian() * 0.015;
    nextVZ = random.nextGaussian() * 0.015;
  }

  @Override
  public void tick() {
    ticks++;
    if (ticks > 200) {
      preVX = nextVX;
      preVY = nextVY;
      preVZ = nextVZ;
      nextVX = random.nextGaussian() * 0.015;
      nextVY = random.nextGaussian() * 0.015;
      nextVZ = random.nextGaussian() * 0.015;
      if (random.nextInt(4) == 0) {
        nextVY = Math.abs(nextVY);
      }
      ticks = 0;
    }
    double delta = (double) ticks / 200.0;

    if (this.age <= 40) {
      this.setAlpha(this.age / 40F);
    } else if (this.age >= this.maxAge - 40) {
      this.setAlpha((this.maxAge - this.age) / 40F);
    }

    if (this.age >= this.maxAge) {
      this.markDead();
    }

    this.velocityX = MathHelper.lerp(delta, preVX, nextVX);
    this.velocityY = MathHelper.lerp(delta, preVY, nextVY);
    this.velocityZ = MathHelper.lerp(delta, preVZ, nextVZ);

    super.tick();
  }

  @Override
  public ParticleTextureSheet getType() {
    return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
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
      return new Snowflake(world, x, y, z, 1, 1, 1, sprites);
    }
  }


}
