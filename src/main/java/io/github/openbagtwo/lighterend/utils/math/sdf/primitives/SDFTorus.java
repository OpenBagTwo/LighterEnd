package io.github.openbagtwo.lighterend.utils.math.sdf.primitives;

import io.github.openbagtwo.lighterend.utils.math.sdf.SDF;
import net.minecraft.util.Mth;

public class SDFTorus extends SDF.Primitive {

  private float radiusSmall;
  private float radiusBig;

  public SDFTorus setBigRadius(float radius) {
    this.radiusBig = radius;
    return this;
  }

  public SDFTorus setSmallRadius(float radius) {
    this.radiusSmall = radius;
    return this;
  }

  @Override
  public float getDistance(float x, float y, float z) {
    float nx = Mth.sqrt(x * x + z * z) - this.radiusBig;
    return Mth.sqrt(nx * nx + y * y) - radiusSmall;
  }
}
