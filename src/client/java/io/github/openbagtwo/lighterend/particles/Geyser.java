package io.github.openbagtwo.lighterend.particles;

import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

public class Geyser extends BillboardParticle {

  private final Mutable mut = new Mutable();
  private boolean changeDir = false;
  private boolean check = true;

  protected Geyser(
      ClientWorld world,
      double x,
      double y,
      double z,
      double vX,
      double vY,
      double vZ,
      Sprite sprite
  ) {
    super(world, x, y, z, vX, vY, vZ, sprite);

    this.maxAge = MathHelper.nextInt(random, 400, 800);
    this.scale = MathHelper.nextFloat(random, 0.5F, 1.0F);

    this.velocityX = vX;
    this.velocityZ = vZ;
    this.lastY = y - 0.125;
  }

  @Override
  public void tick() {

    if (this.lastY == this.y || this.age > this.maxAge) {
      this.markDead();
    } else {
      if (this.age >= this.maxAge - 200) {
        this.setAlpha((this.maxAge - this.age) / 200F);
      }

      this.scale += 0.005F;
      this.velocityY = 0.125;

      if (changeDir) {
        changeDir = false;
        check = false;
        this.velocityX += MathHelper.nextDouble(random, -0.2, 0.2);
        this.velocityZ += MathHelper.nextDouble(random, -0.2, 0.2);
      } else if (check) {
        changeDir = world.getBlockState(mut.set(x, y, z)).getFluidState().isEmpty();
        this.velocityX = 0;
        this.velocityZ = 0;
      }
    }
    super.tick();
  }

  @Override
  public BillboardParticle.RenderType getRenderType() {
    return BillboardParticle.RenderType.PARTICLE_ATLAS_TRANSLUCENT;
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
        double vZ,
        Random random
    ) {
      return new Geyser(world, x, y, z, 0, 0.125, 0, this.sprites.getSprite(random));
    }
  }
}
