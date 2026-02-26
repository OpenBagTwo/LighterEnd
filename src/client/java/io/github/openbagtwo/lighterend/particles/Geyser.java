package io.github.openbagtwo.lighterend.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class Geyser extends SingleQuadParticle {

  private final MutableBlockPos mut = new MutableBlockPos();
  private boolean changeDir = false;
  private boolean check = true;

  protected Geyser(
      ClientLevel world,
      double x,
      double y,
      double z,
      double vX,
      double vY,
      double vZ,
      TextureAtlasSprite sprite
  ) {
    super(world, x, y, z, vX, vY, vZ, sprite);

    this.lifetime = Mth.nextInt(random, 400, 800);
    this.quadSize = Mth.nextFloat(random, 0.5F, 1.0F);

    this.xd = vX;
    this.zd = vZ;
    this.yo = y - 0.125;
  }

  @Override
  public void tick() {

    if (this.yo == this.y || this.age > this.lifetime) {
      this.remove();
    } else {
      if (this.age >= this.lifetime - 200) {
        this.setAlpha((this.lifetime - this.age) / 200F);
      }

      this.quadSize += 0.005F;
      this.yd = 0.125;

      if (changeDir) {
        changeDir = false;
        check = false;
        this.xd += Mth.nextDouble(random, -0.2, 0.2);
        this.zd += Mth.nextDouble(random, -0.2, 0.2);
      } else if (check) {
        changeDir = level.getBlockState(mut.set(x, y, z)).getFluidState().isEmpty();
        this.xd = 0;
        this.zd = 0;
      }
    }
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
      return new Geyser(world, x, y, z, 0, 0.125, 0, this.sprites.get(random));
    }
  }
}
