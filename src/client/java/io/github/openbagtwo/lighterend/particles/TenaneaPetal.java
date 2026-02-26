package io.github.openbagtwo.lighterend.particles;

import io.github.openbagtwo.lighterend.blocks.TenaneaFlowerRenderer;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class TenaneaPetal extends SingleQuadParticle {

  private static BlockColor provider;

  private double preVX;
  private double preVY;
  private double preVZ;
  private double nextVX;
  private double nextVY;
  private double nextVZ;

  protected TenaneaPetal(
      ClientLevel world,
      double x,
      double y,
      double z,
      TextureAtlasSprite sprite
  ) {
    super(world, x, y, z, sprite);

    if (provider == null) {
      provider = TenaneaFlowerRenderer.getBlockColor();
    }
    int color = provider.getColor(null, null, new BlockPos((int) x, (int) y, (int) z), 0);
    this.rCol = ((color >> 16) & 255) / 255F;
    this.gCol = ((color >> 8) & 255) / 255F;
    this.bCol = ((color) & 255) / 255F;

    this.lifetime = Mth.nextInt(this.random, 120, 200);
    this.quadSize = Mth.nextFloat(this.random, 0.05F, 0.15F);
    this.setAlpha(0);

    this.preVX = 0;
    this.preVY = 0;
    this.preVZ = 0;

    this.nextVX = this.random.nextGaussian() * 0.02;
    this.nextVY = -this.random.nextDouble() * 0.02 - 0.02;
    this.nextVZ = this.random.nextGaussian() * 0.02;
  }

  @Override
  public int getLightColor(float tint) {
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
    } else if (this.age >= this.lifetime - 40) {
      this.setAlpha((this.lifetime - this.age) / 40F);
    }

    if (this.age >= this.lifetime) {
      this.remove();
    }

    this.xd = Mth.lerp(delta, preVX, nextVX);
    this.yd = Mth.lerp(delta, preVY, nextVY);
    this.zd = Mth.lerp(delta, preVZ, nextVZ);

    super.tick();
  }

  @Override
  public SingleQuadParticle.Layer getLayer() {
    return SingleQuadParticle.Layer.TRANSLUCENT;
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
      return new TenaneaPetal(world, x, y, z, sprites.get(random));
    }
  }

}
