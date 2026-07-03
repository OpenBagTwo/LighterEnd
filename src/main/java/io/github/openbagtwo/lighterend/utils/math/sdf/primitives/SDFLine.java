package io.github.openbagtwo.lighterend.utils.math.sdf.primitives;

import io.github.openbagtwo.lighterend.utils.math.sdf.SDF;
import net.minecraft.util.math.MathHelper;

public class SDFLine extends SDF.Primitive {

  private float radius;
  private float x1;
  private float y1;
  private float z1;
  private float x2;
  private float y2;
  private float z2;

  public SDFLine setRadius(float radius) {
    this.radius = radius;
    return this;
  }

  public SDFLine setStart(float x, float y, float z) {
    this.x1 = x;
    this.y1 = y;
    this.z1 = z;
    return this;
  }

  public SDFLine setEnd(float x, float y, float z) {
    this.x2 = x;
    this.y2 = y;
    this.z2 = z;
    return this;
  }

  @Override
  public float getDistance(float x, float y, float z) {
    float pax = x - x1;
    float pay = y - y1;
    float paz = z - z1;

    float bax = x2 - x1;
    float bay = y2 - y1;
    float baz = z2 - z1;

    float dpb = pax * bax + pay * bay + paz * baz;
    float dbb = bax * bax + bay * bay + baz * baz;
    float h = MathHelper.clamp(dpb / dbb, 0F, 1F);
    return (float) Math.sqrt(
        Math.pow(pax - bax * h, 2)
            + Math.pow(pay - bay * h, 2)
            + Math.pow(paz - baz * h, 2)
    ) - radius;
  }
}
