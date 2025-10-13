package io.github.openbagtwo.lighterend.utils.math.sdf.primitives;

import io.github.openbagtwo.lighterend.utils.math.sdf.SDF;

public class SDFFlatland extends SDF.Primitive {

  @Override
  public float getDistance(float x, float y, float z) {
    return y;
  }

}
