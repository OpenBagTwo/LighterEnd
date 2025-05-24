package io.github.openbagtwo.lighterend.utils.math.sdf.primitives;

import io.github.openbagtwo.lighterend.utils.math.sdf.SDF;

public class SDFSphere extends SDF.Primitive {

  private float radius;

  public SDFSphere setRadius(float radius) {
    this.radius = radius;
    return this;
  }

  @Override
  public float getDistance(float x, float y, float z) {
    return (float) Math.sqrt(x * x + y * y + z * z) - radius;
  }
}
