package io.github.openbagtwo.lighterend.utils.math.sdf.operators;

import io.github.openbagtwo.lighterend.utils.math.sdf.SDF;

public class SDFInvert extends SDF.UnaryOperator {

  @Override
  public float getDistance(float x, float y, float z) {
    return -this.source.getDistance(x, y, z);
  }
}
