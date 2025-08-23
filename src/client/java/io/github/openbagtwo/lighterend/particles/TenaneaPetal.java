package io.github.openbagtwo.lighterend.particles;

import io.github.openbagtwo.lighterend.blocks.TenaneaFlowerRenderer;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

public class TenaneaPetal extends BillboardParticle {

  private static BlockColorProvider provider;

  private double preVX;
  private double preVY;
  private double preVZ;
  private double nextVX;
  private double nextVY;
  private double nextVZ;

  protected TenaneaPetal(
      ClientWorld world,
      double x,
      double y,
      double z,
      Sprite sprite
  ) {
    super(world, x, y, z, sprite);

    if (provider == null) {
      provider = TenaneaFlowerRenderer.getBlockColor();
    }
    int color = provider.getColor(null, null, new BlockPos((int) x, (int) y, (int) z), 0);
    this.red = ((color >> 16) & 255) / 255F;
    this.green = ((color >> 8) & 255) / 255F;
    this.blue = ((color) & 255) / 255F;

    this.maxAge = MathHelper.nextInt(this.random, 120, 200);
    this.scale = MathHelper.nextFloat(this.random, 0.05F, 0.15F);
    this.setAlpha(0);

    this.preVX = 0;
    this.preVY = 0;
    this.preVZ = 0;

    this.nextVX = this.random.nextGaussian() * 0.02;
    this.nextVY = -this.random.nextDouble() * 0.02 - 0.02;
    this.nextVZ = this.random.nextGaussian() * 0.02;
  }

  @Override
  public int getBrightness(float tint) {
    return 15728880;
  }

  @Override
  public void tick() {
    int ticks = this.age & 63;
    if (ticks == 0) {
      this.preVX = this.nextVX;
      this.preVY = this.nextVY;
      this.preVZ = this.nextVZ;
      this.nextVX = this.random.nextGaussian() * 0.02;
      this.nextVY = -this.random.nextDouble() * 0.02 - 0.02;
      this.nextVZ = this.random.nextGaussian() * 0.02;
    }
    double delta = ticks / 63.0;

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
  public BillboardParticle.RenderType getRenderType() {
    return BillboardParticle.RenderType.field_62640;
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
      return new TenaneaPetal(world, x, y, z, sprites.getSprite(random));
    }
  }

}
