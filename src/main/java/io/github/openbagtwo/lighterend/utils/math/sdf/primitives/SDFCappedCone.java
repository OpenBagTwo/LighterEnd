package io.github.openbagtwo.lighterend.utils.math.sdf.primitives;

import io.github.openbagtwo.lighterend.utils.math.sdf.SDF;
import net.minecraft.util.math.MathHelper;

public class SDFCappedCone extends SDF.Primitive {

  private float radius1;
  private float radius2;
  private float height;

  public SDFCappedCone setRadius1(float radius) {
    this.radius1 = radius;
    return this;
  }

  public SDFCappedCone setRadius2(float radius) {
    this.radius2 = radius;
    return this;
  }

  public SDFCappedCone setHeight(float height) {
    this.height = height;
    return this;
  }

  @Override
  public float getDistance(float x, float y, float z) {
    float qx = MathHelper.sqrt(x * x + z * z);
    float k2x = radius2 - radius1;
    float k2y = 2 * height;
    float cax = qx - Math.min(qx, (y < 0F) ? radius1 : radius2);
    float cay = Math.abs(y) - height;
    float mlt = MathHelper.clamp(
        ((radius2 - qx) * k2x + (height - y) * k2y) / (k2x * k2x + k2y * k2y),
        0F,
        1F
    );
    float cbx = qx - radius2 + k2x * mlt;
    float cby = y - height + k2y * mlt;
    float s = (cbx < 0F && cay < 0F) ? -1F : 1F;
    return s * (float) Math.sqrt(Math.min(cax * cax + cay * cay, cbx * cbx + cby * cby));
  }
}
