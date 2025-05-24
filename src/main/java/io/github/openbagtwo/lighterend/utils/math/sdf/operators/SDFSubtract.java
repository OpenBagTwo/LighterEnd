package io.github.openbagtwo.lighterend.utils.math.sdf.operators;

import io.github.openbagtwo.lighterend.utils.math.sdf.SDF;

public class SDFSubtract extends SDF.BinaryOperator {

  @Override
  public float getDistance(float x, float y, float z) {
    float a = this.sourceA.getDistance(x, y, z);
    float b = this.sourceB.getDistance(x, y, z);
    this.selectValue(a, b);
    return Math.max(a, -b);
  }
}
