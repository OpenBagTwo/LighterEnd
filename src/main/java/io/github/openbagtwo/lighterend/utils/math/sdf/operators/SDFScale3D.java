package io.github.openbagtwo.lighterend.utils.math.sdf.operators;

import io.github.openbagtwo.lighterend.utils.math.sdf.SDF;

public class SDFScale3D extends SDF.UnaryOperator {

  private float x;
  private float y;
  private float z;

  public SDFScale3D setScale(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
    return this;
  }

  @Override
  public float getDistance(float x, float y, float z) {
    return source.getDistance(x / this.x, y / this.y, z / this.z);
  }
}
